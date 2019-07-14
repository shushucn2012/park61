package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.common.tool.ViewInitTool;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改手机号-输入新手机号
 * Created by shushucn2012 on 2016/11/21.
 */
public class ModifyPNInputNewActivity extends BaseActivity{

    private EditText edit_new_phone_num;
    private Button btn_next;

    private String newPhoneNum;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_modify_pn_inputnew);
    }

    @Override
    public void initView() {
        edit_new_phone_num = (EditText) findViewById(R.id.edit_new_phone_num);
        btn_next = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhoneNum = edit_new_phone_num.getText().toString().trim();
                if(TextUtils.isEmpty(newPhoneNum)){
                    showShortToast("请输入新手机号！");
                    return;
                }
                if(!RegexValidator.isMobile(newPhoneNum)){
                    showShortToast("请输入正确的手机号！");
                    return;
                }
                asyncSendVcode();
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_new_phone_num);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_next);
    }

    /**
     * 发送验证码
     * asyncSendVcode
     */
    private void asyncSendVcode() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.SEND_BINDING_VERIFY_CODE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", newPhoneNum);
        String requestBodyData =  ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vcodelistener);
    }

    BaseRequestListener vcodelistener = new JsonRequestListener() {

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
            showShortToast("验证码已发送！");
            Intent it = new Intent(mContext, ModifyPNVerifyNewActivity.class);
            it.putExtra("NEW_PHONE_NUM", newPhoneNum);
            startActivity(it);
        }
    };
}
