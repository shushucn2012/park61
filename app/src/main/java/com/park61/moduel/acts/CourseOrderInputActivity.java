package com.park61.moduel.acts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.CourseApplyBean;
import com.park61.moduel.address.UpdateSelfTakeInfoActivity;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/7/22.
 */

public class CourseOrderInputActivity extends BaseActivity {

    private String coursePlanId;
    private String rname, rphone;
    private CourseApplyBean curApply;
    private List<BabyItem> chosenList = new ArrayList<>();

    private View area_input_contactor, area_coupon_choose, area_baby, area_adult_num, area_session,
            area_invitecode_choose;
    private ImageView img_course;
    private TextView tv_course_title, tv_date, tv_addr, tv_goods_price, tv_goods_old_price, tv_child_names, tv_total_price,
            tv_coupon_money, tv_final_money, tv_total_money, tv_rname, tv_rphone;
    private Button btn_add_baby, btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_order_input);
    }

    @Override
    public void initView() {
        area_input_contactor = findViewById(R.id.area_input_contactor);
        img_course = (ImageView) findViewById(R.id.img_course);
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_goods_old_price = (TextView) findViewById(R.id.tv_goods_old_price);
        tv_child_names = (TextView) findViewById(R.id.tv_child_names);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_rname = (TextView) findViewById(R.id.tv_rname);
        tv_rphone = (TextView) findViewById(R.id.tv_rphone);
        btn_add_baby = (Button) findViewById(R.id.btn_add_baby);
        btn_pay = (Button) findViewById(R.id.btn_pay);
    }

    @Override
    public void initData() {
        coursePlanId = getIntent().getStringExtra("coursePlanId");
        asyncApplyCourse();
    }

    @Override
    public void initListener() {
        area_input_contactor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, UpdateSelfTakeInfoActivity.class);
                intent.putExtra("rname", rname);
                intent.putExtra("rphone", rphone);
                startActivityForResult(intent, 0);
            }
        });
        btn_add_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseChooseBabyActivity.class);
                it.putExtra("childsIds", curApply.getChildId());
                startActivityForResult(it, 1);
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(rname) || TextUtils.isEmpty(rphone)) {
                    showShortToast("联系人姓名和手机号必须填写！");
                    return;
                }
                if (TextUtils.isEmpty(curApply.getChildId())) {
                    showShortToast("请添加宝宝！");
                    return;
                }
                asyncSubmitCourseOrder();
            }
        });
    }

    private void asyncApplyCourse() {
        String wholeUrl = AppUrl.host + AppUrl.applyCourse;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", rname);
        map.put("userMobile", rphone);
        map.put("coursePlanId", coursePlanId);
        if (chosenList.size() > 0) {
            String childIds = "";
            String childNames = "";
            for (int i = 0; i < chosenList.size(); i++) {
                childIds += chosenList.get(i).getId() + ",";
                childNames += chosenList.get(i).getPetName() + ",";
            }
            map.put("childId", childIds.substring(0, childIds.length() - 1));
            map.put("childName", childNames.substring(0, childNames.length() - 1));
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            dDialog.showDialog("提示", errorMsg, "取消", "重试",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asyncApplyCourse();
                        }
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            curApply = gson.fromJson(jsonResult.toString(), CourseApplyBean.class);
            if (!TextUtils.isEmpty(curApply.getTips())) {
                String tipsStr = curApply.getTips();
                String[] strings = tipsStr.split("\\r\\n");
                String tip0 = strings[0];
                showShortToast(tip0);
            }
            fillData();
        }
    };

    private void asyncSubmitCourseOrder() {
        String wholeUrl = AppUrl.host + AppUrl.submitCourseOrder;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("coursePlanId", curApply.getCoursePlanId());
       /* if (chosenList.size() > 0) {
            String childIds = "";
            String childNames = "";
            for (int i = 0; i < chosenList.size(); i++) {
                childIds += chosenList.get(i).getId() + ",";
                childNames += chosenList.get(i).getPetName() + ",";
            }
            map.put("childId", childIds.substring(0, childIds.length() - 1));
            map.put("childName", childNames.substring(0, childNames.length() - 1));
        }*/
        map.put("childId", curApply.getChildId());
        map.put("userName", rname);
        map.put("userMobile", rphone);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, sLsner);
    }

    BaseRequestListener sLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("订单提交成功，请继续支付！");
            GlobalParam.CUR_COURSE_ID = curApply.getCourseId();
            Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
            Long orderId = FU.paseLong(jsonResult.optString("data"));
            it.putExtra("finishToPage", "course");
            it.putExtra("orderId", orderId);
            startActivity(it);
            finish();
        }
    };

    /**
     * 填充数据
     */
    public void fillData() {
        rname = TextUtils.isEmpty(curApply.getUserName()) ? "" : curApply.getUserName();
        rphone = curApply.getUserMobile();
        tv_rname.setText("联系人：" + rname);
        tv_rphone.setText("电话：" + rphone);

        ImageManager.getInstance().displayImg(img_course, curApply.getCourseCover());
        tv_course_title.setText(curApply.getCourseName());
        tv_date.setText(curApply.getWeekCourseDayStr() + " " + curApply.getStartTime());
        tv_addr.setText(curApply.getCourseAddress());
        tv_goods_price.setText("￥" + curApply.getPriceSale());
        tv_goods_old_price.setText("￥" + curApply.getPriceOriginal());
        ViewInitTool.lineText(tv_goods_old_price);
        String childNames = curApply.getChildName();
        if (childNames != null) {
            String[] splitNames = childNames.split(",");
            if (splitNames.length > 3) {
                tv_child_names.setText(splitNames[0] + " " + splitNames[1] + " " + splitNames[2] + "...(" + splitNames.length + ")");
            } else {
                tv_child_names.setText(childNames);
            }
        }
        tv_total_price.setText("￥" + FU.strDbFmt(curApply.getOrderAmount()));
        tv_coupon_money.setText("￥" + FU.strDbFmt(curApply.getCouponAmount()));
        tv_final_money.setText("￥" + FU.strDbFmt(curApply.getTotalAmount()));
        tv_total_money.setText("￥" + FU.strDbFmt(curApply.getTotalAmount()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            rname = data.getStringExtra("rname");
            rphone = data.getStringExtra("rphone");
            tv_rname.setText("联系人：" + rname);
            tv_rphone.setText("电话：" + rphone);
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            chosenList = (List<BabyItem>) data.getSerializableExtra("chosenList");
            asyncApplyCourse();
        }
    }
}
