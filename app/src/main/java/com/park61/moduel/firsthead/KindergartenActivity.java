package com.park61.moduel.firsthead;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.PermissionHelper;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

/**
 * Created by shubei on 2017/11/21.
 */

public class KindergartenActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private View bottom_btn, view_bg, right_img_add;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_kindergarten);
    }

    @Override
    public void initView() {
        bottom_btn = findViewById(R.id.bottom_btn);
        view_bg = findViewById(R.id.view_bg);
        right_img_add = findViewById(R.id.right_img_add);

        bottom_btn.setVisibility(View.GONE);
        view_bg.setVisibility(View.GONE);
        right_img_add.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialog();
        asyncIsJoined();
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(KindergartenActivity.this);
        right_img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToScaner();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToScaner();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToScaner();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
//                        showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
            }
        });
        bottom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog("http://www.61park.cn/teachdownload.html",
                        AppUrl.SHARE_APP_ICON,
                        "亲爱的老师我给您找了一个神器",
                        "您有家长邀请您入住61学院，更多精彩内容就在61学院");
            }
        });
    }

    /**
     * 去扫码
     */
    public void goToScaner() {
        Intent intent = new Intent();
        intent.setClass(mContext, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        }
    }

    /**
     * 用户是否已经加入班级
     */
    private void asyncIsJoined() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.isJoined;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            if (jsonResult.optString("classFlag").equals("true")) {
                Intent it = new Intent(mContext, KgShowActivity.class);
                it.putExtra("PAGE_TITLE", jsonResult.optJSONObject("teachGClass").optString("schoolName"));
                //增加参数幼儿园id
                it.putExtra("groupId", jsonResult.optJSONObject("teachGClass").optInt("groupId", -1));
                //增加班级id
                it.putExtra("classId", jsonResult.optJSONObject("teachGClass").optInt("id", -1));
                startActivity(it);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                bottom_btn.setVisibility(View.VISIBLE);
                view_bg.setVisibility(View.VISIBLE);
                right_img_add.setVisibility(View.VISIBLE);
            }
        }
    };
}
