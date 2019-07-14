package com.park61.moduel.firsthead;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.databinding.ActivityGoodsdetailsToyBinding;
import com.park61.moduel.firsthead.adapter.ColorAndSizeAdapter;
import com.park61.moduel.firsthead.bean.ColorSizeBean;
import com.park61.moduel.firsthead.bean.ToyBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/1/4.
 * <p>
 * 亲子活动商品详情 pmInfoId 和 规格选择页面
 */

public class ToyDetailActivity extends BaseActivity {

    public static final int REQUEST_CODE = 0x0021;
    public static final int RESULT_CODE = 0x0022;

    ActivityGoodsdetailsToyBinding binding;
    List<ColorSizeBean> colors;
    List<ColorSizeBean> sizes;
    private ColorAndSizeAdapter colorAdapter,sizeAdapter;
    //用户切换标志：1:color变化返回listSize；2:size变化返回listColor
    private int typeColorSize = 1;
    //需要回传的商品
    private ToyBean selectBean;
    private int activityId = -1;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goodsdetails_toy);
    }

    @Override
    public void initView() {
        binding.setGoodTvColor(Color.parseColor("#FF5A80"));
        binding.setDetailTvColor(Color.parseColor("#000000"));
        binding.topLine1.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        selectBean = getIntent().getParcelableExtra("toyBean");
        activityId = getIntent().getIntExtra("activityId", -1);

        WebSettings ws1 = binding.toyDescription.getSettings();
        ws1.setJavaScriptEnabled(true);
        ws1.setJavaScriptCanOpenWindowsAutomatically(true);
        ws1.setDomStorageEnabled(true);
        ws1.setAppCacheEnabled(true);
        ws1.setLoadWithOverviewMode(true);

        colors = new ArrayList<>();
        sizes = new ArrayList<>();
        colorAdapter = new ColorAndSizeAdapter(colors, this);
        sizeAdapter = new ColorAndSizeAdapter(sizes, this);
        binding.gvColor.setAdapter(colorAdapter);
        binding.gvSize.setAdapter(sizeAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncDetail();
    }

    private void asyncDetail() {
        String url = AppUrl.NEWHOST_HEAD + AppUrl.toyDetail;
        Map<String, Object> map = new HashMap<>();
        map.put("id", selectBean.getPmInfoId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            setDetail(jsonResult);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    /**
     * "currentUnifyPrice": 0.01,
     * "id": 86,
     * "marketPrice": 5555,
     * "productCname": "联想笔记本电脑Y80",
     * "productDescription": "测试内容h52q",
     * "productPicUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105240281_752.png"
     */
    private void setDetail(JSONObject j) {
        ImageManager.getInstance().displayImg(binding.toyImg, j.optString("productPicUrl"), R.drawable.img_default_h);
        binding.toyName.setText(j.optString("productCname"));
        binding.toyPrice.setText(j.optDouble("currentUnifyPrice") + "");
        double p = j.optDouble("marketPrice");
        if (p > 0) {
            binding.toyOrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.toyOrigPrice.setText("￥" + p);
        }
        setWebData(binding.toyDescription, j.optString("productDescription"));
    }

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
        wv.loadDataWithBaseURL("file:///android_asset", htmlDetailsStr, "text/html", "UTF-8", "");
    }

    private void asyncSize() {
        String url = AppUrl.NEWHOST_HEAD + AppUrl.toySizeAndColor;
        Map<String, Object> map = new HashMap<>();
        map.put("numSelect", selectBean.getNumSelect());
        map.put("partyId", activityId);
        map.put("pmInfoIdList", selectBean.getPmInfoIdList());
        map.put("productColor", selectBean.getProductColor());
        map.put("productSize", selectBean.getProductSize());
        map.put("typeColorSize", typeColorSize);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, sizeListener);
    }

    BaseRequestListener sizeListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
            setSizeAndColor(jsonResult);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    /**
     * @param j
     */
    private void setSizeAndColor(JSONObject j) {
        //选择规格后商品id变化
        selectBean.setPmInfoId(j.optInt("id", -1));
        selectBean.setProductPicUrl(j.optString("productPicUrl"));
        selectBean.setCurrentUnifyPrice(j.optDouble("currentUnifyPrice"));

        ImageManager.getInstance().displayImg(binding.imgGoodsCover, selectBean.getProductPicUrl());
        binding.toyCoverName.setText(j.optString("productCname"));
        binding.toyCoverPrice.setText(selectBean.getCurrentUnifyPrice()+"");
        double p = j.optDouble("marketPrice");
        if(p>0){
            selectBean.setMarketPrice(p);
            binding.toyCoverOrigPrice.setText("￥"+p);
            binding.toyCoverOrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        JSONArray listColor = j.optJSONArray("listProductColor");
        if(listColor !=null && listColor.length() >0){
            colors.clear();
            for(int i=0; i<listColor.length(); i++){
                colors.add(gson.fromJson(listColor.optJSONObject(i).toString(), ColorSizeBean.class));
            }
        }
        JSONArray listSize = j.optJSONArray("listProductSize");
        if(listColor !=null && listSize.length() >0){
            sizes.clear();
            for(int i=0; i<listSize.length(); i++){
                sizes.add(gson.fromJson(listSize.optJSONObject(i).toString(), ColorSizeBean.class));
            }
        }

        colorAdapter.notifyDataChanged();
        sizeAdapter.notifyDataChanged();
        asyncDetail();
    }

    @Override
    public void initListener() {
        binding.setChooseModel(v -> {
            binding.areaChooseColorandsize.setVisibility(View.VISIBLE);
            asyncSize();
        });

        binding.setCloseCover(v -> {
            binding.areaChooseColorandsize.setVisibility(View.GONE);
        });

        binding.setGoodClick(v->{
            binding.contentScroll.scrollTo(0,0);
        });

        binding.setDetailClick(v->{
            binding.contentScroll.scrollTo(0, binding.content.getHeight()+1);
        });

        binding.gvColor.setOnTagClickListener((view, position, parent) -> {
            ColorSizeBean b = colors.get(position);
            if(b.isHasShow()){
                selectBean.setProductColor(b.getProductColor());
                //点击颜色时 后台规定传1
                typeColorSize = 1;
                asyncSize();
            }
            return false;
        });

        binding.gvSize.setOnTagClickListener((view, position, parent) ->{
            ColorSizeBean b = sizes.get(position);
            if(b.isHasShow()){
                selectBean.setProductSize(b.getProductSize());
                //点击规格时 后台规定传2
                typeColorSize = 2;
                asyncSize();
            }
            return false;
        });

        binding.btnConfirm.setOnClickListener(v->{
            //回传到推荐列表数据
            finish();
        });

        binding.contentScroll.setScrollBottomListener(() -> titleChanged(false));

        binding.contentScroll.setScrollViewListener((l,t,oldL,oldT) -> {
            if(t > binding.content.getHeight()){
                titleChanged(false);
            }else{
                titleChanged(true);
            }
        });
    }

    private void titleChanged(boolean isLeft){
        if(isLeft){
            binding.setGoodTvColor(Color.parseColor("#FF5A80"));
            binding.setDetailTvColor(Color.parseColor("#000000"));
            binding.topLine1.setVisibility(View.VISIBLE);
            binding.topLine2.setVisibility(View.GONE);
        }else{
            binding.setGoodTvColor(Color.parseColor("#000000"));
            binding.setDetailTvColor(Color.parseColor("#FF5A80"));
            binding.topLine1.setVisibility(View.GONE);
            binding.topLine2.setVisibility(View.VISIBLE);
        }
    }
}
