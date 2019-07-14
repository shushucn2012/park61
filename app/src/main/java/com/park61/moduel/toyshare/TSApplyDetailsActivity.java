package com.park61.moduel.toyshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.CanBackWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.StringUtils;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.toyshare.bean.ApplyFeeBean;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.moduel.toyshare.bean.TSAddrBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/6/20.
 */

public class TSApplyDetailsActivity extends BaseActivity {

    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private TextView tv_state, tv_state_tip, browse_name_tv, browse_number_tv, browse_address_tv, tv_toy_name, tv_share_price,
            tv_real_price, tv_day_count, tv_day_amount, tv_manage_amount, tv_party_amount, tv_share_days, tv_share_reduce_amount,
            tv_lost_parts_num, tv_lost_parts_amount, tv_total_amount, tv_total_amount_label;
    private ImageView img_state, img_toy, img_rule;
    private Button btn_scan_get, btn_apply_back, btn_apply_again, btn_pay;
    private View area_money, area_lost, area_state_tip;

    private JoyApplyItem mJoyApplyItem;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_apply_details);
    }

    @Override
    public void initView() {
        setPagTitle("申领详情");
        tv_state = (TextView) findViewById(R.id.tv_state);
        img_state = (ImageView) findViewById(R.id.img_state);
        tv_state_tip = (TextView) findViewById(R.id.tv_state_tip);
        browse_name_tv = (TextView) findViewById(R.id.browse_name_tv);
        browse_number_tv = (TextView) findViewById(R.id.browse_number_tv);
        browse_address_tv = (TextView) findViewById(R.id.browse_address_tv);
        img_toy = (ImageView) findViewById(R.id.img_toy);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);
        tv_share_price = (TextView) findViewById(R.id.tv_share_price);
        tv_real_price = (TextView) findViewById(R.id.tv_real_price);
        btn_scan_get = (Button) findViewById(R.id.btn_scan_get);
        btn_apply_back = (Button) findViewById(R.id.btn_apply_back);
        btn_apply_again = (Button) findViewById(R.id.btn_apply_again);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        area_money = findViewById(R.id.area_money);
        tv_day_count = (TextView) findViewById(R.id.tv_day_count);
        tv_day_amount = (TextView) findViewById(R.id.tv_day_amount);
        tv_manage_amount = (TextView) findViewById(R.id.tv_manage_amount);
        tv_party_amount = (TextView) findViewById(R.id.tv_party_amount);
        tv_share_days = (TextView) findViewById(R.id.tv_share_days);
        tv_share_reduce_amount = (TextView) findViewById(R.id.tv_share_reduce_amount);
        tv_lost_parts_num = (TextView) findViewById(R.id.tv_lost_parts_num);
        tv_lost_parts_amount = (TextView) findViewById(R.id.tv_lost_parts_amount);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        area_lost = findViewById(R.id.area_lost);
        area_state_tip = findViewById(R.id.area_state_tip);
        img_rule = (ImageView) findViewById(R.id.img_rule);
        tv_total_amount_label = (TextView) findViewById(R.id.tv_total_amount_label);
    }

    @Override
    public void initData() {
        mJoyApplyItem = (JoyApplyItem) getIntent().getSerializableExtra("TOY_APPLY");
        setDateToView();
        asyncGetApplyFee();
    }

    //status状态:1:待领取;2:使用中;3:申请归还 4:待支付 5:已完成 -1已取消
    private void setDateToView() {
        tv_state.setText(mJoyApplyItem.getStatusName());
        if (mJoyApplyItem.getStatus().equals("1")) {//1:待领取
            img_state.setImageResource(R.drawable.beihuozhong);
            tv_state_tip.setText("快带你的玩具回家吧");
            btn_scan_get.setVisibility(View.VISIBLE);
            area_money.setVisibility(View.GONE);//隐藏计费区域
        } else if (mJoyApplyItem.getStatus().equals("2")) {//2:使用中
            img_state.setImageResource(R.drawable.jiaoyichenggong);
            tv_state_tip.setText("已经玩了 " + mJoyApplyItem.getUseTimeString());
            btn_apply_back.setVisibility(View.VISIBLE);//展示计费区域
            area_lost.setVisibility(View.GONE);//隐藏遗失区域
        } else if (mJoyApplyItem.getStatus().equals("3")) {//3:申请归还
            img_state.setImageResource(R.drawable.beihuozhong);
            tv_state_tip.setText("一共玩了 " + mJoyApplyItem.getUseTimeString());
        } else if (mJoyApplyItem.getStatus().equals("4")) {//4:待支付
            img_state.setImageResource(R.drawable.dengdaifukuan);
            tv_state_tip.setText("一共玩了 " + mJoyApplyItem.getUseTimeString());
            btn_pay.setVisibility(View.VISIBLE);
        } else if (mJoyApplyItem.getStatus().equals("5")) {//5:已完成
            tv_total_amount_label.setText("已支付金额");
            img_state.setImageResource(R.drawable.tuikuanchenggong);
            tv_state_tip.setText("一共玩了 " + mJoyApplyItem.getUseTimeString());
            btn_apply_again.setVisibility(View.VISIBLE);
        } else if (mJoyApplyItem.getStatus().equals("-1")) {//-1已取消
            img_state.setImageResource(R.drawable.liuchengjieshu);
            tv_state_tip.setText("申请取消");
            btn_apply_again.setVisibility(View.VISIBLE);
            area_money.setVisibility(View.GONE);//隐藏计费区域
        }

        setAddrData();
        setToyData();
        setFee();
    }

    private void setAddrData() {
        //店名
        browse_name_tv.setText(mJoyApplyItem.getToyShareReceiveAddressDTO().getAddress());
        //店电话
        browse_number_tv.setText(mJoyApplyItem.getToyShareReceiveAddressDTO().getPhone());
        //店详细地址-拼接
        String addrStr = "";
        TSAddrBean mTSAddrBean = mJoyApplyItem.getToyShareReceiveAddressDTO();
        addrStr += mTSAddrBean.getProvinceName();
        if (!mTSAddrBean.getProvinceName().equals(mTSAddrBean.getCityName())) {//省名和市名相同，不加市名
            addrStr += mTSAddrBean.getCityName();
        }
        if (!mTSAddrBean.getCityName().equals(mTSAddrBean.getCountyName())) {//市名和区名相同，不加区名
            addrStr += mTSAddrBean.getCountyName();
        }
        addrStr += mTSAddrBean.getTownName() + mTSAddrBean.getAddress();
        browse_address_tv.setText(addrStr);
    }

    private void setToyData() {
        ImageManager.getInstance().displayImg(img_toy, mJoyApplyItem.getToyShareBoxSeriesDTO().getIntroductionImg());
        tv_toy_name.setText(mJoyApplyItem.getToyShareBoxSeriesDTO().getName());
        tv_share_price.setText(mJoyApplyItem.getToyShareBoxSeriesDTO().getSharePrice());
        tv_real_price.setText(StringUtils.moneyUnit() + mJoyApplyItem.getToyShareBoxSeriesDTO().getMarketPrice());
        ViewInitTool.lineText(tv_real_price);
    }

    private void setFee() {

    }

    @Override
    public void initListener() {
        area_state_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TSApplyScheduleActivity.class);
                it.putExtra("id", mJoyApplyItem.getId());
                startActivity(it);
            }
        });
        btn_scan_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalParam.CurJoy = mJoyApplyItem.getToyShareBoxSeriesDTO();
                GlobalParam.CUR_TOYSHARE_APPLY_ID = mJoyApplyItem.getId();
                Intent intent = new Intent();
                intent.setClass(mContext, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        btn_apply_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TSToReturnActivity.class);
                it.putExtra("TOY_APPLY", mJoyApplyItem);
                startActivity(it);
            }
        });
        btn_apply_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ToyShareActivity.class);
                CommonMethod.startOnlyNewActivity(mContext, ToyShareActivity.class, it);
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
                it.putExtra("orderId", mJoyApplyItem.getOrderId());
                startActivity(it);
                TSMyApplyToyListActivity.NEED_FRESH = true;
                finish();
            }
        });
        img_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", "http://m.61park.cn/toy/toRuleDetail.do");
                startActivity(intent);
            }
        });
    }


    private void asyncGetApplyFee() {
        String wholeUrl = AppUrl.host + AppUrl.GETTOYBOXCOST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", mJoyApplyItem.getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tLsner);
    }

    BaseRequestListener tLsner = new JsonRequestListener() {

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
            ApplyFeeBean mApplyFeeBean = gson.fromJson(jsonResult.toString(), ApplyFeeBean.class);
            tv_day_count.setText("x" + mApplyFeeBean.getUseTimeLong());
            tv_day_amount.setText(StringUtils.moneyUnit() + mApplyFeeBean.getUseTimeAmount());
            tv_manage_amount.setText(StringUtils.moneyUnit() + mApplyFeeBean.getManageAmount());
            tv_party_amount.setText(StringUtils.moneyUnit() + mApplyFeeBean.getSubtotal());
            tv_share_days.setText("x" + mApplyFeeBean.getShareCount());
            tv_share_reduce_amount.setText("-" + StringUtils.moneyUnit() + mApplyFeeBean.getShareAmount());
            tv_lost_parts_num.setText("x" + (mApplyFeeBean.getLostFitNum() + mApplyFeeBean.getLostBoxNum()));
            tv_lost_parts_amount.setText(StringUtils.moneyUnit() + mApplyFeeBean.getLostAmount());
            tv_total_amount.setText(StringUtils.moneyUnit() + mApplyFeeBean.getPayAmount());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        }
    }
}
