package com.park61.moduel.me;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.ImageManager;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.SingleConfirmDialog;
import com.park61.widget.picselect.utils.SelectPicNoCropPopWin;
import com.park61.widget.picselect.utils.SelectPicPopupWindow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthUpLoadActivity extends BaseActivity {

    private ImageView area_upload_infopaper, area_upload_photo_and_idcard, area_upload_front_idcard;
    private Button btn_commit_photo;
    private ImageView img_upload_infopaper, img_upload_photo_and_idcard, img_upload_front_idcard;
    private TextView tv_paper_tip, tv_paper_tip2, tv_upload_photo_and_idcard, tv_upload_front_idcard;
    private View area_upload_photo_and_idcard_bg, area_upload_front_idcard_bg;
    private SingleConfirmDialog mSingleConfirmDialog;

    private String expertName, identityNo, domainId, industry, resume, evidence, identityFrontUrl, identityBackUrl, organization, title;
    private String path0, path1, path2;
    private ArrayList<String> urlList = new ArrayList<>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_upload);
    }

    @Override
    public void initView() {
        setPagTitle("申请认证");
        mSingleConfirmDialog = new SingleConfirmDialog(mContext);
        area_upload_infopaper = (ImageView) findViewById(R.id.area_upload_infopaper);
        area_upload_photo_and_idcard = (ImageView) findViewById(R.id.area_upload_photo_and_idcard);
        area_upload_front_idcard = (ImageView) findViewById(R.id.area_upload_front_idcard);
        btn_commit_photo = (Button) findViewById(R.id.btn_commit_photo);
        img_upload_infopaper = (ImageView) findViewById(R.id.img_upload_infopaper);
        img_upload_photo_and_idcard = (ImageView) findViewById(R.id.img_upload_photo_and_idcard);
        img_upload_front_idcard = (ImageView) findViewById(R.id.img_upload_front_idcard);
        tv_paper_tip = (TextView) findViewById(R.id.tv_paper_tip);
        tv_paper_tip2 = (TextView) findViewById(R.id.tv_paper_tip2);
        tv_upload_photo_and_idcard = (TextView) findViewById(R.id.tv_upload_photo_and_idcard);
        tv_upload_front_idcard = (TextView) findViewById(R.id.tv_upload_front_idcard);
        area_upload_photo_and_idcard_bg = findViewById(R.id.area_upload_photo_and_idcard_bg);
        area_upload_front_idcard_bg = findViewById(R.id.area_upload_front_idcard_bg);
    }

    @Override
    public void initData() {
        expertName = getIntent().getStringExtra("expertName");
        identityNo = getIntent().getStringExtra("identityNo");
        domainId = getIntent().getStringExtra("domainId");
        industry = getIntent().getStringExtra("industry");
        resume = getIntent().getStringExtra("resume");
        organization = getIntent().getStringExtra("organization");
        title = getIntent().getStringExtra("title");
    }

    @Override
    public void initListener() {
        area_upload_infopaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SelectPicNoCropPopWin.class);
                startActivityForResult(it, 0);
            }
        });
        area_upload_photo_and_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SelectPicNoCropPopWin.class);
                startActivityForResult(it, 1);
            }
        });
        area_upload_front_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SelectPicNoCropPopWin.class);
                startActivityForResult(it, 2);
            }
        });
        btn_commit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(path0)) {
                    showShortToast("证明材料未提交");
                    return;
                }
                if (TextUtils.isEmpty(path1)) {
                    showShortToast("手持身份证照片未提交");
                    return;
                }
                if (TextUtils.isEmpty(path2)) {
                    showShortToast("身份证背面照片未提交");
                    return;
                }
                List<String> resizedPathList = new ArrayList<>();
                resizedPathList.add(path0);
                resizedPathList.add(path1);
                resizedPathList.add(path2);
                new AliyunUploadUtils(mContext).uploadPicList(resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

                    @Override
                    public void onError(String path) {
                        showShortToast("上传失败！请重试");
                    }

                    @Override
                    public void onSuccess(List<String> urllist) {
                        urlList.clear();
                        urlList.addAll(urllist);
                        //showShortToast("资料上传成功！");
                       /* Intent it = new Intent(mContext, AuthInputActivity.class);
                        it.putExtra("evidence", urlList.get(0));
                        it.putExtra("identityFrontUrl", urlList.get(1));
                        it.putExtra("identityBackUrl", urlList.get(2));
                        startActivity(it);*/
                        evidence = urlList.get(0);
                        identityFrontUrl = urlList.get(1);
                        identityBackUrl = urlList.get(2);
                        asyncExpertAuth();
                    }
                });
                //startActivity(new Intent(mContext, AuthInputActivity.class));
            }
        });
    }

    private void asyncExpertAuth() {
        String wholeUrl = AppUrl.host + AppUrl.expertAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("expertName", expertName);
        map.put("identityNo", identityNo);
        map.put("domainId", domainId);
        map.put("resume", resume);
        map.put("evidence", evidence);
        map.put("identityFrontUrl", identityFrontUrl);
        map.put("identityBackUrl", identityBackUrl);
        map.put("industry", industry);
        map.put("organization", organization);
        map.put("title", title);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            mSingleConfirmDialog.showDialog("提交成功", "官方审核需要在24小时后给出认证结果，请耐心等待哦~", "完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSingleConfirmDialog.dismissDialog();
                    startActivity(new Intent(mContext, AuthCheckInfoActivity.class));
                    for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
                        if (mActivity instanceof AuthInputActivity
                                || mActivity instanceof AuthFirstActivity) {
                            if (mActivity != null) {// 关闭所有页面，置回首页
                                mActivity.finish();
                            }
                        }
                    }
                    finish();
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        if (requestCode == 0) {
            path0 = data.getStringExtra("path");
            logout("path0======" + path0);
            /*Bitmap bmp = ImageManager.getInstance().readFileBitMap(path0);
            img_upload_infopaper.setImageBitmap(bmp);*/
            ImageManager.getInstance().displayImg(img_upload_infopaper, "file:///" + path0);
            area_upload_infopaper.setImageResource(R.drawable.genghuan2);
            tv_paper_tip.setVisibility(View.INVISIBLE);
            tv_paper_tip2.setVisibility(View.INVISIBLE);
        } else if (requestCode == 1) {
            path1 = data.getStringExtra("path");
            logout("path1======" + path1);
           /* Bitmap bmp = ImageManager.getInstance().readFileBitMap(path1);
            img_upload_photo_and_idcard.setImageBitmap(bmp);*/
            ImageManager.getInstance().displayImg(img_upload_photo_and_idcard, "file:///" + path1);
            area_upload_photo_and_idcard.setImageResource(R.drawable.genghuan2);
            tv_upload_photo_and_idcard.setVisibility(View.INVISIBLE);
            area_upload_photo_and_idcard_bg.setBackground(null);
        } else if (requestCode == 2) {
            path2 = data.getStringExtra("path");
            logout("path2======" + path2);
          /*  Bitmap bmp = ImageManager.getInstance().readFileBitMap(path2);
            img_upload_front_idcard.setImageBitmap(bmp);*/
            ImageManager.getInstance().displayImg(img_upload_front_idcard, "file:///" + path2);
            area_upload_front_idcard.setImageResource(R.drawable.genghuan2);
            tv_upload_front_idcard.setVisibility(View.INVISIBLE);
            area_upload_front_idcard_bg.setBackground(null);
        }
    }
}
