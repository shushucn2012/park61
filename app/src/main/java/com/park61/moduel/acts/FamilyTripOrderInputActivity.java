package com.park61.moduel.acts;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.adapter.TravellerListAdapter;
import com.park61.moduel.acts.bean.TravellerBean;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shushucn2012 on 2016/7/28.
 */
public class FamilyTripOrderInputActivity extends BaseActivity {

    private static final int REQ_GET_TRAVELLERS = 1;
    private static final int REQ_GET_INSURANCE = 0;
    private View area_choose_insurance, area_traveller_input, area_input_adultnum, area_input_chidlnum;
    private TextView tv_com_travellers_label,tv_adult_num, tv_child_num, tv_insurance_name,
            tv_insurance_price, tv_total_money, tv_price_unit;
    private Button btn_submit;
    private TextView et_adult, et_child;
    private CheckBox chk_xieyi;
    private ListView lv_travellers_chosen;

    private long applyId;
    private ApplyActItem applyInfo;
    private ArrayList<Long> contactorList = new ArrayList<Long>();
    private int isBuyInsurance = 1;
    private ArrayList<BabyItem> checkedList;// 获取选择的孩子列表

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_familytrip_orderinput);
    }

    @Override
    public void initView() {
        area_choose_insurance = findViewById(R.id.area_choose_insurance);
        tv_com_travellers_label = (TextView) findViewById(R.id.tv_com_travellers_label);
        tv_adult_num = (TextView) findViewById(R.id.tv_adult_num);
        tv_child_num = (TextView) findViewById(R.id.tv_child_num);
        tv_insurance_name = (TextView) findViewById(R.id.tv_insurance_name);
        tv_insurance_price = (TextView) findViewById(R.id.tv_insurance_price);
        tv_price_unit = (TextView) findViewById(R.id.tv_price_unit);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_adult = (TextView) findViewById(R.id.et_adult);
        et_child = (TextView) findViewById(R.id.et_child);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        chk_xieyi = (CheckBox) findViewById(R.id.chk_xieyi);
        area_traveller_input = findViewById(R.id.area_traveller_input);
        lv_travellers_chosen = (ListView) findViewById(R.id.lv_travellers_chosen);
    }

    @Override
    public void initData() {
        applyId = getIntent().getLongExtra("applyId", -1l);
        checkedList = (ArrayList<BabyItem>) getIntent().getSerializableExtra("childList");
        asyncGetFamilyTripInfo();
    }

    @Override
    public void initListener() {
        area_choose_insurance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(applyInfo.getInsuranceId()==null){
                    return;
                }
                Intent it = new Intent(mContext, ChooseInsuranceActivity.class);
                it.putExtra("name", applyInfo.getInsuranceName());
                it.putExtra("price", applyInfo.getTotalInsurance()+"");
                boolean isNeed = false;
                if(isBuyInsurance == 0){
                    isNeed = false;
                }else{
                    isNeed = true;
                }
                it.putExtra("IS_NEED", isNeed);
                startActivityForResult(it, REQ_GET_INSURANCE);
            }
        });
        tv_com_travellers_label.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ChooseTravellersActivity.class);
                it.putExtra("ADULT_NUM", applyInfo.getApplyAdultsNumber());
                it.putExtra("CHILD_NUM", applyInfo.getApplyChildrenNumber());
                startActivityForResult(it, REQ_GET_TRAVELLERS);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chk_xieyi.isChecked()){
                    showShortToast("您还未勾选同意‘保险经纪委托协议’");
                    return;
                }
                if(CommonMethod.isListEmpty(contactorList)){
                    showShortToast("您还未填写旅客信息");
                    return;
                }
                asyncDoApply();
            }
        });
        et_adult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ChooseTravellersActivity.class);
                it.putExtra("ADULT_NUM", applyInfo.getApplyAdultsNumber());
                it.putExtra("CHILD_NUM", applyInfo.getApplyChildrenNumber());
                startActivityForResult(it, REQ_GET_TRAVELLERS);
            }
        });
        et_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ChooseTravellersActivity.class);
                it.putExtra("ADULT_NUM", applyInfo.getApplyAdultsNumber());
                it.putExtra("CHILD_NUM", applyInfo.getApplyChildrenNumber());
                startActivityForResult(it, REQ_GET_TRAVELLERS);
            }
        });
    }

    /**
     * 获取用户孩子列表
     */
    private void asyncGetFamilyTripInfo() {
        //String wholeUrl = AppUrl.host + AppUrl.GET_ACT_APPLY_DETAIL;
        String wholeUrl = AppUrl.host + AppUrl.GET_ACT_ORDER_DETAILS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
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
            applyInfo = gson.fromJson(jsonResult.toString(), ApplyActItem.class);
            logout("applyInfo.getApplyAdultsNumber()======"+applyInfo.getApplyAdultsNumber());
            tv_adult_num.setText(applyInfo.getApplyAdultsNumber());
            tv_child_num.setText(applyInfo.getApplyChildrenNumber());
            if(applyInfo.getInsuranceId()==null){
                isBuyInsurance = 0;
                tv_insurance_name.setText("无");
                tv_insurance_price.setText("");
                tv_price_unit.setVisibility(View.GONE);
            }else {
                tv_insurance_name.setText(applyInfo.getInsuranceName());
                tv_insurance_price.setText(applyInfo.getTotalInsurance() + "");
            }
            tv_total_money.setText("￥"+applyInfo.getTotalPrice());
        }
    };

    /**
     * 提交信息
     */
    private void asyncDoApply() {
        String wholeUrl = AppUrl.host + AppUrl.FAMILY_TRIP_APPLY;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("activityId", applyInfo.getActId());
        map.put("isBuyInsurance", isBuyInsurance);
        if(isBuyInsurance != 0){
            map.put("insuranceId", applyInfo.getInsuranceId());
            map.put("insuranceName", applyInfo.getInsuranceName());
            map.put("totalInsurance", applyInfo.getTotalInsurance());
        }
        JSONArray jay = new JSONArray();
        for (Long cId : contactorList) {
            JSONObject jot = new JSONObject();
            try {
                jot.put("contactsId", cId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jay.put(jot);
        }
        map.put("list", jay.toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            Intent it = new Intent(mContext, ActOrderConfirmActivity.class);
            it.putExtra("applyId", applyId);
            it.putExtra("childList", checkedList);
            startActivity(it);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK || data == null){
            return;
        }
        if(requestCode == REQ_GET_TRAVELLERS){
            area_traveller_input.setVisibility(View.GONE);
            lv_travellers_chosen.setVisibility(View.VISIBLE);
            ArrayList<TravellerBean> slist = (ArrayList<TravellerBean>) data.getSerializableExtra("clist");
            TravellerListAdapter mAdapter = new TravellerListAdapter(mContext, slist, true);
            lv_travellers_chosen.setAdapter(mAdapter);
            contactorList.clear();
            for (int i=0;i<slist.size();i++){
                contactorList.add(slist.get(i).getId());
            }
        } else if(requestCode == REQ_GET_INSURANCE){
            boolean isNeed = data.getBooleanExtra("IS_NEED",false);
            if(isNeed){
                isBuyInsurance = 1;
                tv_insurance_name.setText(applyInfo.getInsuranceName());
                tv_insurance_price.setText(applyInfo.getTotalInsurance()+"");
                tv_price_unit.setVisibility(View.VISIBLE);
            }else{
                isBuyInsurance = 0;
                tv_insurance_name.setText("我不需要随行保险");
                tv_insurance_price.setText("");
                tv_price_unit.setVisibility(View.GONE);
            }
        }
    }
}
