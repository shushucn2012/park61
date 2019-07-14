package com.park61.moduel.firsthead;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.firsthead.adapter.ParentalChooseBabyListAdapter;
import com.park61.moduel.firsthead.bean.SelectBabyBean;
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
import java.util.UUID;

/**
 * Created by chenlie on 2017/8/31.
 *
 */

public class ParentalChooseBabyActivity extends BaseActivity {

    private ListView lv_child;
    private Button btn_confirm;
    private int selectBabyId = -1;
    private int activityId;
    private long signOrderId;
    private int linkedBusinessId;
    private List<SelectBabyBean> babyList;
    private ParentalChooseBabyListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_parental_choose_baby);
    }

    @Override
    public void initView() {
        lv_child = (ListView) findViewById(R.id.lv_child);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {

        babyList = new ArrayList<>();
        activityId = getIntent().getIntExtra("activityId", -1);
        String json = getIntent().getStringExtra("list");
        if(!TextUtils.isEmpty(json)){
            //解析亲子活动能参加的，宝宝列表
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i< arr.length(); i++){
                    babyList.add(gson.fromJson(arr.optJSONObject(i).toString(), SelectBabyBean.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new ParentalChooseBabyListAdapter(mContext, babyList);
        lv_child.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //单选
                for (int j = 0; j < babyList.size(); j++) {
                    SelectBabyBean b = babyList.get(j);
                    if (j != position) {
                        b.setChoose(false);
                    } else {
                        selectBabyId = b.getId();
                        b.setChoose(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        btn_confirm.setOnClickListener(v -> {

            if (selectBabyId == -1) {
                showShortToast("请选择要参加活动的宝宝！");
            }else{
                asyncSignActivity();
            }
        });
    }

    private void asyncSignActivity(){
        String url = AppUrl.NEWHOST_HEAD + AppUrl.signActivity;
        Map<String, Object> map = new HashMap<>();
        map.put("childId", selectBabyId);
        map.put("partyId", activityId);
        map.put("uuid", UUID.randomUUID());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, signListener);
    }

    BaseRequestListener signListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            signOrderId = jsonResult.optLong("orderId");
            linkedBusinessId = jsonResult.optInt("id");
            //报名成功，去订单详情页面，复用ArmListActivity
            if(signOrderId > 0){
                goSignSuccess();
            }else{
                showShortToast("报名失败");
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void goSignSuccess(){
        Intent it = new Intent(this, ArmsListActivity.class);
        it.putExtra("signOrderId", signOrderId);
        it.putExtra("linkedBusinessId", linkedBusinessId);
        it.putExtra("isSignSuccess", true);
        it.putExtra("activityId", activityId);
        startActivity(it);
        finish();
    }

}
