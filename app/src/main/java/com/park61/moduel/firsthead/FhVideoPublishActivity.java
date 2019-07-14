package com.park61.moduel.firsthead;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.google.gson.Gson;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.FilesManager;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.TopicDetailsBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CircleProgressDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/6/13.
 */
public class FhVideoPublishActivity extends BaseActivity implements View.OnClickListener{

    private ImageView img_video_cover, back, img_fx_pyq, img_fx_qq, img_fx_qzone, img_fx_wx;
    private TextView tv_video_duration;
    private Button btn_publish;
    private EditText edit_input_word;
    private View area_back;
    private VODUploadCallback callback;

    private String path, duration, size;
    private VODUploadClient uploader;
    private String uploadAuth, uploadAddress, requestIdA, videoId, desStr;
    private String shareType = "-1";

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mCircleProgressDialog.dialogDismiss();
                asyncFabu();
            } else if (msg.what == 1) {
                logout("===================handleMessage=========================" + msg.arg1);
                mCircleProgressDialog.setProgress(msg.arg1);
            } else if (msg.what == 2) {
                showShortToast("上传失败，请重试！");
                mCircleProgressDialog.dialogDismiss();
            }
        }
    };

    private CircleProgressDialog mCircleProgressDialog;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fh_video_publish);
    }

    @Override
    public void initView() {
        mCircleProgressDialog = new CircleProgressDialog(mContext);
        img_video_cover = (ImageView) findViewById(R.id.img_video_cover);
        tv_video_duration = (TextView) findViewById(R.id.tv_video_duration);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        back = (ImageView) findViewById(R.id.back);
        area_back = findViewById(R.id.area_back);
        img_fx_pyq = (ImageView) findViewById(R.id.img_fx_pyq);
        img_fx_qq = (ImageView) findViewById(R.id.img_fx_qq);
        img_fx_qzone = (ImageView) findViewById(R.id.img_fx_qzone);
        img_fx_wx = (ImageView) findViewById(R.id.img_fx_wx);
    }

    @Override
    public void initData() {
        path = getIntent().getStringExtra("videopath");
        duration = getIntent().getStringExtra("videoduration");
        size = getIntent().getStringExtra("videosize");
        logout("============================path=========================" + path);
        logout("============================duration=========================" + duration);
        logout("============================size=========================" + size);
        if (TextUtils.isEmpty(size)) {
            try {
                size = FilesManager.getFileSize(path) + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            logout("============================size2=========================" + size);
        }
        ImageManager.getInstance().displayImg(img_video_cover, "file:///" + path);
        tv_video_duration.setText(duration);
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

        img_fx_pyq.setOnClickListener(this);
        img_fx_qq.setOnClickListener(this);
        img_fx_qzone.setOnClickListener(this);
        img_fx_wx.setOnClickListener(this);

        callback = new VODUploadCallback() {

            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                uploader.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
                logout("=======================================开始上传==========================================");
                mCircleProgressDialog.showDialog();
            }

            /**
             * 上传成功回调
             */
            public void onUploadSucceed(UploadFileInfo info) {
                logout("=======================================上传成功==========================================");
                handler.sendEmptyMessage(0);
            }

            /**
             * 上传失败
             */
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                logout("=======================================上传失败==========================================");
                handler.sendEmptyMessage(2);
            }

            /**
             * 回调上传进度
             * @param uploadedSize 已上传字节数
             * @param totalSize 总共需要上传字节数
             */
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = (int) ((uploadedSize * 100) / totalSize);
                handler.sendMessage(msg);
                logout("=======================================onUploadProgress==========================================" + uploadedSize + "/" + totalSize);
            }

            /**
             * 上传凭证过期后，会回调这个接口
             * 可在这个回调中获取新的上传，然后调用resumeUploadWithAuth继续上传
             */
            public void onUploadTokenExpired() {
                logout("=======================================onUploadTokenExpired==========================================");
            }

            /**
             * 上传过程中，状态由正常切换为异常时触发
             */
            public void onUploadRetry(String code, String message) {
                logout("=======================================onUploadRetry==========================================");
            }

            /**
             * 上传过程中，从异常中恢复时触发
             */
            public void onUploadRetryResume() {
                logout("=======================================onUploadRetryResume==========================================");
            }
        };
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FU.paseLong(size) > 50 * 1024 * 1024) {
                    showShortToast("不能上传大于50M的视频");
                    return;
                }
                desStr = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(desStr)) {
                    showShortToast("请填写视频描述");
                    return;
                }
                asyncDetailsData();
            }
        });
    }

    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle(desStr);
        vodInfo.setDesc(desStr);
        vodInfo.setCateId(0);
        vodInfo.setIsProcess(true);
        //vodInfo.setCoverUrl("http://www.taobao.com/" + 0 + ".jpg");
        List<String> tags = new ArrayList<>();
        tags.add("61区");
        vodInfo.setTags(tags);
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        return vodInfo;
    }

    /**
     * 请求详情数据
     */
    private void asyncDetailsData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALIYUN_AUTH;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("localPath", path);
        map.put("size", size);
        map.put("title", desStr);
        map.put("description", desStr);
        map.put("tags", "61区");
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            requestIdA = jsonResult.optString("requestId");
            uploadAuth = jsonResult.optString("uploadAuth");
            uploadAddress = jsonResult.optString("uploadAddress");
            videoId = jsonResult.optString("videoId");

            uploader = new VODUploadClientImpl(mContext);
            uploader.init(callback);
            uploader.addFile(path, getVodInfo());
            uploader.start();
        }
    };

    private void asyncFabu() {
        String wholeUrl = AppUrl.host + AppUrl.RELEASE_VIDEO;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("summary", desStr);
        map.put("mediaId", videoId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, fLsner);
    }

    BaseRequestListener fLsner = new JsonRequestListener() {

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
            //showShortToast("发布成功！");
            Intent it = new Intent();
            it.putExtra("id", jsonResult.optString("id"));
            it.putExtra("img", jsonResult.optString("img"));
            it.putExtra("descrp", desStr);
            it.putExtra("path", path);
            it.putExtra("shareType", shareType);
            setResult(RESULT_OK, it);
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_fx_pyq:
                shareType = "0";
                img_fx_pyq.setImageResource(R.drawable.fxpengyouquan);
                img_fx_qq.setImageResource(R.drawable.qq_default);
                img_fx_qzone.setImageResource(R.drawable.kj_default);
                img_fx_wx.setImageResource(R.drawable.wx_default);
                break;
            case R.id.img_fx_qq:
                shareType = "1";
                img_fx_qq.setImageResource(R.drawable.fxqq);
                img_fx_pyq.setImageResource(R.drawable.pyq_default);
                img_fx_qzone.setImageResource(R.drawable.kj_default);
                img_fx_wx.setImageResource(R.drawable.wx_default);
                break;
            case R.id.img_fx_qzone:
                shareType = "2";
                img_fx_qzone.setImageResource(R.drawable.fxkongjian);
                img_fx_qq.setImageResource(R.drawable.qq_default);
                img_fx_pyq.setImageResource(R.drawable.pyq_default);
                img_fx_wx.setImageResource(R.drawable.wx_default);
                break;
            case R.id.img_fx_wx:
                shareType = "3";
                img_fx_wx.setImageResource(R.drawable.fxweixin);
                img_fx_qq.setImageResource(R.drawable.qq_default);
                img_fx_pyq.setImageResource(R.drawable.pyq_default);
                img_fx_qzone.setImageResource(R.drawable.kj_default);
                break;
        }
    }
}
