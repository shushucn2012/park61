package com.park61.moduel.order;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.order.adapter.OrderGoodsListAdapter;
import com.park61.moduel.order.bean.OrderBean;
import com.park61.moduel.salesafter.AuditStatuActivity;
import com.park61.moduel.salesafter.MerchantSignActivity;
import com.park61.moduel.salesafter.RefundFinishActivity;
import com.park61.moduel.salesafter.SalesClosedActivity;
import com.park61.moduel.salesafter.WaitForMerchanSignActivity;
import com.park61.moduel.salesafter.WaitForSendGoodsActivity;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商品清单界面
 */
public class SalesGoodsListActivity extends BaseActivity implements OrderGoodsListAdapter.BtnClickListener {
    private ListView goods_listview;
    private OrderGoodsListAdapter mAdapter;
    private TextView tv_bags_num;
    private ArrayList<OrderBean> mList;
    private int packageNum;
    private int orderItemNum;
    private long id;
    private int grfStatus,auditResults,waybillType,isReturnGoods,returnGoodsNum;
    private long grfApplyTime,refundWay;
    private String grfCode,remark,grfReason,storePhone,storeAddress,deliveryCompanyName,waybillCode;
    private float grfAmount;
    private ArrayList<String> grfPicUrls;
    private long pmInfoId;//商品id
    private long grfId;//退货id
    private long updateTime;//撤销售后申请时间
    private String closeReason;//售后关闭原因;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sales_goodslist);
    }

    @Override
    public void initView() {
        tv_bags_num = (TextView) findViewById(R.id.tv_bags_num);
        goods_listview = (ListView) findViewById(R.id.goods_listview);
    }

    @Override
    public void initData() {
        packageNum = getIntent().getIntExtra("packageNum",packageNum);
        orderItemNum = getIntent().getIntExtra("orderItemNum",orderItemNum);
        id = getIntent().getLongExtra("soId",0l);
        logout("========父单id========="+id);
        tv_bags_num.setText(packageNum+"个包裹(共"+orderItemNum+"件)");
        mList = (ArrayList)getIntent().getParcelableArrayListExtra("orderList");
        mAdapter = new OrderGoodsListAdapter((BaseActivity)mContext,mList);
        mAdapter.setBtnClickListener(SalesGoodsListActivity.this);
        goods_listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void confirmReceipt(final int position) {
        dDialog.showDialog("提示", "确定已收到货物吗？", "确定", "取消",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        showDialog();
                        asyncConfirmReceiptGoods(mList.get(position));

                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                    }
                });
    }

    @Override
    public void checkLogistics(int position) {
        Intent it = new Intent(mContext, LogisticsDetailActivty.class);
        it.putExtra("soId", mList.get(position).getId());
        mContext.startActivity(it);
    }

    @Override
    public void afterSaleDetail(int position) {
        grfId = mList.get(position).getId();
        pmInfoId = mList.get(position).getItems().get(0).getPmInfoId();
        asyncQueryReturnApply();
    }


    /**
     * 确认收货
     */
    private void asyncConfirmReceiptGoods(OrderBean order) {

        String wholeUrl = AppUrl.host + AppUrl.ORDER_CONFRIM_RECEIPT;
        long soId = 0L;
        soId = order.getId();
        String requestBodyData = ParamBuild.getOrderDetail(soId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                new ConfirmReceiptRequestListener(order));
    }

    class ConfirmReceiptRequestListener implements JsonRequestListener {

        OrderBean order;

        public ConfirmReceiptRequestListener(OrderBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast("确认收货失败!");
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            showShortToast("确认收货成功");
            dismissDialog();
            Intent it = new Intent(mContext, OrderDealSuccessActivity.class);
            it.putExtra("soId", id);
            it.putExtra("orderInfo", order);
            startActivity(it);
            finish();
        }
    }
    /**
     * 退货申请查询
     */
    private void asyncQueryReturnApply() {
        String wholeUrl = AppUrl.host + AppUrl.RETURN_QUERY;
//        String requestBodyData = ParamBuild.queryById(grfId);
        String requestBodyData = ParamBuild.queryByIdV2(grfId,pmInfoId);
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
            ApplySalesAfterInfo item = gson.fromJson(jsonResult.toString(), ApplySalesAfterInfo.class);// 把Json数据解析成对象
            grfStatus = item.getGrfStatus();
            auditResults = item.getAuditResults();
            waybillType = item.getWaybillType();
            isReturnGoods = item.getIsReturnGoods();

            grfApplyTime = item.getGrfApplyTime();
            grfCode = item.getGrfCode();
            remark = item.getRemark();
            returnGoodsNum = item.getReturnGoodsNum();
            grfAmount = item.getGrfAmount();
            grfReason = item.getGrfReason();
            storePhone = item.getStorePhone();
            storeAddress = item.getStoreAddress();

            deliveryCompanyName = item.getDeliveryCompanyName();
            waybillCode = item.getWaybillCode();
            updateTime = item.getUpdateTime();
            closeReason = item.getCloseReason();

            refundWay = item.getRefundWay();
            grfPicUrls = new ArrayList<String>();
            for (int i = 0; i < item.getPics().size(); i++) {
                String givebackPicUrl = item.getPics().get(i).getGivebackPicUrl();
                grfPicUrls.add(givebackPicUrl);
            }
            // 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
            if (grfStatus == 0) {
                Intent intent1 = new Intent(mContext, AuditStatuActivity.class);
                transmitData(intent1);
                mContext.startActivity(intent1);
            } else if (grfStatus == 1) {
                Intent intent2 = new Intent(mContext, WaitForSendGoodsActivity.class);
                transmitData(intent2);
                intent2.putExtra("grfId",item.getItems().get(0).getGrfId());
                intent2.putExtra("pmInfoId",pmInfoId);
                intent2.putExtra("soId", item.getSoId());
                intent2.putExtra("auditResults", auditResults);
                intent2.putExtra("waybillType", waybillType);
                intent2.putExtra("storePhone", storePhone);
                intent2.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent2);
            } else if (grfStatus == 2) {
                Intent intent3 = new Intent(mContext, WaitForMerchanSignActivity.class);
                transmitData(intent3);
                intent3.putExtra("isReturnGoods", isReturnGoods);
                intent3.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent3.putExtra("waybillCode", waybillCode);

                intent3.putExtra("waybillType", waybillType);
                intent3.putExtra("storePhone", storePhone);
                intent3.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent3);
            } else if (grfStatus == 3) {
                Intent intent4 = new Intent(mContext, MerchantSignActivity.class);
                transmitData(intent4);
                intent4.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent4.putExtra("waybillCode", waybillCode);
                intent4.putExtra("waybillType", waybillType);
                intent4.putExtra("storePhone", storePhone);
                intent4.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent4);
            } else if (grfStatus == 4) {
                Intent intent5 = new Intent(mContext, RefundFinishActivity.class);
                intent5.putExtra("grfAmount", grfAmount);
                intent5.putExtra("refundWay", refundWay);
                mContext.startActivity(intent5);
            } else if (grfStatus == 5) {
                Intent intent = new Intent(mContext, SalesClosedActivity.class);
                transmitData(intent);
                intent.putExtra("updateTime", updateTime);
                intent.putExtra("closeReason", closeReason);

                intent.putExtra("isReturnGoods", isReturnGoods);
                intent.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent.putExtra("waybillCode", waybillCode);
                mContext.startActivity(intent);
            }
        }
    };
    private void transmitData(Intent intent){
        intent.putExtra("grfApplyTime", grfApplyTime);
        intent.putExtra("grfCode", grfCode);
        intent.putExtra("remark", remark);
        intent.putExtra("returnGoodsNum", returnGoodsNum);
        intent.putExtra("grfAmount", grfAmount);
        intent.putExtra("grfReason", grfReason);
        intent.putExtra("grfPicUrls", grfPicUrls);
    }
}
