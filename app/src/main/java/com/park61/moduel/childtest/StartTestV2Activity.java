package com.park61.moduel.childtest;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.HtmlParserTool;
import com.park61.moduel.childtest.bean.EaItemDetailsBean;
import com.park61.moduel.childtest.bean.QuestionCache;
import com.park61.moduel.childtest.bean.TestQuestion;
import com.park61.moduel.me.AddBabyActivity;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.circleimageview.GlideCircleTransform;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shushucn2012 on 2017/2/22.
 */
public class StartTestV2Activity extends BaseActivity {

    private ImageView img_qiehuan;
    private WebView webview;
    private TextView tv_baby_age, tv_baby_name, tv_right;
    private Button btn_start;
    private ImageView img_baby;

    private String chosenId;
    private List<BabyItem> babyList;
    private int childIndex = 0;//当前所选孩子序号

    private int eaItemId;
    private EaItemDetailsBean eaItemDetailsBean;
    private String resultId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_start_test);
    }

    @Override
    public void initView() {
        webview = (WebView) findViewById(R.id.webview);
        img_baby = (ImageView) findViewById(R.id.img_baby);
        img_qiehuan = (ImageView) findViewById(R.id.img_qiehuan);
        tv_baby_name = (TextView) findViewById(R.id.tv_baby_name);
        tv_baby_age = (TextView) findViewById(R.id.tv_baby_age);
        tv_right = (TextView) findViewById(R.id.tv_right);
        btn_start = (Button) findViewById(R.id.btn_start);
        tv_right = (TextView) findViewById(R.id.tv_right);
       /* img_back = (ImageView) findViewById(R.id.img_back);
        img_baby = (ImageView) findViewById(R.id.img_baby);
        //img_qiehuan = (ImageView) findViewById(R.id.img_qiehuan);
        img_share = (ImageView) findViewById(R.id.img_share);*/
    }

    @Override
    public void initData() {
        eaItemId = getIntent().getIntExtra("eaItemId", 0);
        babyList = new ArrayList<BabyItem>();

        asyncGetItemDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        /*img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        img_qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (babyList.size() == 1) {
                    showShortToast("没有其他宝宝可以切换");
                    return;
                }
                if (childIndex < babyList.size() - 1) {
                    childIndex++;
                } else if (childIndex == babyList.size() - 1) {
                    childIndex = 0;
                }
                QuestionCache.chosenChildId = babyList.get(childIndex).getId();
                //圆形头像
                Glide.with(StartTestV2Activity.this).load(babyList.get(childIndex).getPictureUrl()).placeholder(R.drawable.headimg_default_img).
                        transform(new GlideCircleTransform(StartTestV2Activity.this)).into(img_baby);
                tv_baby_name.setText(babyList.get(childIndex).getName());
                tv_baby_age.setText(babyList.get(childIndex).getAge());
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/html-sui/dapTest/test-index.html";
                String title = "多元测评";
                String description = "多元智慧发展教育";
                showShareDialog(shareUrl, AppUrl.SHARE_APP_ICON, title, description);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncEaGuide();
                //asyncListSubjects();
            }
        });
    }

    private void asyncGetItemDetail() {
        String wholeUrl = AppUrl.host + AppUrl.ea_itemDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", eaItemId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

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
            eaItemDetailsBean = gson.fromJson(jsonResult.toString(), EaItemDetailsBean.class);
            String htmlDetailsStr = "";
            try {
                htmlDetailsStr = HtmlParserTool.replaceImgStr(eaItemDetailsBean.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
        }
    };

    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getBabysLsner);
    }

    BaseRequestListener getBabysLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray jay = jsonResult.optJSONArray("list");
            if (jay == null || jay.length() <= 0) {
                showShortToast("您还没有添加宝宝哦！");
                startActivity(new Intent(mContext, AddBabyActivity.class));
                Intent changeIt = new Intent();
                changeIt.setAction("ACTION_TAB_CHANGE");
                changeIt.putExtra("TAB_NAME", "tab_me");
                mContext.sendBroadcast(changeIt);
                finish();
                return;
            }
            babyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                babyList.add(b);
            }
            if (QuestionCache.chosenChildId != 0) {
                for (int i = 0; i < babyList.size(); i++) {
                    if (babyList.get(i).getId() == QuestionCache.chosenChildId) {
                        childIndex = i;
                        break;
                    }
                }
            }
            QuestionCache.chosenChildId = babyList.get(childIndex).getId();
            //圆形头像
            Glide.with(StartTestV2Activity.this).load(babyList.get(childIndex).getPictureUrl()).placeholder(R.drawable.headimg_default_img).
                    transform(new GlideCircleTransform(StartTestV2Activity.this)).into(img_baby);
            tv_baby_name.setText(babyList.get(childIndex).getName());
            tv_baby_age.setText(babyList.get(childIndex).getAge());
        }
    };

    private void asyncEaGuide() {
        String wholeUrl = AppUrl.host + AppUrl.eaGuide;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", eaItemId);
        map.put("childId", QuestionCache.chosenChildId);
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
        public void onSuccess(int requestId, String url, final JSONObject jsonResult) {
            dismissDialog();
            if (jsonResult == null || "".equals(jsonResult.optString("data"))) {
                asyncListSubjects();
            } else {
                dDialog.showDialog("提示", "发现您有未完成测评，是否继续测评", "取消", "确定",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                asyncListSubjects();
                                dDialog.dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultId = jsonResult.optString("data");
                                asyncListSubjects();
                                dDialog.dismissDialog();
                            }
                        });
            }
        }
    };

    private void asyncListSubjects() {
        String wholeUrl = AppUrl.host + AppUrl.LISTSUBJECTS_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", eaItemId);
        map.put("childId", QuestionCache.chosenChildId);
        if (!TextUtils.isEmpty(resultId)) {
            map.put("resultId", resultId);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listSubjectsLsner);
    }

    BaseRequestListener listSubjectsLsner = new JsonRequestListener() {

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
            QuestionCache.questionList.clear();
            JSONArray finishedJay = jsonResult.optJSONArray("finishedSubjects");
            for (int i = 0; i < finishedJay.length(); i++) {
                JSONObject actJot = finishedJay.optJSONObject(i);
                TestQuestion question = gson.fromJson(actJot.toString(), TestQuestion.class);
                QuestionCache.questionList.add(question);
            }
            JSONArray unFinishedJay = jsonResult.optJSONArray("unFinishedSubjects");
            for (int i = 0; i < unFinishedJay.length(); i++) {
                JSONObject actJot = unFinishedJay.optJSONObject(i);
                TestQuestion question = gson.fromJson(actJot.toString(), TestQuestion.class);
                QuestionCache.questionList.add(question);
            }
            QuestionCache.eaItemId = eaItemId;
            Intent it = new Intent(mContext, TestQuestionActivity.class);
            it.putExtra("finished_num", finishedJay.length());
            it.putExtra("resultId", resultId);
            startActivity(it);
            finish();
        }
    };

   /* *//**
     * 将题目大系列表获取出来
     *//*
    private void asyncListEasys() {
        String wholeUrl = AppUrl.host + AppUrl.LISTEASYS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaSysId", 1);
        map.put("childId", QuestionCache.chosenChildId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listEasysLsner);
    }

    BaseRequestListener listEasysLsner = new JsonRequestListener() {

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
            eaSysList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            JSONArray sysJay = actJay.optJSONObject(0).optJSONArray("items");
            for (int i = 0; i < sysJay.length(); i++) {
                JSONObject actJot = sysJay.optJSONObject(i);
                EaSysBean eaSysBean = new Gson().fromJson(actJot.toString(), EaSysBean.class);
                eaSysList.add(eaSysBean);
            }
            changeEaSysIcon();
        }
    };

    private void changeEaSysIcon() {
        if (eaSysList.get(1).isCanEa()) {
            img_two.setImageResource(R.drawable.xuexixingwei);
        } else {
            img_two.setImageResource(R.drawable.xuexixingwei_mengban);
        }
        if (eaSysList.get(2).isCanEa()) {
            img_three.setImageResource(R.drawable.xuexidongji);
        } else {
            img_three.setImageResource(R.drawable.xuexidongji_mengban);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_one:
                chosenId = eaSysList.get(0).getId();
                break;
            case R.id.img_two:
                if (!eaSysList.get(1).isCanEa()) {
                    return;
                }
                chosenId = eaSysList.get(1).getId();
                break;
            case R.id.img_three:
                if (!eaSysList.get(2).isCanEa()) {
                    return;
                }
                chosenId = eaSysList.get(2).getId();
                break;
        }
        QuestionCache.eaItemId = Long.parseLong(chosenId);
        asyncListSubjects();
    }*/
}
