package com.park61.moduel.child;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.AliyunUploadUtils.OnUploadListFinish;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.PermissionHelper;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.wxpicselect.AlbumActivity;
import com.park61.widget.wxpicselect.bean.Bimp;
import com.park61.widget.wxpicselect.bean.ImageBean;
import com.park61.widget.wxpicselect.bean.ImageBucket;
import com.park61.widget.wxpicselect.utils.AlbumHelper;

/**
 * 秀萌照发表页面
 *
 * @author super
 */
public class ShowPhotoActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    protected static final int REQ_GET_PIC = 0;// 去获取图片
    protected static final int REQ_SHOW_BIG_PIC = 1;// 去显示图片
    private ArrayList<Bitmap> inputPicList = new ArrayList<Bitmap>();
    private ArrayList<String> inputPicPathList = new ArrayList<String>();
    private ArrayList<String> urlList = new ArrayList<String>();
    // 记录点击完成后回传的选中的imagebean
    private ArrayList<ImageBean> selectedImgBeans = new ArrayList<ImageBean>();
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private Long showingBbId;// 传过来的孩子id
    private Bitmap tianjiaBmp;// 添加图片

    private EditText edit_input_word;// 文字输入框
    private GridView gv_input_pic;// 图片展示gridview
    private Button btn_commit;// 提交按钮

    private static final int TAKE_PHOTO_PICTURE = 10;
    private AlbumHelper helper;
    // 用来存照片的临时文件URI
    private Uri imageUri;
    private String from;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_showphoto);
    }

    @Override
    public void initView() {
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
    }

    @Override
    public void initData() {
        tianjiaBmp = ImageManager.getInstance().readResBitMap(R.drawable.tianjiazhaopian, mContext);
        showingBbId = getIntent().getLongExtra("showingBbId", 0l);
        from = getIntent().getStringExtra("from");
        inputPicList.add(tianjiaBmp);
        adapter = new InputPicAdapter();
        gv_input_pic.setAdapter(adapter);
        if (from != null) {
            if (from.equals("take_photo")) {
                // 临时文件用来存照片
                File temp = new File(Environment.getExternalStorageDirectory().getPath() + "/GHPCacheFolder/photo");// 自已缓存文件夹
                if (!temp.exists()) {
                    temp.mkdirs();
                }
                String filePath = temp.getAbsolutePath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                File tempFile = new File(filePath);
                imageUri = Uri.fromFile(tempFile);
                // 使用MediaStore.ACTION_IMAGE_CAPTURE可以轻松调用Camera程序进行拍照：
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 不使用默认位置，使用uri存图片
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                logout("=====imageUri======" + imageUri);
                startActivityForResult(intent, TAKE_PHOTO_PICTURE);
            }
            if (from.equals("album_photo")) {
                Intent it = new Intent(mContext, AlbumActivity.class);
                Bimp.tempSelectBitmap.clear();
                Bimp.tempSelectBitmap.addAll(selectedImgBeans);
                startActivityForResult(it, REQ_GET_PIC);
            }
        }
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(ShowPhotoActivity.this);
        gv_input_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == inputPicList.size() - 1) {
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
                } else {
                    Intent it = new Intent(mContext, ShowBigPicActivity.class);
                    it.putExtra("big_pic", inputPicPathList.get(position));
                    it.putExtra("position", position);
                    startActivityForResult(it, REQ_SHOW_BIG_PIC);
                }
            }
        });
        btn_commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String inputWords = edit_input_word.getText().toString().trim();
                // 图片和文字都为空
                if (inputPicList.size() == 1 && TextUtils.isEmpty(inputWords)) {
                    showShortToast("您没有添加任何内容呀！");
                    return;
                }
                // 图片为空，文字不为空
                if (inputPicList.size() == 1 && !TextUtils.isEmpty(inputWords)) {
                    asyncAddShowPhotos();
                    return;
                }

                // 图片不为空时,异步压缩再上传
                new CompressNUploadTask().execute();
            }
        });
    }

    public void goToAlbum(){
        Intent it = new Intent(mContext, AlbumActivity.class);
        Bimp.tempSelectBitmap.clear();
        Bimp.tempSelectBitmap.addAll(selectedImgBeans);
        startActivityForResult(it, REQ_GET_PIC);
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
//		if (resultCode != RESULT_OK || data == null)
//			return;
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO_PICTURE) {
            Bimp.tempSelectBitmap.clear();
            Bimp.tempSelectBitmap.addAll(selectedImgBeans);
            final Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(imageUri);
            GlobalParam.PhotosNeedRefresh = true;
            mContext.sendBroadcast(intent);// 这个广播的目的就是更新图库，发了这个广播进入相册就
            showDialog();
            new Handler().postDelayed((new Runnable() {

                @Override
                public void run() {
                    List<ImageBucket> mFolderList = helper.getImagesBucketList(true);
                    List<ImageBean> imgList = new ArrayList<ImageBean>();
                    for (int i = 0; i < mFolderList.size(); i++) {
                        imgList.addAll(mFolderList.get(i).imageList);
                    }
                    // 这是个分组的排序算法，为了让所有的数据统一性
                    Collections.sort(imgList, new Comparator<ImageBean>() {

                        @Override
                        public int compare(ImageBean lhs, ImageBean rhs) {
                            Long leftSize = Long.parseLong(lhs.date);
                            Long rightSize = Long.parseLong(rhs.date);
                            return rightSize.compareTo(leftSize);
                        }
                    });
                    Bimp.tempSelectBitmap.add(imgList.get(0));
                    setResult(RESULT_OK, intent);
                    selectedImgBeans.clear();
                    selectedImgBeans.addAll(Bimp.tempSelectBitmap);
                    inputPicList.clear();
                    inputPicPathList.clear();
                    for (int i = 0; i < selectedImgBeans.size(); i++) {
                        inputPicList.add(i, selectedImgBeans.get(i).getBitmap());
                        inputPicPathList.add(selectedImgBeans.get(i).getPath());
                    }
                    // 把“+”直接加在最后，adapter里面会判断隐藏的
                    inputPicList.add(tianjiaBmp);
                    adapter = new InputPicAdapter();
                    gv_input_pic.setAdapter(adapter);
                    dismissDialog();
                }
            }), 2000);

        } else if (resultCode == RESULT_OK && requestCode == REQ_GET_PIC) {
            selectedImgBeans.clear();
            selectedImgBeans.addAll(Bimp.tempSelectBitmap);
            inputPicList.clear();
            inputPicPathList.clear();
            for (int i = 0; i < selectedImgBeans.size(); i++) {
                inputPicList.add(i, selectedImgBeans.get(i).getBitmap());
                inputPicPathList.add(selectedImgBeans.get(i).getPath());
            }
            // 把“+”直接加在最后，adapter里面会判断隐藏的
            inputPicList.add(tianjiaBmp);
            adapter = new InputPicAdapter();
            gv_input_pic.setAdapter(adapter);
            // adapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == REQ_SHOW_BIG_PIC) {
            int position = data.getIntExtra("position", -1);
            inputPicList.remove(position);
            inputPicPathList.remove(position);
            selectedImgBeans.remove(position);
            adapter = new InputPicAdapter();
            gv_input_pic.setAdapter(adapter);
            // adapter.notifyDataSetChanged();
        }
    }

    private class InputPicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return inputPicList.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return inputPicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_inputpic_item, null);
            }
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            img_input.setImageBitmap(inputPicList.get(position));
            if (position == 9) {
                img_input.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    /**
     * 请求添加萌照
     */
    private void asyncAddShowPhotos() {
        String wholeUrl = AppUrl.host + AppUrl.ADD_SHOW_PHOTOS;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urlList.size(); i++) {
            logout("urlList ====== " + urlList.get(i));
            sb.append(urlList.get(i));
            if (i != urlList.size() - 1) {
                sb.append(",");
            }
        }
        String requestBodyData = ParamBuild.addShowPhotos(showingBbId, edit_input_word.getText().toString(), sb.toString());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
            showShortToast("提交成功！");
            setResult(RESULT_OK);
            GlobalParam.GrowingMainNeedRefresh = true;
            finish();
        }
    };

    /**
     * 压缩再上传
     */
    private class CompressNUploadTask extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // 准备将上传的已压缩图片的文件路径
            ArrayList<String> resizedPathList = new ArrayList<String>();
            for (String wholePath : inputPicPathList) {
                File temp = new File(Environment.getExternalStorageDirectory().getPath() + "/GHPCacheFolder/Format");// 自已缓存文件夹
                if (!temp.exists()) {
                    temp.mkdirs();
                }
                String filePath = temp.getAbsolutePath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                File tempFile = new File(filePath);
                // 图像保存到文件中
                try {
                    Bimp.compressBmpToFile(tempFile, wholePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    showShortToast("图片压缩失败！");
                    finish();
                }
                // 将压缩后的地址放入集合
                resizedPathList.add(filePath);
            }
            return resizedPathList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resizedPathList) {
            dismissDialog();
            new AliyunUploadUtils(mContext).uploadPicList(resizedPathList, new OnUploadListFinish() {

                @Override
                public void onError(String path) {
                    showShortToast("上传失败！");
                }

                @Override
                public void onSuccess(List<String> urllist) {
                    urlList.clear();
                    urlList.addAll(urllist);
                    asyncAddShowPhotos();
                }
            });
        }
    }

}
