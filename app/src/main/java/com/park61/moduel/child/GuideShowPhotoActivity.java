package com.park61.moduel.child;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.PermissionHelper;
import com.park61.moduel.firsthead.TopicPublisActivity;

/**
 * 秀萌照拍照、相册、文字选择界面
 */
public class GuideShowPhotoActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private ImageView img_close;
    private View view_take_photo, view_photo_album, view_text_description;
    private Long showingBbId;// 传过来的孩子id
    private String from = null;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pre_showphoto);
    }

    @Override
    public void initView() {
        img_close = (ImageView) findViewById(R.id.img_close);
        view_take_photo = findViewById(R.id.view_take_photo);
        view_photo_album = findViewById(R.id.view_photo_album);
        view_text_description = findViewById(R.id.view_text_description);
    }

    @Override
    public void initData() {
        showingBbId = getIntent().getLongExtra("showingBbId", 0l);
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(GuideShowPhotoActivity.this);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        view_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToTakePhoto();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToTakePhoto();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToTakePhoto();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                    }
                }
            }
        });
        view_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToAlbum();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToAlbum();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToAlbum();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                    }
                }
            }
        });
        view_text_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = "text_description";
                Intent showPhotoIt = new Intent(mContext, ShowPhotoActivity.class);
                showPhotoIt.putExtra("showingBbId", showingBbId);
                showPhotoIt.putExtra("from", from);
                startActivityForResult(showPhotoIt, 0);
                finish();
            }
        });
    }

    public void goToTakePhoto() {
        from = "take_photo";
        Intent showPhotoIt = new Intent(mContext, ShowPhotoActivity.class);
        showPhotoIt.putExtra("showingBbId", showingBbId);
        showPhotoIt.putExtra("from", from);
        startActivity(showPhotoIt);
        finish();
    }

    public void goToAlbum() {
        from = "album_photo";
        Intent showPhotoIt = new Intent(mContext, ShowPhotoActivity.class);
        showPhotoIt.putExtra("showingBbId", showingBbId);
        showPhotoIt.putExtra("from", from);
        startActivity(showPhotoIt);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityResult(requestCode, resultCode, data);
    }
}
