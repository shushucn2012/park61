package com.park61.moduel.acts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shushucn2012 on 2016/7/28.
 */
public class TravellerAddActivity extends BaseActivity{

    private static final int REQ_CHOOSE_PAPER_TYPE = 0;//选择证据类型
    private static final int REQ_CHOOSE_TRAVELLER_TYPE = 1;//选择旅客类型
    private EditText et_traveller_name, et_paper_no, tv_paper_type, tv_traveller_type;
    private Button btn_save;
    private View area_choose_paper_type, area_choose_traveller_type;

    private String contactsName, credentialsNo;
    private int credentialsType; // 证件类型 (0:身份证;1:护照)
    private int personType; // 人员类型(0:成人;1:小孩)

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_traveller_add);
    }

    @Override
    public void initView() {
        et_traveller_name = (EditText) findViewById(R.id.et_traveller_name);
        et_paper_no = (EditText) findViewById(R.id.et_paper_no);
        tv_paper_type = (EditText) findViewById(R.id.tv_paper_type);
        tv_paper_type.setEnabled(false);
        tv_traveller_type = (EditText) findViewById(R.id.tv_traveller_type);
        tv_traveller_type.setEnabled(false);
        btn_save = (Button) findViewById(R.id.btn_save);

        area_choose_paper_type = findViewById(R.id.area_choose_paper_type);
        area_choose_traveller_type = findViewById(R.id.area_choose_traveller_type);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        area_choose_paper_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, MySimpleChoosePage.class);
                it.putExtra("PAGE_TITLE", "证件类型");
                ArrayList<String> slist = new ArrayList<String>();
                slist.add("身份证");
                slist.add("护照");
                it.putStringArrayListExtra("LIST_DATA", slist);
                it.putExtra("CHOSEN_ITEM", tv_paper_type.getText().toString());
                startActivityForResult(it, REQ_CHOOSE_PAPER_TYPE);
            }
        });
        area_choose_traveller_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, MySimpleChoosePage.class);
                it.putExtra("PAGE_TITLE", "旅客类型");
                ArrayList<String> slist = new ArrayList<String>();
                slist.add("成人");
                slist.add("儿童");
                it.putStringArrayListExtra("LIST_DATA", slist);
                it.putExtra("CHOSEN_ITEM", tv_traveller_type.getText().toString());
                startActivityForResult(it, REQ_CHOOSE_TRAVELLER_TYPE);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }
                asyncAddTraveller();
            }
        });
    }

    protected boolean validate() {
        contactsName = et_traveller_name.getText().toString().trim();
        if (tv_paper_type.getText().toString().equals("身份证")) {
            credentialsType = 0;
        } else if (tv_paper_type.getText().toString().equals("护照")){
            credentialsType = 1;
        }
        credentialsNo = et_paper_no.getText().toString().trim();
        if (tv_traveller_type.getText().toString().equals("成人")) {
            personType = 0;
        } else if (tv_traveller_type.getText().toString().equals("儿童")){
            personType = 1;
        }
        if (TextUtils.isEmpty(contactsName)) {
            showShortToast("姓名不能为空！");
            return false;
        }
        if (contactsName.length()<2) {
            showShortToast("姓名不能小于2个字符！");
            return false;
        }
        if (!RegexValidator.isTravellerName(contactsName)) {
            showShortToast("姓名输入有误！姓名必须是全中文或者全英文");
            return false;
        }
        if (TextUtils.isEmpty(credentialsNo)) {
            showShortToast("证件号码不能为空！");
            return false;
        }
        if(credentialsType == 0 && !RegexValidator.isIDCard(credentialsNo)){
            showShortToast("身份证号输入有误！");
            return false;
        }
        return true;
    }

    /**
     * 添加旅客信息
     */
    private void asyncAddTraveller() {
        String wholeUrl = AppUrl.host + AppUrl.INSERT_TRAVELLER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contactsName", contactsName);
        map.put("credentialsType", credentialsType);
        map.put("credentialsNo", credentialsNo);
        map.put("personType", personType);
        String requestBodyData = ParamBuild.buildParams(map);
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
            showShortToast("添加成功！");
            ChooseTravellersActivity.NeedRefresh = true;
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK || data == null){ // 取消或者无数据返回时不处理
            return;
        }
        if(requestCode == REQ_CHOOSE_PAPER_TYPE ){
            tv_paper_type.setText(data.getStringExtra("RETURN_DATA"));
        }else if(requestCode == REQ_CHOOSE_TRAVELLER_TYPE){
            tv_traveller_type.setText(data.getStringExtra("RETURN_DATA"));
        }
    }
}
