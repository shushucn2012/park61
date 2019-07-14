package com.park61.moduel.sales;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.RegexValidator;
import com.park61.moduel.address.AddressActivity;
import com.park61.moduel.coupon.GoodsSelectCouponActivity;
import com.park61.moduel.openmember.CreateRecommendCodeActivity;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.sales.adapter.OrderConfirmAdapter;
import com.park61.moduel.sales.bean.GoodReceiverVO;
import com.park61.moduel.sales.bean.InvalidGoodsBean;
import com.park61.moduel.sales.bean.MerchantTradeOrder;
import com.park61.moduel.sales.bean.TradeOrder;
import com.park61.moduel.sales.bean.TradeOrderConfirm;
import com.park61.moduel.sales.bean.TradeOrderItem;
import com.park61.moduel.shoppincart.TradeCartActivity;
import com.park61.moduel.shoppincart.bean.DeleteItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AddAddressDialog;
import com.park61.widget.dialog.CanRefreshProgressDialog;
import com.park61.widget.dialog.CanRefreshProgressDialog.OnRefreshClickedLsner;
import com.park61.widget.dialog.InputDialog;
import com.park61.widget.dialog.OrderConfirmInvalidDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmV3Activity extends BaseActivity {
    private int buyNum = 1;// 购买数量
    private Long pmInfoId;// 选择的商品id
    private Long promotionId;// 促销id
    private String from;// 来源

    private String recommenderCode;// 推荐码

    private TextView tv_goods_money, tv_postage_money, tv_total_money, tv_zongji_money,
            browse_name_tv, browse_number_tv, select_tv, browse_address_tv, tv_confirm,
            tv_recommendcode, tv_coupon_money, tv_coupon_value, tv_coupon_size, discount_amount;
    private Button btn_pay;
    private View addr_area, null_addr_area, details_addr_area, identity_area,
            area_recommendCode, coupon_area;
    private InputDialog iDialog;
    private View browse_choice_area;
    private int goodReceiverType;// 0-快递 1-自提

    private CanRefreshProgressDialog cpDialog;
    private String couponUseId = null;
    private String sCouponUseId;// 已选中优惠券的id
    private String productColor, productSize;
    private String isFightGroup;
    private Long fightGroupOpenId;

    private ListView order_listview;
    private OrderConfirmAdapter adapter;
    private ArrayList<TradeOrderConfirm> mList;
    private TradeOrder info;
    private EditText edit_identity_number;
    private String identityCardNo;// 身份证号码
    private int containOverSeas;// 是否包含海淘商品 0 不包含，1 包含
    private OrderConfirmInvalidDialog dialog;
    private ArrayList<InvalidGoodsBean> invalidList;
    private String couponUseIds;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_order_confirm);
    }

    @Override
    public void initView() {
        addr_area = findViewById(R.id.addr_area);
        null_addr_area = findViewById(R.id.null_addr_area);
        details_addr_area = findViewById(R.id.details_addr_area);
        coupon_area = findViewById(R.id.coupon_area);
        select_tv = (TextView) findViewById(R.id.select_tv);
        browse_name_tv = (TextView) findViewById(R.id.browse_name_tv);
        browse_number_tv = (TextView) findViewById(R.id.browse_number_tv);
        browse_address_tv = (TextView) findViewById(R.id.browse_address_tv);

        order_listview = (ListView) findViewById(R.id.order_listview);

        tv_goods_money = (TextView) findViewById(R.id.tv_goods_money);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_postage_money = (TextView) findViewById(R.id.tv_postage_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_coupon_value = (TextView) findViewById(R.id.tv_coupon_value);
        discount_amount = (TextView) findViewById(R.id.discount_amount);
        tv_coupon_size = (TextView) findViewById(R.id.tv_coupon_size);
        btn_pay = (Button) findViewById(R.id.btn_pay);

        area_recommendCode = findViewById(R.id.area_recommendCode);
        tv_recommendcode = (TextView) findViewById(R.id.tv_recommendcode);
        browse_choice_area = findViewById(R.id.browse_choice_area);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        edit_identity_number = (EditText) findViewById(R.id.edit_identity_number);
        identity_area = findViewById(R.id.identity_area);

        cpDialog = new CanRefreshProgressDialog(mContext,
                new OnRefreshClickedLsner() {

                    @Override
                    public void OnRefreshClicked() {
                        asyncGetConfirmInfo();
                    }
                });

        tv_zongji_money = (TextView) findViewById(R.id.tv_zongji_money);
        findViewById(R.id.remark_area).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mList = new ArrayList<TradeOrderConfirm>();
        adapter = new OrderConfirmAdapter(mContext, mList);
        order_listview.setAdapter(adapter);
        isFightGroup = getIntent().getStringExtra("isFightGroup");
        from = getIntent().getStringExtra("from");
        buyNum = getIntent().getIntExtra("buyNum", 0);
        pmInfoId = getIntent().getLongExtra("pmInfoId", -1l);
        promotionId = getIntent().getLongExtra("promotionId", -1l);
        fightGroupOpenId = getIntent().getLongExtra("fightGroupOpenId", -1l);
        if (fightGroupOpenId == -1l) {
            fightGroupOpenId = null;
        }
        logout("=======结算========" + fightGroupOpenId);
        asyncGetConfirmInfo();
    }

    @Override
    public void initListener() {
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_confirm.setTextColor(getResources().getColor(R.color.com_orange));
                identityCardNo = edit_identity_number.getText().toString();
                if (TextUtils.isEmpty(identityCardNo) || !RegexValidator.isIDCard(identityCardNo)) {
                    showShortToast("请输入有效身份证号码");
                } else {
                    showShortToast("验证成功啦!");
                    edit_identity_number.setText(identityCardNo);
                }
            }
        });
        addr_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddressActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        btn_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (containOverSeas == 1 && (TextUtils.isEmpty(identityCardNo) || !RegexValidator.isIDCard(identityCardNo))) {
                    showShortToast("请验证身份证号码");
                    return;
                }
                if (null_addr_area.getVisibility() == View.VISIBLE) {
                    final AddAddressDialog dialog = new AddAddressDialog(mContext);
                    dialog.showDialog(null, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismissDialog();
                            Intent intent = new Intent(mContext, AddressActivity.class);
                            startActivityForResult(intent, 0);
                        }
                    });
                } else {
                    asyncGetSubmitOrder();
                }
            }
        });
        area_recommendCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, CreateRecommendCodeActivity.class);
                it.putExtra("title", "推荐码");
                startActivityForResult(it, 4);
            }
        });

        coupon_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (info.getCouponCount() != null && info.getCouponCount() > 0) {
//                    Intent intent = new Intent(mContext,
//                            GoodsSelectCouponActivity.class);
//                    sCouponUseId = info.getCouponUseId() + "";
//                    intent.putExtra("sCouponUseId", sCouponUseId);
//                    intent.putExtra("amount", info.getProductAmount().doubleValue());
//                    startActivityForResult(intent, 3);
//                } else {
//                    showShortToast("无可用优惠券");
//                }

                Intent intent = new Intent(mContext, GoodsSelectCouponActivity.class);
                intent.putExtra("couponUseIds", couponUseId);
                intent.putExtra("amount", info.getProductAmount().doubleValue());
                startActivityForResult(intent, 3);

            }
        });
    }

    /**
     * 获取结算详情
     */
    protected void asyncGetConfirmInfo() {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_CONFIRM_V3;
        String requestBodyData;
        if (from.equals("detail")) {//来源detail为商品详情
            if (isFightGroup != null && isFightGroup.equals("true")) {//isFightGroup="true"为拼团
                if (fightGroupOpenId == null) {//fightGroupOpenId等于null为开团，不为null则为参团
                    requestBodyData = ParamBuild.getJoinGroupConfirmV2(from, pmInfoId, buyNum, promotionId, couponUseId, isFightGroup);
                } else {//参团
                    requestBodyData = ParamBuild.getFightGroupGoodsConfirmV2(from, pmInfoId, buyNum, promotionId, couponUseId, fightGroupOpenId, isFightGroup);
                }
            } else {
                requestBodyData = ParamBuild.getGoodsConfirmV2(from, pmInfoId, buyNum, promotionId, couponUseId);
            }
        } else {//购物车结算
            requestBodyData = ParamBuild.getGoodsConfirmV2(from, pmInfoId, buyNum, promotionId, couponUseId);
        }

        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getGoodsConfirmInfoLsner);
    }

    BaseRequestListener getGoodsConfirmInfoLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            cpDialog.showDialog();

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            cpDialog.showRefreshBtn();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            cpDialog.dialogDismiss();
            info = gson.fromJson(jsonResult.toString(), TradeOrder.class);
            for (int i = 0; i < info.getMerchantOrderList().size(); i++) {
                TradeOrderConfirm item = info.getMerchantOrderList().get(i);
                mList.add(item);
            }
            containOverSeas = info.getContainOverSeas();
            logout("=======海淘========" + containOverSeas);
            if (containOverSeas == 0) {
                identity_area.setVisibility(View.GONE);
            } else {
                identity_area.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
            setDataToView();
        }
    };

    public void setDataToView() {
        logout("===fightGroupOpenId====" + fightGroupOpenId);
        tv_goods_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getProductAmount()));
        tv_postage_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getOrderDeliveryFee()));
        tv_total_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getOrderToPayAmount()));
        tv_zongji_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getOrderToPayAmount()));
//        tv_coupon_money.setText("-" + FU.strBFmt(info.getCouponAmount()));
        discount_amount.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getDiscountAmount()));
//        if (info.getCouponCount() != null && info.getCouponCount() > 0) {
//            tv_coupon_size.setText(info.getCouponCount() + "张可用");
//            tv_coupon_value.setText("-" + FU.strBFmt(info.getCouponAmount()));
//        } else {
//            tv_coupon_size.setText("");
//            tv_coupon_value.setText("无可用");
//        }
        setDateToAddr();
    }

    private void setDateToAddr() {
        if (info.getGoodReceiver() != null) {
            GoodReceiverVO gr = info.getGoodReceiver();
            null_addr_area.setVisibility(View.GONE);
            details_addr_area.setVisibility(View.VISIBLE);
            browse_name_tv.setText("联系人：" + gr.getGoodReceiverName());
            browse_number_tv.setText(gr.getGoodReceiverMobile());
            if (gr.getGoodReceiverType() == 1) {
                browse_address_tv.setText("地址："
                        + gr.getGoodReceiverProvinceName()
                        + gr.getGoodReceiverCityName()
                        + gr.getGoodReceiverCountyName()
                        + gr.getGoodReceiverAddress() + "    "
                        + gr.getStoreName() + "(联系方式：" + gr.getStorePhone()
                        + ")");
                select_tv.setText("您选择了");
            } else {
                if (gr.getGoodReceiverTownName() != null) {
                    browse_address_tv.setText("地址："
                            + gr.getGoodReceiverProvinceName()
                            + gr.getGoodReceiverCityName()
                            + gr.getGoodReceiverCountyName() + gr.getGoodReceiverTownName()
                            + gr.getGoodReceiverAddress());
                } else {
                    browse_address_tv.setText("地址："
                            + gr.getGoodReceiverProvinceName()
                            + gr.getGoodReceiverCityName()
                            + gr.getGoodReceiverCountyName()
                            + gr.getGoodReceiverAddress());
                }
                select_tv.setText("您还可以选择");
            }
        } else {
            null_addr_area.setVisibility(View.VISIBLE);
            details_addr_area.setVisibility(View.GONE);
        }
    }

    /**
     * 获取提交订单
     */
    protected void asyncGetSubmitOrder() {
        String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_SUBMITV2;
        String requestBodyData;
        if (containOverSeas == 0) {//是否包含海淘商品 0 不包含，1 包含
            requestBodyData = ParamBuild.getOrderSubmitV2(tv_recommendcode.getText().toString().trim());
        } else {//1：海淘商品，增加传参真实姓名和身份证号
            requestBodyData = ParamBuild.getOrderIdentitySubmitV2(tv_recommendcode.getText().toString().trim(), identityCardNo);
        }
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, submitLsner);
    }

    BaseRequestListener submitLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000025025".equals(errorCode)) {
                List<TradeOrderItem> itemList = new ArrayList<TradeOrderItem>();
                for (int i = 0; i < mList.size(); i++) {//遍历所有商品,获取所有商品TradeOrderItem的list
                    List<MerchantTradeOrder> merchantOrderList = mList.get(i).getMerchantOrder();
                    for (int j = 0; j < merchantOrderList.size(); j++) {
                        List<TradeOrderItem> orderItemList = merchantOrderList.get(j).getOrderItemList();
                        for (int k = 0; k < orderItemList.size(); k++) {
                            TradeOrderItem item = orderItemList.get(k);
                            itemList.add(item);
                        }
                    }
                }
                logout("======itemList============" + itemList.size());
                if (from.equals("cart")) {//来源购物车
                    invalidList = new ArrayList<InvalidGoodsBean>();
                    JSONArray jayList = null;
                    try {
                        jayList = new JSONArray(errorMsg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    invalidList.clear();
                    for (int i = 0; i < jayList.length(); i++) {
                        JSONObject jot = jayList.optJSONObject(i);
                        InvalidGoodsBean item = gson.fromJson(jot.toString(), InvalidGoodsBean.class);
                        invalidList.add(item);
                    }
                    logout("======invalidList=========" + invalidList.size());
                    if (itemList.size() > invalidList.size() && invalidList.size() > 0) {//若所有商品list数量大于失效商品list数量，则弹框
                        dialog = new OrderConfirmInvalidDialog(mContext, invalidList);
                        dialog.showDialog(null, new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismissDialog();
                                ArrayList<DeleteItem> deleteItems = new ArrayList<DeleteItem>();
                                for (int i = 0; i < invalidList.size(); i++) {
                                    deleteItems.add(new DeleteItem(invalidList.get(i).getPmInfoId(), invalidList.get(i).getPromotionId()));
                                }
                                logout("========deleteItems========" + deleteItems.size());
                                Intent intent = new Intent(mContext, TradeCartActivity.class);
//                            intent.putParcelableArrayListExtra("deleteItems",(ArrayList)deleteItems);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        showShortToast("下单失败，商品库存不足!");
                    }
                } else {
                    showShortToast("下单失败，商品库存不足!");
                }
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Long orderId = Long.parseLong(jsonResult.optString("data"));
            Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", orderId);
            startActivity(it);
            finish();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 ) {
            /*if(resultCode == RESULT_OK && data != null) {
                null_addr_area.setVisibility(View.GONE);
                details_addr_area.setVisibility(View.VISIBLE);
                String rname = data.getStringExtra("rname");
                String rphone = data.getStringExtra("rphone");
                String raddr = data.getStringExtra("raddr");
                browse_name_tv.setText("收件人：" + rname);
                browse_number_tv.setText(rphone);
                browse_address_tv.setText("地址：" + raddr);
                goodReceiverType = data.getIntExtra("rtype", goodReceiverType);
                if (goodReceiverType == 1) {
                    select_tv.setText("您选择了");
                } else {
                    select_tv.setText("您还可以选择");
                }
                asyncGetTradeOrderInfo();
            }*/
            asyncGetTradeOrderInfo();
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            couponUseId = data.getStringExtra("couponUseId");
            asyncGetTradeOrderInfo();
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            recommenderCode = data.getStringExtra("recommendCode");
            tv_recommendcode.setText(recommenderCode);
            asyncGetTradeOrderInfo();
        }
    }

    /**
     * 选择优惠券后进入结算页面
     */
    protected void asyncGetTradeOrderInfo() {
        String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_GETORDER;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getLsner);
    }

    BaseRequestListener getLsner = new JsonRequestListener() {

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
            info = gson.fromJson(jsonResult.toString(), TradeOrder.class);
            tv_coupon_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getCouponAmount()));
            if (info.getCouponAmount() != null) {
                tv_coupon_value.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getCouponAmount()));
            } else {
                tv_coupon_value.setText("");
            }
            tv_total_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getOrderToPayAmount()));
            discount_amount.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getDiscountAmount()));
            tv_postage_money.setText(FU.RENMINBI_UNIT + FU.strBFmt(info.getOrderDeliveryFee()));
            mList.clear();
            mList.addAll(info.getMerchantOrderList());
            adapter.notifyDataSetChanged();
            setDateToAddr();
        }
    };


}
