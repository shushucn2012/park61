package com.park61.moduel.firsthead.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.bean.SearchImpointBean;
import com.park61.moduel.firsthead.bean.SearchImpointContentBean;
import com.park61.moduel.firsthead.bean.SpecialTypeBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.service.broadcast.BCExpertListRefresh;
import com.park61.widget.pw.SharePopWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nieyu on 2017/11/17.
 */

public class SpecialTypeActivity extends BaseActivity implements View.OnClickListener {

    private int PAGE_NUM =0;
    private static final int PAGE_SIZE = 10;
    private long itemId;
    private int totalPage = 100;
    private List<FirstHeadBean> dataList = new ArrayList<>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    //    private BlogerInfoBean infoBean;
    private BCExpertListRefresh mBCExpertListRefresh;
    private TextView tv_title;
    private LRecyclerView rv_firsthead;


    View cityhot_header_blank;
    ImageView iv_teachpic, iv_back, iv_redshre,iv_empty;
    RelativeLayout ll_title,rr_specalheader;
    int watch = 0;
    int id = 15;
    int destance = 0;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_specialtype);
    }

    @Override
    public void initView() {
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cityhot_header_blank = localLayoutInflater.inflate(R.layout.specialheader, null, false);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        rv_firsthead.setPullRefreshEnabled(false);

        iv_empty = (ImageView) findViewById(R.id.iv_empty);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(mContext));


        iv_teachpic = (ImageView) cityhot_header_blank.findViewById(R.id.iv_teachpic);

        ll_title = (RelativeLayout) findViewById(R.id.ll_title);

                tv_title = (TextView) cityhot_header_blank.findViewById(R.id.tv_title);
        rr_specalheader = (RelativeLayout) cityhot_header_blank.findViewById(R.id.rr_specalheader);
        iv_back = (ImageView) cityhot_header_blank.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_redshre = (ImageView) cityhot_header_blank.findViewById(R.id.iv_redshre);
        iv_redshre.setOnClickListener(this);

        rv_firsthead.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollY) {
                super.onScrolled(recyclerView, dx, scrollY);
                // 控制TOPBAR的渐变效果
                destance = destance + scrollY;
                if (destance > DevAttr.dip2px(mContext, 20)) {
//                    ll_title.setVisibility(View.VISIBLE);
                } else {
//                    ll_title.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void initData() {
        itemId = getIntent().getLongExtra("itemId", -1);
//        itemId = 75;
        adapter = new FirstHeadAdapter(mContext, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerViewAdapter.addHeaderView(cityhot_header_blank);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
//        asyncGetTeachDetail();
        asyncGetSpecialDetail();
    }

    @Override
    public void initListener() {
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataList.clear();
                PAGE_NUM = 0;
                asyncGetSpecialDetail();

            }
        });
        rv_firsthead.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PAGE_NUM++;
                asyncGetSpecialDetail();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.iv_back) {
            finish();
        } else if (viewid == R.id.iv_redshre) {
            toShare();
        }
    }

    public void toShare() {
//        Toast.makeText(this, "我要分享", Toast.LENGTH_LONG).show();
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl="";

        shareUrl= "http://m.61park.cn/#/home/homeseminar/"+specialTypeBean.getTopicId();

        String picUrl =specialTypeBean.getDetailImg();
        String title = specialTypeBean.getTitle();
        String description = specialTypeBean.getIntro();
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", picUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }
    SpecialTypeBean specialTypeBean;
    public void asyncGetSpecialDetail() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.SPECIALDETAIL;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/contentTopic/getTopicDetail";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("topicId", itemId);
//        map.put("pageSize", PAGE_SIZE);
//        map.put("pageIndex", PAGE_NUM);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, speciallisenter);
    }
    BaseRequestListener speciallisenter = new JsonRequestListener() {

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
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            if(rv_firsthead!=null){
                rv_firsthead.refreshComplete(PAGE_SIZE);
            }
            JSONArray jayList=   jsonResult.optJSONObject("page").getJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jayList == null || jayList.length() <= 0)) {
                dataList.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyTipByDefaultPic(mContext, rv_firsthead, "暂无数据");
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                dataList.clear();
            }
            ArrayList<FirstHeadBean> currentPageList = new ArrayList<>();
            SearchImpointBean searchImpointBean = gson.fromJson(jsonResult.optJSONObject("page").toString(), SearchImpointBean.class);
            List<MediaBean>  mediaBeenlist;
//            totalPage = jsonResult.optInt("totalPage");
            for (int i = 0; i < jayList.length(); i++) {
                JSONObject jot = jayList.optJSONObject(i);
//            FirstHeadBean p = new Gson().fromJson(jot.toString(), FirstHeadBean.class);
                FirstHeadBean firstHeadBean = new FirstHeadBean();
                mediaBeenlist = new ArrayList<>();
                mediaBeenlist.clear();
                SearchImpointContentBean searchImpointContentBean = searchImpointBean.getRows().get(i);
                firstHeadBean.setComposeType(searchImpointContentBean.getCoverType());
                firstHeadBean.setContentType(searchImpointContentBean.getContentType());
                firstHeadBean.setIssuerName(searchImpointContentBean.getAuthorName());
                firstHeadBean.setCommentNum(searchImpointContentBean.getCommentNum());
                firstHeadBean.setItemReadNum(searchImpointContentBean.getViewNum());
                firstHeadBean.setItemTitle(searchImpointContentBean.getTitle());

                if (0 == searchImpointContentBean.getCoverType()) {
                    MediaBean mediaBean = new MediaBean();
                    mediaBean.setMediaUrl(searchImpointContentBean.getCoverImg1());
                    mediaBeenlist.add(mediaBean);
                    firstHeadBean.setComposeType(2);
                } else if (1 == searchImpointContentBean.getCoverType()) {
                    MediaBean mediaBean = new MediaBean();
                    mediaBean.setMediaUrl(searchImpointContentBean.getCoverImg1());
                    mediaBeenlist.add(mediaBean);
                    firstHeadBean.setComposeType(1);
                } else if (2 == searchImpointContentBean.getCoverType()) {
                    firstHeadBean.setComposeType(3);
                    MediaBean mediaBean1 = new MediaBean();
                    mediaBean1.setMediaUrl(searchImpointContentBean.getCoverImg1());
                    mediaBeenlist.add(mediaBean1);

                    MediaBean mediaBean2 = new MediaBean();
                    mediaBean2.setMediaUrl(searchImpointContentBean.getCoverImg2());
                    mediaBeenlist.add(mediaBean2);

                    MediaBean mediaBean3 = new MediaBean();
                    mediaBean3.setMediaUrl(searchImpointContentBean.getCoverImg3());
                    mediaBeenlist.add(mediaBean3);

                }
                firstHeadBean.setClassifyType(searchImpointContentBean.getContentType());
                firstHeadBean.setItemMediaList(mediaBeenlist);
                firstHeadBean.setViewNum(searchImpointContentBean.getViewNum());
                firstHeadBean.setItemId((long) searchImpointContentBean.getId());
                firstHeadBean.setItemtype("teach");
                firstHeadBean.setItemCommentNum(searchImpointContentBean.getCommentNum());
                currentPageList.add(firstHeadBean);
            }
            if (PAGE_NUM == jsonResult.optInt("pageCount") ) {
                if(adapter!=null){
                    //rv_firsthead.setLoadMoreEnabled(false);
                    rv_firsthead.setNoMore(true);
                }
            }
            if(jayList.length()==0){
                rv_firsthead.setVisibility(View.GONE);
                rr_specalheader.setVisibility(View.GONE);
                iv_empty.setVisibility(View.VISIBLE);
            }else {
                rv_firsthead.setVisibility(View.VISIBLE);
                rr_specalheader.setVisibility(View.VISIBLE);
                iv_empty.setVisibility(View.GONE);
            }
            dataList.addAll(currentPageList);
            mLRecyclerViewAdapter.notifyDataSetChanged();
            specialTypeBean = gson.fromJson(jsonResult.toString(), SpecialTypeBean.class);
            tv_title.setText( specialTypeBean.getTitle());
            ImageManager.getInstance().displayImg(iv_teachpic, specialTypeBean.getDetailImg());
        }
    };
}