package com.park61.moduel.order;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.order.bean.InterestClassOrderDetailBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 兴趣课活动订单详情
 * Created by shubei on 2018/8/20.
 */

public class InterestClassOrderDetailActivity extends BaseActivity {

    private ImageView img_goods;
    private TextView tv_goods_name, tv_goods_money, tv_goods_num, tv_total_amount, tv_buyer_name, tv_class_name, tv_mobile, tv_remark,
            tv_order_no, tv_order_time;
    private Button btn_to_saleafter;

    private long orderId;
    private InterestClassOrderDetailBean mInterestClassOrderDetailBean;
    private String telPhone = "4008499599";

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_interestclass_order_detail);
    }

    @Override
    public void initView() {
        img_goods = (ImageView) findViewById(R.id.img_goods);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_money = (TextView) findViewById(R.id.tv_goods_money);
        tv_goods_num = (TextView) findViewById(R.id.tv_goods_num);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        tv_buyer_name = (TextView) findViewById(R.id.tv_buyer_name);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        btn_to_saleafter = (Button) findViewById(R.id.btn_to_saleafter);
        findViewById(R.id.area_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        setPagTitle("订单详情");
        orderId = getIntent().getLongExtra("orderid", -1);
        asyncGetInterestClassOrderDetail();
        //asyncGetTel();
    }

    @Override
    public void initListener() {
        btn_to_saleafter.setOnClickListener(v -> {
            if (TextUtils.isEmpty(telPhone)) {
                telPhone = "4008499599";
            }
            dDialog.showDialog("提醒", "确定需要拨打" + telPhone + "申请售后吗？", "取消", "确定", null, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
                    startActivity(intent);
                    dDialog.dismissDialog();
                }
            });
        });
    }

    /**
     * 兴趣班订单详情
     */
    private void asyncGetInterestClassOrderDetail() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.interestClassOrderDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dLsner);
    }

    BaseRequestListener dLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mInterestClassOrderDetailBean = gson.fromJson(jsonResult.toString(), InterestClassOrderDetailBean.class);
            if (mInterestClassOrderDetailBean != null) {
                findViewById(R.id.area_empty).setVisibility(View.GONE);
                fillData();
            }
        }
    };

    private void fillData() {
        if (!CommonMethod.isListEmpty(mInterestClassOrderDetailBean.getItems())) {
            ImageManager.getInstance().displayImg(img_goods, mInterestClassOrderDetailBean.getItems().get(0).getProductPicUrl());
            tv_goods_name.setText(mInterestClassOrderDetailBean.getItems().get(0).getProductCname());
            tv_goods_money.setText(FU.RENMINBI_UNIT + FU.strDbFmt(mInterestClassOrderDetailBean.getItems().get(0).getOrderItemPrice()));
            tv_goods_num.setText("x" + mInterestClassOrderDetailBean.getItems().get(0).getOrderItemNum());
        }
        tv_total_amount.setText(FU.RENMINBI_UNIT + FU.strDbFmt(mInterestClassOrderDetailBean.getOrderToPayAmount()));
        tv_buyer_name.setText(mInterestClassOrderDetailBean.getChildName());
        tv_class_name.setText(mInterestClassOrderDetailBean.getClassName());
        tv_mobile.setText(mInterestClassOrderDetailBean.getContactMobile());
        tv_remark.setText(mInterestClassOrderDetailBean.getOrderRemark());
        tv_order_no.setText(mInterestClassOrderDetailBean.getId() + "");
        tv_order_time.setText(mInterestClassOrderDetailBean.getOrderCreateTime());
    }

//    /**
//     * 请求获取售后电话
//     */
//    private void asyncGetTel() {
//        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getTel;
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("orderId", orderId);
//        String requestBodyData = ParamBuild.buildParams(map);
//        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tlistener);
//    }
//
//    BaseRequestListener tlistener = new JsonRequestListener() {
//
//        @Override
//        public void onStart(int requestId) {
//        }
//
//        @Override
//        public void onError(int requestId, String errorCode, String errorMsg) {
//        }
//
//        @Override
//        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
//            telPhone = jsonResult.optString("data");
//        }
//    };
}
