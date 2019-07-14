package com.park61.moduel.firsthead.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.R;
import com.park61.VideoBaseActivity;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ScreenStatusController;
import com.park61.common.tool.ShareTool;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.MainHomeActivity;
import com.park61.moduel.firsthead.adapter.CommentAllAdapter;
import com.park61.moduel.firsthead.adapter.ContentRecommendAdapter;
import com.park61.moduel.firsthead.bean.CommentItemBean;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.bean.SearchImpointContentBean;
import com.park61.moduel.firsthead.bean.TextBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.CirButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nieyu on 2017/11/21.
 * modify by super on 2018/1/31
 */
public class VidoDeatilActivity extends VideoBaseActivity implements View.OnClickListener, CommentAllAdapter.OnReplyClickedLsner {

    private static final int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private int rootBottom = Integer.MIN_VALUE;

    private TextView tv_texttitles, tv_scannumber, tv_titlename, tv_tiem, tv_messagecount, tv_commentsend, tv_newcommend, tv_pricenum;
    private ImageView civ_icon, iv_picback, iv_admires, iv_attent, iv_weixin, iv_admirecicle, iv_weixinquan, iv_share;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private ListView rv_firsthead;
    private LinearLayout iv_admire;
    private LinearLayout ll_store;
    private CirButton tv_expert_focus_btn;
    private LinearLayout ll_commentbottom, ll_recommend, ll_newprice;
    private EditText et_commentwrite;
    private View cityhot_header_blank;
    private RelativeLayout rl_parent, rl_edit;

    private SearchImpointContentBean searchImpointContentBean;
    private List<FirstHeadBean> mlsit = new ArrayList<FirstHeadBean>();
    private List<SearchImpointContentBean> oList = new ArrayList<>();
    private long itemid;
    private CommentAllAdapter mAdapter;
    private List<CommentItemBean> mList;
    private AliyunPlayAuth.AliyunPlayAuthBuilder aliyunAuthInfoBuilder;
    private String authInfo;
    private String videoId;
    private TextBean textBean;
    private int parentid = -1;//父评论id
    private int isreply = 0;//是否是回复 0不是；1是

    @Override
    public void setLayout() {
        setContentView(R.layout.content_detaile);
    }

    @Override
    public void initView() {
        itemid = getIntent().getLongExtra("itemId", -1);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cityhot_header_blank = localLayoutInflater.inflate(R.layout.activity_vadiodetail, actualListView, false);
        actualListView.addHeaderView(cityhot_header_blank);
        iv_attent = (ImageView) findViewById(R.id.iv_attent);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
        iv_admirecicle = (ImageView) findViewById(R.id.iv_admirecicle);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        iv_admires = (ImageView) findViewById(R.id.iv_admires);
        iv_admire = (LinearLayout) findViewById(R.id.iv_admire);
        iv_admire.setOnClickListener(this);
        iv_picback = (ImageView) findViewById(R.id.iv_picback);
        iv_picback.setOnClickListener(this);
        ll_store = (LinearLayout) findViewById(R.id.ll_store);
        ll_store.setOnClickListener(this);

        ll_newprice = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_newprice);
        tv_pricenum = (TextView) cityhot_header_blank.findViewById(R.id.tv_pricenum);
        ll_recommend = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_recommend);
        tv_newcommend = (TextView) cityhot_header_blank.findViewById(R.id.tv_newcommend);
        rv_firsthead = (ListView) cityhot_header_blank.findViewById(R.id.rv_firsthead);
        tv_titlename = (TextView) cityhot_header_blank.findViewById(R.id.tv_titlename);
        civ_icon = (ImageView) cityhot_header_blank.findViewById(R.id.civ_icon);
        tv_scannumber = (TextView) cityhot_header_blank.findViewById(R.id.tv_scannumber);
        tv_texttitles = (TextView) cityhot_header_blank.findViewById(R.id.tv_texttitles);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        mAliyunVodPlayerView.setTitleBarCanShow(false);
        mAliyunVodPlayerView.setControlBarCanShow(false);
        tv_tiem = (TextView) cityhot_header_blank.findViewById(R.id.tv_tiem);

        tv_expert_focus_btn = (CirButton) cityhot_header_blank.findViewById(R.id.tv_expert_focus_btn);
        tv_expert_focus_btn.setOnClickListener(this);

        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        iv_weixin = (ImageView) findViewById(R.id.iv_weixin);
        iv_weixin.setOnClickListener(this);
        iv_weixinquan = (ImageView) findViewById(R.id.iv_weixinquan);
        iv_weixinquan.setOnClickListener(this);

        rl_parent = (RelativeLayout) findViewById(R.id.rl_parent);
        rl_parent.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mAdapter = new CommentAllAdapter(this, mList);
        actualListView.setAdapter(mAdapter);
        mAdapter.setOnReplyClickedLsner(this);
        anyscGetVido();
        ansycGetComment();
        ansygetRecomend();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                ansycGetComment();
            }
        });
        //锁屏监听
        mScreenStatusController = new ScreenStatusController(this);
        mScreenStatusController.setScreenStatusListener(new ScreenStatusController.ScreenStatusListener() {
            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {
            }
        });
        mScreenStatusController.startListen();
        tv_commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                sedCommend();
            }
        });
        et_commentwrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (view.getId() == R.id.et_commentwrite && hasfocus) {
                    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl.leftMargin = 15;
                    rl.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    rl.height = DevAttr.dip2px(mContext, 30);
                    rl.width = DevAttr.dip2px(mContext, 250);
                    rl_edit.setLayoutParams(rl);
                    ll_commentbottom.setVisibility(View.GONE);
                    tv_commentsend.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl.leftMargin = 15;
                    rl.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    rl.height = DevAttr.dip2px(mContext, 30);
                    rl.width = DevAttr.dip2px(mContext, 200);
                    rl_edit.setLayoutParams(rl);
                    ll_commentbottom.setVisibility(View.VISIBLE);
                    tv_commentsend.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.iv_picback) {
           /* int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            } else {
                finish();
            }*/
            finish();
        } else if (viewid == R.id.iv_admire) {
            if (!textBean.getPraise()) {
                ansyToAdmire();
            }
        } else if (viewid == R.id.ll_store) {
            if (!textBean.getFocus()) {
                anyscWatch();
            } else {
                anyscCancelWatch();
            }
        } else if (viewid == R.id.tv_expert_focus_btn) {
            if (!textBean.getFocusUser()) {//未关注
                anyscTeachWatch();
            } else {//已关注
                anyscTeachCancelWatch();
            }
        } else if (viewid == R.id.iv_weixinquan) {
            ShareTool.init("http://m.61park.cn/#/home/homeexpert/" + textBean.getAuthor().getId(), textBean.getCoverImg(), textBean.getTitle(), textBean.getIntro());
            ShareTool.shareToWeiXin(mContext, 1);
        } else if (viewid == R.id.iv_weixin) {
            ShareTool.init("http://m.61park.cn/#/home/homeexpert/" + textBean.getAuthor().getId(), textBean.getCoverImg(), textBean.getTitle(), textBean.getIntro());
            ShareTool.shareToWeiXin(mContext, 0);
        } else if (viewid == R.id.iv_share) {
            showShareDialog("http://m.61park.cn/#/home/homecontent/" + itemid, textBean.getCoverImg(), textBean.getTitle(), textBean.getIntro());
        }
    }

    /**
     * 请求详情数据
     */
    public void anyscGetVido() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.VIDODETAIL;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vidodatalisenter);
    }

    BaseRequestListener vidodatalisenter = new JsonRequestListener() {

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
            textBean = gson.fromJson(jsonResult.toString(), TextBean.class);
            tv_scannumber.setText(textBean.getViewNum() + "次浏览");
            tv_texttitles.setText(textBean.getTitle());
            tv_tiem.setText(textBean.getUpdateDateStr());
            if (textBean.getAuthor() != null) {
                if (!TextUtils.isEmpty(textBean.getAuthor().getUserPic())) {
                    ImageManager.getInstance().displayImg(civ_icon, textBean.getAuthor().getUserPic());
                }
                tv_titlename.setText(textBean.getAuthor().getUsername());
            }
            mAliyunVodPlayerView.setCoverUri(textBean.getCoverImg());

            authInfo = textBean.getVideoPlayAuth();
            videoId = textBean.getVideoId();
            if (!TextUtils.isEmpty(authInfo) && !TextUtils.isEmpty(videoId)) {
                initAliyunVodPlayerView();
            }

            if (textBean.getFocusUser()) {
                tv_expert_focus_btn.setColor(mContext, R.color.g999999);
                tv_expert_focus_btn.setText("已关注");
            } else {
                tv_expert_focus_btn.setColor(mContext, R.color.com_red);
                tv_expert_focus_btn.setText("+关注");
            }

            if (textBean.getFocus()) {
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.icon_redstor));
            } else {
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.content_whit_restore));
            }

            if (textBean.getPraise()) {
                iv_admires.setImageDrawable(getResources().getDrawable(R.drawable.icon_redadmire));
                iv_admirecicle.setImageDrawable(getResources().getDrawable(R.drawable.admire_redcircl));
            }
            tv_pricenum.setText(textBean.getPraiseNum() + "");
            tv_messagecount.setText(textBean.getPraiseNum() + "");
        }
    };

    /**
     * 初始化视频播放器
     */
    private void initAliyunVodPlayerView() {
        aliyunAuthInfoBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunAuthInfoBuilder.setPlayAuth(authInfo);
        aliyunAuthInfoBuilder.setVid(videoId);
        aliyunAuthInfoBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL);
        mAliyunVodPlayerView.setAuthInfo(aliyunAuthInfoBuilder.build());
        mAliyunVodPlayerView.setControlBarCanShow(true);
    }

    /**
     * 请求评论数据
     */
    public void ansycGetComment() {
        String wholeUrl = AppUrl.host + AppUrl.TEXTCOMM;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("itemId", itemid);
        map.put("pageSize", PAGE_SIZE);
        map.put("source", 12);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commlister);
    }

    BaseRequestListener commlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            mPullRefreshListView.onRefreshComplete();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            ArrayList<CommentItemBean> currentPageList = new ArrayList<CommentItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                findViewById(R.id.ll_newprice).setVisibility(View.GONE);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
            }
            if (cmtJay != null) {
                for (int i = 0; i < cmtJay.length(); i++) {
                    JSONObject actJot = cmtJay.optJSONObject(i);
                    CommentItemBean p = gson.fromJson(actJot.toString(), CommentItemBean.class);
                    currentPageList.add(p);
                }
            }
            if (cmtJay.length() == 0) {
                ll_newprice.setVisibility(View.GONE);
                tv_newcommend.setVisibility(View.GONE);
            } else {
                ll_newprice.setVisibility(View.VISIBLE);
                tv_newcommend.setVisibility(View.VISIBLE);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 点赞
     */
    public void ansyToAdmire() {
        String wholeUrl = AppUrl.host + AppUrl.ADMIRE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemid);
        map.put("source", 12);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, admirelisenter);
    }

    BaseRequestListener admirelisenter = new JsonRequestListener() {

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
            textBean.setPraise(true);
            iv_admires.setImageDrawable(getResources().getDrawable(R.drawable.icon_redadmire));
            tv_messagecount.setText(textBean.getPraiseNum() + 1 + "");
            tv_pricenum.setText(textBean.getPraiseNum() + 1 + "");
        }
    };

    /**
     * 关注文章
     */
    public void anyscWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.WATCH;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkContentId", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, watchlister);
    }

    BaseRequestListener watchlister = new JsonRequestListener() {

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
            textBean.setFocus(true);
            iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.icon_redstor));
        }
    };

    /**
     * 取消关注文章
     */
    public void anyscCancelWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.CANCELWATCH;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkContentId", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cancelwatchlister);
    }

    BaseRequestListener cancelwatchlister = new JsonRequestListener() {

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
            textBean.setFocus(false);
            iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.content_whit_restore));
        }
    };

    /**
     * 关注作者
     */
    public void anyscTeachWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHADDFORCUS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", textBean.getAuthor().getId());
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
            textBean.setFocusUser(true);
            tv_expert_focus_btn.setColor(mContext, R.color.g999999);
            tv_expert_focus_btn.setText("已关注");
            showShortToast("已关注");
        }
    };

    /**
     * 取消关注作者
     */
    public void anyscTeachCancelWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHCANCELFORCK;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", textBean.getAuthor().getId());
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
            textBean.setFocusUser(false);
            tv_expert_focus_btn.setColor(mContext, R.color.com_red);
            tv_expert_focus_btn.setText("+关注");
            showShortToast("已取消关注");
        }
    };

    /**
     * 评论
     */
    public void sedCommend() {
        String wholeUrl = AppUrl.host + AppUrl.SENDCOMMEN;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemid);
        map.put("source", "12");
        map.put("content", et_commentwrite.getText().toString().trim());
        if (isreply == 1) {
            map.put("parentId", parentid);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commentsedlistener);
    }

    BaseRequestListener commentsedlistener = new JsonRequestListener() {

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
            PAGE_NUM = 1;
            ansycGetComment();
            ll_commentbottom.setVisibility(View.VISIBLE);
            tv_commentsend.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            et_commentwrite.setText("");
            et_commentwrite.clearFocus();
            Intent it = new Intent();
            it.putExtra("itemId", itemid);
            it.setAction(MainHomeActivity.COMMENT_SUCCESS);
            sendBroadcast(it);
            Toast.makeText(mContext, "评论成功", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 获取推荐列表
     */
    public void ansygetRecomend() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.RECOMMEND;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, recomendlistlister);
    }

    BaseRequestListener recomendlistlister = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            oList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                oList.add(gson.fromJson(actJay.optJSONObject(i).toString(), SearchImpointContentBean.class));
            }

            if (oList != null) {
                for (int i = 0; i < oList.size(); i++) {
                    List<MediaBean> mediaBeenlist = new ArrayList<>();
                    mediaBeenlist.clear();
                    FirstHeadBean firstHeadBean = new FirstHeadBean();
                    if (oList != null) {
                        searchImpointContentBean = oList.get(i);
                        firstHeadBean.setClassifyType(searchImpointContentBean.getContentType());
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
                        }
                        firstHeadBean.setItemMediaList(mediaBeenlist);
                        firstHeadBean.setViewNum(searchImpointContentBean.getViewNum());
                        firstHeadBean.setItemId((long) searchImpointContentBean.getId());
                        firstHeadBean.setItemtype("search");
                        firstHeadBean.setItemCommentNum(searchImpointContentBean.getCommentNum());
                    }
                    mlsit.add(firstHeadBean);
                }
                ContentRecommendAdapter cra = new ContentRecommendAdapter(mContext, mlsit);
                rv_firsthead.setAdapter(cra);

                rv_firsthead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mlsit != null) {
                            // 资源类型;0:图文;1:视频;2:专栏;3:活动推广
                            FirstHeadBean fhb = mlsit.get(i);
                            if (fhb.getContentType() == 0) {
                                Intent intent = new Intent(mContext, TextDetailActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            } else if (fhb.getContentType() == 1) {
                                Intent intent = new Intent(mContext, VidoDeatilActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            } else if (fhb.getContentType() == 2) {
                                Intent intent = new Intent(mContext, SpecialTypeActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
            if (oList.size() == 0) {
                ll_recommend.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onComtClicked(View view, int poston) {
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        isreply = 1;
        parentid = mList.get(poston).getParentId();
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            rl_parent.getGlobalVisibleRect(r);
            // 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
            if (rootBottom == Integer.MIN_VALUE) {
                rootBottom = r.bottom;
                return;
            }
            // adjustResize，软键盘弹出后高度会变小
            if (r.bottom < rootBottom) {
                et_commentwrite.requestFocus();
            } else {
                et_commentwrite.clearFocus();
            }
        }
    };
}
