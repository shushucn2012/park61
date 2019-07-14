package com.park61.moduel.openmember;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.pay.MaMaPartnerXieyiActivity;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.pay.bean.MemberConfirmOrderVO;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CanRefreshProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 会员确认订单页面
 */
public class MemberOrderConfirmActivity extends BaseActivity {
    private long rechargeId;
    private String couponUseId;
    private MemberConfirmOrderVO info;
    private String inviteCode;

    private ImageView img_membertyp_logo;
    private TextView tv_membertype_name, tv_card_price, tv_child_name,
            tv_card_money, tv_canuse_coupon_count, tv_coupon_reduce_money,tv_xieyi,
            tv_total_price, tv_coupon_money, tv_final_money, tv_total_money, tv_invitecode;
    private Button btn_pay;
    private EditText edit_mark;
    private View area_coupon_choose, area_invitecode_choose;
    private CheckBox chk_xieyi;

    private CanRefreshProgressDialog cpDialog;
    private int isBindChild;//（0：无需绑定宝宝，1：需绑定宝宝）
    private View xieyi_area;
    private String contractLink;//超链接地址
    private long cardId;//卡id
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_member_orderconfirm);
    }

    @Override
    public void initView() {
        img_membertyp_logo = (ImageView) findViewById(R.id.img_membertyp_logo);
        tv_membertype_name = (TextView) findViewById(R.id.tv_membertype_name);
        tv_card_price = (TextView) findViewById(R.id.tv_card_price);
        tv_child_name = (TextView) findViewById(R.id.tv_child_name);
        tv_card_money = (TextView) findViewById(R.id.tv_card_money);
        tv_canuse_coupon_count = (TextView) findViewById(R.id.tv_canuse_coupon_count);
        tv_coupon_reduce_money = (TextView) findViewById(R.id.tv_coupon_reduce_money);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        edit_mark = (EditText) findViewById(R.id.edit_mark);
        area_coupon_choose = findViewById(R.id.area_coupon_choose);
        area_invitecode_choose = findViewById(R.id.area_invitecode_choose);
        tv_invitecode = (TextView) findViewById(R.id.tv_invitecode);

        xieyi_area = findViewById(R.id.xieyi_area);
        tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);
        chk_xieyi = (CheckBox) findViewById(R.id.chk_xieyi);

        cpDialog = new CanRefreshProgressDialog(mContext,
                new CanRefreshProgressDialog.OnRefreshClickedLsner() {

                    @Override
                    public void OnRefreshClicked() {
                        asyncGetMemberOrder();
                    }
                });
    }

    @Override
    public void initData() {
        isBindChild = getIntent().getIntExtra("isBindChild", 0);
        rechargeId = getIntent().getLongExtra("rechargeId", 0l);
        asyncGetMemberOrder();
    }

    @Override
    public void initListener() {
        area_coupon_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, MemberChooseCouponActivity.class);
                intent.putExtra("couponUseId", couponUseId);
                intent.putExtra("childId",info.getChildId());
                intent.putExtra("amount", info.getAmountPaid());
                intent.putExtra("cardId",cardId);
                intent.putExtra("rechargeId",rechargeId);
                startActivityForResult(intent, 1);
            }
        });
        area_invitecode_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncGetShopInviteCode();
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(xieyi_area.getVisibility()==View.VISIBLE){
                    if (!chk_xieyi.isChecked()) {
                        showShortToast("您未勾选妈妈合伙人购买协议！");
                        return;
                    }
                }
                asyncDoConfirm();
            }
        });

        tv_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MaMaPartnerXieyiActivity.class);
                intent.putExtra("url",contractLink);
                startActivity(intent);
            }
        });
    }
    /**
     * 获取订单详情
     */
    private void asyncGetMemberOrder() {
        String wholeUrl = AppUrl.host + AppUrl.GET_SAVE_COUPON_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rechargeId", rechargeId);
        if (!TextUtils.isEmpty(couponUseId)) {
            map.put("couponUseIds", couponUseId);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getMemberOrderLsner);
    }

    BaseRequestListener getMemberOrderLsner = new JsonRequestListener() {

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
            info = gson.fromJson(jsonResult.toString(), MemberConfirmOrderVO.class);
            cardId = info.getCardId();
            logout("=======会员确认订单页面==cardId====="+cardId);
            fillData();
            cpDialog.dialogDismiss();
        }
    };

    protected void fillData() {
        ImageManager.getInstance().displayImg( img_membertyp_logo,info.getCardPicUrl());
        tv_membertype_name.setText(info.getCardName() + " " + info.getCardLengthName());
        tv_card_price.setText("￥" + FU.strBFmt(info.getSubTotalPrice()));
        if (isBindChild == 0) {//小时卡和次卡不显示小孩名字
            tv_child_name.setVisibility(View.GONE);
        } else {
            tv_child_name.setVisibility(View.VISIBLE);
            tv_child_name.setText("宝宝：" + info.getChildName());
        }
        tv_card_money.setText("￥" + FU.strBFmt(info.getSubTotalPrice()));
        logout("========getUseCouponPrice=============="+info.getUseCouponPrice());
        if(info.getUseCouponPrice()==null){
            tv_coupon_reduce_money.setText("");
        }else{
            tv_coupon_reduce_money.setText("-￥" + FU.strBFmt(info.getUseCouponPrice()));
        }
        tv_total_price.setText("￥" + FU.strBFmt(info.getSubTotalPrice()));
        tv_coupon_money.setText("-￥" + FU.strBFmt(info.getUseCouponPrice()));
        tv_final_money.setText("￥" + FU.strBFmt(info.getTotalPrice()));
        tv_total_money.setText("￥" + FU.strBFmt(info.getTotalPrice()));
        if(info.getHasContract()==1){
            xieyi_area.setVisibility(View.VISIBLE);
        }else{
            xieyi_area.setVisibility(View.GONE);
        }
        if(info.getContractLink()!=null){
            contractLink = info.getContractLink();
        }
        logout("============contractLink==============="+info.getContractLink());
    }

    /**
     * 获取店铺店员推荐码列表
     */
    protected void asyncGetShopInviteCode() {
        String wholeUrl = AppUrl.host + AppUrl.GET_INVITECODES_BYSHOP;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", info.getShopId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getInviteCodesLsner);
    }

    BaseRequestListener getInviteCodesLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Intent it = new Intent(mContext, ChooseRecommendActivity.class);
            it.putExtra("PAGE_TITLE", "选择推荐码");
            ArrayList<String> slist = new ArrayList<String>();
            ArrayList<String> slist2 = new ArrayList<String>();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject item = jay.optJSONObject(i);
                slist.add(item.optString("name"));
                slist2.add(item.optString("recommenderCode"));
            }
            it.putStringArrayListExtra("LIST_DATA", slist);
            it.putStringArrayListExtra("LIST_DATA2", slist2);
            it.putExtra("CHOSEN_ITEM", tv_invitecode.getText().toString());
            startActivityForResult(it, 0);

        }
    };

    /**
     * 确认订单核销优惠券
     */
    private void asyncDoConfirm() {
        String wholeUrl = AppUrl.host + AppUrl.DO_CONFIRM_ORDER_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rechargeId", rechargeId);
        map.put("recommend", inviteCode);
        if (!TextUtils.isEmpty(couponUseId)) {
            map.put("couponUseIds", couponUseId);
        }
        map.put("uuid", UUID.randomUUID());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, doConfirmLsner);
    }

    BaseRequestListener doConfirmLsner = new JsonRequestListener() {

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
            rechargeId = Long.parseLong(jsonResult.optString("data"));
            Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", rechargeId);
            it.putExtra("payMoney", info.getTotalPrice() + "");
            startActivity(it);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            couponUseId = data.getStringExtra("couponUseId");
            asyncGetMemberOrder();
        } else if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            if(!TextUtils.isEmpty(data.getStringExtra("RETURN_DATA2"))){
                inviteCode = data.getStringExtra("RETURN_DATA2");
                tv_invitecode.setText(data.getStringExtra("RETURN_DATA") + "  " + data.getStringExtra("RETURN_DATA2"));
            }
            if(!TextUtils.isEmpty(data.getStringExtra("recommendCode"))){
                inviteCode = data.getStringExtra("recommendCode");
                tv_invitecode.setText(data.getStringExtra("recommendCode"));
            }
        }
    }
}
