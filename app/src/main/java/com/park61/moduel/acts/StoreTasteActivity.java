package com.park61.moduel.acts;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.ActStoreTasteAdapter;
import com.park61.moduel.acts.bean.ActItem;
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
 * 推荐体检游戏列表页面
 */
public class StoreTasteActivity extends BaseActivity {
    private List<ActItem> actDataList;
    private ActStoreTasteAdapter mAdapter;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private View block;
    private Long pmInfoId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_taste);
    }

    @Override
    public void initView() {
        block = findViewById(R.id.block);
        block.setVisibility(View.GONE);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        asyncActStoreTaste();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
//                        asyncActStoreTaste();
                    }
                });
        actualListView = mPullRefreshListView.getRefreshableView();
//        actualListView.setEmptyView(block);
    }

    @Override
    public void initData() {
        pmInfoId = getIntent().getLongExtra("pmInfoId", -1l);
        actDataList = new ArrayList<ActItem>();
        mAdapter = new ActStoreTasteAdapter(mContext, actDataList);
        actualListView.setAdapter(mAdapter);
        asyncActStoreTaste();
    }

    @Override
    public void initListener() {
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("id", actDataList.get(position - 1).getId());
                it.putExtra("actTempId", actDataList.get(position - 1).getRefTemplateId());
                startActivity(it);
            }
        });
    }

    /**
     * 到店体验游戏列表
     */
    private void asyncActStoreTaste() {
        String wholeUrl = AppUrl.host + AppUrl.ACT_STATE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmId", pmInfoId.toString());
        map.put("cityId", GlobalParam.chooseCityCode);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                listener);
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
            listStopLoadView();
            JSONArray actJay = jsonResult.optJSONArray("list");
            actDataList.clear();
            ArrayList<ActItem> currentPageList = new ArrayList<ActItem>();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ActItem c = gson.fromJson(actJot.toString(), ActItem.class);
                currentPageList.add(c);
            }
            actDataList.addAll(currentPageList);
            if(actDataList.size()<1){
                actualListView.setEmptyView(block);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }


}
