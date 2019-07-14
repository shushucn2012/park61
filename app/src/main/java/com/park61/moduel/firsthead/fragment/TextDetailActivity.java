package com.park61.moduel.firsthead.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.AudioListDetailsActivity;
import com.park61.moduel.firsthead.MainHomeActivity;
import com.park61.moduel.firsthead.VideoListDetailsActivity;
import com.park61.moduel.firsthead.adapter.CommentAllAdapter;
import com.park61.moduel.firsthead.adapter.ContentRecommendAdapter;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.CommentItemBean;
import com.park61.moduel.firsthead.bean.CommentListBean;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.bean.SearchImpointContentBean;
import com.park61.moduel.firsthead.bean.TextBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ObservableScrollView;
import com.park61.widget.pw.SharePopWin;
import com.park61.widget.textview.CirButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nieyu on 2017/11/17.
 */

public class TextDetailActivity extends BaseActivity implements View.OnClickListener, ObservableScrollView.ScrollBottomListener, CommentAllAdapter.OnReplyClickedLsner {

    long itemid;
    private int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 10;
    private String baseUrl = "file:///android_asset";
    ImageView iv_back;
    //    public int id=2;
    TextView tv_texttitles;
    TextView tv_texttime;
    TextView tv_scannumber;
    ImageView civ_icon;
    TextView tv_titlename;
    WebView wvContent;

    private List<CommentItemBean> mList;
    private ListView rv_firsthead;
    private WebSettings websettings;
    private List<SearchImpointContentBean> oList = new ArrayList<SearchImpointContentBean>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private CirButton tv_expert_focus_btn;
    long watch = 0;
    int teachwatch = 0;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private CommentAllAdapter mAdapter;
    private LinearLayout iv_admire;
    private LinearLayout ll_store;
    private LinearLayout ll_share;
    private ImageView iv_admires;
    private TextView tv_messagecount, tv_newcommend;
    private ImageView iv_attent, iv_share;
    private LinearLayout ll_commentbottom, ll_newprice;
    private EditText et_commentwrite;
    private TextView tv_commentsend, tv_title;

    View cityhot_header_blank, ll_commentsend;
    private RelativeLayout rl_parent, rl_edit;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_listbody);

    }

    @Override
    public void initView() {
        itemid = getIntent().getLongExtra("itemId", -1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cityhot_header_blank = localLayoutInflater.inflate(R.layout.activity_textdetail, actualListView, false);
        actualListView.addHeaderView(cityhot_header_blank);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);

        rl_parent = (RelativeLayout) findViewById(R.id.rl_parent);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);

        ll_newprice = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_newprice);
        tv_newcommend = (TextView) cityhot_header_blank.findViewById(R.id.tv_newcommend);
        rv_firsthead = (ListView) cityhot_header_blank.findViewById(R.id.rv_firsthead);
        wvContent = (WebView) cityhot_header_blank.findViewById(R.id.wvContent);
        tv_titlename = (TextView) findViewById(R.id.tv_titlename);
        civ_icon = (ImageView) cityhot_header_blank.findViewById(R.id.civ_icon);
        tv_scannumber = (TextView) cityhot_header_blank.findViewById(R.id.tv_scannumber);
        tv_texttime = (TextView) cityhot_header_blank.findViewById(R.id.tv_texttime);
        tv_texttitles = (TextView) cityhot_header_blank.findViewById(R.id.tv_texttitles);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_expert_focus_btn = (CirButton) cityhot_header_blank.findViewById(R.id.tv_expert_focus_btn);
        tv_expert_focus_btn.setOnClickListener(this);

        iv_admire = (LinearLayout) findViewById(R.id.iv_admire);
        iv_admire.setOnClickListener(this);
        ll_store = (LinearLayout) findViewById(R.id.ll_store);
        ll_store.setOnClickListener(this);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);
        ll_share.setOnClickListener(this);
        iv_admires = (ImageView) findViewById(R.id.iv_admires);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        iv_attent = (ImageView) findViewById(R.id.iv_attent);
        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);

        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);


        final RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        et_commentwrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {

                if (CommonMethod.checkUserLogin(TextDetailActivity.this)) {
//                    if(TextUtils.isEmpty(et_commentwrite.getText().toString().trim())){
//                        ll_commentbottom.setVisibility(View.VISIBLE);
//                        tv_commentsend.setVisibility(View.GONE);
//                    }else {
                    if (hasfocus) {
                        rl.leftMargin = 15;
                        rl.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        rl.height = DevAttr.dip2px(TextDetailActivity.this, 30);
                        rl.width = DevAttr.dip2px(TextDetailActivity.this, 250);
                        rl_edit.setLayoutParams(rl);
                        ll_commentbottom.setVisibility(View.GONE);
                        tv_commentsend.setVisibility(View.VISIBLE);
                    } else {
                        rl.leftMargin = 15;
                        rl.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        rl.height = DevAttr.dip2px(TextDetailActivity.this, 30);
                        rl.width = DevAttr.dip2px(TextDetailActivity.this, 200);
                        rl_edit.setLayoutParams(rl);
                        ll_commentbottom.setVisibility(View.VISIBLE);
                        tv_commentsend.setVisibility(View.GONE);
                    }
//                    }
                }

            }
        });


        et_commentwrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length()==0){
//                    ll_commentbottom.setVisibility(View.VISIBLE);
//                    tv_commentsend.setVisibility(View.GONE);
//                }else {
//                    ll_commentbottom.setVisibility(View.GONE);
//                    tv_commentsend.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonMethod.checkUserLogin(mContext)) {
                    return;
                }
                if (TextUtils.isEmpty(et_commentwrite.getText().toString().trim())) {
                    showShortToast("请输入评论内容");
                    return;
                }
                sedCommend();
            }
        });
//        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(TextDetailActivity.this,i+"我被按了",Toast.LENGTH_LONG).show();
//            }
//        });
        rl_parent.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    public void initData() {
//        mlsit = new ArrayList<>();
//        mlsit.add(new FirstHeadBean(1));
//        mlsit.add(new FirstHeadBean(1));
//        mlsit.add(new FirstHeadBean(1));
//        adapter = new FirstHeadAdapter(TextDetailActivity.this, mlsit);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
//        rv_firsthead.setAdapter(mLRecyclerViewAdapter);

        mList = new ArrayList<>();
        mAdapter = new CommentAllAdapter(this, mList);
        actualListView.setAdapter(mAdapter);
        mAdapter.setOnReplyClickedLsner(TextDetailActivity.this);
        ansyGetText();
        ansygetRecomend();
        ansycGetComment();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                PAGE_NUM=0;
//                ansycGetComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                ansycGetComment();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.iv_back) {
            finish();
        } else if (viewid == R.id.tv_expert_focus_btn) {
            if (teachwatch == 0) {
                anyscTeachWatch();
            } else if (teachwatch == 1) {
                anyscTeachCancelWatch();
            }
        } else if (viewid == R.id.ll_share) {
            toShare();
        } else if (viewid == R.id.ll_store) {
            if (watch == 0) {
                anyscWatch();
            } else if (watch == 1) {
                anyscCancelWatch();
            }
        } else if (viewid == R.id.iv_admire) {

            ansyToAdmire();
        } else if (viewid == R.id.iv_share) {
            toShare();
        }
    }

    TextBean textBean;

    public void ansyGetText() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEXTDETAIL;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/content/findContentBlogDetailById";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, textdatalisenter);
    }

    BaseRequestListener textdatalisenter = new JsonRequestListener() {

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
            tv_texttime.setText(textBean.getUpdateDateStr());
            tv_texttitles.setText(textBean.getTitle());
            if (textBean.getAuthor() != null) {
                if (!TextUtils.isEmpty(textBean.getAuthor().getUserPic())) {
                    ImageManager.getInstance().displayImg(civ_icon, textBean.getAuthor().getUserPic());
                }
                tv_titlename.setText(textBean.getAuthor().getUsername());
            }

            websettings = wvContent.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setJavaScriptCanOpenWindowsAutomatically(true);
            websettings.setDomStorageEnabled(true);
            websettings.setAppCacheEnabled(true);
            websettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
            setWebData(textBean.getContent());
            tv_messagecount.setText(textBean.getPraiseNum() + "");

            tv_title.setText(textBean.getTitle());
            if (textBean.getFocusUser()) {
                teachwatch = 1;
                tv_expert_focus_btn.setColor(TextDetailActivity.this, R.color.g999999);
                tv_expert_focus_btn.setText("已关注");
            } else {
                teachwatch = 0;
                tv_expert_focus_btn.setColor(TextDetailActivity.this, R.color.com_red);
                tv_expert_focus_btn.setText("+关注");
//                tv_expert_focus_btn.invalidate();
            }

            if (textBean.getFocus()) {
                watch = 1;
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.icon_redstor));
            } else {
                watch = 0;
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.content_whit_restore));
            }

            if (textBean.getPraise()) {
                isprese = 1;
                iv_admires.setImageDrawable(getResources().getDrawable(R.drawable.icon_redadmire));
            }
        }
    };

    private void setWebData(String webcontent) {
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(webcontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wvContent.loadDataWithBaseURL(baseUrl, htmlDetailsStr, "text/html", "UTF-8", "");
    }


    List<FirstHeadBean> mlsit = new ArrayList<FirstHeadBean>();
    ;

    public void ansygetRecomend() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.RECOMMEND;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/content/getRecommendContentList";
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
                    if (oList != null) {
                        List<MediaBean> mediaBeenlist = new ArrayList<>();
                        FirstHeadBean firstHeadBean = new FirstHeadBean();
                        SearchImpointContentBean searchImpointContentBean = oList.get(i);
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
//                            MediaBean mediaBean2=new MediaBean();
//                            mediaBean2.setMediaUrl(searchImpointContentBean.getCoverImg2());
//                            mediaBeenlist.add(mediaBean1);
//
//                            MediaBean mediaBean3=new MediaBean();
//                            mediaBean3.setMediaUrl(searchImpointContentBean.getCoverImg3());
//                            mediaBeenlist.add(mediaBean3);
                        }
                        firstHeadBean.setItemMediaList(mediaBeenlist);
                        firstHeadBean.setViewNum(searchImpointContentBean.getViewNum());
                        firstHeadBean.setItemId((long) searchImpointContentBean.getId());
                        firstHeadBean.setItemtype("search");
                        firstHeadBean.setItemCommentNum(searchImpointContentBean.getCommentNum());
                        mlsit.add(firstHeadBean);
                    }
                }
                ContentRecommendAdapter cra = new ContentRecommendAdapter(TextDetailActivity.this, mlsit);
                rv_firsthead.setAdapter(cra);
                rv_firsthead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mlsit != null) {
//                               资源类型;0:图文;1:视频;2:专栏;3:活动推广
                            FirstHeadBean fhb = mlsit.get(i);
                            if (fhb.getContentType() == 0) {
                                Intent intent = new Intent(TextDetailActivity.this, TextDetailActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            } else if (fhb.getContentType() == 1) {
                                Intent intent = new Intent(TextDetailActivity.this, VideoListDetailsActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            } else if (fhb.getContentType() == 2) {
                                Intent intent = new Intent(TextDetailActivity.this, SpecialTypeActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            } else if (fhb.getContentType() == 3) {
                                Intent intent = new Intent(TextDetailActivity.this, AudioListDetailsActivity.class);
                                intent.putExtra("itemId", fhb.getItemId());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }

        }
    };

    public void anyscWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.WATCH;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/content/findContentBlogDetailById";
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
            iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.icon_redstor));
            watch = 1;
            /*watch=1;
            tv_expert_focus_btn.setColor(R.color.gray);
            tv_expert_focus_btn.setText("已关注");
            showShortToast("已关注");*/

        }
    };

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
            iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.content_whit_restore));
            watch = 0;
            /*tv_expert_focus_btn.setColor(R.color.text_blue);
            tv_expert_focus_btn.setText("+关注");
            showShortToast("请关注");*/
        }
    };

    public void ansycGetComment() {
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/home/commentList";
        String wholeUrl = AppUrl.host + AppUrl.TEXTCOMM;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("itemId", itemid);
        map.put("pageSize", 10);
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
            CommentListBean commentListBean = gson.fromJson(jsonResult.toString(), CommentListBean.class);
            ArrayList<CommentItemBean> currentPageList = new ArrayList<CommentItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
//                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                findViewById(R.id.ll_newprice).setVisibility(View.GONE);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
//                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                findViewById(R.id.ll_newprice).setVisibility(View.VISIBLE);
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

//            if( cmtJay.length()==0){
//                ll_newprice.setVisibility(View.GONE);
//            }else {
//                ll_newprice.setVisibility(View.VISIBLE);
//            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();


        }

    };

    @Override
    public void scrollBottom() {
//        Toast.makeText(this,"wo da到跌了",Toast.LENGTH_LONG).show();
    }

    private int isprese = 0;

    public void ansyToAdmire() {
        String wholeUrl = AppUrl.host + AppUrl.ADMIRE;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/home/addPraise";
//        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEXTCOMM;
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
            iv_admires.setImageDrawable(getResources().getDrawable(R.drawable.icon_redadmire));


            if (isprese == 0) {
                isprese = 1;
                Toast.makeText(TextDetailActivity.this, "已赞", Toast.LENGTH_LONG).show();
                tv_messagecount.setText(textBean.getPraiseNum() + 1 + "");
            } else {
                Toast.makeText(TextDetailActivity.this, "您已赞过", Toast.LENGTH_LONG).show();
            }

        }

    };

    public void toShare() {
        if (textBean == null)
            return;

//        AddStatistics("courseShare");
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = "";

        shareUrl = "http://m.61park.cn/#/home/homecontent/" + itemid;

        String picUrl = textBean.getCoverImg();
        String title = textBean.getTitle();
        String description = textBean.getIntro();
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", picUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }

    private int parentid = -1;

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
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
           /* if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }*/
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            PAGE_NUM = 1;
            ansycGetComment();
            ll_commentbottom.setVisibility(View.VISIBLE);
            tv_commentsend.setVisibility(View.GONE);
            et_commentwrite.setText("");
            et_commentwrite.clearFocus();
            Intent it = new Intent();
            it.putExtra("itemId", itemid);
            it.setAction(MainHomeActivity.COMMENT_SUCCESS);
            sendBroadcast(it);
            showShortToast("评论成功");
        }
    };

    public void anyscTeachWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHADDFORCUS;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/focus/addTrainerFocus";
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
            teachwatch = 1;
            tv_expert_focus_btn.setColor(TextDetailActivity.this, R.color.g999999);
            tv_expert_focus_btn.setText("已关注");
            showShortToast("已关注");
        }
    };


    public void anyscTeachCancelWatch() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEACHCANCELFORCK;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/focus/delTrainerFocus";
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
            teachwatch = 0;
            tv_expert_focus_btn.setColor(TextDetailActivity.this, R.color.com_red);
            tv_expert_focus_btn.setText("+关注");
            showShortToast("已取消关注");
        }
    };
    private int isreply = 0;

    @Override
    public void onComtClicked(View view, int poston) {
        //Toast.makeText(TextDetailActivity.this,poston+"我被按了",Toast.LENGTH_LONG).show();
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        isreply = 1;
        parentid = mList.get(poston).getParentId();
//        InputMethodManager inputMethodManager = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
//// 接受软键盘输入的编辑文本或其它视图
//        inputMethodManager.showSoftInput(et_commentwrite,InputMethodManager.SHOW_FORCED);
    }


    private boolean mBackEnable = false;
    private boolean mIsBtnBack = false;
    private int rootBottom = Integer.MIN_VALUE;
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
                mBackEnable = false;
                et_commentwrite.requestFocus();
            } else {
                et_commentwrite.clearFocus();
                mBackEnable = true;
                if (mIsBtnBack) {
                    finish();
                }
            }
        }
    };
}
