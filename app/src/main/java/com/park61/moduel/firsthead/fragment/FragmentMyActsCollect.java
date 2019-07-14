package com.park61.moduel.firsthead.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.me.adapter.FocusActListAdapter;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyActsCollect extends BaseFragment implements FocusActListAdapter.ToApplyCallBack {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private FocusActListAdapter adapter;
    private List<ActItem> actList;
    private TextView tv_top_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_me_actlist, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        parentView.findViewById(R.id.top_bar).setVisibility(View.GONE);
//        tv_top_title = (TextView) parentView.findViewById(R.id.tv_top_title);
//        tv_top_title.setText("关注的游戏");
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetFocusActs();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetFocusActs();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        asyncGetFocusActs();
    }

    @Override
    public void initData() {
        actList = new ArrayList<ActItem>();
        adapter = new FocusActListAdapter(parentActivity, actList, FragmentMyActsCollect.this);
        mPullRefreshListView.getRefreshableView().setAdapter(adapter);
    }

    @Override
    public void initListener() {
    }

    /**
     * 请求关注的游戏数据
     */
    private void asyncGetFocusActs() {
        String wholeUrl = AppUrl.host + AppUrl.GET_FOCUS_ACTS_END;
        String requestBodyData = ParamBuild.getActList(PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 1) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ActItem> currentPageList = new ArrayList<ActItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                actList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(parentActivity, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                actList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), ActItem.class));
            }
            actList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 停在列表控件加载框
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    @Override
    public void onApplyClicked(int pos) {
        Intent it = new Intent(parentActivity, ActDetailsActivity.class);
        it.putExtra("id", actList.get(pos).getId());
        it.putExtra("isFocus", true);
        parentActivity.startActivity(it);
    }

    @Override
    public void onShareClicked(int pos) {
        ActItem curAct = actList.get(pos);
        String shareUrl = String.format(AppUrl.ACT_SHARE_URL, curAct.getId() + "");
        String picUrl = curAct.getActCover();
        String title = getString(R.string.app_name);
        String description = curAct.getActTitle() + "\n游戏详情";
        ((BaseActivity)parentActivity).showShareDialog(shareUrl, picUrl, title, description);
    }

    @Override
    public void onComtClicked(int pos) {
        Intent it = new Intent(parentActivity, ActDetailsActivity.class);
        it.putExtra("id", actList.get(pos).getId());
        it.putExtra("isToComt", true);
        startActivity(it);
    }

}
