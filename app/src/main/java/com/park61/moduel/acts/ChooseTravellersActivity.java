package com.park61.moduel.acts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.moduel.acts.adapter.TravellerListAdapter;
import com.park61.moduel.acts.bean.TravellerBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushucn2012 on 2016/7/28.
 */
public class ChooseTravellersActivity extends BaseActivity{

    private TextView tv_add, tv_adult_num, tv_child_num;
    private ListView lv_travellers;
    private Button btn_confirm;

    private List<TravellerBean> travellerList;
    private TravellerListAdapter mAdapter;
    private String adultNeedNum;
    private String childNeedNum;
    public static boolean NeedRefresh = true;//是否需要刷新

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_choose_travellers);
    }

    @Override
    public void initView() {
        tv_add = (TextView) findViewById(R.id.tv_add);
        lv_travellers = (ListView) findViewById(R.id.lv_travellers);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_adult_num = (TextView) findViewById(R.id.tv_adult_num);
        tv_child_num = (TextView) findViewById(R.id.tv_child_num);
    }

    @Override
    public void initData() {
        NeedRefresh = true;
        adultNeedNum = getIntent().getStringExtra("ADULT_NUM");
        tv_adult_num.setText("0/"+adultNeedNum);
        childNeedNum = getIntent().getStringExtra("CHILD_NUM");
        tv_child_num.setText("0/"+childNeedNum);
        travellerList = new ArrayList<TravellerBean>();
        mAdapter = new TravellerListAdapter(mContext, travellerList, false);
        mAdapter.setOnOnTravellerChosenLsner(new TravellerListAdapter.OnTravellerChosenLsner() {
            @Override
            public void onChosen() {
                int chosenAdult = 0;
                int chosenChild = 0;
                logout("size===="+travellerList.size());
                for (TravellerBean travellerBean :travellerList){
                    if(travellerBean.getChecked()) {
                        if(travellerBean.getPersonType() == 0){
                            chosenAdult++;
                        }else if(travellerBean.getPersonType() == 1){
                            chosenChild++;
                        }
                    }
                }
                tv_adult_num.setText(chosenAdult+"/"+adultNeedNum);
                tv_child_num.setText(chosenChild+"/"+childNeedNum);
            }
        });
        lv_travellers.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, TravellerAddActivity.class);
                startActivityForResult(it, 0);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int chosenAdult = 0;
                int chosenChild = 0;
                ArrayList<TravellerBean> checkedList = new ArrayList<TravellerBean>();
                for(int i=0;i<travellerList.size();i++){
                    TravellerBean travellerBean = travellerList.get(i);
                    if(travellerBean.getChecked()) {
                        checkedList.add(travellerBean);
                        if(travellerBean.getPersonType() == 0){
                            chosenAdult++;
                        }else if(travellerBean.getPersonType() == 1){
                            chosenChild++;
                        }
                    }
                }
                if(chosenAdult < Integer.parseInt(adultNeedNum)){
                    showShortToast("成人未选完！");
                    return;
                }
                if(chosenAdult > Integer.parseInt(adultNeedNum)){
                    showShortToast("选择的成人数量超过报名数量！");
                    return;
                }
                if(chosenChild < Integer.parseInt(childNeedNum)){
                    showShortToast("儿童未选完！");
                    return;
                }
                if(chosenChild > Integer.parseInt(childNeedNum)){
                    showShortToast("选择的儿童数量超过报名数量！");
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("clist",checkedList);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NeedRefresh) {
            asyncGetTravellers();
            NeedRefresh = false;
        }
    }

    /**
     * 获取旅客列表
     */
    private void asyncGetTravellers() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TRAVELLERS_LIST;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if(!TextUtils.isEmpty(errorMsg)) {
                showShortToast(errorMsg);
            }else{
                showShortToast("暂无数据");
            }
            travellerList.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            tv_adult_num.setText("0/" + adultNeedNum);
            tv_child_num.setText("0/" + childNeedNum);
            if (jay == null) {
                travellerList.clear();
                mAdapter.notifyDataSetChanged();
                showShortToast("暂无数据");
            } else {
                travellerList.clear();
                for (int i = 0; i < jay.length(); i++) {
                    JSONObject jot = jay.optJSONObject(i);
                    TravellerBean t = gson.fromJson(jot.toString(), TravellerBean.class);
                    travellerList.add(t);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /*private boolean isTravellerInOldList(TravellerBean tb) {
        for (TravellerBean travellerBean : travellerList) {
            if (travellerBean.getId() == tb.getId()) {
                return true;
            }
        }
        return false;
    }*/

}
