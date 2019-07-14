package com.park61.moduel.toyshare;

import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.toyshare.adapter.JoyStateListAdapter;
import com.park61.moduel.toyshare.bean.TSProgressBean;
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
 * Created by shubei on 2017/6/20.
 */

public class TSApplyScheduleActivity extends BaseActivity {

    private int id;
    private List<TSProgressBean> stateList;
    private JoyStateListAdapter adapter;

    private ListView lv_state;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_apply_schedule);
    }

    @Override
    public void initView() {
        setPagTitle("申领进程");
        lv_state = (ListView) findViewById(R.id.lv_state);
    }

    @Override
    public void initData() {
        stateList = new ArrayList<>();
        adapter = new JoyStateListAdapter(mContext, stateList);
        lv_state.setAdapter(adapter);
        id = getIntent().getIntExtra("id", -1);
        asyncGetDatas();
    }

    @Override
    public void initListener() {

    }

    private void asyncGetDatas() {
        String wholeUrl = AppUrl.host + AppUrl.GETTOYAPPLYPROCESSLIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray actJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < actJay.length(); i++) {
                stateList.add(gson.fromJson(actJay.optJSONObject(i).toString(), TSProgressBean.class));
            }
            adapter.notifyDataSetChanged();
        }
    };
}
