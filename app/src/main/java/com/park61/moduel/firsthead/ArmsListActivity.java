package com.park61.moduel.firsthead;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.databinding.ActivityArmsListBinding;
import com.park61.databinding.ToyListHeadviewBinding;
import com.park61.moduel.firsthead.adapter.ToysAdapter;
import com.park61.moduel.firsthead.bean.ToyBean;
import com.park61.moduel.sales.ParentalOrderConfirmActivity;
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
 * Created by chenlie on 2017/12/29.
 * <p>
 * 点击一键购买装备跳到该 推荐列表页面
 * <p>
 * 装备列表是上个界面传过来的 jsonArray 的 String
 * <p>
 * 报名成功，订单详情可以复用该页面
 */

public class ArmsListActivity extends BaseActivity implements ToysAdapter.OnNumChanged {

    ActivityArmsListBinding binding;
    ToyListHeadviewBinding head;
    List<ToyBean> list;
    ToysAdapter adapter;
    LRecyclerViewAdapter lAdapter;
    long signOrderId = -1;
    int linkedBusinessId = -1;
    int activityId = -1;
    boolean isInputNum = false;

    @Override
    public void setLayout() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_arms_list);
        head = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.toy_list_headview, null, false);

    }

    @Override
    public void initView() {

        setPagTitle("推荐列表");

        list = new ArrayList<>();
        adapter = new ToysAdapter(this, list);
        adapter.setOnNumChangedListener(this);
        lAdapter = new LRecyclerViewAdapter(adapter);

        if(getIntent().getBooleanExtra("isSignSuccess", false)){
            setPagTitle("订单详情");
            head.orderStateArea.setVisibility(View.VISIBLE);
            head.setHeadState(getString(R.string.activity_sign_success));
            head.orderStateImg.setImageResource(R.drawable.jiaoyichenggong);
            lAdapter.addHeaderView(head.getRoot());
        }

        binding.toysLv.setLayoutManager(new LinearLayoutManager(this));
        binding.toysLv.setAdapter(lAdapter);
    }


    @Override
    public void onChanged(int position, int num) {
        ToyBean b = list.get(position);
        b.setNumSelect(num);
        lAdapter.notifyDataSetChanged();

        if(num != 0){
            String url = AppUrl.NEWHOST_HEAD + AppUrl.childNumSelect;
            Map<String, Object> map = new HashMap<>();
            map.put("numSelect", num);
            map.put("partyId", activityId);
            map.put("pmInfoIdList", b.getPmInfoIdList());
            String requestData = ParamBuild.buildParams(map);
            netRequest.startRequest(url, Request.Method.POST, requestData, 0, numListener);
        }
    }

    BaseRequestListener numListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    @Override
    public void initData() {
        //隐藏topbar右边功能
        binding.top.areaRight.setVisibility(View.GONE);
        //获取亲子活动报名id
        signOrderId = getIntent().getLongExtra("signOrderId", -1);
        linkedBusinessId = getIntent().getIntExtra("linkedBusinessId", -1);
        activityId = getIntent().getIntExtra("activityId", -1);
        adapter.setActivityId(activityId);

        if(signOrderId != -1){
            asyncHasBuyToys();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isInputNum){
            asyncToyList();
        }else{
            isInputNum = false;
        }
    }

    /**
     * 获取关联装备列表
     */
    private void asyncToyList() {
        String url = AppUrl.NEWHOST_HEAD + AppUrl.childActivityToys;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, toysListener);
    }

    BaseRequestListener toysListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if(arr != null && arr.length() >0){
                //刷新列表
                list.clear();
                for (int i = 0; i < arr.length(); i++) {
                    ToyBean b = gson.fromJson(arr.optJSONObject(i).toString(), ToyBean.class);
                    if(b.getNumSelect() == 0){
                        b.setNumSelect(1);
                    }
                    list.add(b);
                }
                lAdapter.notifyDataSetChanged();
                head.toylistTopArea.setVisibility(View.VISIBLE);
                binding.buyBt.setVisibility(View.VISIBLE);
            }else{
                head.toylistTopArea.setVisibility(View.GONE);
                binding.buyBt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };


    private void asyncHasBuyToys(){
        String url = AppUrl.NEWHOST_HEAD + AppUrl.hasBuyToys;
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", signOrderId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, buyToyListener);
    }

    BaseRequestListener buyToyListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            int state = jsonResult.optInt("data");
            //0为未购买 ,1 已购买
            if(state == 0){
                head.buyToyState.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {

        }
    };

    @Override
    public void initListener() {
        binding.toysLv.setPullRefreshEnabled(false);
        binding.toysLv.setLoadMoreEnabled(false);

        binding.setOneKeyBuy(v->{
            //一键购买
            goOrderActivity();
        });
    }

    private void goOrderActivity(){
        boolean hasSelected = false;
        StringBuilder sbId = new StringBuilder();
        StringBuilder sbCount = new StringBuilder();
        if(list.size()>0){
            for(int i=0; i<list.size(); i++){
                ToyBean b = list.get(i);
                if(b.getNumSelect()>0){
                    hasSelected = true;
                    sbId.append(b.getPmInfoId());
                    sbCount.append(b.getNumSelect());
                }else{
                    continue;
                }
                if(i<list.size()-1){
                    sbId.append(",");
                    sbCount.append(",");
                }
            }
        }
        if(hasSelected){
            Intent it = new Intent(this, ParentalOrderConfirmActivity.class);
            it.putExtra("partyId", activityId);
            it.putExtra("linkedBusinessId", linkedBusinessId);
            it.putExtra("pmInfoIdList", sbId.toString());
            it.putExtra("pmInfoCountList", sbCount.toString());
            startActivity(it);
            finish();
        }else{
            showShortToast("请先选择要购买的商品");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ToyNumActivity.RESULT_CODE){
            //更新填写的装备数量
            int p = data.getIntExtra("position", -1);
            int tempNum = data.getIntExtra("tempNum", -1);
            if(p != -1 && tempNum != -1){
                onChanged(p, tempNum);
            }
            isInputNum = true;
        }
    }
}
