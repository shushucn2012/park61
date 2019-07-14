package com.park61.moduel.pay;

import android.view.View;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.moduel.pay.bean.BillDetail;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BillDetailsActivity extends BaseActivity {

    private TextView tv_operate_lable, tv_operate_amount, tv_available_money, tv_deal_remark,
            tv_pay_method, tv_deal_time, tv_order_number;
    private View pay_method_area, order_area;
    private long id;
    private BillDetail item;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_bill_details);
    }

    @Override
    public void initView() {
        tv_operate_lable = (TextView) findViewById(R.id.tv_operate_lable);
        tv_operate_amount = (TextView) findViewById(R.id.tv_operate_amount);
        tv_available_money = (TextView) findViewById(R.id.tv_available_money);
        tv_deal_remark = (TextView) findViewById(R.id.tv_deal_remark);
        tv_pay_method = (TextView) findViewById(R.id.tv_pay_method);
        tv_deal_time = (TextView) findViewById(R.id.tv_deal_time);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        pay_method_area = findViewById(R.id.pay_method_area);
        order_area = findViewById(R.id.order_area);
    }

    @Override
    public void initData() {
        id = getIntent().getLongExtra("id", 0l);
        asyncGetDetail();
    }

    @Override
    public void initListener() {

    }

    /**
     * 余额明细详情
     */
    private void asyncGetDetail() {
        String wholeUrl = AppUrl.host + AppUrl.ACCOUNT_BALANCE_DETAIL;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
            item = gson.fromJson(jsonResult.toString(), BillDetail.class);
            fillData();
        }
    };

    protected void fillData() {
        if (item.getOrderId() != null) {
            order_area.setVisibility(View.VISIBLE);
            tv_order_number.setText(item.getOrderId() + "");
        } else {
            order_area.setVisibility(View.GONE);
        }
        if (item.getRechargeType() != null) {
            pay_method_area.setVisibility(View.VISIBLE);
            tv_pay_method.setText(item.getRechargeType() == 0 ? "微信" : "支付宝");
        } else {
            pay_method_area.setVisibility(View.GONE);
        }
        tv_operate_amount.setText(item.getAvailableOperateAmount() + "");
        if (item.getAvailableOperateAmount() != null) {
            if(item.getType()<200){
                tv_operate_amount.setText("+"+item.getAvailableOperateAmount() + "");
                tv_operate_amount.setTextColor(getResources().getColor(R.color.com_orange));
                tv_operate_lable.setText("收        入");
            }else{
                tv_operate_amount.setText(item.getAvailableOperateAmount() + "");
                tv_operate_lable.setText("支        出");
                tv_operate_amount.setTextColor(getResources().getColor(R.color.g222222));
            }
        } else {
            tv_operate_amount.setText("0");
        }
        if (item.getAvailableAfterAmount() != null) {
            tv_available_money.setText(item.getAvailableAfterAmount() + "");
        } else {
            tv_available_money.setText("0");
        }
        tv_deal_remark.setText(item.getRemark());
        tv_deal_time.setText(DateTool.L2S(item.getCreateTime()));
    }

}
