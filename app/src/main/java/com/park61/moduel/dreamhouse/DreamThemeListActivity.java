package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.dreamhouse.adapter.DreamThemeListAdapter;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 梦想主题列表界面
 */
public class DreamThemeListActivity extends BaseActivity {
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private DreamThemeListAdapter mAdapter;
    private ArrayList<DreamItemInfo> mList;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 6;// 评论列表每页条数
    private String cityIds, classIds;
    private TextView tv_title;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dreamhouse_themelist);
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetRequireList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetRequireList();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        mList = new ArrayList<DreamItemInfo>();
        mAdapter = new DreamThemeListAdapter(mContext, mList);
        actualListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        cityIds = getIntent().getStringExtra("cityIds");
        classIds = getIntent().getStringExtra("classIds");
        tv_title.setText(getIntent().getStringExtra("requirementClassName"));
        PAGE_NUM = 1;
        asyncGetRequireList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent it = new Intent(mContext, DreamDetailActivity.class);
                it.putExtra("requirementId", mList.get(position-1).getId());
                startActivity(it);
            }
        });
    }

    /**
     * 我的梦想列表
     */
    protected void asyncGetRequireList() {
        String wholeUrl = AppUrl.host + AppUrl.THEME_REQUIREMENT_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        map.put("cityIds", cityIds);
        map.put("classIds", classIds);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<DreamItemInfo> currentPageList = new ArrayList<DreamItemInfo>();
            JSONArray jay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyTipByDefaultPic(mContext, actualListView, "暂无数据");
                mList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            if (PAGE_NUM == 1) {
                mList.clear();
            }
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            } else {
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                DreamItemInfo itemInfo = gson.fromJson(jot.toString(), DreamItemInfo.class);
                currentPageList.add(itemInfo);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

}
