package com.park61.moduel.me;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.PermissionHelper;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.firsthead.TopicPublisActivity;
import com.park61.moduel.me.bean.MyBabyCourseItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.ActsRatingBar;
import com.park61.widget.wxpicselect.bean.Bimp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by shubei on 2017/7/23.
 */

public class BabyCourseEvaActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE = 2;

    private View area_add_and_tip;
    private GridView gv_input_pic;// 图片展示gridview
    private ActsRatingBar ratingbar_course_point, ratingbar_teacher_point;
    private Button btn_evaluate;
    private EditText edit_input_word;
    private ImageView img_course, img_teacher;
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private TextView tv_teacher_name;

    private ArrayList<String> mSelectPath;
    private ArrayList<String> mSelectPathAdd = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private int courseId, teacherId, childId;
    private long applyId;
    private String content, courseImg, teacherUrl, teacherName;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_baby_course_eva);
    }

    @Override
    public void initView() {
        setPagTitle("评价");
        area_add_and_tip = findViewById(R.id.area_add_and_tip);
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
        ratingbar_course_point = (ActsRatingBar) findViewById(R.id.ratingbar_course_point);
        ratingbar_teacher_point = (ActsRatingBar) findViewById(R.id.ratingbar_teacher_point);
        btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        img_course = (ImageView) findViewById(R.id.img_course);
        img_teacher = (ImageView) findViewById(R.id.img_teacher);
        tv_teacher_name = (TextView) findViewById(R.id.tv_teacher_name);
    }

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra("courseId", -1);
        teacherId = getIntent().getIntExtra("teacherId", -1);
        childId = getIntent().getIntExtra("childId", -1);
        applyId = getIntent().getLongExtra("applyId", -1);
        courseImg = getIntent().getStringExtra("courseImg");
        teacherUrl = getIntent().getStringExtra("teacherUrl");
        teacherName = getIntent().getStringExtra("teacherName");
        ImageManager.getInstance().displayImg(img_course, courseImg);
        ImageManager.getInstance().displayImg(img_teacher, teacherUrl);
        tv_teacher_name.setText(teacherName + "老师辛苦了，评价一下老师吧！");
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(BabyCourseEvaActivity.this);
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
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingbar_course_point.getPoint() == 0 || ratingbar_teacher_point.getPoint() == 0) {
                    showShortToast("评分不能为空！");
                    return;
                }
                content = edit_input_word.getText().toString().trim();
                if (!TextUtils.isEmpty(content) && content.length() < 5) {
                    showShortToast("评价不能少于5个字哦！");
                    return;
                }
                // 图片为空，文字不为空
                if (CommonMethod.isListEmpty(mSelectPath)) {
                    asyncAddEvaluate();
                    return;
                }
                // 图片不为空时,异步压缩再上传
                new CompressNUploadTask().execute();
            }
        });
    }

    /**
     * 去相册
     */
    public void goToMultiImageSelector(){
        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(true);
        selector.count(4);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(BabyCourseEvaActivity.this, REQUEST_IMAGE);
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
                area_add_and_tip.setVisibility(View.GONE);
                gv_input_pic.setVisibility(View.VISIBLE);
                mSelectPathAdd.clear();
                mSelectPathAdd.addAll(mSelectPath);
                mSelectPathAdd.add("+");
                adapter = new InputPicAdapter();
                gv_input_pic.setAdapter(adapter);
            }
        }
    }

    private class InputPicAdapter extends BaseAdapter {

        private static final int TYPE_COM = 0;
        private static final int TYPE_ADD = 1;

        @Override
        public int getCount() {
            return mSelectPathAdd.size();
        }

        @Override
        public String getItem(int position) {
            return mSelectPathAdd.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mSelectPathAdd.size() - 1) {
                return TYPE_ADD;
            }
            return TYPE_COM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_COM:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_inputpic_item, null);
                    ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
                    ImageManager.getInstance().displayImg(img_input, "file:///" + mSelectPathAdd.get(position));
                    ImageView img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
                    img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSelectPath.remove(position);
                            mSelectPathAdd.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    break;
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_add_item, null);
                    View area_add_pic = convertView.findViewById(R.id.area_add_pic);
                    area_add_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MultiImageSelector selector = MultiImageSelector.create();
                            selector.showCamera(true);
                            selector.count(4);
                            selector.multi();
                            selector.origin(mSelectPath);
                            selector.start(BabyCourseEvaActivity.this, REQUEST_IMAGE);
                        }
                    });
                    break;
            }
            return convertView;
        }
    }

    private void asyncAddEvaluate() {
        String wholeUrl = AppUrl.host + AppUrl.addEvaluate;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("teacherId", teacherId);
        map.put("childId", childId);
        map.put("applyId", applyId);
        map.put("courseScore", ratingbar_course_point.getPoint());
        map.put("teacherScore", ratingbar_teacher_point.getPoint());
        if (!TextUtils.isEmpty(content)) {
            map.put("content", content);
        }
        if (!CommonMethod.isListEmpty(urlList)) {
            StringBuilder sb = new StringBuilder();
            for (String p : urlList) {
                sb.append(p);
                sb.append(",");
            }
            map.put("commentUrl", sb.substring(0, sb.length() - 1).toString());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
            showShortToast("评价成功！");
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
                    asyncAddEvaluate();
                }
            });
        }
    }
}
