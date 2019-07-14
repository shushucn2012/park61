package com.park61.moduel.acts.course;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.ActOrderConfirmActivity;
import com.park61.moduel.acts.adapter.ApplyBabyListAdapter;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 小课报名页面
 */
public class CApplyConfirmActivity extends BaseActivity implements ApplyBabyListAdapter.CheckedBabyCallBack {

    private ArrayList<BabyItem> applyBabyList = new ArrayList<BabyItem>();
    private ArrayList<BabyItem> checkedList;// 获取选择的孩子列表
    private ApplyBabyListAdapter adapter;

    private int elderNum = 0;
    private int babyNum = 0;
    private String price = "";//活动场次价格
    private String adultPrice = "";//成人价格

    private long sessionId;//场次id
    private String actTitle;//活动标题
    private String actStartTime;//活动开始时间
    private String courselistNumInfo;//课程可报名节数信息
    private int leftNum;//剩余名额

    private Button btn_add, btn_reduce, btn_add_child, btn_confirm;
    private TextView tv_adult_price, tv_total_money, tv_act_course_title, tv_act_course_starttime, tv_courselist_num_info,
            tv_act_price, tv_left_num;
    private ListView lv_babylist;
    private EditText et_elder_num;
    private Long reqPredId;//梦想预报名id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_capply_confirm);
    }

    @Override
    public void initView() {
        lv_babylist = (ListView) findViewById(R.id.lv_babylist);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        et_elder_num = (EditText) findViewById(R.id.et_elder_num);
        tv_adult_price = (TextView) findViewById(R.id.tv_adult_price);
        btn_add_child = (Button) findViewById(R.id.btn_add_child);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_act_course_title = (TextView) findViewById(R.id.tv_act_course_title);
        tv_act_course_starttime = (TextView) findViewById(R.id.tv_act_course_starttime);
        tv_courselist_num_info = (TextView) findViewById(R.id.tv_courselist_num_info);
        tv_act_price = (TextView) findViewById(R.id.tv_act_price);
        tv_left_num = (TextView) findViewById(R.id.tv_left_num);
    }

    @Override
    public void initData() {
        reqPredId = getIntent().getLongExtra("reqPredId",-1l);
        sessionId = getIntent().getLongExtra("sessionId", -1);
        price = getIntent().getStringExtra("actPrice");
        adultPrice = getIntent().getStringExtra("adultPrice");
        actTitle = getIntent().getStringExtra("actTitle");
        actStartTime = getIntent().getStringExtra("actStartTime");
        courselistNumInfo = getIntent().getStringExtra("courselistNumInfo");
        leftNum = getIntent().getIntExtra("leftNum", 0);

        tv_act_course_title.setText(actTitle);
        tv_act_course_starttime.setText("开始时间："+DateTool.L2SByDay2(actStartTime));
        tv_courselist_num_info.setText(courselistNumInfo);
        tv_act_price.setText("￥" + price);
        tv_left_num.setText("还剩" + leftNum + "个名额");
        tv_adult_price.setText(FU.zeroToMF(adultPrice));

        applyBabyList = new ArrayList<BabyItem>();
        adapter = new ApplyBabyListAdapter(mContext, applyBabyList, price);
        adapter.setCheckedBabyCallBack(this);
        lv_babylist.setAdapter(adapter);

        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int maxNum = leftNum;
                if (elderNum < maxNum) {
                    elderNum++;
                    updateElderNumView();
                } else {
                    showShortToast("人数超过限制");
                }
            }
        });
        btn_reduce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (elderNum > 0) {
                    elderNum--;
                    updateElderNumView();
                }
            }
        });
        btn_add_child.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int maxNum = leftNum;
                if (adapter.countAddChild() >= maxNum)// 最大添加
                    return;
                if (adapter.getSelectedPos().size() < maxNum) {
                    applyBabyList.add(new BabyItem(-1l, ""));
                    adapter.notifyDataSetChanged();
                } else {
                    showShortToast("人数超过限制");
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                asyncConfirmApply();
            }
        });
        et_elder_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                elderNum = FU.paseInt(s.toString());
                countTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 报名验证
     */
    protected boolean validate() {
        // 获取选择的孩子列表
        checkedList = new ArrayList<BabyItem>();
        for (int i = 0; i < applyBabyList.size(); i++) {
            if (adapter.getSelectedPos().contains(i))
                checkedList.add(applyBabyList.get(i));
        }
        /*if (checkedList.size() == 0) {
            showShortToast("您没有选择孩子！");
            return false;
        }*/
        for (BabyItem item : checkedList) {
            if (TextUtils.isEmpty(item.getName()) && TextUtils.isEmpty(item.getPetName())) {
                showShortToast("您没有输入孩子的名称！");
                return false;
            }
        }
        return true;
    }

    public void updateElderNumView() {
        et_elder_num.setText(elderNum + "");
        countTotal();
    }

    @Override
    public void onCheckedBaby(int babyNum) {
        this.babyNum = babyNum;
        countTotal();
    }

    /**
     * 计算并显示总价
     */
    public void countTotal() {
        double childTotal = FU.mul(FU.paseDb(babyNum + ""), Double.parseDouble(price));
        double adultTotal = FU.mul(FU.paseDb(elderNum + ""), Double.parseDouble(adultPrice));
        double total = FU.add(childTotal, adultTotal);
        tv_total_money.setText("￥" + FU.strDbFmt(total));
    }

    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("list");
            applyBabyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                applyBabyList.add(b);
            }
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 确认报名
     */
    private void asyncConfirmApply() {
        String wholeUrl = AppUrl.host + AppUrl.CONFIRM_APPLY_END;
        String totalMoney = tv_total_money.getText().toString().replace("￥", "");
        String requestBodyData = ParamBuild.confirmApply(reqPredId,sessionId, totalMoney,
                elderNum + "", "", GlobalParam.currentUser.getName(), GlobalParam.currentUser.getMobile(),
                checkedList);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, applylistener);
    }

    BaseRequestListener applylistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if (errorMsg != null) {
                String s = errorMsg.substring(errorMsg.length() - 1, errorMsg.length());
                if (s.equals("@")) {
                    errorMsg = errorMsg.substring(0, errorMsg.length() - 1);
                }
            }
            showShortToast(errorMsg.replace("@", "\n"));
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Long applyId = jsonResult.optLong("applyId");
            Intent it = new Intent(mContext, ActOrderConfirmActivity.class);
            it.putExtra("applyId", applyId);
            it.putExtra("childList", checkedList);
            startActivity(it);
            finish();
        }
    };

}

