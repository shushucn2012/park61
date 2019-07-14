package com.park61.moduel.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.RegexValidator;
import com.park61.moduel.dreamhouse.bean.DreamFlagBean;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthInputActivity extends BaseActivity {

    private Button btn_next;
    private EditText edit_expertName, edit_identityNo, edit_industry, edit_resume, edit_organization, edit_title, edit_phone_num;
    private TextView tv_add_label, tv_label;

    private DreamFlagBean chosenFlag;
    private String expertName, identityNo, domainId, industry, resume, evidence, identityFrontUrl, identityBackUrl, organization, title;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_input);
    }

    @Override
    public void initView() {
        setPagTitle("申请认证");
        btn_next = (Button) findViewById(R.id.btn_next);
        edit_expertName = (EditText) findViewById(R.id.edit_expertName);
        edit_identityNo = (EditText) findViewById(R.id.edit_identityNo);
        edit_industry = (EditText) findViewById(R.id.edit_industry);
        edit_resume = (EditText) findViewById(R.id.edit_resume);
        edit_organization = (EditText) findViewById(R.id.edit_organization);
        edit_title = (EditText) findViewById(R.id.edit_title);
        tv_add_label = (TextView) findViewById(R.id.tv_add_label);
        tv_label = (TextView) findViewById(R.id.tv_label);
        edit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
    }

    @Override
    public void initData() {
        /*evidence = getIntent().getStringExtra("evidence");
        identityFrontUrl = getIntent().getStringExtra("identityFrontUrl");
        identityBackUrl = getIntent().getStringExtra("identityBackUrl");*/
    }

    @Override
    public void initListener() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                //asyncExpertAuth();

//                map.put("expertName", expertName);
//                map.put("identityNo", identityNo);
//                map.put("domainId", domainId);
//                map.put("resume", resume);
//                map.put("evidence", evidence);
//                map.put("identityFrontUrl", identityFrontUrl);
//                map.put("identityBackUrl", identityBackUrl);
//                map.put("industry", industry);
//                map.put("organization", organization);
//                map.put("title", title);
                Intent it = new Intent(mContext, AuthUpLoadActivity.class);
                it.putExtra("expertName", expertName);
                it.putExtra("identityNo", identityNo);
                it.putExtra("domainId", domainId);
                it.putExtra("resume", resume);
                it.putExtra("industry", industry);
                it.putExtra("organization", organization);
                it.putExtra("title", title);
                startActivity(it);
            }
        });
        tv_add_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AuthChooseLabelActivity.class);
                startActivityForResult(it, 0);
            }
        });
    }

    private boolean validate() {
        expertName = edit_expertName.getText().toString().trim();
        if (TextUtils.isEmpty(expertName)) {
            showShortToast("请填写姓名");
            return false;
        }
        if (!RegexValidator.isTravellerName(expertName)) {
            showShortToast("姓名输入有误");
            return false;
        }
        String phoneNum = edit_phone_num.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNum) && !RegexValidator.isMobile(phoneNum)) {
            showShortToast("手机号输入有误");
            return false;
        }
        identityNo = edit_identityNo.getText().toString().trim();
        if (TextUtils.isEmpty(identityNo)) {
            showShortToast("请填写身份证号");
            return false;
        }
        if (!RegexValidator.isIdentityCard(identityNo)) {
            showShortToast("身份证号输入有误");
            return false;
        }
        if (TextUtils.isEmpty(domainId)) {
            showShortToast("请选择标签");
            return false;
        }
        industry = edit_industry.getText().toString().trim();
        organization = edit_organization.getText().toString().trim();
        title = edit_title.getText().toString().trim();
        resume = edit_resume.getText().toString().trim();
        if (TextUtils.isEmpty(resume)) {
            showShortToast("请填写个人简介");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            chosenFlag = (DreamFlagBean) data.getSerializableExtra("flagBean");
            tv_label.setText(chosenFlag.getName());
            domainId = chosenFlag.getId();
        }
    }
}
