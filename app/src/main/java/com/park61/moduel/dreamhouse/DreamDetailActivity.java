package com.park61.moduel.dreamhouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ListViewFootState;
import com.park61.moduel.dreamhouse.adapter.DetailListAdapter;
import com.park61.moduel.dreamhouse.bean.DreamDetailBean;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;
import com.park61.moduel.dreamhouse.bean.SameDreamInfo;
import com.park61.moduel.dreamhouse.lib.PullToZoomBase;
import com.park61.moduel.dreamhouse.lib.PullToZoomListViewEx;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.circleimageview.GlideCircleTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 梦想详情
 */
public class DreamDetailActivity extends BaseActivity implements View.OnClickListener, PullToZoomListViewEx.SrollToShowListener,
        DetailListAdapter.OnReplyClickedLsner, DetailListAdapter.ToSameDreamPeople {
    private View evaluate_area, support_area, bottom_input_area, bottom_bar;
    private RelativeLayout img_area;
    private EditText edit_comt;
    private ImageView img_dianzan, img_share, img_head;
    private ImageView user_img;
    private Button btn_join_dream, btn_send;
    private TextView support_num, evaluate_num;
    private PullToZoomListViewEx listView;
    private DetailListAdapter detailAdapter;
    private LinearLayout top_bar;
    private ListViewFootState lvFootState;

    private ArrayList<EvaluateItemInfo> mList;
    private DreamDetailBean dreamDetail;
    private Long requirementId;//梦想id
    private Long curParentId;// 当前评论的回复id
    private int comtType = 0;// 评论类型 0 评论；1评论回复；2评论评论
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 5;// 评论列表每页条数
    private int totalSize;
    private List<SameDreamInfo> predictionList;//同行人列表
    private boolean hadPraised;    //是否点赞


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dream_detail);
    }

    @Override
    public void initView() {
        evaluate_area = findViewById(R.id.evaluate_area);
        support_area = findViewById(R.id.support_area);
        bottom_input_area = findViewById(R.id.bottom_input_area);
        bottom_bar = findViewById(R.id.bottom_bar);
        edit_comt = (EditText) findViewById(R.id.edit_comt);
        img_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        support_num = (TextView) findViewById(R.id.support_num);
        evaluate_num = (TextView) findViewById(R.id.evaluate_num);
        btn_join_dream = (Button) findViewById(R.id.btn_join_dream);
        btn_send = (Button) findViewById(R.id.btn_send);
        listView = (PullToZoomListViewEx) findViewById(R.id.listview);
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        img_share = (ImageView) findViewById(R.id.img_share);
        img_head = (ImageView) findViewById(R.id.img_head);
        user_img = (ImageView) findViewById(R.id.user_img);
        img_area = (RelativeLayout) findViewById(R.id.img_area);
        lvFootState = new ListViewFootState(listView.getPullRootView(), mContext);
    }

    @Override
    public void initData() {
        requirementId = getIntent().getLongExtra("requirementId", 0l);
        mList = new ArrayList<EvaluateItemInfo>();
        detailAdapter = new DetailListAdapter(mContext, mList);
        detailAdapter.setToSameDreamPeople(this);
        detailAdapter.setOnReplyClickedLsner(this);
        listView.setSrollToShowListener(this);
        listView.setAdapter(detailAdapter);
        asyncDreamDetail();
    }

    @Override
    public void initListener() {
        img_share.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        evaluate_area.setOnClickListener(this);
        support_area.setOnClickListener(this);
        btn_join_dream.setOnClickListener(this);
        listView.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                RelativeLayout.MarginLayoutParams margin = new RelativeLayout.MarginLayoutParams(img_area.getLayoutParams());
                int y = DevAttr.dip2px(mContext, 120) - newScrollValue;
                margin.setMargins(DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                        y,
                        DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                        DevAttr.dip2px(mContext, 120) + margin.height);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
                img_area.setLayoutParams(layoutParams);
            }

            @Override
            public void onPullZoomEnd() {
                RelativeLayout.MarginLayoutParams margin = new RelativeLayout.MarginLayoutParams(img_area.getLayoutParams());
                int y = DevAttr.dip2px(mContext, 120);
                margin.setMargins(
                        DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                        y,
                        DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                        y + margin.height);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
                img_area.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_area:
                if (GlobalParam.userToken == null) {// 未登录则先去登录
                    mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                showComtArea(0);
                break;
            case R.id.btn_join_dream:
                if (GlobalParam.userToken == null) {
                    mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                } else {
                    Intent joinIntent = new Intent(mContext, ConfirmJourneyActivity.class);
                    joinIntent.putExtra("requirementId", requirementId);
                    startActivity(joinIntent);
                }
                finish();
                break;
            case R.id.support_area:
                if (hadPraised) {
                    support_area.setClickable(false);
                } else {
                    if (GlobalParam.userToken == null) {// 未登录则先去登录
                        mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
                        return;
                    }
                    asyncSupport();
                }
                break;
            case R.id.btn_send:
                if (GlobalParam.userToken == null) {// 未登录则先去登录
                    mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (TextUtils.isEmpty(edit_comt.getText().toString().trim())) {
                    showShortToast("您未填写评论！");
                    return;
                }
                switch (comtType) {
                    case 0:
                        asyncCommitEvaluate(requirementId, null);
                        break;
                    case 1:
                        asyncCommitEvaluate(requirementId, curParentId);
                        break;
                }
                break;
            case R.id.img_share:
                String shareUrl = "http://m.61park.cn/dream-house/dream-detail.html?id=" +
                        requirementId + "&";
                logout("======shareUrl======" + shareUrl);
                String picUrl = dreamDetail.getCoverPic();
                String title = getString(R.string.app_name) + dreamDetail.getTitle();
                showShareDialog(shareUrl, picUrl, title, "");
                break;
        }
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = bottom_input_area;
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                bottom_bar.setVisibility(View.VISIBLE);
                bottom_input_area.setVisibility(View.GONE);
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
     * 点击评论按钮，显示评论输入框
     *
     * @param curComtType 当前评论类型
     */
    public void showComtArea(int curComtType) {
        comtType = curComtType;
        // 点击评论按钮，显示评论输入框
        bottom_input_area.setVisibility(View.VISIBLE);
        bottom_bar.setVisibility(View.GONE);
        edit_comt.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edit_comt, 0);
    }

    /**
     * 请求提交评论
     */
    private void asyncCommitEvaluate(Long requirementId, Long parentId) {
        String wholeUrl = AppUrl.host + AppUrl.REQUERE_ADD_COMMENT;
        String requestBodyData = ParamBuild.comtDreamDetail(requirementId, edit_comt.getText().toString(), parentId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtListener);
    }

    BaseRequestListener comtListener = new JsonRequestListener() {
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
            showShortToast("评论成功！");
            // 清空评论并隐藏评论框
            edit_comt.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edit_comt.getWindowToken(), 0);
            }
            bottom_input_area.setVisibility(View.GONE);
            bottom_bar.setVisibility(View.VISIBLE);
            PAGE_NUM = 1;
            asyncGetDreamComts();
        }
    };

    /**
     * 梦想详情
     */
    private void asyncDreamDetail() {
        String wholeUrl = AppUrl.host + AppUrl.REQUERE_DETAIL;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {

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
            dreamDetail = gson.fromJson(jsonResult.toString(), DreamDetailBean.class);
            predictionList = dreamDetail.getPredictionList();
            hadPraised = dreamDetail.isHadPraised();
            if (hadPraised) {
                img_dianzan.setBackgroundResource(R.drawable.hongsedianzan_img);
            } else {
                img_dianzan.setBackgroundResource(R.drawable.mengxiangxiangqing_dianzan);
            }
            if (dreamDetail.getPraiseNum() == 0) {
                support_num.setText("点赞");
            } else {
                //support_num.setText(dreamDetail.getPraiseNum() + "");

                support_num.setText("点赞");
            }
            logout("dreamDetail.getHeadUrl()======" + dreamDetail.getHeadUrl());
            Glide.with(DreamDetailActivity.this).load(dreamDetail.getHeadUrl()).placeholder(R.drawable.headimg_default_img)
                    .transform(new GlideCircleTransform(DreamDetailActivity.this)).into(user_img);
            //ImageManager.getInstance().displayImg(img_head, dreamDetail.getCoverPic());
            Glide.with(DreamDetailActivity.this).load(dreamDetail.getCoverPic()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    img_head.setImageBitmap(ImageManager.zoomBitmap(resource, 750, 400));
                }
            });
            detailAdapter.detailInfo(dreamDetail);
            detailAdapter.notifyDataSetChanged();
            PAGE_NUM = 1;
            asyncGetDreamComts();
        }
    };

    /**
     * 请求点赞
     */
    private void asyncSupport() {
        String wholeUrl = AppUrl.host + AppUrl.REQUERE_PRAISE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, supportListener);
    }

    BaseRequestListener supportListener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            //showShortToast(errorMsg);
            showShortToast("点赞成功");
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            img_dianzan.setBackgroundResource(R.drawable.hongsedianzan_img);
            showShortToast("点赞成功");
            asyncDreamDetail();
        }
    };

    /**
     * 获取梦想评论列表
     */
    protected void asyncGetDreamComts() {
        String wholeUrl = AppUrl.host + AppUrl.REQUERE_COMMENT_LIST;
        String requestBodyData = ParamBuild.getDreamComts(requirementId, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
//            if (mList.size() < totalSize) {
//                showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
//            dismissDialog();
//            showShortToast(errorMsg);
            lvFootState.addListFootErr();
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
//            dismissDialog();
            try {
                totalSize = jsonResult.getInt("totalSize");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<EvaluateItemInfo> currentPageList = new ArrayList<EvaluateItemInfo>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                mList.clear();
                lvFootState.addListFootEndNew();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                lvFootState.addListFootEndNew();
            } else {
                lvFootState.addListFootIdle();
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                EvaluateItemInfo c = gson.fromJson(actJot.toString(), EvaluateItemInfo.class);
                currentPageList.add(c);
            }
            mList.addAll(currentPageList);
            detailAdapter.setTotalSize(totalSize);
            if (totalSize == 0) {
                evaluate_num.setText("评论");
            } else {
                evaluate_num.setText(totalSize + "");
            }
            detailAdapter.notifyDataSetChanged();
        }
    };

    //展示标题
    @Override
    public void srollOnShow() {
        top_bar.setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
    }

    //隐藏标题
    @Override
    public void srollHide() {
        top_bar.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
    }

    @Override
    public void loadMore() {
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                if (lvFootState.getCurrState() != ListViewFootState.END) {
                    lvFootState.addListFootLoading();
                    PAGE_NUM++;
                    asyncGetDreamComts();
                }
            }
        }, 200);
    }

    @Override
    public void scrolling(int top, int firstVisibleItem) {
        RelativeLayout.MarginLayoutParams margin = new RelativeLayout.MarginLayoutParams(img_area.getLayoutParams());
        margin.setMargins(DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                top,
                DevAttr.getScreenWidth(mContext) / 2 - DevAttr.dip2px(mContext, 34),
                DevAttr.dip2px(mContext, 120) + margin.height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        img_area.setLayoutParams(layoutParams);
        if (firstVisibleItem > 0) {
            img_area.setVisibility(View.GONE);
        } else {
            img_area.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onComtClicked(Long parentId) {
        curParentId = parentId;
        showComtArea(1);
    }

    @Override
    public void toSameDreamPeople() {
        Intent intent = new Intent(mContext, SameDreamActivity.class);
        intent.putParcelableArrayListExtra("predictionList", (ArrayList) predictionList);
        startActivity(intent);
    }
}
