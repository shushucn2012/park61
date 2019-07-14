package com.park61.widget.picselect.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.PermissionHelper;
import com.park61.moduel.child.ShowPhotoActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectPicPopupWindow extends BaseActivity implements OnClickListener {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    public static final int CIRCLE_CROP_PIC = 0;
    public static final int REC_CROP_PIC = 1;

    private int curCropShap = 0; // 裁剪形状 0 圆形，1 方形 750-400

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    // temp file
    private static final String IMAGE_FILE_LOCATION = "file://"
            + Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";

    private static final int TAKE_PHOTO_PICTURE = 10;

    // The Uri to store the bigbitmap
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
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
                break;
            case R.id.btn_pick_photo:
                Utils.getInstance().selectPicture(this);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    public void goToTakePhoto(){
        // 使用MediaStore.ACTION_IMAGE_CAPTURE可以轻松调用Camera程序进行拍照：
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 不使用默认位置，使用uri存图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO_PICTURE);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.select_pic_dialog);
    }

    @Override
    public void initView() {
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
        layout = (LinearLayout) findViewById(R.id.pop_layout);
    }

    @Override
    public void initData() {
        curCropShap = getIntent().getIntExtra("CROP_SHAPE", 0);
        logout("curCropShap===========" + curCropShap);
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(SelectPicPopupWindow.this);
        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，
        // 点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (null == data && requestCode != TAKE_PHOTO_PICTURE) {
            return;
        }
        Uri uri = null;
        if (requestCode == AppConstant.KITKAT_LESS) {
            uri = data.getData();
            // 调用图片裁剪方法
            if (curCropShap == CIRCLE_CROP_PIC) {
                Utils.getInstance().cropPicture(this, uri);
            } else {
                Utils.getInstance().cropPictureRec(this, uri);
            }
        } else if (requestCode == AppConstant.KITKAT_ABOVE) {
            uri = data.getData();
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = Utils.getInstance().getPath(this, uri);
            if (curCropShap == CIRCLE_CROP_PIC) {
                Utils.getInstance().cropPicture(this, Uri.fromFile(new File(thePath)));
            } else {
                Utils.getInstance().cropPictureRec(this, Uri.fromFile(new File(thePath)));
            }
        } else if (requestCode == TAKE_PHOTO_PICTURE) {
            // 调用照片裁剪方法
            if (curCropShap == CIRCLE_CROP_PIC) {
                Utils.getInstance().cropImageUri(this, imageUri);
            } else {
                Utils.getInstance().cropImageUriRec(this, imageUri);
            }
        } else if (requestCode == AppConstant.INTENT_CROP) {
            Bitmap bitmap = data.getParcelableExtra("data");
            // 如果没有data返回,就取uri
            if (bitmap == null)
                bitmap = Utils.getInstance().decodeUriAsBitmap(mContext, imageUri);
            File temp = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/GHPCacheFolder/");// 自已缓存文件夹
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String filePath = temp.getAbsolutePath() + "/"
                    + Calendar.getInstance().getTimeInMillis() + ".jpg";
            File tempFile = new File(filePath);
            // 图像保存到文件中
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(tempFile);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
//					showShortToast("已生成缓存文件，等待上传！文件位置："+ tempFile.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                showShortToast("图片裁剪有误！请重试！");
                finish();
            }
            Intent picData = new Intent();
            picData.putExtra("path", filePath);
            setResult(RESULT_OK, picData);
            finish();
        }
    }

}
