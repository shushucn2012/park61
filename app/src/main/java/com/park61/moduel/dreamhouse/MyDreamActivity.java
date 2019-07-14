package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.dreamhouse.adapter.MyDreamListAdapter;
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
 * 我的梦想列表界面
 */
public class MyDreamActivity extends BaseActivity implements MyDreamListAdapter.OnClickLsner {
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private MyDreamListAdapter mAdapter;
    private ArrayList<DreamItemInfo> mList;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 6;// 评论列表每页条数

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dreamhouse_mydream);
    }

    @Override
    public void initView() {
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
        mAdapter = new MyDreamListAdapter(mContext, mList);
        mAdapter.setOnClickLsner(this);
        actualListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        PAGE_NUM = 1;
        asyncGetRequireList();
    }

    @Override
    public void initListener() {

    }

    /**
     * 我的梦想列表
     */
    protected void asyncGetRequireList() {
        String wholeUrl = AppUrl.host + AppUrl.REQUIREMEN_TLIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
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
            listStopLoadView();
            ArrayList<DreamItemInfo> currentPageList = new ArrayList<DreamItemInfo>();
            JSONArray jay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyView(mContext, actualListView, "空空的耶！发布梦想吧~", R.drawable.quexing, null,
                        100, 95);
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

    private void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    @Override
    public void giveUp(final int position) {
        dDialog.showDialog("提示", "小主，您确定要放弃梦想吗？",
                "否", "是", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        onRefresh();
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        Long requirementPredictionId = mList.get(position).getRequirementPredictionId();
                        logout("======我的梦想===requirementPredictionId======"+requirementPredictionId);
                        asyncGiveUpRequirement(mList.get(position).getId(),requirementPredictionId);
                    }
                });
    }

//    @Override
//    public void update(int position) {
//
//    }

    @Override
    public void detail(int position) {
        Intent intent = new Intent(mContext,DreamDetailActivity.class);
        intent.putExtra("requirementId", mList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void dreamPay(int position) {
        Intent it = new Intent(mContext, ActDetailsActivity.class);
        it.putExtra("actTempId",mList.get(position).getActTemplateId());
        it.putExtra("reqPredId",mList.get(position).getRequirementPredictionId());
        logout("========我的梦想列表=====requirementPredictionId======"+mList.get(position).getRequirementPredictionId());
        startActivity(it);
    }

    @Override
    public void againJoin(int position) {
        Intent joinIntent = new Intent(mContext, ConfirmJourneyActivity.class);
        joinIntent.putExtra("requirementId", mList.get(position).getId());
        startActivity(joinIntent);
    }

    private void onRefresh() {
        PAGE_NUM = 1;
        asyncGetRequireList();
    }
    /**
     * 放弃梦想
     */
    protected void asyncGiveUpRequirement(Long requirementId,Long requirementPredictionId){
        String wholeUrl = AppUrl.host + AppUrl.GIVE_UP_REQUIREMENT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        map.put("requirementPredictionId", requirementPredictionId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, giveUpListener);
    }
    BaseRequestListener giveUpListener = new JsonRequestListener() {
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
            onRefresh();
        }
    };
}
