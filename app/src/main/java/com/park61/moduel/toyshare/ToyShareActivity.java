package com.park61.moduel.toyshare;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.toyshare.adapter.JoyShareListAdapter;
import com.park61.moduel.toyshare.bean.JoyShareItem;
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
 * Created by shubei on 2017/6/17.
 */
public class ToyShareActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;

    private JoyShareListAdapter mAdapter;
    private List<JoyShareItem> mDataList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_toyshare_list);
    }

    @Override
    public void initView() {
        setPagTitle("玩具共享");

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
    }

    @Override
    public void initData() {
        mDataList = new ArrayList<JoyShareItem>();
        mAdapter = new JoyShareListAdapter(mContext, mDataList);
        mPullRefreshListView.setAdapter(mAdapter);
        asyncGetDataList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetDataList();
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, ToyDetailsActivity.class);
                it.putExtra("id", mDataList.get(position - 1).getId());
                startActivity(it);
            }
        });
    }

    /**
     * 请求消息数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_BOX_SERIESLIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
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
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            List<JoyShareItem> currentPageList = new ArrayList<JoyShareItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mDataList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                JoyShareItem c = gson.fromJson(actJot.toString(), JoyShareItem.class);
                currentPageList.add(c);
            }
            mDataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
}
