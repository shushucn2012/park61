package com.park61.moduel.firsthead;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.PermissionHelper;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.wxpicselect.bean.Bimp;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by shubei on 2017/6/13.
 */

public class TopicPublisActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;


    private static final int REQUEST_IMAGE = 2;
    private View area_add_and_tip;
    private GridView gv_input_pic;// 图片展示gridview
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private TextView tv_left_pic_num, tv_finish, tv_title_num;
    private EditText edit_title, edit_input_word;

    private ArrayList<String> mSelectPath;
    private ArrayList<String> urlList = new ArrayList<String>();
    private String titleStr, inputWords;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_topic_publish);
    }

    @Override
    public void initView() {
        area_add_and_tip = findViewById(R.id.area_add_and_tip);
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
        tv_left_pic_num = (TextView) findViewById(R.id.tv_left_pic_num);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        tv_title_num = (TextView) findViewById(R.id.tv_title_num);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(TopicPublisActivity.this);
        area_add_and_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToMultiImageSelector();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToMultiImageSelector();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToMultiImageSelector();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                    }
                }
            }
        });
        gv_input_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                bIt.putExtra("picUrl", mSelectPath.get(position));
                bIt.putStringArrayListExtra("urlList", mSelectPath);
                mContext.startActivity(bIt);
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                titleStr = edit_title.getText().toString().trim();
                inputWords = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(titleStr)) {
                    showShortToast("请填写标题");
                    return;
                }
                if (TextUtils.isEmpty(inputWords)) {
                    showShortToast("请填写内容");
                    return;
                }
                if (mSelectPath == null || mSelectPath.size() == 0) {
                    showShortToast("请添加图片");
                    return;
                }
                // 图片不为空时,异步压缩再上传
                new CompressNUploadTask().execute();
            }
        });

        edit_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_title_num.setText(s.length() + "/50");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 去相册
     */
    public void goToMultiImageSelector(){
        MultiImageSelector selector = MultiImageSelector.create(mContext);
        selector.showCamera(true);
        selector.count(9);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(TopicPublisActivity.this, REQUEST_IMAGE);
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
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                logout("mSelectPath===========================" + sb.toString());
                if (mSelectPath.size() == 9) {
                    area_add_and_tip.setVisibility(View.GONE);
                }
                tv_left_pic_num.setText("还能添加" + (9 - mSelectPath.size()) + "张\n支持png、jpg、jpeg");
                gv_input_pic.setVisibility(View.VISIBLE);
                adapter = new InputPicAdapter();
                gv_input_pic.setAdapter(adapter);
            }
        }
    }

    private class InputPicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSelectPath.size();
        }

        @Override
        public String getItem(int position) {
            return mSelectPath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_fh_inputpic_item, null);
            }
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            ImageManager.getInstance().displayImg(img_input, "file:///" + mSelectPath.get(position));
            return convertView;
        }
    }

    /**
     * 请求添加萌照
     */
    private void asyncAddShowPhotos() {
        String wholeUrl = AppUrl.host + AppUrl.RELEASE_BLOG;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urlList.size(); i++) {
            logout("urlList ====== " + urlList.get(i));
            sb.append(urlList.get(i));
            if (i != urlList.size() - 1) {
                sb.append(",");
            }
        }
        String requestBodyData = ParamBuild.addBlogPhotos(titleStr, urlList.get(0), inputWords, inputWords, sb.toString());
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
            showShortToast("提交成功！");
            setResult(RESULT_OK);
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
            for (String wholePath : mSelectPath) {
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
            new AliyunUploadUtils(mContext).uploadPicList(resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

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
