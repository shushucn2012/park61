package com.park61.moduel.salesafter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.ClosedSalesDialog;
import com.park61.widget.dialog.SelectLogisticsDetailDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 官方已审核等待寄出商品的界面（退货状态：1、退货审核）
 *
 * @author Lucia
 */
public class WaitForSendGoodsActivity extends BaseActivity implements OnClickListener {

    private TextView reason_value, refund_amount, regoods_amount, detail_value, regoods_tv,
            sales_after_number_value, checked_tv,
            request_time_value, express_name_value, express_number_value, store_phone_value, store_addr_value;
    private View rl_express_name, rl_express_number, rl_store_phone, rl_store_addr, stickyView;
    private Button btn_ok;
    private ImageView photo0, photo1, photo2, choice_img;
    private long soId;// 订单id
    private int auditResults;// 审核结果：0、通过，1、不通过
    private long grfApplyTime;// 退货申请时间
    private String grfCode;// 退货单编码
    private int returnGoodsNum;// 退回商品总数
    private float grfAmount;// //退货金额：商品金额+运费
    private String remark;// 退货备注
    private String grfReason;// 退货原因
    private String deliveryCompanyName;// 快递名称
    private String waybillCode;// 快递单号
    private int waybillType;// 退货类型：0：快递退货 1：退回到自提点
    private String storePhone;//门店电话
    private String storeAddress;//门店地址
    private ArrayList<String> grfPicUrls = new ArrayList<String>();// 图片地址集合
    private List<String> list;//快递名称集合
    private TextView sales_after_policy;
    private long pmInfoId;//商品id
    private long grfId;//退货id

    private RadioButton self_pickup_tv, logistics_regoods, cancle_request;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sales_after_audit);

    }

    @Override
    public void initView() {
        reason_value = (TextView) findViewById(R.id.refund_reason_value);
        refund_amount = (TextView) findViewById(R.id.refund_amount_value);
        regoods_amount = (TextView) findViewById(R.id.regoods_amount_value);
        detail_value = (TextView) findViewById(R.id.detail_value);
        sales_after_number_value = (TextView) findViewById(R.id.sales_after_number_value);
        request_time_value = (TextView) findViewById(R.id.request_time_value);
        cancle_request = (RadioButton) findViewById(R.id.cancle_request);
        express_name_value = (TextView) findViewById(R.id.express_name_value);
        express_number_value = (TextView) findViewById(R.id.express_number_value);
        sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);
        store_phone_value = (TextView) findViewById(R.id.store_phone_value);
        store_addr_value = (TextView) findViewById(R.id.store_addr_value);
        checked_tv = (TextView) findViewById(R.id.checked_tv);
        regoods_tv = (TextView) findViewById(R.id.regoods_tv);

        rl_express_name = findViewById(R.id.rl_express_name);
        rl_express_number = findViewById(R.id.rl_express_number);
        rl_store_phone = findViewById(R.id.rl_store_phone);
        rl_store_addr = findViewById(R.id.rl_store_addr);
        stickyView = findViewById(R.id.stickyView);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        photo0 = (ImageView) findViewById(R.id.photo0);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        choice_img = (ImageView) findViewById(R.id.choice_img);
        btn_ok.setVisibility(View.GONE);

        self_pickup_tv = (RadioButton) findViewById(R.id.self_pickup_tv);
        logistics_regoods = (RadioButton) findViewById(R.id.logistics_regoods);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void initData() {
        grfId = getIntent().getLongExtra("grfId", 0l);
        pmInfoId = getIntent().getLongExtra("pmInfoId", 0l);
        waybillType = getIntent().getIntExtra("waybillType", waybillType);
        storePhone = getIntent().getStringExtra("storePhone");
        storeAddress = getIntent().getStringExtra("storeAddress");
        logout("=====门店地址====" + storeAddress);
        soId = getIntent().getLongExtra("soId", -1L);
        auditResults = getIntent().getIntExtra("auditResults", auditResults);
        grfApplyTime = getIntent().getLongExtra("grfApplyTime", grfApplyTime);
        grfCode = getIntent().getStringExtra("grfCode");
        remark = getIntent().getStringExtra("remark");
        grfReason = getIntent().getStringExtra("grfReason");
        returnGoodsNum = getIntent().getIntExtra("returnGoodsNum", returnGoodsNum);
        grfAmount = getIntent().getFloatExtra("grfAmount", grfAmount);

        reason_value.setText(grfReason);
        detail_value.setText(remark);
        regoods_amount.setText("" + returnGoodsNum);
        refund_amount.setText("" + grfAmount);
        sales_after_number_value.setText(grfCode);
        long time = Long.valueOf(grfApplyTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        request_time_value.setText(sdf.format(new Date(time)));

        grfPicUrls = getIntent().getStringArrayListExtra("grfPicUrls");
        if (grfPicUrls.size() == 1) {
            ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
        } else if (grfPicUrls.size() == 2) {
            ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
            ImageManager.getInstance().displayImg(photo1, grfPicUrls.get(1));
        } else if (grfPicUrls.size() == 3) {
            ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
            ImageManager.getInstance().displayImg(photo1, grfPicUrls.get(1));
            ImageManager.getInstance().displayImg(photo2, grfPicUrls.get(2));
        }
        if (waybillType == 1) {//到店自提
            self_pickup_tv.setVisibility(View.GONE);
            logistics_regoods.setVisibility(View.GONE);
            rl_express_name.setVisibility(View.GONE);
            rl_express_number.setVisibility(View.GONE);
            logistics_regoods.setVisibility(View.GONE);
            rl_store_phone.setVisibility(View.VISIBLE);
            rl_store_addr.setVisibility(View.VISIBLE);
            store_phone_value.setText(storePhone);
            store_addr_value.setText(storeAddress);
        } else {
            self_pickup_tv.setVisibility(View.GONE);
            logistics_regoods.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListener() {
        self_pickup_tv.setOnClickListener(this);
        cancle_request.setOnClickListener(this);
        logistics_regoods.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        photo0.setOnClickListener(this);
        photo1.setOnClickListener(this);
        photo2.setOnClickListener(this);
        sales_after_policy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SalesAfterPolicyActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_request:
                final ClosedSalesDialog dialog = new ClosedSalesDialog(mContext);
                dialog.showDialog(null, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();
                        asyncCancleRequest();
                    }
                });
                break;
            case R.id.logistics_regoods:
                final SelectLogisticsDetailDialog logisticsDialog = new SelectLogisticsDetailDialog(mContext, list);
                logisticsDialog.showDialog(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        logisticsDialog.dismissDialog();
                        btn_ok.setVisibility(View.GONE);
                        rl_express_name.setVisibility(View.GONE);
                        rl_express_number.setVisibility(View.GONE);
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deliveryCompanyName = logisticsDialog
                                .getDeliveryCompanyName();
                        waybillCode = logisticsDialog.getWaybillCode();
                        if (TextUtils.isEmpty(waybillCode)) {
                            showShortToast("请输入单号");
                        } else {
                            logisticsDialog.dismissDialog();
                            stickyView.setVisibility(View.GONE);
                            rl_express_name.setVisibility(View.VISIBLE);
                            rl_express_number.setVisibility(View.VISIBLE);
                            btn_ok.setVisibility(View.VISIBLE);

                            choice_img.setVisibility(View.GONE);
                            checked_tv.setText("等待商品寄出");
                            regoods_tv.setBackgroundResource(R.drawable.sheheliang1);

                            express_name_value.setText(deliveryCompanyName);
                            express_number_value.setText(waybillCode);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });
                break;
            case R.id.photo0:
                Intent it = new Intent(mContext, ShowBigPicActivity.class);
                it.putExtra("picUrl", grfPicUrls.get(0));//当前位置图片的url
                it.putExtra("urlList", grfPicUrls);//图片的url集合
                startActivity(it);
                break;

            case R.id.photo1:
                Intent it1 = new Intent(mContext, ShowBigPicActivity.class);
                it1.putExtra("picUrl", grfPicUrls.get(1));//当前位置图片的url
                it1.putExtra("urlList", grfPicUrls);//图片的url集合
                startActivity(it1);
                break;

            case R.id.photo2:
                Intent it2 = new Intent(mContext, ShowBigPicActivity.class);
                it2.putExtra("picUrl", grfPicUrls.get(2));//当前位置图片的url
                it2.putExtra("urlList", grfPicUrls);//图片的url集合
                startActivity(it2);
                break;
            case R.id.btn_ok:
                updateReturnApply();
                break;

            default:
                break;
        }

    }

    /**
     * 退货更新接口
     */
    private void updateReturnApply() {
        String wholeUrl = AppUrl.host + AppUrl.RETURN_UPDATE;
        String requestBodyData = ParamBuild.updateGrfV2(soId, pmInfoId, deliveryCompanyName, waybillCode);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, updateListener);
    }

    BaseRequestListener updateListener = new JsonRequestListener() {

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
            showShortToast("请求成功");
            GlobalParam.AfterSaleListNeedRefresh = true;
            Intent it = new Intent(WaitForSendGoodsActivity.this, WaitForMerchanSignActivity.class);
            it.putExtra("grfApplyTime", grfApplyTime);
            it.putExtra("grfCode", grfCode);
            it.putExtra("remark", remark);
            it.putExtra("returnGoodsNum", returnGoodsNum);
            it.putExtra("grfAmount", grfAmount);
            it.putExtra("grfReason", grfReason);
            it.putExtra("deliveryCompanyName", deliveryCompanyName);
            it.putExtra("waybillCode", waybillCode);
            it.putExtra("grfPicUrls", grfPicUrls);
            it.putExtra("waybillType", waybillType);
            it.putExtra("storePhone", storePhone);
            it.putExtra("storeAddress", storeAddress);
            startActivity(it);
            finish();
        }
    };

    /**
     * 撤销售后申请
     */
    private void asyncCancleRequest() {
        String wholeUrl = AppUrl.host + AppUrl.CANCLE_GRF;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("grfId", grfId);
        logout("======撤销售后申请========" + grfId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, cancleListener);
    }

    BaseRequestListener cancleListener = new JsonRequestListener() {

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
            showShortToast("撤销售后申请成功");
            cancle_request.setVisibility(View.GONE);
            logistics_regoods.setVisibility(View.GONE);
            GlobalParam.AfterSaleListNeedRefresh = true;
            finish();
        }
    };

}
