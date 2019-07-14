package com.park61.moduel.firsthead;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.adapter.MyFunsListAdapter;
import com.park61.moduel.firsthead.bean.BlogerInfoBean;
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
 * Created by shubei on 2017/7/5.
 */

public class MyFunsListActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 15;

    private PullToRefreshListView mPullRefreshListView;
    private MyFunsListAdapter adapter;
    private List<BlogerInfoBean> eList;
    private int curClickedExpertIndex;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_myfuns_list);
    }

    @Override
    public void initView() {
        setPagTitle("我的粉丝");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetFansList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetFansList();
            }
        });
    }

    @Override
    public void initData() {
        eList = new ArrayList<>();
        adapter = new MyFunsListAdapter(mContext, eList, false);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetFansList();
    }

    @Override
    public void initListener() {
        adapter.setOnFocusClickedLsner(new MyFunsListAdapter.OnFocusClickedLsner() {
            @Override
            public void onFocus(int position) {
                curClickedExpertIndex = position;
                if (eList.get(curClickedExpertIndex).isFocus()) {
                    dDialog.showDialog("提示", "确定不再关注此人？", "取消", "确认", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asyncCollectData(eList.get(curClickedExpertIndex).isFocus(), eList.get(curClickedExpertIndex).getUserId() + "");
                            dDialog.dismissDialog();
                        }
                    });
                } else {
                    asyncCollectData(eList.get(curClickedExpertIndex).isFocus(), eList.get(curClickedExpertIndex).getUserId() + "");
                }
            }
        });
    }

    private void asyncGetFansList() {
        String wholeUrl = AppUrl.host + AppUrl.fansList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
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
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            List<BlogerInfoBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                eList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                eList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), BlogerInfoBean.class));
            }
            eList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    private void asyncCollectData(boolean isFocus, String itemId) {
        String wholeUrl = "";
        if (isFocus) {
            wholeUrl = AppUrl.host + AppUrl.DEL_COLLECTION;
        } else {
            wholeUrl = AppUrl.host + AppUrl.ADD_COLLECTION;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", 4);//来源：1游戏，2图文，3视频，4用户，关注对象为人时，传4
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, isFocus ? 1 : 0, cLsner);
    }

    BaseRequestListener cLsner = new JsonRequestListener() {

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
            if (requestId == 1) {
                eList.get(curClickedExpertIndex).setFocus(false);
                adapter.notifyDataSetChanged();
            } else {
                eList.get(curClickedExpertIndex).setFocus(true);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
