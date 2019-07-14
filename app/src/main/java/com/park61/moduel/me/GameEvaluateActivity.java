package com.park61.moduel.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.me.actlist.ActOrderListActivity;
import com.park61.moduel.me.adapter.EvaluateDimensionListAdapter;
import com.park61.moduel.me.adapter.EvaluateInputPictureAdapter;
import com.park61.moduel.me.bean.ActEvaluateDimension;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.wxpicselect.bean.Bimp;
import com.park61.widget.wxpicselect.bean.ImageBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏评价界面
 */
public class GameEvaluateActivity extends BaseActivity {
    private ImageView img_acts;
    private EditText edit_input_word;
    private Button btn_evaluate;
    private CheckBox cb_anonymous;
    private ListViewForScrollView listview;

    private GridView gv_input_pic;
    private Bitmap tianjiaBmp;//添加图片
    private ArrayList<Bitmap> inputPicList = new ArrayList<Bitmap>();
    private ArrayList<String> inputPicPathList = new ArrayList<String>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private EvaluateInputPictureAdapter picAdapter;
    protected static final int REQ_GET_PIC = 0;//获取图片
    protected static final int REQ_BIG_SHOW_PIC = 1;//显示图片
    //记录点击完成后回传的选中的ImageBean;
    private ArrayList<ImageBean> selectedImgBeans = new ArrayList<ImageBean>();
    private Long applyId;// 订单id
    private String actCover;// 游戏封面

    private EvaluateDimensionListAdapter dismensionAdapter;
    private List<ActEvaluateDimension> mList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_game_evaluate);
    }

    @Override
    public void initView() {
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        img_acts = (ImageView) findViewById(R.id.img_acts);
        btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        cb_anonymous = (CheckBox) findViewById(R.id.cb_anonymous);
        listview = (ListViewForScrollView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        applyId = getIntent().getLongExtra("applyId", 0l);
        actCover = getIntent().getStringExtra("actCover");
        ImageManager.getInstance().displayImg(img_acts, actCover);
        tianjiaBmp = ImageManager.getInstance().readResBitMap(R.drawable.ic_evaluate_camera, mContext);
        inputPicList.add(tianjiaBmp);
        picAdapter = new EvaluateInputPictureAdapter(inputPicList, mContext);
        gv_input_pic.setAdapter(picAdapter);

        mList = new ArrayList<ActEvaluateDimension>();
        dismensionAdapter = new EvaluateDimensionListAdapter(mContext,mList);
        listview.setAdapter(dismensionAdapter);
        asyncGetDimension();
    }

    @Override
    public void initListener() {
        gv_input_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == inputPicList.size() - 1) {
                    Intent it = new Intent(mContext, ActsEvaluatePhotoAlbumActivity.class);
                    Bimp.tempSelectBitmap.clear();
                    Bimp.tempSelectBitmap.addAll(selectedImgBeans);
                    startActivityForResult(it, REQ_GET_PIC);
                } else {
                    Intent it = new Intent(mContext, ShowBigPicActivity.class);
                    it.putExtra("big_pic", inputPicPathList.get(position));
                    it.putExtra("position", position);
                    startActivityForResult(it, REQ_BIG_SHOW_PIC);
                }
            }
        });
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mList.size();i++){
                    if(mList.get(i).getPoint()==0){
                        showShortToast("评分不能为空");
                        return;
                    }
                }
                // 图片为空，文字不为空
                if (inputPicList.size() == 1 || TextUtils.isEmpty(edit_input_word.
                        getText().toString())) {
                    asyncEvaluateAct();
                    return;
                }
                // 图片不为空时,异步压缩再上传
                new CompressNUploadTask().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQ_GET_PIC) {
            selectedImgBeans.clear();
            selectedImgBeans.addAll(Bimp.tempSelectBitmap);
            inputPicList.clear();
            inputPicPathList.clear();
            for (int i = 0; i < selectedImgBeans.size(); i++) {
                inputPicList.add(i, selectedImgBeans.get(i).getBitmap());
                inputPicPathList.add(selectedImgBeans.get(i).getPath());
            }
            // 把“照相机图标”直接加在最后，adapter里面会判断隐藏的
            inputPicList.add(tianjiaBmp);
            picAdapter = new EvaluateInputPictureAdapter(inputPicList, mContext);
            gv_input_pic.setAdapter(picAdapter);
        } else if (requestCode == REQ_BIG_SHOW_PIC) {
            int position = data.getIntExtra("position", -1);
            inputPicList.remove(position);
            inputPicPathList.remove(position);
            selectedImgBeans.remove(position);
            picAdapter = new EvaluateInputPictureAdapter(inputPicList, mContext);
            gv_input_pic.setAdapter(picAdapter);
        }
    }
    /**
     * 压缩再上传
     */
    private class CompressNUploadTask extends AsyncTask<String,Integer,
            ArrayList<String>>{

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
                File temp = new File(Environment.getExternalStorageDirectory()
                        .getPath() + "/GHPCacheFolder/Format");// 自已缓存文件夹
                if (!temp.exists()) {
                    temp.mkdirs();
                }
                String filePath = temp.getAbsolutePath() + "/"
                        + Calendar.getInstance().getTimeInMillis() + ".jpg";
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
            new AliyunUploadUtils(mContext).uploadPicList(
                    resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

                        @Override
                        public void onError(String path) {
                            showShortToast("上传失败！");
                        }

                        @Override
                        public void onSuccess(List<String> urllist) {
                            urlList.clear();
                            urlList.addAll(urllist);
                            asyncEvaluateAct();
                        }
                    });
        }
    }
    /**
     * 请求获取评价维度
     */
    private void asyncGetDimension(){
        String wholeUrl = AppUrl.host + AppUrl.GET_DIMENSION;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                listener);
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
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ActEvaluateDimension item = gson.fromJson(jot.toString(), ActEvaluateDimension.class);
                mList.add(item);
            }
            dismensionAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 请求提交评价
     */
    private void asyncEvaluateAct() {
        String wholeUrl = AppUrl.host + AppUrl.CLASS_EVALUATE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityApplyId", applyId);
        map.put("content", edit_input_word.getText().toString());
        JSONArray jay = new JSONArray();
        for (int i = 0; i < urlList.size(); i++) {
            jay.put(urlList.get(i));
        }
        map.put("pictureUrlList", jay.toString());
        for(int i=0;i<mList.size();i++){
            map.put(mList.get(i).getId()+"",mList.get(i).getPoint());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                commitListener);
    }

    BaseRequestListener commitListener = new JsonRequestListener() {

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
            ActOrderListActivity.needRefresh = true;
        }
    };
}
