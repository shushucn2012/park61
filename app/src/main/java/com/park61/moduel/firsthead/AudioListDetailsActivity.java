package com.park61.moduel.firsthead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.AudioBaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.adapter.CommentAllAdapter;
import com.park61.moduel.firsthead.adapter.ContentRecommendAdapter;
import com.park61.moduel.firsthead.adapter.VideoSourcesAdapter;
import com.park61.moduel.firsthead.bean.CommentItemBean;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.FirstHeadChildBean;
import com.park61.moduel.firsthead.bean.TextBean;
import com.park61.moduel.firsthead.bean.VideoItemSource;
import com.park61.moduel.okdownload.DownloadService;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音频播放详情
 * Created by super on 2018/1/31
 */
public class AudioListDetailsActivity extends AudioBaseActivity implements View.OnClickListener {

    private static final int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private int rootBottom = Integer.MIN_VALUE;

    private TextView tv_title, tv_scannumber, tv_auth_name, tv_time, tv_messagecount, tv_commentsend, tv_newcommend, tv_source_num, tv_page_title;
    private ImageView img_auth, img_audio_cover, iv_admires, iv_attent, iv_share, img_audio_center;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView, rv_firsthead;
    private LinearLayout ll_commentbottom, ll_recommend, ll_newprice, iv_admire, ll_store;
    private EditText et_commentwrite;
    private View cityhot_header_blank, area_to_play_all, area_details_content, area_video_list, area_videolist_top, area_real_input, area_show_input, ll_commentsend;
    private RelativeLayout rl_parent, rl_edit;
    private ListViewScrollView lv_video_source;
    private WebView wv_content;
    private RadioGroup rg;//标签组
    private View[] stickArray = new View[2];//下划线

    private List<FirstHeadBean> recommendLsit = new ArrayList<>();
    private long itemid;
    private CommentAllAdapter mAdapter;
    private List<CommentItemBean> mList = new ArrayList<>();
    private List<VideoItemSource> sList = new ArrayList<>();
    private VideoSourcesAdapter videoSourcesAdapter;
    ;
    private TextBean textBean;
    private int parentid = -1;//父评论id
    private int isreply = 0;//是否是回复 0不是；1是
    private int curPlayId;//当前播放资源id
    private boolean isPlayAll = false;//是否进行全部顺序播放
    private int curDownLoadId;//当前下载资源id
    private boolean isShowed;//是否显示下载提示

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.content_audio);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        initHeadView();

        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_attent = (ImageView) findViewById(R.id.iv_attent);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        iv_admires = (ImageView) findViewById(R.id.iv_admires);
        iv_admire = (LinearLayout) findViewById(R.id.iv_admire);
        ll_store = (LinearLayout) findViewById(R.id.ll_store);
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);

        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        area_real_input = findViewById(R.id.area_real_input);
        area_show_input = findViewById(R.id.area_show_input);
        ll_commentsend = findViewById(R.id.ll_commentsend);

        initVodPlayer();
    }

    /**
     * 列表头部-视频详情界面-初始化
     */
    private void initHeadView() {
        cityhot_header_blank = LayoutInflater.from(mContext).inflate(R.layout.activity_audiodetail_haslist, actualListView, false);

        ll_newprice = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_newprice);
        ll_recommend = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_recommend);
        tv_newcommend = (TextView) cityhot_header_blank.findViewById(R.id.tv_newcommend);
        rv_firsthead = (ListView) cityhot_header_blank.findViewById(R.id.rv_firsthead);
        tv_auth_name = (TextView) cityhot_header_blank.findViewById(R.id.tv_auth_name);
        img_auth = (ImageView) cityhot_header_blank.findViewById(R.id.img_auth);
        img_audio_cover = (ImageView) cityhot_header_blank.findViewById(R.id.img_audio_cover);
        img_audio_center = (ImageView) cityhot_header_blank.findViewById(R.id.img_audio_center);
        tv_scannumber = (TextView) cityhot_header_blank.findViewById(R.id.tv_scannumber);
        tv_title = (TextView) cityhot_header_blank.findViewById(R.id.tv_title);
        tv_time = (TextView) cityhot_header_blank.findViewById(R.id.tv_time);
        lv_video_source = (ListViewScrollView) cityhot_header_blank.findViewById(R.id.lv_video_source);
        tv_source_num = (TextView) cityhot_header_blank.findViewById(R.id.tv_source_num);
        wv_content = (WebView) cityhot_header_blank.findViewById(R.id.wv_content);
        area_to_play_all = cityhot_header_blank.findViewById(R.id.area_to_play_all);
        area_details_content = cityhot_header_blank.findViewById(R.id.area_details_content);
        area_video_list = cityhot_header_blank.findViewById(R.id.area_video_list);
        area_videolist_top = cityhot_header_blank.findViewById(R.id.area_videolist_top);

        rg = (RadioGroup) cityhot_header_blank.findViewById(R.id.rg);
        stickArray[0] = cityhot_header_blank.findViewById(R.id.stick0);
        stickArray[1] = cityhot_header_blank.findViewById(R.id.stick1);
        stickArray[0].setVisibility(View.VISIBLE);
        initActTabs();

        actualListView.addHeaderView(cityhot_header_blank);
    }

    @Override
    public void initData() {
        itemid = getIntent().getLongExtra("itemId", -1);
        mAdapter = new CommentAllAdapter(this, mList);
        actualListView.setAdapter(mAdapter);
        anyscGetVido();
        ansygetRecomend();
    }

    @Override
    public void initListener() {
        iv_share.setOnClickListener(this);
        iv_admire.setOnClickListener(this);
        ll_store.setOnClickListener(this);

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
        tv_commentsend.setOnClickListener(view -> {
            if (CommonMethod.checkUserLogin(mContext)) {
                if (TextUtils.isEmpty(et_commentwrite.getText().toString().trim())) {
                    showShortToast("请输入评论内容");
                    return;
                }
                sedCommend();
            }
        });
        rl_edit.setOnClickListener(v -> {
            isreply = 0;
            showComtArea();
        });
        mAdapter.setOnReplyClickedLsner((view, position) -> {
            isreply = 1;
            parentid = mList.get(position).getParentId();
            showComtArea();
        });
        lv_video_source.setOnItemClickListener((parent, view, position, id) -> {
            if (curPlayId == sList.get(position).getSourceId()) {//如果点击歌曲是当前播放的歌曲
                mPlayerState = aliyunVodPlayer.getPlayerState();
                if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {//如果已经开始，就暂停
                    aliyunVodPlayer.pause();
                    sList.get(position).setPlaying(false);
                    videoSourcesAdapter.notifyDataSetChanged();
                } else {//其他状态
                    aliyunVodPlayer.start();
                    sList.get(position).setPlaying(true);
                    videoSourcesAdapter.notifyDataSetChanged();
                }
            } else {//如果点击歌曲不是当前播放的歌曲
                curPlayId = sList.get(position).getSourceId();
                ansycGetVideoAuth();
            }
        });
        area_to_play_all.setOnClickListener(v -> {
            isPlayAll = true;
            curPlayId = sList.get(0).getSourceId();
            ansycGetVideoAuth();
        });
        aliyunVodPlayer.setOnCompletionListener(() -> {
            if (isPlayAll) {//播放完成时，如果是全部播放就播放下一首
                curPlayId = getNextPlayId();
                ansycGetVideoAuth();
            } else {//如果没有全部播放，则停止
                curPlayId = 0;
                changePlayingState();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.iv_picback) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//全屏播放情况下返回时先变小屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            } else {
                finish();
            }
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
        } else if (viewid == R.id.iv_share) {
            showShareDialog("http://m.61park.cn/#/home/homeaudio/" + itemid, textBean.getCoverImg(), textBean.getTitle(), textBean.getIntro());
        }
    }

    /**
     * 请求详情数据
     */
    public void anyscGetVido() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.findContentDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", itemid);
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
            dDialog.showDialog("提示", "网络请求失败", "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        anyscGetVido();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            textBean = gson.fromJson(jsonResult.toString(), TextBean.class);
            tv_scannumber.setText(textBean.getPlayTotalNum());
            tv_title.setText(textBean.getTitle());
            tv_time.setText(textBean.getUpdateDateStr());
            if (textBean.getAuthor() != null) {
                if (!TextUtils.isEmpty(textBean.getAuthor().getUserPic())) {
                    ImageManager.getInstance().displayImg(img_auth, textBean.getAuthor().getUserPic());
                }
                tv_auth_name.setText(textBean.getAuthor().getUsername());
                tv_page_title.setText(textBean.getAuthor().getUsername());
            }
            ImageManager.getInstance().displayImg(img_audio_cover, textBean.getCoverImg());
            ImageManager.getInstance().displayImg(img_audio_center, textBean.getCoverImg());

            authInfo = textBean.getVideoPlayAuth();
            videoId = textBean.getVideoId();

            if (textBean.getFocus()) {
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.icon_redstor));
            } else {
                iv_attent.setImageDrawable(getResources().getDrawable(R.drawable.content_whit_restore));
            }

            if (textBean.getPraise()) {
                iv_admires.setImageDrawable(getResources().getDrawable(R.drawable.icon_redadmire));
            }
            tv_messagecount.setText(textBean.getPraiseNum() + "");

            String htmlDetailsStr = "";
            try {
                htmlDetailsStr = HtmlParserTool.replaceImgStr(textBean.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            wv_content.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);

            //视频详情加载完了再加载评论数据,视频资源列表
            ansycGetComment();
            ansycGetVideoList();
        }
    };

    /**
     * 初始化标签
     */
    private void initActTabs() {
        rg.check(R.id.rb_intro);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            int checkedIndex = 0;
            for (int i = 0; i < group.getChildCount(); i++) {
                if (checkedId == group.getChildAt(i).getId()) {
                    checkedIndex = i;
                }
            }
            showStick(checkedIndex);
            swithPage(checkedIndex);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rg.check(R.id.rb_list);
            }
        }, 500);
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 切换详情和游戏评价
     */
    public void swithPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                area_details_content.setVisibility(View.VISIBLE);
                area_video_list.setVisibility(View.GONE);
                break;
            case 1:
                area_details_content.setVisibility(View.GONE);
                area_video_list.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 获取音频列表
     */
    public void ansycGetVideoList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getAudioList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", itemid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vlister);
    }

    BaseRequestListener vlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            tv_source_num.setText("歌单(" + jsonResult.optInt("total") + ")");
            JSONArray actJay = jsonResult.optJSONArray("rows");
            if (actJay == null || actJay.length() == 0) {
                area_videolist_top.setVisibility(View.GONE);
                return;
            }
            if (actJay.length() == 1) {
                area_videolist_top.setVisibility(View.GONE);
            }
            sList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                VideoItemSource source = gson.fromJson(jot.toString(), VideoItemSource.class);
                source.setPicUrl(textBean.getCoverImg());
                source.setStatus(MainHomeViewHelper.getSourceItemDownLoadStatus(source.getSourceId(), itemid + ""));//下载状态
                source.setPlaying(source.getSourceId() == curPlayId ? true : false);//播放状态
                sList.add(source);
            }
            videoSourcesAdapter = new VideoSourcesAdapter(mContext, sList);
            lv_video_source.setAdapter(videoSourcesAdapter);

            videoSourcesAdapter.setOnDownLoadClickedLsner(index -> {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                if (sList.get(index).getStatus() == 1 || sList.get(index).getStatus() == 2) {
                    showShortToast("已下载");
                    return;
                }
                curDownLoadId = sList.get(index).getSourceId();
                ansycGetDownLoadAuth();
            });

            if (curPlayId == 0) {//刚进来还没有点播，播放第一个
                curPlayId = sList.get(0).getSourceId();
                ansycGetVideoAuth();
            }
        }
    };

    /**
     * 获取视频播放权证
     */
    public void ansycGetVideoAuth() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curPlayId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alister);
    }

    BaseRequestListener alister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            videoId = jsonResult.optString("videoId");
            authInfo = jsonResult.optString("videoPlayAuth");
            setPlaySource();
            audioStartPlay();
            changePlayingState();
//            if (GlobalParam.userToken != null) {
            ansycAddViewNum();
//            }
        }
    };

    /**
     * 增加播放次数
     */
    public void ansycAddViewNum() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.addViewNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curPlayId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 获取视频下载权证
     */
    public void ansycGetDownLoadAuth() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curDownLoadId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, adlister);
    }

    BaseRequestListener adlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String mVideoId = jsonResult.optString("videoId");
            String mAuthInfo = jsonResult.optString("videoPlayAuth");

            int curDownLoadIndex = 0;
            for (int i = 0; i < sList.size(); i++) {
                if (curDownLoadId == sList.get(i).getSourceId()) {
                    curDownLoadIndex = i;
                }
            }

            Intent it = new Intent(mContext, DownloadService.class);
            it.putExtra("title", sList.get(curDownLoadIndex).getTitle());
            it.putExtra("sid", FU.paseLong(sList.get(curDownLoadIndex).getSourceId() + ""));
            it.putExtra("vid", mVideoId);
            it.putExtra("contentId", itemid);
            it.putExtra("type", DownloadService.TYPE_AUDIO);
            it.putExtra("playAuth", mAuthInfo);
            it.putExtra("icon", textBean.getCoverImg());
            startService(it);
            sList.get(curDownLoadIndex).setStatus(2);//未下载完成状态
            videoSourcesAdapter.notifyDataSetChanged();
            addDowLoadNum();
            showDownLoadTip();
        }
    };

    /**
     * 增加下载次数
     */
    public void addDowLoadNum() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.addDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curDownLoadId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }


    /**
     * 改变正在播放的图标
     */
    private void changePlayingState() {
        for (int i = 0; i < sList.size(); i++) {
            if (curPlayId == sList.get(i).getSourceId()) {
                sList.get(i).setPlaying(true);
            } else {
                sList.get(i).setPlaying(false);
            }
        }
        videoSourcesAdapter.notifyDataSetChanged();
    }


    /**
     * 获取当前视频下一个视频的id
     */
    public int getNextPlayId() {
        int nextIndex = 0;
        for (int i = 0; i < sList.size(); i++) {
            if (curPlayId == sList.get(i).getSourceId()) {
                nextIndex = i + 1;
            }
        }
        //如果下一个曲目的序号比最后一个还大，将下一个置成0
        return sList.get(nextIndex > sList.size() - 1 ? 0 : nextIndex).getSourceId();
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
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
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
            iv_admires.setImageResource(R.drawable.icon_redadmire);
            tv_messagecount.setText(textBean.getPraiseNum() + 1 + "");
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
            showShortToast("评论成功");
            //刷新数据
            PAGE_NUM = 1;
            ansycGetComment();
            // 清空评论并隐藏评论框
            et_commentwrite.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(et_commentwrite.getWindowToken(), 0);
            }
            area_show_input.setVisibility(View.VISIBLE);
            area_real_input.setVisibility(View.GONE);
            //通知首页数据刷新
            Intent it = new Intent(MainHomeActivity.COMMENT_SUCCESS);
            it.putExtra("itemId", itemid);
            sendBroadcast(it);
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
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0)
                return;
            recommendLsit.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                FirstHeadChildBean p = gson.fromJson(jot.toString(), FirstHeadChildBean.class);
                recommendLsit.add(MainHomeViewHelper.transNewBeanToOld(p));
            }
            ContentRecommendAdapter cra = new ContentRecommendAdapter(mContext, recommendLsit);
            rv_firsthead.setAdapter(cra);

            rv_firsthead.setOnItemClickListener((adapterView, view, i, l) -> {
                FirstHeadBean fhb = recommendLsit.get(i);
                MainHomeViewHelper.judgeGoWhereByFirstHeadBean(fhb, mContext);
                finish();
            });
            if (recommendLsit.size() == 0) {
                ll_recommend.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadChangeReceiver);
    }

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(VideoListDetailsActivity.ACTION_DOWNLOAD_FINISHED);
        registerReceiver(downloadChangeReceiver, filter);
    }

    private BroadcastReceiver downloadChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            showShortToast("下载完成");
            ansycGetVideoList();
        }
    };

    /**
     * 点击评论按钮，显示评论输入框
     */
    public void showComtArea() {
        // 点击评论按钮，显示评论输入框
        area_real_input.setVisibility(View.VISIBLE);
        area_show_input.setVisibility(View.GONE);
        et_commentwrite.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(et_commentwrite, 0);
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = ll_commentsend;// getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                area_show_input.setVisibility(View.VISIBLE);
                area_real_input.setVisibility(View.GONE);
                et_commentwrite.setText("");
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 显示下载提示
     */
    private void showDownLoadTip() {
        if (!isShowed) {
            showShortToast("已加入下载列表，可在我的-我的下载中查看");
            isShowed = true;
        }
    }

}
