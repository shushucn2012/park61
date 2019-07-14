package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.moduel.dreamhouse.bean.LoveShopBean;
import com.park61.moduel.shop.FilterShopActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.wheel.weekcalendar.ConfirmjourneryDateTimeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 确认行程界面
 */
public class ConfirmJourneyActivity extends BaseActivity {
    private TextView tv_time;
    private TextView tv_store_name, tv_change_store;
    private Button btn_confirm;
    private ConfirmjourneryDateTimeDialog myTimeDialog;
    private Calendar calendar;
    private String backData = "";
    private Long shopId;//店铺id
    private String shopName;//店铺名称
    private Long requirementId;//梦想id
    private Long reqPredId;
    private ArrayList<LoveShopBean> loveShopList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dreamhouse_confirm_journey);
    }

    @Override
    public void initView() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_store_name = (TextView) findViewById(R.id.tv_store_name);
        tv_change_store = (TextView) findViewById(R.id.tv_change_store);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        requirementId = getIntent().getLongExtra("requirementId", 0l);
        myTimeDialog = new ConfirmjourneryDateTimeDialog(mContext);
        calendar = Calendar.getInstance();

        loveShopList = new ArrayList<LoveShopBean>();
        asyncGetLoveShop();
    }

    @Override
    public void initListener() {
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTimeDialog.setTime(calendar);
                myTimeDialog.setOnChosenListener(new ConfirmjourneryDateTimeDialog.OnChosenListener() {

                    @Override
                    public void onFinish(String year, String month, String day,
                                         String hour, String min) {
                        backData = year + "-" + month + "-" + day + "   " + hour + ":" + min;
                        if (DateTool.getDateByDay(backData)
                                .before(new Date(new Date().getTime() - 24 * 60 * 60
                                        * 1000))) {
                            showShortToast("您选择的日期不能小于今天！");
                            return;
                        } else {
                            tv_time.setText(backData);
                        }
                    }
                });
                myTimeDialog.show();
            }
        });
        tv_change_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, FilterShopActivity.class);
                startActivityForResult(it, 0);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(tv_time.getText().toString().trim())) {
                    showShortToast("请选择时间");
                    return;
                }
                asyncAddReqPred();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            shopId = data.getLongExtra("shopId", 0l);
            shopName = data.getStringExtra("shopName");
            tv_store_name.setText(shopName);
        }
    }

    /**
     * 加入梦想
     */
    protected void asyncAddReqPred() {
        String wholeUrl = AppUrl.host + AppUrl.ADD_REQPRED;
        String requestBodyData = ParamBuild.getaddReqPred(requirementId, backData, shopId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, addListener);
    }

    BaseRequestListener addListener = new JsonRequestListener() {
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
            try {
                reqPredId = jsonResult.getLong("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(mContext, AddDreamSuccessActivity.class);
            intent.putExtra("reqPredId", reqPredId);
            intent.putExtra("requirementId", requirementId);
            startActivity(intent);
            finish();
        }
    };

    /**
     * 常去店铺
     */
    protected void asyncGetLoveShop() {
        String wholeUrl = AppUrl.host + AppUrl.GET_LOVE_SHOP_BY_USERID;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, shopListener);
    }

    BaseRequestListener shopListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ArrayList<LoveShopBean> currentPageList = new ArrayList<LoveShopBean>();
            JSONArray jay = jsonResult.optJSONArray("list");
            if (jay == null || jay.length() == 0)
                return;
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                LoveShopBean item = gson.fromJson(jot.toString(),LoveShopBean.class);
                currentPageList.add(item);
            }
            loveShopList.addAll(currentPageList);
            if (loveShopList.size() < 1) {
                tv_store_name.setText("");
            } else {
                shopName = loveShopList.get(0).getShopName();
                tv_store_name.setText(shopName);
                shopId = loveShopList.get(0).getShopId();
            }
        }
    };
}
