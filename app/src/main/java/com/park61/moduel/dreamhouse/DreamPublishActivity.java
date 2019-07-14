package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.ComWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.adapter.DreamInputPicAdapter;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.picselect.utils.SelectPicPopupWindow;
import com.park61.widget.wheel.DateTimeDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 梦想发布页面
 * Created by shushucn2012 on 2017/1/10.
 */
public class DreamPublishActivity extends BaseActivity {

    public static final int GET_FLAG = 10;
    public static final int GET_GAME = 11;
    public static final int GET_CONTENT_PIC = 100;

    private TextView tv_dream_time_value, tv_addflag_tip, tv_flag_name, tv_fuwu_xieyi;
    private ImageView img_choose_photo, img_dream_cover;
    private View area_choose_dream_time, area_add_flag, area_choose_game;
    private Button btn_publish;
    private CheckBox chk_xieyi;
    private EditText edit_dream_title;
    private LinearLayout actdetails_content, area_input_content;
    private WebView webview_actdetails, webview_input_textarea;
    private ImageView img_xiangji;
    private ScrollView scroll_content;
    private GridView gv_input_pic;
    private DreamInputPicAdapter mDreamInputPicAdapter;

    //    private Button btn_insert_pic, btn_show_source;
    private List<String> contentPicList;
    private List<Bitmap> bmpList;
    private DateTimeDialog myTimeDialog;
    private Calendar calendar;
    private String chosenFlagId;//需求标签id
    private String title;//标题
    private String expectTimeStr;//用户期望实现时间
    private String coverPic;//封面图片
    private String content;//内容描述
    private String isAgree;//是否同意服务协议
    private Bitmap tianjiaBmp;// 添加图片
    private Bitmap addbmp;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showShortToast("封面上传成功！");
                    break;
                case 1:
                    showShortToast("封面上传失败请重试！");
                    break;
                case 10:
                    showShortToast("图片上传成功！");
                    logout("1111111111111111112222222222222222222");
                    bmpList.add(0, addbmp);
                    contentPicList.add(0, (String) msg.obj);
                    mDreamInputPicAdapter.notifyDataSetChanged();
                    break;
                case 11:
                    showShortToast("图片上传失败！");
                    break;
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dream_publish);
    }

    @Override
    public void initView() {
        tv_dream_time_value = (TextView) findViewById(R.id.tv_dream_time_value);
        img_dream_cover = (ImageView) findViewById(R.id.img_dream_cover);
        img_choose_photo = (ImageView) findViewById(R.id.img_choose_photo);
        area_choose_dream_time = findViewById(R.id.area_choose_dream_time);
        area_add_flag = findViewById(R.id.area_add_flag);
        area_choose_game = findViewById(R.id.area_choose_game);
        tv_addflag_tip = (TextView) findViewById(R.id.tv_addflag_tip);
        tv_flag_name = (TextView) findViewById(R.id.tv_flag_name);
        tv_fuwu_xieyi = (TextView) findViewById(R.id.tv_fuwu_xieyi);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        chk_xieyi = (CheckBox) findViewById(R.id.chk_xieyi);
        edit_dream_title = (EditText) findViewById(R.id.edit_dream_title);
        actdetails_content = (LinearLayout) findViewById(R.id.actdetails_content);
        area_input_content = (LinearLayout) findViewById(R.id.area_input_content);
        webview_input_textarea = (WebView) findViewById(R.id.webview_input_textarea);
        myTimeDialog = new DateTimeDialog(mContext, true);
        scroll_content = (ScrollView) findViewById(R.id.scroll_content);
        img_xiangji = (ImageView) findViewById(R.id.img_xiangji);
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
    }

    @Override
    public void initData() {
        tianjiaBmp = ImageManager.getInstance().readResBitMap(R.drawable.xiangji, mContext);
        bmpList = new ArrayList<>();
        bmpList.add(tianjiaBmp);
        contentPicList = new ArrayList<>();
        //修改ua让后台识别
        String ua = webview_input_textarea.getSettings().getUserAgentString();
        webview_input_textarea.getSettings().setUserAgentString(ua + "; 61park/android");
        webview_input_textarea.getSettings().setJavaScriptEnabled(true);
        // 在js中调用本地java方法
        webview_input_textarea.addJavascriptInterface(new JsInterface(), "Android");
        //webview_input_textarea.loadUrl("file:///android_asset/textarea.html");
        webview_input_textarea.loadUrl("http://m.61park.cn/html-sui/requirement-house/write.html");
        calendar = Calendar.getInstance();
        mDreamInputPicAdapter = new DreamInputPicAdapter(mContext, bmpList);
        gv_input_pic.setAdapter(mDreamInputPicAdapter);
    }

    private class JsInterface {
        @JavascriptInterface
        public void call(String resultStr) {
            logout("resultStr====" + resultStr);
            content = resultStr.trim();
            content = content.replace("<p id=\"welcome\">写下您的梦想，说不定还可以实现的哦！</p>", "");
            logout("content333====" + content);
            if (TextUtils.isEmpty(content)) {
                showShortToast("梦想内容未填写");
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    asyncPublishDream();
                }
            }, 200);
        }
    }

    @Override
    public void initListener() {
        img_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                it.putExtra("CROP_SHAPE", SelectPicPopupWindow.REC_CROP_PIC);
                startActivityForResult(it, 0);
            }
        });
        area_choose_dream_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthday = tv_dream_time_value.getText().toString();
                if (!TextUtils.isEmpty(birthday))
                    calendar.setTime(DateTool.getDateByMin(birthday));
                myTimeDialog.setTime(calendar);
                myTimeDialog.setOnChosenListener(new DateTimeDialog.OnChosenListener() {

                    @Override
                    public void onFinish(String year, String month, String day,
                                         String hour, String min) {
                        String backData = year + "-" + month + "-" + day + " " + hour + ":" + min;
                        if (DateTool.getDateByDay(backData)
                                .before(new Date(new Date().getTime() - 24 * 60 * 60 * 1000))) {
                            showShortToast("您选择的日期不能早于今天");
                            return;
                        }
                        tv_dream_time_value.setText(backData);
                    }
                });
                myTimeDialog.show();
            }
        });
        area_add_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ChooseDreamFlagActivity.class);
                startActivityForResult(it, GET_FLAG);
            }
        });
        area_choose_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, DreamChooseActTempActivity.class);
                startActivityForResult(it, GET_GAME);
            }
        });
        tv_fuwu_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ComWebViewActivity.class);
                it.putExtra("url", "http://m.61park.cn/html-sui/requirement-house/agreement.html");
                it.putExtra("PAGE_TITLE", "61区服务协议");
                startActivity(it);
            }
        });
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chk_xieyi.isChecked()) {
                    showShortToast("您未勾选61区服务协议！");
                    return;
                }
                title = edit_dream_title.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    showShortToast("梦想标题未输入");
                    return;
                }
                if (TextUtils.isEmpty(coverPic)) {
                    showShortToast("梦想封面未上传");
                    return;
                }
                expectTimeStr = tv_dream_time_value.getText().toString().trim();
                if (TextUtils.isEmpty(expectTimeStr)) {
                    showShortToast("梦想期望实现时间未填写");
                    return;
                }
                if (TextUtils.isEmpty(chosenFlagId)) {
                    showShortToast("梦想标签未填写");
                    return;
                }
                // 如果展示的webview显示，则是选择现有游戏，判断现有游戏是否为空
                if (actdetails_content.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(content)) {
                        showShortToast("请输入梦想内容");
                        return;
                    }
                    asyncPublishDream();
                } else {// 如果展示的webview不显示，则是自己输入的游戏，在js调用之后判断
                    webview_input_textarea.loadUrl("javascript:output()");
                }
            }
        });
        img_xiangji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                it.putExtra("CROP_SHAPE", SelectPicPopupWindow.REC_CROP_PIC);
                startActivityForResult(it, GET_CONTENT_PIC);
            }
        });
        webview_input_textarea.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    scroll_content.requestDisallowInterceptTouchEvent(false);
                else
                    scroll_content.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        gv_input_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bmpList.size() - 1) {
                    Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                    it.putExtra("CROP_SHAPE", SelectPicPopupWindow.REC_CROP_PIC);
                    startActivityForResult(it, GET_CONTENT_PIC);
                    return;
                }
                String url = contentPicList.get(position);
                webview_input_textarea.loadUrl("javascript:fun(\"" + url + "\")");
            }
        });

        mDreamInputPicAdapter.setOnDelClickListener(new DreamInputPicAdapter.OnDelClickListener() {
            @Override
            public void onDel(int pos) {
                bmpList.remove(pos);
                contentPicList.remove(pos);
                mDreamInputPicAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        if (requestCode == 0) {
            String picPath = data.getStringExtra("path");
            Bitmap bmp = ImageManager.getInstance().readFileBitMap(picPath);
            img_dream_cover.setImageBitmap(bmp);
            img_dream_cover.setVisibility(View.VISIBLE);
            new AliyunUploadUtils(mContext).uploadPic(picPath,
                    new AliyunUploadUtils.OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            handler.sendEmptyMessage(0);
                            coverPic = picUrl;
                        }

                        @Override
                        public void onError() {
                            handler.sendEmptyMessage(1);
                        }
                    });
            return;
        } else if (requestCode == GET_FLAG) {
            chosenFlagId = data.getStringExtra("FLAG_ID");
            tv_addflag_tip.setVisibility(View.GONE);
            tv_flag_name.setText("#" + data.getStringExtra("FLAG_NAME") + "#");
        } else if (requestCode == GET_GAME) {
            String webContent = data.getStringExtra("ActContent");
            actdetails_content.setVisibility(View.VISIBLE);
            area_input_content.setVisibility(View.GONE);
            actdetails_content.removeAllViews();
            webview_actdetails = new WebView(mContext);
            actdetails_content.addView(webview_actdetails);
            String htmlDetailsStr = "";
            try {
                htmlDetailsStr = HtmlParserTool.replaceImgStr(webContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            webview_actdetails.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
            content = webContent;
        } else if (requestCode == GET_CONTENT_PIC) {
            String picPath = data.getStringExtra("path");
            addbmp = ImageManager.getInstance().readFileBitMap(picPath);
            gv_input_pic.setVisibility(View.VISIBLE);
            new AliyunUploadUtils(mContext).uploadPic(picPath,
                    new AliyunUploadUtils.OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            Message msg = handler.obtainMessage();
                            msg.what = 10;
                            msg.obj = picUrl;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onError() {
                            handler.sendEmptyMessage(11);
                        }
                    });
        }
    }

    public void asyncPublishDream() {
        String wholeUrl = AppUrl.host + AppUrl.PUBLISH_REQUIREMENT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classId", chosenFlagId);
        map.put("title", title);
        map.put("expectTimeStr", expectTimeStr);
        map.put("coverPic", coverPic);
        if (content.contains("&quot;")) {
            content = content.replace("&quot;", "'");
        }
        if (content.contains("&amp;")) {
            content = content.replace("&amp;", "");
        }
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", "");
        }
        logout("content123=========="+content);
        map.put("content", content);
        map.put("isAgree", "1");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, publishLsner);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showDialog();
                    break;
                case 1:
                    dismissDialog();
                    showShortToast((String) msg.obj);
                    break;
                case 2:
                    dismissDialog();
                    JSONObject jsonResult = (JSONObject) msg.obj;
                    long requirementId = jsonResult.optLong("id");
                    Intent it = new Intent(mContext, PublishSuccessActivity.class);
                    it.putExtra("requirementId", requirementId);
                    startActivity(it);
                    finish();
                    break;
            }
        }
    };

    BaseRequestListener publishLsner = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = jsonResult;
            mHandler.sendMessage(msg);
        }
    };

}