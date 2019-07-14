package com.park61.moduel.firsthead;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.databinding.ActivityParentalDetailBinding;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.SharePopWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by chenlie on 2017/12/26.
 * <p>
 * 亲子活动详情页
 */

public class ParentalDetailActivity extends BaseActivity {

    private String baseUrl = "file:///android_asset";
    //报名中
    public static final int SIGN_ING = 0;
    //报名结束
    public static final int SIGN_CLOSE = 1;
    //活动结束
    public static final int ACTIVITY_CLOSE = 2;
    ActivityParentalDetailBinding binding;
    private String title = "";
    private int activityId;
    private int classId;
    private long signOrderId = -1;
    private int linkedBusinessId;
    private String imgUrl,intro;
    private JSONArray toyJsonArr = new JSONArray();

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parental_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //透明状态栏
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            //低版本去掉透明状态栏背景
            binding.titleStatusBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {

        WebSettings ws1 = binding.wvNotice.getSettings();
        WebSettings ws2 = binding.wvDetail.getSettings();
        ws1.setJavaScriptEnabled(true);
        ws2.setJavaScriptEnabled(true);
        ws1.setJavaScriptCanOpenWindowsAutomatically(true);
        ws2.setJavaScriptCanOpenWindowsAutomatically(true);
        ws1.setDomStorageEnabled(true);
        ws2.setDomStorageEnabled(true);
        ws1.setAppCacheEnabled(true);
        ws2.setAppCacheEnabled(true);
        // setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws1.setLoadWithOverviewMode(true);
        ws2.setLoadWithOverviewMode(true);

        binding.signBt.setEnabled(false);
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("id", -1);
        classId = getIntent().getIntExtra("classId", -1);
        //获取活动详情
        asyncActivityDetail();

        //获取活动参与人员
        asyncJoinPeoples();

        //获取活动装备列表
        asyncToyList();
    }

    /**
     * 活动详情
     */
    private void asyncActivityDetail() {

        String url = AppUrl.NEWHOST_HEAD + AppUrl.activityDetail;
        Map<String, Object> map = new HashMap<>();
        //活动id
        map.put("id", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) throws JSONException {

            imgUrl = j.optString("coverUrl");
            intro = j.optString("intro");
            ImageManager.getInstance().displayImg(binding.topImg, imgUrl, R.drawable.img_default_h);
            title = j.optString("name");
            binding.activityTitle.setText(title);
            binding.topBarTitle.setText(title);
            binding.activityIntro.setText(intro);
            binding.activityTime.setText(j.optString("startDateStr"));
            binding.activitySite.setText(j.optString("schoolName"));
            //活动状态
            int state = j.optInt("showStatus");
            if(state == SIGN_ING){
                //报名中
                binding.activityState.setImageResource(R.drawable.icon_signing);
                //报名按钮状态
                binding.signBt.setEnabled(true);
                binding.signBt.setText(getString(R.string.activity_sign));
                binding.signBt.setBackgroundColor(Color.parseColor("#fe698b"));
            }else if(state == SIGN_CLOSE){
                //报名结束
                binding.activityState.setImageResource(R.drawable.icon_sign_close);
            }else if(state == ACTIVITY_CLOSE){
                //活动结束
                binding.activityState.setImageResource(R.drawable.icon_activity_close);
            }

            setWebData(binding.wvNotice, j.optString("notice"));
            setWebData(binding.wvDetail, j.optString("content"));
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {

        }
    };

    /**
     * 设置后台编辑的富文本
     */
    private void setWebData(WebView wv, String webContent) {
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(webContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wv.loadDataWithBaseURL(baseUrl, htmlDetailsStr, "text/html", "UTF-8", "");
    }

    /**
     * 参加人员列表
     */
    private void asyncJoinPeoples() {

        String url = AppUrl.NEWHOST_HEAD + AppUrl.signPeopleHeads;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, headsListener);
    }

    BaseRequestListener headsListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            int count = jsonResult.optInt("count");
            JSONArray heads = jsonResult.optJSONArray("picList");
            if(count>6){
                binding.peoplesMore.setVisibility(View.VISIBLE);
                binding.peoplesMore.setText(""+count);
            }
            if(count == 0){
                binding.peopleLlyt.setVisibility(View.GONE);
            }else{
                setJoinPeopleHeads(heads);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            binding.peopleLlyt.setVisibility(View.GONE);
        }
    };

    private void setJoinPeopleHeads(JSONArray arr){
        if(arr != null && arr.length() >0){
            int margin = DevAttr.dip2px(this, 9f);
            int radius = DevAttr.dip2px(this, 42);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(radius, radius);
            params.setMargins(0, 0, margin, 0);
            for(int i=0; i<arr.length(); i++){
                ImageView img = new ImageView(this);
                img.setLayoutParams(params);
                ImageManager.getInstance().displayCircleImg(img, arr.optString(i));
                binding.joinPeoples.addView(img);
            }
        }
    }

    /**
     * 获取关联装备列表
     */
    private void asyncToyList() {
        String url = AppUrl.NEWHOST_HEAD + AppUrl.childActivityToys;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, toysListener);
    }

    BaseRequestListener toysListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            toyJsonArr = jsonResult.optJSONArray("list");
            if(toyJsonArr != null && toyJsonArr.length() >0){
                setToysData(toyJsonArr);
            }else{
                //隐藏推荐装备布局
                binding.toyLlyt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {

        }
    };

    /**
     * "num": 1, 最少购买数量，为0默认显示1
     * "originalPrice": 6666,
     * "pmInfoId": 85,   商品ID
     * "price": 0.02,
     * "productName": "联想笔记本电脑Y80",
     * "productPic": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105237232_118.png"
     */
    private void setToysData(JSONArray arr) {
        if (arr.length() > 0) {
            JSONObject j1 = arr.optJSONObject(0);
            binding.arms1.setVisibility(View.VISIBLE);
            //图片展示
            ImageManager.getInstance().displayImg(binding.arms1Img, j1.optString("productPicUrl"));
            binding.arms1Name.setText(j1.optString("productCname"));
            binding.arms1CurrPrice.setText("￥" + j1.optDouble("currentUnifyPrice"));
            if (j1.optDouble("marketPrice") != 0) {
                binding.arms1OrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                binding.arms1OrigPrice.setText("￥" + j1.optDouble("marketPrice"));
            }
        }
        if (arr.length() > 1) {
            JSONObject j2 = arr.optJSONObject(1);
            binding.arms2.setVisibility(View.VISIBLE);
            //图片展示
            ImageManager.getInstance().displayImg(binding.arms2Img, j2.optString("productPicUrl"));
            binding.arms2Name.setText(j2.optString("productCname"));
            binding.arms2CurrPrice.setText("￥" + j2.optDouble("currentUnifyPrice"));
            if (j2.optDouble("marketPrice") != 0) {
                binding.arms2OrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                binding.arms2OrigPrice.setText("￥" + j2.optDouble("marketPrice"));
            }
        }

        if (arr.length() > 0) {
            binding.setArmListClick(v-> goArmsList());
        }
    }

    private void goArmsList(){
        Intent it = new Intent(mContext, ArmsListActivity.class);
        it.putExtra("activityId", activityId);
        startActivity(it);
    }

    @Override
    public void initListener() {
        binding.setBackClick(v -> finish());
//        binding.setShareClick(v -> toShare());
        int h = DevAttr.dip2px(mContext, 130);
        binding.detailSv.setScrollViewListener((x, y, oldx, oldy) -> {
                    int alpha = y * 255 / h;
                    if (alpha > 255) {
                        alpha = 255;
                        binding.backImg.setImageResource(R.drawable.icon_red_backimg);
//                        binding.shareImg.setImageResource(R.drawable.red_contentshare);
                        binding.topBarTitle.setVisibility(View.VISIBLE);
                    } else {
                        binding.backImg.setImageResource(R.drawable.icon_content_backimg);
//                        binding.shareImg.setImageResource(R.drawable.contents_share);
                        binding.topBarTitle.setVisibility(View.GONE);
                    }
                    binding.topBar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    binding.titleStatusBar.setBackgroundColor(Color.argb(alpha, 254, 105, 139));
                }
        );

        binding.joinPeoplesArea.setOnClickListener(v -> goPeopleList());

        binding.signBt.setOnClickListener(v->{
            //去报名 -> 改为先去选择宝宝，再报名
            asyncBabyList();
        });
    }

    private void asyncBabyList(){
        String url = AppUrl.NEWHOST_HEAD + AppUrl.canJoinBabyList;
        Map<String, Object> map = new HashMap<>();
        map.put("partyId", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, babyListener);
    }

    BaseRequestListener babyListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            JSONArray bList = jsonResult.optJSONArray("list");
            if(bList !=null){
                goSelectBaby(bList);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void goSelectBaby(JSONArray bList){

        if(bList.length() > 1){
            //去选择宝宝页面 ParentalChooseBabyActivity
            Intent it = new Intent(this, ParentalChooseBabyActivity.class);
            it.putExtra("list", bList.toString());
            it.putExtra("activityId", activityId);
            startActivity(it);
        }else{
            //只有一个宝宝，直接去报名
            JSONObject baby = bList.optJSONObject(0);
            if(baby !=null)
                asyncSignActivity(baby.optInt("id"));
        }
    }

    private void asyncSignActivity(int childId){
        String url = AppUrl.NEWHOST_HEAD + AppUrl.signActivity;
        Map<String, Object> map = new HashMap<>();
        map.put("childId", childId);
        map.put("partyId", activityId);
        map.put("uuid", UUID.randomUUID());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, signListener);
    }

    BaseRequestListener signListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            signOrderId = jsonResult.optLong("orderId");
            linkedBusinessId = jsonResult.optInt("id");
            //报名成功，去订单详情页面，复用ArmListActivity
            if(signOrderId > 0){
                goSignSuccess();
            }else{
                showShortToast("报名失败");
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void goSignSuccess(){
        Intent it = new Intent(this, ArmsListActivity.class);
        it.putExtra("signOrderId", signOrderId);
        it.putExtra("linkedBusinessId", linkedBusinessId);
        it.putExtra("isSignSuccess", true);
        it.putExtra("activityId", activityId);
        startActivity(it);
    }

    /**
     * 打开报名人员列表
     */
    private void goPeopleList() {
        Intent it = new Intent(this, SignListActivity.class);
        it.putExtra("activityId", activityId);
        startActivity(it);
    }

    /**
     * 右上角分享按钮
     */
    public void toShare() {
        Intent its = new Intent(this, SharePopWin.class);
        String shareUrl = "http://m.61park.cn/#/fm/fmindex/"+activityId+"/"+classId;
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", imgUrl);
        its.putExtra("title", title);
        its.putExtra("description", intro);
        startActivity(its);
    }
}
