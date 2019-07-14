package com.park61.moduel.firsthead;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.bean.SearchImpointBean;
import com.park61.moduel.firsthead.bean.SearchImpointContentBean;
import com.park61.moduel.firsthead.bean.TeachDeatilBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.SharePopWin;
import com.park61.widget.textview.CirButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shubei on 2017/7/4.
 */

public class BlogerInfoActivity extends BaseActivity implements View.OnClickListener {

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 8;
    private long userId;
    private int totalPage = 100;
    private List<FirstHeadBean> dataList = new ArrayList<>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    //    private BlogerInfoBean infoBean;
//    private BCExpertListRefresh mBCExpertListRefresh;
    private TextView tv_teachername, tv_title;
    private LRecyclerView rv_firsthead;
    private TextView tv_expert_funs, tvContent, tvSet, tv_listtitle;
    private CirButton tv_expert_focus_btn;
    View cityhot_header_blank;
    ImageView iv_teachpic, iv_teahbackheader, iv_titlesharehearch, iv_back, iv_redshre, iv_empty;
    RelativeLayout ll_title, rl_parenetheader;
    int watch = 0;
    int id = 708;
    int destance = 0;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_blogerinfo);
    }

    @Override
    public void initView() {
//        id= getIntent().getIntExtra("teachId",-1);
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cityhot_header_blank = localLayoutInflater.inflate(R.layout.superteach_header, null, false);

        iv_empty = (ImageView) findViewById(R.id.iv_empty);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        rv_firsthead.setPullRefreshEnabled(false);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(mContext));

        rl_parenetheader = (RelativeLayout) cityhot_header_blank.findViewById(R.id.rl_parenetheader);
        tv_teachername = (TextView) cityhot_header_blank.findViewById(R.id.tv_teachername);
        tv_expert_focus_btn = (CirButton) cityhot_header_blank.findViewById(R.id.tv_expert_focus_btn);
        tv_expert_focus_btn.setOnClickListener(this);
        tv_expert_funs = (TextView) cityhot_header_blank.findViewById(R.id.tv_expert_funs);
        tvContent = (TextView) cityhot_header_blank.findViewById(R.id.tvContent);
        tvSet = (TextView) cityhot_header_blank.findViewById(R.id.tvSet);
        iv_teachpic = (ImageView) cityhot_header_blank.findViewById(R.id.iv_teachpic);
        iv_teahbackheader = (ImageView) cityhot_header_blank.findViewById(R.id.iv_teahbackheader);
        iv_teahbackheader.setOnClickListener(this);
        iv_titlesharehearch = (ImageView) cityhot_header_blank.findViewById(R.id.iv_titlesharehearch);
        iv_titlesharehearch.setOnClickListener(this);
        tv_listtitle = (TextView) cityhot_header_blank.findViewById(R.id.tv_listtitle);

        ll_title = (RelativeLayout) findViewById(R.id.ll_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_redshre = (ImageView) findViewById(R.id.iv_redshre);
        iv_redshre.setOnClickListener(this);

        rv_firsthead.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollY) {
                super.onScrolled(recyclerView, dx, scrollY);
                // 控制TOPBAR的渐变效果
                destance = destance + scrollY;
                if (destance > DevAttr.dip2px(mContext, 20)) {

                    iv_titlesharehearch.setVisibility(View.GONE);
                    iv_teahbackheader.setVisibility(View.GONE);
                    ll_title.setVisibility(View.VISIBLE);
                } else {
                    iv_titlesharehearch.setVisibility(View.VISIBLE);
                    iv_teahbackheader.setVisibility(View.VISIBLE);
                    ll_title.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void initData() {
//        userId = getIntent().getLongExtra("userId", -1);
        id = getIntent().getIntExtra("teacherId", -1);
        adapter = new FirstHeadAdapter(mContext, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerViewAdapter.addHeaderView(cityhot_header_blank);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        asyncGetFirstHeadData();
        asyncGetTeachDetail();
    }

    @Override
    public void initListener() {
//        mBCExpertListRefresh = new BCExpertListRefresh(new BCExpertListRefresh.OnReceiveDoneLsner() {
//            @Override
//            public void onGot(Intent intent) {
//                PAGE_NUM = 0;
//                asyncGetFirstHeadData();
//            }
//        });
//        mBCExpertListRefresh.registerReceiver(mContext);
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
                if (PAGE_NUM < totalPage - 1) {
                    PAGE_NUM++;
                    asyncGetFirstHeadData();
                } else {
                    rv_firsthead.setNoMore(true);
                }
            }
        });

    }

    private void asyncGetFirstHeadData() {
//        String wholeUrl = AppUrl.host + AppUrl.contentUserInfo;
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACH;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/trainer/getContentListPageByTrainerId";
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("curPage", PAGE_NUM);
//        map.put("pageSize", PAGE_SIZE);
//        map.put("userId", userId);
        map.put("pageIndex", PAGE_NUM);
        map.put("userId", id);
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
            //ViewInitTool.setListEmptyTipByDefaultPic(mContext, rv_firsthead, "暂无数据");
            iv_empty.setVisibility(View.VISIBLE);
            rl_parenetheader.setVisibility(View.GONE);
            rv_firsthead.setVisibility(View.GONE);
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            iv_empty.setVisibility(View.GONE);
            rl_parenetheader.setVisibility(View.VISIBLE);
            rv_firsthead.setVisibility(View.VISIBLE);
            dataList.clear();
        }
        ArrayList<FirstHeadBean> currentPageList = new ArrayList<>();
        SearchImpointBean searchImpointBean = gson.fromJson(jsonResult.toString(), SearchImpointBean.class);
        List<MediaBean> mediaBeenlist;
        totalPage = jsonResult.optInt("pageCount");
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

        tv_listtitle.setText("文章." + jsonResult.optInt("total") + "篇");
        dataList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

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

            } else {

            }
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_EXPERTLIST"));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mBCExpertListRefresh.unregisterReceiver(mContext);123
    }

    TeachDeatilBean teachDeatilBean;
    private SparseArray<Integer> mTextStateList = new SparseArray<>();
    private final int STATE_UNKNOW = -1;
    private final int STATE_NOT_OVERFLOW = 1; //文本行数不超过限定行数

    private final int STATE_COLLAPSED = 2; //文本行数超过限定行数,处于折叠状态

    private final int STATE_EXPANDED = 3; //文本行数超过限定行数,被点击全文展开
    private final int MAX_LINE_COUNT = 1;

    public void asyncGetTeachDetail() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHDETAIL;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/trainer/getTrainerDetail";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, teachdetaillisenter);
    }

    BaseRequestListener teachdetaillisenter = new JsonRequestListener() {

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
            teachDeatilBean = gson.fromJson(jsonResult.toString(), TeachDeatilBean.class);
            tv_title.setText(teachDeatilBean.getName());
            tv_teachername.setText(teachDeatilBean.getName());
            tv_expert_funs.setText(teachDeatilBean.getFansNum() + "粉丝");
            tvContent.setText(teachDeatilBean.getDescription() + "");
            ImageManager.getInstance().displayImg(iv_teachpic, teachDeatilBean.getBigPictureUrl());
            int state = mTextStateList.get(0, STATE_UNKNOW);
            //如果该item是第一次初始化，则去获取文本的行数
            if (state == STATE_UNKNOW) {
                tvContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回调会调用多次，获取完行数记得注销监听
                        tvContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (tvContent.getLineCount() > MAX_LINE_COUNT) {
                            tvContent.setMaxLines(MAX_LINE_COUNT);
                            tvSet.setVisibility(View.VISIBLE);
                            tvSet.setText("展开");
                            mTextStateList.put(0, STATE_COLLAPSED);
                        } else {
                            tvSet.setVisibility(View.GONE);
                            mTextStateList.put(0, STATE_NOT_OVERFLOW);
                        }
                        return true;
                    }
                });
                tvContent.setMaxLines(Integer.MAX_VALUE);
            } else {
                //如果之前已经初始化过了，则使用保存的状态，无需再获取一次
                switch (state) {
                    case STATE_NOT_OVERFLOW:
                        tvSet.setVisibility(View.GONE);
                        break;
                    case STATE_COLLAPSED:
                        tvContent.setMaxLines(MAX_LINE_COUNT);
                        tvSet.setVisibility(View.VISIBLE);
                        tvSet.setText("展开");
                        break;
                    case STATE_EXPANDED:
                        tvContent.setMaxLines(Integer.MAX_VALUE);
                        tvSet.setVisibility(View.VISIBLE);
                        tvSet.setText("收起");
                        break;
                }
            }
            tvSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int state = mTextStateList.get(0, STATE_UNKNOW);
                    if (state == STATE_COLLAPSED) {
                        tvContent.setMaxLines(Integer.MAX_VALUE);
                        tvSet.setText("收起");
                        mTextStateList.put(0, STATE_EXPANDED);
                    } else if (state == STATE_EXPANDED) {
                        tvContent.setMaxLines(MAX_LINE_COUNT);
                        tvSet.setText("展开");
                        mTextStateList.put(0, STATE_COLLAPSED);
                    }
                }
            });

            if (teachDeatilBean.getFocus()) {
                watch = 1;
                tv_expert_focus_btn.setColor(BlogerInfoActivity.this, R.color.g999999);
                tv_expert_focus_btn.setText("已关注");
            } else {
                watch = 0;
                tv_expert_focus_btn.setColor(BlogerInfoActivity.this, R.color.com_red);
                tv_expert_focus_btn.setText("+关注");
            }
        }
    };

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.tv_expert_focus_btn) {
            if (watch == 0) {
                anyscWatch();
            } else if (watch == 1) {
                dDialog.showDialog("提示", "确定不再关注此人？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        anyscCancelWatch();
                    }
                });
            }
        } else if (viewid == R.id.iv_back || viewid == R.id.iv_teahbackheader) {
            finish();
        } else if (viewid == R.id.iv_redshre || viewid == R.id.iv_titlesharehearch) {
            toShare();
        }
    }

    public void anyscWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHADDFORCUS;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/focus/addTrainerFocus";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, teachWatch);
    }

    BaseRequestListener teachWatch = new JsonRequestListener() {

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
            watch = 1;
            tv_expert_focus_btn.setColor(BlogerInfoActivity.this, R.color.g999999);
            tv_expert_focus_btn.setText("已关注");
            showShortToast("已关注");
        }
    };

    public void anyscCancelWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHCANCELFORCK;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/focus/delTrainerFocus";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, teachcancelWatch);
    }

    BaseRequestListener teachcancelWatch = new JsonRequestListener() {

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
            watch = 0;
            tv_expert_focus_btn.setColor(BlogerInfoActivity.this, R.color.com_red);
            tv_expert_focus_btn.setText("+关注");
            showShortToast("已取消关注");
        }
    };

    public void toShare() {
//        Toast.makeText(this,"我要分享",Toast.LENGTH_LONG).show();
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = "";

        shareUrl = "http://m.61park.cn/#/home/homeexpert/" + teachDeatilBean.getId();

        String picUrl = teachDeatilBean.getHeadPictureUrl();
        String title = teachDeatilBean.getName();
        String description = teachDeatilBean.getDescription();
        logout("shareUrl=====================" + shareUrl);
        logout("picUrl========================" + picUrl);
        logout("title=========================" + title);
        logout("description===================" + description);
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", picUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }
}
