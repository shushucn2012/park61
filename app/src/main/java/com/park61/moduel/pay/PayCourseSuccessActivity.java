package com.park61.moduel.pay;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ShareTool;
import com.park61.moduel.me.MyBabyCourseActivity;
import com.park61.moduel.shophome.bean.MyCourseDetailsBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/7/24.
 */

public class PayCourseSuccessActivity extends BaseActivity {

    private long orderId;
    private String tip;

    private TextView tv_details;
    private Button btn_finish, btn_continue;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pay_course_success);
    }

    @Override
    public void initView() {
        setPagTitle("支付成功");
        tv_details = (TextView) findViewById(R.id.tv_details);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_continue = (Button) findViewById(R.id.btn_continue);
    }

    @Override
    public void initData() {
        orderId = getIntent().getLongExtra("orderId", -1);
        asyncGetOrderPaySuccess();
    }

    @Override
    public void initListener() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyBabyCourseActivity.class));
                finish();
            }
        });
    }

    private void asyncGetOrderPaySuccess() {
        String wholeUrl = AppUrl.host + AppUrl.orderPaySuccess;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, sLsner);
    }

    BaseRequestListener sLsner = new JsonRequestListener() {

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
            tip = jsonResult.optString("data");
            tv_details.setText(tip.substring(0, 3) + " " + tip.substring(3));
            initShareViewAndData();
        }
    };

    private void initShareViewAndData() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_fx_pyq:
                        ShareTool.shareToWeiXin(mContext, 1);
                        break;
                    case R.id.img_fx_wx:
                        ShareTool.shareToWeiXin(mContext, 0);
                        break;
                    case R.id.img_fx_qq:
                        ShareTool.shareToQQ(mContext);
                        break;
                    case R.id.img_fx_qzone:
                        ShareTool.shareToQzone(mContext);
                        break;
                }
            }
        };
        findViewById(R.id.img_fx_pyq).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_wx).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_qq).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_qzone).setOnClickListener(clickListener);
    }

}
