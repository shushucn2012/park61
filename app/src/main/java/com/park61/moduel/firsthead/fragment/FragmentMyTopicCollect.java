package com.park61.moduel.firsthead.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.FirstHeadChildBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMyTopicCollect extends BaseFragment {

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 8;
    private int totalPage = 100;
    private List<FirstHeadBean> dataList = new ArrayList<>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private LRecyclerView rv_firsthead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_topic_search_result, container, false);
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
        asyncGetFirstHeadData();
    }

    @Override
    public void initListener() {
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 0;
                asyncGetFirstHeadData();
            }
        });
        rv_firsthead.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (PAGE_NUM < totalPage) {
                    PAGE_NUM++;
                    asyncGetFirstHeadData();
                } else {
                    rv_firsthead.setNoMore(true);
                }
            }
        });
    }

    private void asyncGetFirstHeadData() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.CONTENT_COLLECT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
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
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
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
        JSONArray jayList = jsonResult.optJSONArray("rows");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 0 && (jayList == null || jayList.length() <= 0)) {
            dataList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            ViewInitTool.setListEmptyTipByDefaultPic(parentActivity, rv_firsthead, "暂无数据");
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            dataList.clear();
        }
        ArrayList<FirstHeadBean> currentPageList = new ArrayList<>();
        totalPage = jsonResult.optInt("pageCount") - 1;
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            FirstHeadChildBean p = gson.fromJson(jot.toString(), FirstHeadChildBean.class);
            FirstHeadBean fb = new FirstHeadBean();
            fb.setClassifyType(p.getContentType());
            fb.setComposeType(p.getCoverType());
            fb.setIssuerName(p.getAuthorName());
            fb.setItemCommentNum(p.getCommentNum());
            fb.setItemTitle(p.getTitle());
            fb.setItemReadNum(p.getViewNum());
            fb.setItemId(p.getId());

            List<MediaBean> mediaList = new ArrayList<>();
            if (!TextUtils.isEmpty(p.getCoverImg1()))
                mediaList.add(new MediaBean("", p.getCoverImg1()));

            if (!TextUtils.isEmpty(p.getCoverImg2()))
                mediaList.add(new MediaBean("", p.getCoverImg2()));

            if (!TextUtils.isEmpty(p.getCoverImg3()))
                mediaList.add(new MediaBean("", p.getCoverImg3()));

            if (0 == p.getCoverType()) {
                fb.setComposeType(2);
            } else if (1 == p.getCoverType()) {
                fb.setComposeType(1);
            } else if (2 == p.getCoverType()) {
                fb.setComposeType(3);
            }

            fb.setItemMediaList(mediaList);
            currentPageList.add(fb);
        }
        dataList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

}
