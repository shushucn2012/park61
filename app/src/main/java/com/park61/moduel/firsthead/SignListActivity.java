package com.park61.moduel.firsthead;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.databinding.ActivitySignListBinding;
import com.park61.moduel.firsthead.adapter.SignDataAdapter;
import com.park61.moduel.firsthead.bean.SignBabyBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2017/12/28.
 *
 */

public class SignListActivity extends BaseActivity {


    ActivitySignListBinding binding;
    List<SignBabyBean> list;
    LRecyclerViewAdapter mAdapter;
    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 7;
    private int totalPage = 100;
    private long activityId;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_list);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
        list = new ArrayList<>();
        SignDataAdapter adapter = new SignDataAdapter(this, list);
        mAdapter = new LRecyclerViewAdapter(adapter);
        binding.peopleList.setLayoutManager(new LinearLayoutManager(this));
        binding.peopleList.setAdapter(mAdapter);

        asyncPeopleList();
    }

    private void asyncPeopleList(){
        String url = AppUrl.NEWHOST_HEAD + AppUrl.joinPartyPeoples;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, peoplesListener);
    }

    BaseRequestListener peoplesListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            binding.peopleList.refreshComplete(PAGE_SIZE);
            totalPage = jsonResult.optInt("pageCount");

            parsePeopleList(jsonResult.optJSONArray("rows"));
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            binding.peopleList.refreshComplete(PAGE_SIZE);
            if(PAGE_NUM>0){
                PAGE_NUM--;
            }
        }
    };

    private void parsePeopleList(JSONArray arr){
        if(PAGE_NUM == 0){
            list.clear();
        }

        if(arr != null && arr.length() > 0){
            for(int i = 0; i<arr.length() ;i++){
                JSONObject j = arr.optJSONObject(i);
                SignBabyBean b = gson.fromJson(j.toString(), SignBabyBean.class);
                list.add(b);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initListener() {
        binding.setGoBack(v -> finish());

        binding.peopleList.setOnRefreshListener(()->{
            PAGE_NUM = 0;
            binding.peopleList.setNoMore(false);
            asyncPeopleList();
        });

        binding.peopleList.setOnLoadMoreListener(()->{
            if(PAGE_NUM < totalPage - 1){
                PAGE_NUM ++;
                asyncPeopleList();
            }else{
                binding.peopleList.setNoMore(true);
            }
        });
    }
}
