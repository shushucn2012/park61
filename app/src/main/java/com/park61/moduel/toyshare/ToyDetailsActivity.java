package com.park61.moduel.toyshare;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.park61.BaseActivity;
import com.park61.CanBackWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.StringUtils;
import com.park61.moduel.toyshare.bean.JoyShareItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/6/19.
 */

public class ToyDetailsActivity extends BaseActivity {

    private int id;
    private JoyShareItem mJoyShareItem;

    private AliyunVodPlayer aliyunVodPlayer;
    private SurfaceView surfaceView;
    private TextView currentPosition, totalDuration, tv_title, tv_real_price, tv_share_price, tv_read_num, tv_tmall_price, tv_jd_price,
            tv_left_num;
    private SeekBar progress;
    private View area_play, area_back, area_tmall, area_jd, area_bottom_right, area_share, area_apply_progress_rule, line_between_price_tamll;
    private ImageView img_play, img_praise, img_sc;
    private Button btn_send;
    private AliyunPlayAuth mAliyunPlayAuth;
    private WebView webview;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_toy_details);
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_real_price = (TextView) findViewById(R.id.tv_real_price);
        tv_share_price = (TextView) findViewById(R.id.tv_share_price);
        tv_read_num = (TextView) findViewById(R.id.tv_read_num);
        tv_tmall_price = (TextView) findViewById(R.id.tv_tmall_price);
        tv_jd_price = (TextView) findViewById(R.id.tv_jd_price);
        webview = (WebView) findViewById(R.id.webview);
        tv_left_num = (TextView) findViewById(R.id.tv_left_num);
        area_back = findViewById(R.id.area_back);
        area_tmall = findViewById(R.id.area_tmall);
        area_jd = findViewById(R.id.area_jd);
        area_bottom_right = findViewById(R.id.area_bottom_right);
        area_share = findViewById(R.id.area_share);
        area_apply_progress_rule = findViewById(R.id.area_apply_progress_rule);
        line_between_price_tamll = findViewById(R.id.line_between_price_tamll);

        initPlayer();
    }

    private void initPlayer() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        currentPosition = (TextView) findViewById(R.id.currentPosition);
        totalDuration = (TextView) findViewById(R.id.totalDuration);
        progress = (SeekBar) findViewById(R.id.progress);
        area_play = findViewById(R.id.area_play);
        img_play = (ImageView) findViewById(R.id.img_play);

        aliyunVodPlayer = new AliyunVodPlayer(this);
        aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功之后可以调用start方法开始播放
                showVideoProgressInfo();
                startAliyunPlayer();
            }
        });
        aliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(int i, int i1, String s) {
                Toast.makeText(mContext, "失败!原因：" + i + " " + i1 + s, Toast.LENGTH_SHORT).show();
            }
        });
        aliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                stopUpdateTimer();
//                progress.setProgress(0);
//                img_play.setVisibility(View.VISIBLE);
//                aliyunVodPlayer.setAuthInfo(mAliyunPlayAuth);
//                aliyunVodPlayer.prepareAsync();
            }
        });
        aliyunVodPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int percent) {
                updateBufferingProgress(percent);
            }
        });
        aliyunVodPlayer.setDisplay(surfaceView.getHolder());
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAliyunPlayer();
                img_play.setVisibility(View.GONE);
            }
        });
        area_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliyunVodPlayer.pause();
                img_play.setVisibility(View.VISIBLE);
            }
        });
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    aliyunVodPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateBufferingProgress(int percent) {
        int duration = (int) aliyunVodPlayer.getDuration();
        progress.setSecondaryProgress((int) (duration * percent * 1.0f / 100));
    }

    private void showVideoProgressInfo() {
        int curPosition = (int) aliyunVodPlayer.getCurrentPosition();
        currentPosition.setText(DateTool.formatMsTime(curPosition));
        int duration = (int) aliyunVodPlayer.getDuration();
        totalDuration.setText(DateTool.formatMsTime(duration));

        progress.setMax(duration);
        progress.setProgress(curPosition);

        startUpdateTimer();
    }

    private void startUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
        progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
    }

    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }

    private Handler progressUpdateTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showVideoProgressInfo();
        }
    };

    //用来记录前后台切换时的状态，以供恢复。
    private IAliyunVodPlayer.PlayerState mPlayerState;

    private void resumePlayerState() {
        if (mPlayerState == IAliyunVodPlayer.PlayerState.Paused) {
            aliyunVodPlayer.pause();
        } else if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {
            startAliyunPlayer();
        }
    }

    private void savePlayerState() {
        mPlayerState = aliyunVodPlayer.getPlayerState();
        if (aliyunVodPlayer.isPlaying()) {
            //然后再暂停播放器
            aliyunVodPlayer.pause();
        }
    }

    /**
     * aliyunVodPlayer.start()
     */
    private void startAliyunPlayer() {
        if (!CommonMethod.getNetworkType(mContext).equals("WIFI")) {
            dDialog.showDialog("提示", "您当前使用的是‘3G/4G’网络，确定继续播放吗？", "确定", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aliyunVodPlayer.start();
                }
            }, null);
        } else {
            aliyunVodPlayer.start();
        }
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra("id", -1);
        asyncDetailsData();
    }

    @Override
    public void initListener() {
        area_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        area_tmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mJoyShareItem.getTmallLink())) {
                    showShortToast("暂无链接");
                    return;
                }
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", mJoyShareItem.getTmallLink());
                startActivity(intent);
            }
        });
        area_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mJoyShareItem.getJdLink())) {
                    showShortToast("暂无链接");
                    return;
                }
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", mJoyShareItem.getJdLink());
                startActivity(intent);
            }
        });
        area_bottom_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonMethod.checkUserLogin(mContext)) {
                    return;
                }
                GlobalParam.CurJoy = mJoyShareItem;
                startActivity(new Intent(mContext, ToySharePayDepositActivity.class));
            }
        });
        area_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl = "http://m.61park.cn/toy/toMyToyShareDetail.do?toyShareId=" + mJoyShareItem.getId();
                sharePic = mJoyShareItem.getIntroductionImg();
                shareTitle = mContext.getResources().getString(R.string.app_name);
                shareDescription = mJoyShareItem.getName();
                showShareDialog(shareUrl, sharePic, shareTitle, shareDescription, mJoyShareItem.getId(), 0);
            }
        });
        area_apply_progress_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", "http://m.61park.cn/toy/toRuleDetail.do");
                startActivity(intent);
            }
        });
    }

    /**
     * 请求详情数据
     */
    private void asyncDetailsData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_BOX_SERIES_OBJECT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
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
            String authInfo = jsonResult.optString("playAuth");
            String vid = jsonResult.optString("introductionVideo");
            AliyunPlayAuth.AliyunPlayAuthBuilder aliyunAuthInfoBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            aliyunAuthInfoBuilder.setPlayAuth(authInfo);
            aliyunAuthInfoBuilder.setVid(vid);
            aliyunAuthInfoBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL);
            mAliyunPlayAuth = aliyunAuthInfoBuilder.build();
//            aliyunVodPlayer.setAuthInfo(mAliyunPlayAuth);
//            aliyunVodPlayer.prepareAsync();

            mJoyShareItem = gson.fromJson(jsonResult.toString(), JoyShareItem.class);
            setDataToView();
        }
    };

    public void setDataToView() {
        /*shareUrl = AppUrl.APP_INVITE_URL + "/home/toMedialDetail.do?classifyType=0&itemId=" + mTopicDetailsBean.getItemId();
        sharePic = mTopicDetailsBean.getItemMediaList().get(0).getMediaUrl();
        if (TextUtils.isEmpty(sharePic)) {
            sharePic = AppUrl.SHARE_APP_ICON;
        }
        shareTitle =mContext.getResources().getString(R.string.app_name);
        shareDescription = mTopicDetailsBean.getItemTitle();
        initShareViewAndData();*/

        tv_title.setText(mJoyShareItem.getName());
        tv_real_price.setText(StringUtils.moneyUnit() + mJoyShareItem.getMarketPrice());
        tv_real_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_share_price.setText(mJoyShareItem.getSharePrice());
        tv_read_num.setText(mJoyShareItem.getUserParticipateNum() + "人已参加");
        tv_tmall_price.setText(StringUtils.moneyUnit() + (mJoyShareItem.getTmallPrice() == null ? "" : mJoyShareItem.getTmallPrice()));
        tv_jd_price.setText(StringUtils.moneyUnit() + (mJoyShareItem.getJdPrice() == null ? "" : mJoyShareItem.getJdPrice()));

        if (TextUtils.isEmpty(mJoyShareItem.getTmallLink())) {
            area_tmall.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mJoyShareItem.getJdLink())) {
            area_jd.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mJoyShareItem.getTmallLink()) && TextUtils.isEmpty(mJoyShareItem.getJdLink())) {
            line_between_price_tamll.setVisibility(View.GONE);
        }

        tv_left_num.setText("只剩" + mJoyShareItem.getCanApplyNum() + "件，把TA带回家");

        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(mJoyShareItem.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);

        if (FU.isNumEmpty(mJoyShareItem.getCanApplyNum() + "") || !mJoyShareItem.isCanApply()) {
            area_bottom_right.setBackgroundResource(R.color.gcccccc);
            area_bottom_right.setClickable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayerState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePlayerState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aliyunVodPlayer.release();
        stopUpdateTimer();
        progressUpdateTimer = null;
    }


}
