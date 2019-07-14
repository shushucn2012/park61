package com.park61.moduel.firsthead;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;
import com.park61.moduel.firsthead.adapter.FhCommtAdapter;
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
 * Created by shubei on 2017/6/12.
 */

public class FhDetailsCommtActivity  extends BaseActivity implements FhCommtAdapter.OnReplyClickedLsner{

    private int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;

    private long itemId;
    private int source;
    private FhCommtAdapter mAdapter;
    private List<EvaluateItemInfo> mList;
    private String content;
    private long parentId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fhdetails_commt);
    }

    @Override
    public void initView() {
        setPagTitle("评论");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        itemId = getIntent().getLongExtra("itemId", -1);
        source = getIntent().getIntExtra("source", -1);

        mList = new ArrayList<>();
        mAdapter = new FhCommtAdapter(mContext, mList, FhDetailsCommtActivity.this);
        actualListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, actualListView);

        asyncCommtData();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncCommtData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncCommtData();
            }
        });
    }

    /**
     * 请求评论数据
     */
    private void asyncCommtData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_COMMENT_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", source);//source：评论对象类型（1需求，2图文，3视频）当不传入source时默认为梦想屋的评论
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshListView.onRefreshComplete();
            ArrayList<EvaluateItemInfo> currentPageList = new ArrayList<EvaluateItemInfo>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            for (int i = 0; i < cmtJay.length(); i++) {
                JSONObject actJot = cmtJay.optJSONObject(i);
                EvaluateItemInfo p = gson.fromJson(actJot.toString(), EvaluateItemInfo.class);
                currentPageList.add(p);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onComtClicked(long parentId) {
        this.parentId = parentId;
        //asyncCommitComt();
    }
}
