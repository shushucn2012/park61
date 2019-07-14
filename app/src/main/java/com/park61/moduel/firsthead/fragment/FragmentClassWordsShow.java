package com.park61.moduel.firsthead.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.bean.BannerItem;
import com.park61.moduel.firsthead.ParentalDetailActivity;
import com.park61.moduel.firsthead.adapter.ClassWordAdapter;
import com.park61.moduel.firsthead.adapter.ParentalAdapter;
import com.park61.moduel.firsthead.bean.TeachClassNotice;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by shubei on 2017/8/30.
 */

public class FragmentClassWordsShow extends BaseFragment {

    private LRecyclerView noticeRlv;
    private LRecyclerViewAdapter mAdapter;

    private List<TeachClassNotice> mList;
    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 6;
    private int totalPage = 100;
    private BGABanner banner;
    private View headView;
    //幼儿园id
    private int groupId;
    private int classId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_class_word, container, false);
        //活动banner
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.class_word_banner, null);
        banner = (BGABanner) headView.findViewById(R.id.banner);
        groupId = getArguments().getInt("groupId", -1);
        classId = getArguments().getInt("classId", -1);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        noticeRlv = (LRecyclerView) parentView.findViewById(R.id.notice_rlv);

        //老师活动通知列表
        mList = new ArrayList<>();
        ClassWordAdapter adapter = new ClassWordAdapter(parentActivity, mList);
        mAdapter = new LRecyclerViewAdapter(adapter);
        noticeRlv.setLayoutManager(new LinearLayoutManager(parentActivity));
        noticeRlv.setAdapter(mAdapter);
        mAdapter.addHeaderView(headView);
    }

    @Override
    public void onResume() {
        super.onResume();
        asyncGetList();

        //获取亲子活动banner
        asyncGetActivity();
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        noticeRlv.setOnRefreshListener(()->{
            PAGE_NUM = 0;
            noticeRlv.setNoMore(false);
            asyncGetList();
        });
        noticeRlv.setOnLoadMoreListener(()->{
            if(PAGE_NUM < totalPage -1){
                PAGE_NUM++;
                asyncGetList();
            }else{
                noticeRlv.setNoMore(true);
            }
        });
    }

    /**
     * 获取活动banner
     */
    private void asyncGetActivity(){
        String realUrl = AppUrl.NEWHOST_HEAD + AppUrl.classWordBanner;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teachGroupId", groupId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(realUrl, Request.Method.POST, requestBodyData, 0, bannerListener);
    }

    BaseRequestListener bannerListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            //jsonResult 获取数据
            JSONArray list = jsonResult.optJSONArray("list");
            if(list != null && list.length() >0){
                setClassBanner(list);
            }else{
                headView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            headView.setVisibility(View.GONE);
        }
    };

    private void setClassBanner(JSONArray list){
        List<BannerItem> bannerList = new ArrayList<>();
        for(int i=0; i<list.length(); i++){
            JSONObject j = list.optJSONObject(i);
            BannerItem b = new BannerItem();
            b.setImg(j.optString("coverUrl"));
            b.setId(j.optInt("id"));
            bannerList.add(b);
        }

        banner.setData(bannerList,null);
        banner.setAdapter(new ParentalAdapter());
        banner.setDelegate((bgaBanner, itemView, model, position) -> {
            Intent it = new Intent(parentActivity, ParentalDetailActivity.class);
            it.putExtra("id", bannerList.get(position).getId());
            it.putExtra("classId", classId);
            startActivity(it);
        });
        banner.setVisibility(View.VISIBLE);
        if(bannerList.size() > 1){
            banner.setAutoPlayAble(true);
        }else{
            banner.setAutoPlayAble(false);
        }
    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.classNotice_noticeList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0 && CommonMethod.isListEmpty(mList)) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            noticeRlv.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            noticeRlv.refreshComplete(PAGE_SIZE);
            JSONArray jay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (jay == null || jay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                return;
            }
            if (PAGE_NUM == 0) {
                mList.clear();
            }
            totalPage = jsonResult.optInt("pageCount");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                TeachClassNotice itemInfo = gson.fromJson(jot.toString(), TeachClassNotice.class);
                mList.add(itemInfo);
            }
            mAdapter.notifyDataSetChanged();
        }
    };
}
