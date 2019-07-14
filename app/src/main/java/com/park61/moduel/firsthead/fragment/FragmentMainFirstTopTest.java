package com.park61.moduel.firsthead.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.ExpertHeader;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.ExpertBean;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.service.broadcast.BCExpertListRefresh;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMainFirstTopTest extends BaseFragment {

    private int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 6;
    private int totalPage = 100;
    private List<ExpertBean> expertList = new ArrayList<>();
    private List<FirstHeadBean> dataList = new ArrayList<>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String singlePicFrom, multiPicFrom, adFrom;
    private ExpertHeader mExpertHeader;
    private BCExpertListRefresh mBCExpertListRefresh;
    private int curClickedExpertIndex;

    private LRecyclerView rv_firsthead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_topic_search_result, container, false);
        Log.out("========================FragmentMainFirstTop=onCreateView============================");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        rv_firsthead = (LRecyclerView) parentView.findViewById(R.id.rv_firsthead);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(parentActivity));
    }

    @Override
    public void initData() {
        adapter = new FirstHeadAdapter(parentActivity, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        asyncGetEXPERTLIST();
    }

    @Override
    public void initListener() {
        mBCExpertListRefresh = new BCExpertListRefresh(new BCExpertListRefresh.OnReceiveDoneLsner() {
            @Override
            public void onGot(Intent intent) {
                asyncGetEXPERTLIST();
            }
        });
        mBCExpertListRefresh.registerReceiver(parentActivity);
        rv_firsthead.setPullRefreshEnabled(false);
       /* rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncGetEXPERTLIST();
            }
        });*/
        rv_firsthead.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (PAGE_NUM < totalPage) {
                    getNextPage();
                } else {
                    rv_firsthead.setNoMore(true);
                }
            }
        });
    }

    public void refreshList() {
        PAGE_NUM = 1;
        asyncGetFirstHeadData();
    }

    public void getNextPage() {
        PAGE_NUM++;
        asyncGetFirstHeadData();
    }

    private void asyncGetEXPERTLIST() {
        String wholeUrl = AppUrl.host + AppUrl.EXPERTLIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", 1);
        map.put("pageSize", 4);
        map.put("needFansCnt", false);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
            JSONArray jayList = jsonResult.optJSONArray("list");
            expertList.clear();
            for (int i = 0; i < jayList.length(); i++) {
                JSONObject jot = jayList.optJSONObject(i);
                ExpertBean p = gson.fromJson(jot.toString(), ExpertBean.class);
                expertList.add(p);
            }
            if (expertList.size() >= 4) {
                if (mExpertHeader == null) {
                    mExpertHeader = new ExpertHeader(parentActivity, expertList, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.tv_expert_focus_one:
                                    curClickedExpertIndex = 0;
                                    break;
                                case R.id.tv_expert_focus_two:
                                    curClickedExpertIndex = 1;
                                    break;
                                case R.id.tv_expert_focus_three:
                                    curClickedExpertIndex = 2;
                                    break;
                                case R.id.tv_expert_focus_four:
                                    curClickedExpertIndex = 3;
                                    break;
                            }

                            if (expertList.get(curClickedExpertIndex).isFocus()) {
                                dDialog.showDialog("提示", "确定不再关注此人？", "取消", "确认", null, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        asyncCollectData(expertList.get(curClickedExpertIndex).isFocus(), expertList.get(curClickedExpertIndex).getUserId() + "");
                                        dDialog.dismissDialog();
                                    }
                                });
                            } else {
                                asyncCollectData(expertList.get(curClickedExpertIndex).isFocus(), expertList.get(curClickedExpertIndex).getUserId() + "");
                            }
                        }
                    });
                    mLRecyclerViewAdapter.addHeaderView(mExpertHeader);
                } else {
                    mExpertHeader.fillData(parentActivity, expertList);
                }
            }
            refreshList();
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
                mExpertHeader.unfocusView(parentActivity, curClickedExpertIndex);
                expertList.get(curClickedExpertIndex).setFocus(false);
            } else {
                mExpertHeader.focusView(parentActivity, curClickedExpertIndex);
                expertList.get(curClickedExpertIndex).setFocus(true);
            }
        }
    };

    private void asyncGetFirstHeadData() {
        String wholeUrl = AppUrl.host + AppUrl.VIDEO_SEARCH;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("interface_version", "1");//二期必须传1，一期默认不传
        map.put("curPage", PAGE_NUM);
        if (PAGE_NUM > 1) {
            map.put("singlePicFrom", singlePicFrom);
            map.put("multiPicFrom", multiPicFrom);
            map.put("adFrom", adFrom);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getLsner);
    }

    BaseRequestListener getLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            rv_firsthead.refreshComplete(PAGE_SIZE);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            parseJosnToShow(jsonResult);
        }
    };

    private void parseJosnToShow(JSONObject jsonResult) {
        JSONArray jayList = jsonResult.optJSONArray("list");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 1 && (jayList == null || jayList.length() <= 0)) {
            dataList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            ViewInitTool.setListEmptyTipByDefaultPic(parentActivity, rv_firsthead, "暂无数据");
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 1) {
            dataList.clear();
        }
        ArrayList<FirstHeadBean> currentPageList = new ArrayList<>();
        totalPage = jsonResult.optInt("totalPage");
        singlePicFrom = jsonResult.optString("singlePicFrom");
        multiPicFrom = jsonResult.optString("multiPicFrom");
        adFrom = jsonResult.optString("adFrom");
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            FirstHeadBean p = gson.fromJson(jot.toString(), FirstHeadBean.class);
            currentPageList.add(p);
        }
        dataList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBCExpertListRefresh.unregisterReceiver(parentActivity);
    }

}
