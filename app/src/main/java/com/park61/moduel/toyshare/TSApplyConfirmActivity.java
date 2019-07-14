package com.park61.moduel.toyshare;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.toyshare.bean.TSAddrBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/6/19.
 */

public class TSApplyConfirmActivity extends BaseActivity {

    private TSAddrBean mTSAddrBean;
    private List<TSAddrBean> addrList;
    private JSONArray addrJay;

    private ImageView img_toy;
    private TextView browse_name_tv, browse_number_tv, browse_address_tv, tv_toy_name, tv_share_price, tv_real_price;
    private Button btn_confirm;
    private View addr_area;
    private int index = 0;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_apply_confirm);
    }

    @Override
    public void initView() {
        setPagTitle("确认申请");
        browse_name_tv = (TextView) findViewById(R.id.browse_name_tv);
        browse_number_tv = (TextView) findViewById(R.id.browse_number_tv);
        browse_address_tv = (TextView) findViewById(R.id.browse_address_tv);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);
        tv_share_price = (TextView) findViewById(R.id.tv_share_price);
        tv_real_price = (TextView) findViewById(R.id.tv_real_price);
        img_toy = (ImageView) findViewById(R.id.img_toy);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        addr_area = findViewById(R.id.addr_area);
    }

    @Override
    public void initData() {
        addrList = new ArrayList<>();
        asyncDetailsAddrData();
    }

    @Override
    public void initListener() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncToyBoxApply();
            }
        });
        addr_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TSAddrListActivity.class);
                it.putExtra("ADDRLIST", addrJay.toString());
                it.putExtra("index", index);
                startActivityForResult(it, 1);
            }
        });
    }

    /**
     * 请求详情数据
     */
    private void asyncDetailsAddrData() {
        String wholeUrl = AppUrl.host + AppUrl.GETUSERTOYADDRESSLIST;
        /*Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);*/
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

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
            addrJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < addrJay.length(); i++) {
                TSAddrBean bean = gson.fromJson(addrJay.optJSONObject(i).toString(), TSAddrBean.class);
                addrList.add(bean);
            }
            mTSAddrBean = addrList.get(0);
            setDataToView();
        }
    };

    private void setDataToView() {
        setAddrData();
        ImageManager.getInstance().displayImg(img_toy, GlobalParam.CurJoy.getIntroductionImg());
        tv_toy_name.setText(GlobalParam.CurJoy.getName());
        tv_share_price.setText(GlobalParam.CurJoy.getSharePrice());
        tv_real_price.setText(GlobalParam.CurJoy.getMarketPrice());
        ViewInitTool.lineText(tv_real_price);
    }

    private void setAddrData() {
        browse_name_tv.setText(mTSAddrBean.getAddress());
        browse_number_tv.setText(mTSAddrBean.getPhone());
        String addrStr = "";
        addrStr += mTSAddrBean.getProvinceName();
        if (!mTSAddrBean.getProvinceName().equals(mTSAddrBean.getCityName())) {
            addrStr += mTSAddrBean.getCityName();
        }
        if (!mTSAddrBean.getCityName().equals(mTSAddrBean.getCountyName())) {
            addrStr += mTSAddrBean.getCountyName();
        }
        addrStr += mTSAddrBean.getTownName() + mTSAddrBean.getAddress();
        browse_address_tv.setText(addrStr);
    }

    private void asyncToyBoxApply() {
        String wholeUrl = AppUrl.host + AppUrl.TOYBOXAPPLY;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seriesId", GlobalParam.CurJoy.getId());
        map.put("addressId", mTSAddrBean.getId());
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
            Intent it = new Intent(mContext, TSApplySuccessActivity.class);
            it.putExtra("tip", "申请领取成功");
            it.putExtra("details", jsonResult.optString("receiveTimeEnd") + "之前");
            it.putExtra("bottom_tip", "快去" + mTSAddrBean.getAddress() + "扫码领取");
            it.putExtra("shareApplyId", jsonResult.optString("id"));
            startActivity(it);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            index = data.getIntExtra("index", 0);
            mTSAddrBean = addrList.get(index);
            setAddrData();
        }
    }
}
