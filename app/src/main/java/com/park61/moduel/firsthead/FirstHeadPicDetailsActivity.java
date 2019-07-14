package com.park61.moduel.firsthead;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ShareTool;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;
import com.park61.moduel.firsthead.adapter.FhCommtAdapter;
import com.park61.moduel.firsthead.adapter.FhPicAdapter;
import com.park61.moduel.firsthead.bean.TopicDetailsBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/6/12.
 */

public class FirstHeadPicDetailsActivity extends BaseActivity implements FhCommtAdapter.OnReplyClickedLsner {

    private int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 5;

    private WebView webview;
    private ListViewForScrollView lv_fh_commt, lv_pic;
    private ImageView img_fx_pyq, img_fx_qq, img_fx_qzone, img_fx_wx, img_pl, img_praise, img_sc, img_fx;
    private View area_tocommt, bottom_btn;
    private RelativeLayout bottom_input_area;// 底部输入区域
    private EditText edit_comt;// 评论输入框
    private ImageView img_user_head;
    private TextView tv_title, tv_time_and_readnum, tv_give_up_num, tv_commt_num, tv_expert_focus_btn;
    private Button btn_send;

    private long itemId;
    private TopicDetailsBean mTopicDetailsBean;
    private ArrayList<EvaluateItemInfo> mList;
    private FhCommtAdapter commtAdapter;
    private String content;
    private long parentId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_firsthead_pic_details);
    }

    @Override
    public void initView() {
        webview = (WebView) findViewById(R.id.webview);
        lv_fh_commt = (ListViewForScrollView) findViewById(R.id.lv_fh_commt);
        img_fx_pyq = (ImageView) findViewById(R.id.img_fx_pyq);
        img_fx_qq = (ImageView) findViewById(R.id.img_fx_qq);
        img_fx_qzone = (ImageView) findViewById(R.id.img_fx_qzone);
        img_fx_wx = (ImageView) findViewById(R.id.img_fx_wx);
        img_pl = (ImageView) findViewById(R.id.img_pl);
        area_tocommt = findViewById(R.id.area_tocommt);
        bottom_input_area = (RelativeLayout) findViewById(R.id.bottom_input_area);
        edit_comt = (EditText) findViewById(R.id.edit_comt);
        bottom_btn = findViewById(R.id.bottom_btn);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time_and_readnum = (TextView) findViewById(R.id.tv_time_and_readnum);
        tv_give_up_num = (TextView) findViewById(R.id.tv_give_up_num);
        img_user_head = (ImageView) findViewById(R.id.img_user_head);
        tv_commt_num = (TextView) findViewById(R.id.tv_commt_num);
        lv_pic = (ListViewForScrollView) findViewById(R.id.lv_pic);
        img_praise = (ImageView) findViewById(R.id.img_praise);
        img_sc = (ImageView) findViewById(R.id.img_sc);
        img_fx = (ImageView) findViewById(R.id.img_fx);
        tv_expert_focus_btn = (TextView) findViewById(R.id.tv_expert_focus_btn);
    }

    @Override
    public void initData() {
        itemId = getIntent().getLongExtra("itemId", -1);

        mList = new ArrayList<>();
        commtAdapter = new FhCommtAdapter(mContext, mList, FirstHeadPicDetailsActivity.this);
        lv_fh_commt.setAdapter(commtAdapter);

        asyncDetailsData();
    }

    @Override
    public void initListener() {
        img_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
                it.putExtra("itemId", itemId);
                it.putExtra("source", 2);
                startActivity(it);
            }
        });
        area_tocommt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                parentId = 0;
                showComtArea();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                content = edit_comt.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showShortToast("您未填写评论！");
                    return;
                }
                asyncCommitComt();
            }
        });
        img_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncPraiseData();
            }
        });
        img_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncCollectData();
            }
        });
        img_fx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
            }
        });
        tv_expert_focus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTopicDetailsBean.isFocusUser()){
                    dDialog.showDialog("提示", "确定不再关注此人？", "取消", "确认", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asyncCollectDataUser(mTopicDetailsBean.isFocusUser(), mTopicDetailsBean.getUserId() + "");
                            dDialog.dismissDialog();
                        }
                    });
                }else {
                    asyncCollectDataUser(mTopicDetailsBean.isFocusUser(), mTopicDetailsBean.getUserId() + "");
                }
            }
        });
    }

    private void initShareViewAndData() {
        ShareTool.init(shareUrl, sharePic, shareTitle, shareDescription);
        ((ImageView) findViewById(R.id.img_fx_pyq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToWeiXin(mContext, 1);
            }
        });
        ((ImageView) findViewById(R.id.img_fx_wx)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToWeiXin(mContext, 0);
            }
        });
        ((ImageView) findViewById(R.id.img_fx_qq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToQQ(mContext);
            }
        });
        ((ImageView) findViewById(R.id.img_fx_qzone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToQzone(mContext);
            }
        });
    }

    /**
     * 请求详情数据
     */
    private void asyncDetailsData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PIC_DETAILS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mTopicDetailsBean = gson.fromJson(jsonResult.toString(), TopicDetailsBean.class);
            setDataToView();
            asyncCommtData();
        }
    };

    public void setDataToView() {
        shareUrl = AppUrl.APP_INVITE_URL + "/home/toMedialDetail.do?classifyType=0&itemId=" + mTopicDetailsBean.getItemId();
        sharePic = mTopicDetailsBean.getItemMediaList().get(0).getMediaUrl();
        if (TextUtils.isEmpty(sharePic)) {
            sharePic = AppUrl.SHARE_APP_ICON;
        }
        shareTitle =  mTopicDetailsBean.getItemTitle();//mContext.getResources().getString(R.string.app_name);
        shareDescription = mTopicDetailsBean.getSummary();//mTopicDetailsBean.getItemTitle();
        initShareViewAndData();
        
        ImageManager.getInstance().displayImg(img_user_head, mTopicDetailsBean.getIssuerHeadPic());
        tv_title.setText(mTopicDetailsBean.getItemTitle());
        tv_time_and_readnum.setText(mTopicDetailsBean.getIssuerDate() + " | " + mTopicDetailsBean.getItemReadNum() + "次阅读");

        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(mTopicDetailsBean.getItemDesc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
        tv_give_up_num.setText(mTopicDetailsBean.getItemGiveUpNum() + "");
        if (mTopicDetailsBean.isFocus()) {
            img_sc.setImageResource(R.drawable.xqshoucang_focus);
        }

        if (!mTopicDetailsBean.isBackendUser()) {
            FhPicAdapter fhPicAdapter = new FhPicAdapter(mContext, mTopicDetailsBean.getItemMediaList());
            lv_pic.setAdapter(fhPicAdapter);
        }

        if(mTopicDetailsBean.isPraise()){
            img_praise.setImageResource(R.drawable.spdianzan_focus);
            img_praise.setClickable(false);
        }

        if (mTopicDetailsBean.isFocusUser()) {
            tv_expert_focus_btn.setText("已关注");
            tv_expert_focus_btn.setBackgroundColor(Color.parseColor("#dadada"));
        } else {
            tv_expert_focus_btn.setText("+关注");
            tv_expert_focus_btn.setBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
        }
        img_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BlogerInfoActivity.class);
                it.putExtra("userId", mTopicDetailsBean.getUserId());
                mContext.startActivity(it);
            }
        });
    }

    private void asyncPraiseData() {
        String wholeUrl = AppUrl.host + AppUrl.ADD_PRAISE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", 2);//1需求，2图文，3视频
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, pLsner);
    }

    BaseRequestListener pLsner = new JsonRequestListener() {

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
            img_praise.setImageResource(R.drawable.spdianzan_focus);
            tv_give_up_num.setText((mTopicDetailsBean.getItemGiveUpNum() + 1) + "");
            img_praise.setClickable(false);
            showShortToast("点赞成功！");
        }
    };

    private void asyncCollectData() {
        String wholeUrl = "";
        if (mTopicDetailsBean.isFocus()) {
            wholeUrl = AppUrl.host + AppUrl.DEL_COLLECTION;
        } else {
            wholeUrl = AppUrl.host + AppUrl.ADD_COLLECTION;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", 2);//1需求，2图文，3视频
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cLsner);
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
            if (mTopicDetailsBean.isFocus()) {
                img_sc.setImageResource(R.drawable.xqshoucang_defaul);
                mTopicDetailsBean.setFocus(false);
                showShortToast("已取消收藏");
            } else {
                img_sc.setImageResource(R.drawable.xqshoucang_focus);
                mTopicDetailsBean.setFocus(true);
                showShortToast("收藏成功！");
            }
        }
    };

    /**
     * 请求评论数据
     */
    private void asyncCommtData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_COMMENT_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", 2);//source：评论对象类型（1需求，2图文，3视频）当不传入source时默认为梦想屋的评论
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ArrayList<EvaluateItemInfo> currentPageList = new ArrayList<EvaluateItemInfo>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (cmtJay == null || cmtJay.length() <= 0) {
                mList.clear();
                return;
            }
            for (int i = 0; i < cmtJay.length(); i++) {
                JSONObject actJot = cmtJay.optJSONObject(i);
                EvaluateItemInfo p = gson.fromJson(actJot.toString(), EvaluateItemInfo.class);
                currentPageList.add(p);
            }
            mList.clear();
            mList.addAll(currentPageList);
            commtAdapter.notifyDataSetChanged();
            tv_commt_num.setText(jsonResult.optInt("totalSize") + "");

        }
    };

    /**
     * 请求提交评论
     */
    private void asyncCommitComt() {
        String wholeUrl = AppUrl.host + AppUrl.ADD_COMMENT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("source", 2);//source：评论对象类型（1需求，2图文，3视频）当不传入source时默认为梦想屋的评论
        map.put("content", content);
        if (parentId != 0) {
            map.put("parentId", parentId);
        }
        String requestBodyData = ParamBuild.buildParams(map);
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
            bottom_btn.setVisibility(View.VISIBLE);
            bottom_input_area.setVisibility(View.GONE);
            asyncCommtData();
        }
    };

    /**
     * 点击评论按钮，显示评论输入框
     */
    public void showComtArea() {
        // 点击评论按钮，显示评论输入框
        bottom_input_area.setVisibility(View.VISIBLE);
        bottom_btn.setVisibility(View.GONE);
        edit_comt.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edit_comt, 0);
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = bottom_input_area;// getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                bottom_btn.setVisibility(View.VISIBLE);
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

    @Override
    public void onComtClicked(long parentId) {
        if (!CommonMethod.checkUserLogin(mContext))
            return;
        this.parentId = parentId;
        showComtArea();
    }

    private void asyncCollectDataUser(boolean isFocus, String itemId) {
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
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, isFocus ? 1 : 0, coLsner);
    }

    BaseRequestListener coLsner = new JsonRequestListener() {

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
                mTopicDetailsBean.setFocusUser(false);
                tv_expert_focus_btn.setBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
                tv_expert_focus_btn.setText("+关注");
            } else {
                mTopicDetailsBean.setFocusUser(true);
                tv_expert_focus_btn.setBackgroundColor(Color.parseColor("#dadada"));
                tv_expert_focus_btn.setText("已关注");
            }
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_EXPERTLIST"));
        }
    };
}
