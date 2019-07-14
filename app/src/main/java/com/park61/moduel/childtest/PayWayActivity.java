package com.park61.moduel.childtest;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.bean.TrandCodeBean;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.CirButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nieyu on 2017/12/12.
 */

public class PayWayActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_price,tv_trandcode;
    private ImageView iv_wx_check,iv_alicheck,iv_back;
    int wxcheck=0;
    int alicheck=0;
    CirButton tv_expert_focus_btn;
    @Override
    public void setLayout() {
setContentView(R.layout.activity_payway);
    }

    @Override
    public void initView() {
        iv_back=(ImageView) findViewById(R.id.iv_back);
        tv_price=(TextView) findViewById(R.id.tv_price);
        tv_price.setText("¥"+getIntent().getIntExtra("saleprice",-1));
        tv_trandcode=(TextView) findViewById(R.id.tv_trandcode);
        iv_wx_check=(ImageView) findViewById(R.id.iv_wx_check);
        iv_wx_check.setOnClickListener(this);
        iv_alicheck=(ImageView) findViewById(R.id.iv_alicheck);
        iv_alicheck.setOnClickListener(this);
        tv_expert_focus_btn=(CirButton)findViewById(R.id.tv_expert_focus_btn);
        tv_expert_focus_btn.setOnClickListener(this);
    }

    @Override
    public void initData() {
ansyGetTranSubmit();
    }

    @Override
    public void initListener() {

    }

    public void ansyGetTranSubmit(){
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TRAND_SUBMIT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaServiceId", getIntent().getIntExtra("eaServiceId",-1));

        map.put("uuid",getMyUUID());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, trandsubmitlisenter);

    }

    BaseRequestListener trandsubmitlisenter = new JsonRequestListener() {

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
            TrandCodeBean tob = gson.fromJson(jsonResult.toString(), TrandCodeBean.class);
            tv_trandcode.setText("订单号："+tob.getData());
            Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", Long.parseLong(tob.getData()));
            it.putExtra("payMoney", getIntent().getIntExtra("saleprice",-1)+"");
            startActivity(it);

        }
    };

//    private String getMyUUID(){
//
//        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//        final String tmDevice, tmSerial, tmPhone, androidId;
//
//        tmDevice = "" + tm.getDeviceId();
//
//        tmSerial = "" + tm.getSimSerialNumber();
//
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//
//        String uniqueId = deviceUuid.toString();
//
//        return uniqueId;
//
//    }
private String getMyUUID(){

    UUID uuid = UUID.randomUUID();

    String uniqueId = uuid.toString();
    uniqueId=uniqueId.replace("-","");
//    Log.d("debug","----->UUID"+uuid);/

    return uniqueId;

}

    @Override
    public void onClick(View view) {
        int viewid=view.getId();
        if(R.id.iv_alicheck==viewid){
            if(alicheck==0){
                alicheck=1;
                wxcheck=0;
                iv_alicheck.setImageDrawable(getResources().getDrawable(R.drawable.check_cicle));
                iv_wx_check.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_cicle));
            }else if(alicheck==1){
                alicheck=0;
                iv_alicheck.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_cicle));
                wxcheck=1;
                iv_wx_check.setImageDrawable(getResources().getDrawable(R.drawable.check_cicle));
            }
        }else if(R.id.iv_wx_check==viewid){
          if(wxcheck==0){
              wxcheck=1;
              iv_wx_check.setImageDrawable(getResources().getDrawable(R.drawable.check_cicle));
              alicheck=0;
              iv_alicheck.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_cicle));
          }else if(wxcheck==1){
              alicheck=1;
              wxcheck=0;
              iv_wx_check.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_cicle));
              iv_alicheck.setImageDrawable(getResources().getDrawable(R.drawable.check_cicle));
          }
        }else if(R.id.iv_back==viewid){
            finish();
        }else if(R.id.tv_expert_focus_btn==viewid){
            if(wxcheck==1||alicheck==1){
                if(wxcheck==1){
                    showShortToast("微信支付");
                }else {
                    showShortToast("支付宝支付");
                }
            }else {
                showShortToast("请选择支付方式");
            }

        }
    }
}
