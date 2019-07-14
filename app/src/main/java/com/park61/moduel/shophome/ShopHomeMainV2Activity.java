package com.park61.moduel.shophome;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActMapActivity;
import com.park61.moduel.acts.adapter.ActShopListAdapter;
import com.park61.moduel.acts.adapter.HotCourseListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.BannerItem;
import com.park61.moduel.dreamhouse.DreamHouseMainActivity;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.msg.MsgActivity;
import com.park61.moduel.shop.ShopQrCodeActivity;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.moduel.shophome.adapter.DateGvAdapter;
import com.park61.moduel.shophome.bean.MyCourseBean;
import com.park61.moduel.shophome.bean.ShopAlbumBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.gridview.GridViewForScrollView;
import com.park61.widget.list.HorizontalListViewV2;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.viewpager.MyBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2017/3/6.
 */
public class ShopHomeMainV2Activity extends BaseActivity {

//    public static boolean isFirstIn = true;

    private ImageView img_store, erweima_img, img_phone, img_photo_one, img_photo_three0, img_photo_three1, img_photo_three2;
    private TextView tv_store_name, tv_addr, tv_photo_one_title, tv_photo_one_num, tv_photo_three_title0, tv_photo_three_title1,
            tv_photo_three_title2, tv_photo_three_num0, tv_photo_three_num1, tv_photo_three_num2, tv_msg_remind, tv_whole_white;
    private View shop_home_photo_one, shop_home_photo_three, area_more_photos, area_more_date, area_more_game, area_more_course,
            area_top_left, area_msg, area_banner_top, area_photo_show, area_hot_course, area_hot_game, area_date_show, area_content_whole;
    private HorizontalListViewV2 horilistview_course;
    private HotCourseListAdapter mHotCourseListAdapter;
    private ListViewForScrollView lv_game;
    private ActShopListAdapter mActShopListAdapter;
    private GridViewForScrollView gv_date;
    private DateGvAdapter mDateGvAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_home_main);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lowSdkLayoutInit();
        }
    }

    @Override
    public void initView() {
        tv_store_name = (TextView) findViewById(R.id.tv_store_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        img_store = (ImageView) findViewById(R.id.img_store);
        erweima_img = (ImageView) findViewById(R.id.erweima_img);
        img_phone = (ImageView) findViewById(R.id.img_phone);
        shop_home_photo_one = findViewById(R.id.shop_home_photo_one);
        shop_home_photo_three = findViewById(R.id.shop_home_photo_three);
        area_more_photos = findViewById(R.id.area_more_photos);
        area_more_date = findViewById(R.id.area_more_date);
        area_more_game = findViewById(R.id.area_more_game);
        area_more_course = findViewById(R.id.area_more_course);
        img_photo_one = (ImageView) findViewById(R.id.img_photo_one);
        tv_photo_one_title = (TextView) findViewById(R.id.tv_photo_one_title);
        tv_photo_one_num = (TextView) findViewById(R.id.tv_photo_one_num);
        img_photo_three0 = (ImageView) findViewById(R.id.img_photo_three0);
        img_photo_three1 = (ImageView) findViewById(R.id.img_photo_three1);
        img_photo_three2 = (ImageView) findViewById(R.id.img_photo_three2);
        tv_photo_three_title0 = (TextView) findViewById(R.id.tv_photo_three_title0);
        tv_photo_three_title1 = (TextView) findViewById(R.id.tv_photo_three_title1);
        tv_photo_three_title2 = (TextView) findViewById(R.id.tv_photo_three_title2);
        tv_photo_three_num0 = (TextView) findViewById(R.id.tv_photo_three_num0);
        tv_photo_three_num1 = (TextView) findViewById(R.id.tv_photo_three_num1);
        tv_photo_three_num2 = (TextView) findViewById(R.id.tv_photo_three_num2);
        horilistview_course = (HorizontalListViewV2) findViewById(R.id.horilistview_course);
        lv_game = (ListViewForScrollView) findViewById(R.id.lv_game);
        lv_game.setFocusable(false);
        gv_date = (GridViewForScrollView) findViewById(R.id.gv_date);
        gv_date.setFocusable(false);
        area_top_left = findViewById(R.id.area_top_left);
        tv_msg_remind = (TextView) findViewById(R.id.tv_msg_remind);
        area_msg = findViewById(R.id.area_msg);
        area_banner_top = findViewById(R.id.area_banner_top);
        area_photo_show = findViewById(R.id.area_photo_show);
        area_hot_course = findViewById(R.id.area_hot_course);
        area_hot_game = findViewById(R.id.area_hot_game);
        area_date_show = findViewById(R.id.area_date_show);
        area_content_whole = findViewById(R.id.area_content_whole);
        tv_whole_white = (TextView) findViewById(R.id.tv_whole_white);
    }

    @Override
    public void initData() {
//        judgeNeedChangeCity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (!isFirstIn) {//第一次进的时候不调用，执行城市判断
            if (GlobalParam.CUR_SHOP_ID == 0) {
                asyncGetClosestShop();
            } else {
                asyncGetOfficePage();
            }
        }
        isFirstIn = false;*/

        asyncGetOfficePage();
        if (GlobalParam.MSG_NUM <= 0) {
            tv_msg_remind.setVisibility(View.GONE);
        } else {
            tv_msg_remind.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListener() {
        area_more_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShopSampleActivity.class));
            }
        });
        area_more_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DreamHouseMainActivity.class));
            }
        });
        area_more_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShopHotGameListActivity.class));
            }
        });
        area_more_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShopHotCourseListActivity.class));
            }
        });
        area_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ShopChooseV2Activity.class);
                startActivity(it);
            }
        });
        area_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, MsgActivity.class));
            }
        });
    }


    /**
     * 获取可用店铺列表
     * String wholeUrl = AppUrl.host + AppUrl.TOLOVEOFFICE;
     *//*
    private void asyncGetClosestShop() {
        String wholeUrl = AppUrl.host + AppUrl.TOLOVEOFFICE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getClosestShopLsner);
    }

    BaseRequestListener getClosestShopLsner = new JsonRequestListener() {

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
            GlobalParam.CUR_SHOP_ID = FU.paseLong(jsonResult.optString("data"));
            asyncGetOfficePage();
        }
    };*/

    /**
     * 获取店铺首页相册
     */
    private void asyncGetOfficePage() {
        String wholeUrl = AppUrl.host + AppUrl.GETOFFICEPAGE_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        if (GlobalParam.CUR_SHOP_ID != 0) {
            map.put("officeId", GlobalParam.CUR_SHOP_ID);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getOfficePageLsner);
    }

    BaseRequestListener getOfficePageLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
            tv_whole_white.setVisibility(View.GONE);
            area_content_whole.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            tv_whole_white.setVisibility(View.VISIBLE);
            area_content_whole.setVisibility(View.GONE);
            showShortToast(errorMsg);
           /* if("999".equals(errorCode)){
                dDialog.showDialog("提示", errorMsg, "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mContext, ShopChooseV2Activity.class);
                        startActivity(it);
                        dDialog.dismissDialog();
                    }
                });
            }*/
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Log.outMore("jsonResult=======" + jsonResult.toString());
            JSONArray dataJay = jsonResult.optJSONArray("modules");
            if (dataJay == null) {//为空检验
                tv_whole_white.setVisibility(View.VISIBLE);
                area_content_whole.setVisibility(View.GONE);
                return;
            }
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                if ("storeinfo".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONObject("templeteData") != null) {
                        ShopBean mShopBean = gson.fromJson(tempItemJot.optJSONObject("templeteData").toString(), ShopBean.class);
                        fillShopData(mShopBean);
                        GlobalParam.CUR_SHOP_ID = mShopBean.getId();
                        GlobalParam.CUR_COUNTRY_ID = mShopBean.getCountyId();
                        GlobalParam.chooseCityCode = mShopBean.getAreaId() + "";
                        GlobalParam.CUR_SHOP_PHONE = mShopBean.getPhone();
                        GlobalParam.CUR_SHOP_NAME = mShopBean.getName();
                        GlobalParam.CUR_SHOP_IMG = mShopBean.getPicUrl();
                        GlobalParam.chooseCityStr = mShopBean.getCityName();
                    }
                }
                if ("photo_list".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONArray("templeteData") != null && tempItemJot.optJSONArray("templeteData").length() > 0) {
                        area_photo_show.setVisibility(View.VISIBLE);
                        JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                        List<ShopAlbumBean> mShopAlbumList = new ArrayList<>();
                        if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                            for (int j = 0; j < dateJay.length(); j++) {
                                JSONObject dateJot = dateJay.optJSONObject(j);
                                ShopAlbumBean item = gson.fromJson(dateJot.toString(), ShopAlbumBean.class);
                                mShopAlbumList.add(item);
                            }
                        }
                        fillAlbumData(mShopAlbumList);
                    } else {
                        area_photo_show.setVisibility(View.GONE);
                    }
                }
                if ("store_banner".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        area_banner_top.setVisibility(View.VISIBLE);
                        List<BannerItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            JSONObject bannerItemJot = bannerJay.optJSONObject(j);
                            BannerItem bItem = new BannerItem();
                            bItem.setImg(bannerItemJot.optString("bannerPositionPic"));
                            bItem.setUrl(bannerItemJot.optString("bannerPositionWebsite"));
                            bItem.setDescription(bannerItemJot.optString("name"));
                            bannerlList.add(bItem);
                        }
                        ViewPager top_viewpager = (ViewPager) findViewById(R.id.top_viewpager);
                        LinearLayout top_vp_dot = (LinearLayout) findViewById(R.id.top_vp_dot);
                        ;
                        MyBanner mp = new MyBanner(mContext, top_viewpager, top_vp_dot);
                        mp.init(bannerlList);
                    } else {
                        area_banner_top.setVisibility(View.GONE);
                    }
                }
                if ("hot_course".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONObject("templeteData").optJSONArray("list");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        area_hot_course.setVisibility(View.VISIBLE);
                        List<MyCourseBean> dList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            JSONObject dateJot = dateJay.optJSONObject(j);
                            MyCourseBean item = gson.fromJson(dateJot.toString(), MyCourseBean.class);
                            dList.add(item);
                        }
                        mHotCourseListAdapter = new HotCourseListAdapter(mContext, dList);
                        horilistview_course.setAdapter(mHotCourseListAdapter);
                    } else {
                        area_hot_course.setVisibility(View.GONE);
                    }
                }
                if ("hot_game".equals(tempItemJot.optString("templeteCode"))) {
                    ArrayList<ActItem> currentPageList = new ArrayList<>();
                    JSONObject templeteDataJob = tempItemJot.optJSONObject("templeteData");
                    if (templeteDataJob != null && templeteDataJob.optJSONArray("list") != null && templeteDataJob.optJSONArray("list").length() > 0) {
                        area_hot_game.setVisibility(View.VISIBLE);
                        JSONArray actJay = templeteDataJob.optJSONArray("list");
                        for (int j = 0; j < actJay.length(); j++) {
                            JSONObject actJot = actJay.optJSONObject(j);
                            ActItem c = gson.fromJson(actJot.toString(), ActItem.class);
                            currentPageList.add(c);
                        }
                        mActShopListAdapter = new ActShopListAdapter(mContext, currentPageList);
                        lv_game.setAdapter(mActShopListAdapter);
                    } else {
                        area_hot_game.setVisibility(View.GONE);
                    }
                }
                if ("date_show".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        area_date_show.setVisibility(View.VISIBLE);
                        List<DreamItemInfo> dList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            JSONObject dateJot = dateJay.optJSONObject(j);
                            DreamItemInfo dreamItemInfo = gson.fromJson(dateJot.toString(), DreamItemInfo.class);
                            dList.add(dreamItemInfo);
                        }
                        mDateGvAdapter = new DateGvAdapter(mContext, dList);
                        gv_date.setAdapter(mDateGvAdapter);
                    } else {
                        area_date_show.setVisibility(View.GONE);
                    }
                }
                if ("alert_type".equals(tempItemJot.optString("templeteCode"))) {
                    String msg = tempItemJot.optString("templeteData");
                    final String cityCode = tempItemJot.optString("templeteHead").split(",")[0];
                    final String cityName = tempItemJot.optString("templeteHead").split(",")[1];
                    dDialog.showDialog("提示", msg, "取消", "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(mContext, ShopChooseV2Activity.class);
                            //只有确定了以后才改变cityId
                            GlobalParam.chooseCityCode = cityCode;
                            GlobalParam.chooseCityStr = cityName;
                            startActivity(it);
                            dDialog.dismissDialog();
                        }
                    });
                }
                if ("tip_type".equals(tempItemJot.optString("templeteCode"))) {
                    String msg = tempItemJot.optString("templeteData");
                    showShortToast(msg);
                }
            }
        }
    };

    public void fillShopData(final ShopBean shopBean) {
        ImageManager.getInstance().displayImg(img_store, shopBean.getPicUrl(), R.drawable.img_default_v);
        tv_store_name.setText(shopBean.getName());
        tv_addr.setText(shopBean.getAddress());
        erweima_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/page/store-index/page.html?officeId=" + shopBean.getId();
                Intent it = new Intent(mContext, ShopQrCodeActivity.class);
                it.putExtra("shopName", shopBean.getName());
                it.putExtra("shopQrCode", shareUrl);
                it.putExtra("shopId", shopBean.getId() + "");
                it.putExtra("shopContactor", shopBean.getPhone());
                it.putExtra("shopAddr", shopBean.getAddress());
                it.putExtra("shopPicUrl", shopBean.getPicUrl());
                mContext.startActivity(it);
            }
        });
        tv_addr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActMapActivity.class);
                it.putExtra("act_latitude", shopBean.getLatitude());
                it.putExtra("act_longitude", shopBean.getLongitude());
                mContext.startActivity(it);
            }
        });
        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shopBean.getPhone())) {
                    showShortToast("无效的电话号码！");
                    return;
                }
                dDialog.showDialog("提示", "确认拨打" + shopBean.getPhone() + "?", "取消", "确认", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopBean.getPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dDialog.dismissDialog();
                    }
                });
            }
        });
    }

    public void fillAlbumData(final List<ShopAlbumBean> mShopAlbumList) {
        if (mShopAlbumList.size() == 1 || mShopAlbumList.size() == 2) {
            final ShopAlbumBean shopAlbumBeanOne = mShopAlbumList.get(0);
            shop_home_photo_one.setVisibility(View.VISIBLE);
            shop_home_photo_three.setVisibility(View.GONE);
            ImageManager.getInstance().displayImg(img_photo_one, shopAlbumBeanOne.getOfficeContentItemsList().get(0).getResourceUrl());
            tv_photo_one_title.setText(shopAlbumBeanOne.getTitle());
            tv_photo_one_num.setText(shopAlbumBeanOne.getViewNum());

            img_photo_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < shopAlbumBeanOne.getOfficeContentItemsList().size(); j++) {
                        urlList.add(shopAlbumBeanOne.getOfficeContentItemsList().get(j).getResourceUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", shopAlbumBeanOne.getId());
                    bIt.putExtra("itemsId", shopAlbumBeanOne.getOfficeContentItemsList().get(0).getId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) shopAlbumBeanOne.getOfficeContentItemsList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", shopAlbumBeanOne.getOfficeContentItemsList().get(0).getResourceUrl());
                    if (urlList.size() >= 1)// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    mContext.startActivity(bIt);
                }
            });
        } else if (mShopAlbumList.size() >= 3) {
            shop_home_photo_one.setVisibility(View.GONE);
            shop_home_photo_three.setVisibility(View.VISIBLE);

            final ShopAlbumBean shopAlbumBeanThree0 = mShopAlbumList.get(0);
            ImageManager.getInstance().displayImg(img_photo_three0, shopAlbumBeanThree0.getOfficeContentItemsList().get(0).getResourceUrl());
            tv_photo_three_title0.setText(shopAlbumBeanThree0.getTitle());
            tv_photo_three_num0.setText(shopAlbumBeanThree0.getViewNum());
            img_photo_three0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < shopAlbumBeanThree0.getOfficeContentItemsList().size(); j++) {
                        urlList.add(shopAlbumBeanThree0.getOfficeContentItemsList().get(j).getResourceUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", shopAlbumBeanThree0.getId());
                    bIt.putExtra("itemsId", shopAlbumBeanThree0.getOfficeContentItemsList().get(0).getId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) shopAlbumBeanThree0.getOfficeContentItemsList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", shopAlbumBeanThree0.getOfficeContentItemsList().get(0).getResourceUrl());
                    if (urlList.size() >= 1)// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    mContext.startActivity(bIt);
                }
            });

            final ShopAlbumBean shopAlbumBeanThree1 = mShopAlbumList.get(1);
            ImageManager.getInstance().displayImg(img_photo_three1, shopAlbumBeanThree1.getOfficeContentItemsList().get(0).getResourceUrl());
            tv_photo_three_title1.setText(shopAlbumBeanThree1.getTitle());
            tv_photo_three_num1.setText(shopAlbumBeanThree1.getViewNum());
            img_photo_three1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < shopAlbumBeanThree1.getOfficeContentItemsList().size(); j++) {
                        urlList.add(shopAlbumBeanThree1.getOfficeContentItemsList().get(j).getResourceUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", shopAlbumBeanThree1.getId());
                    bIt.putExtra("itemsId", shopAlbumBeanThree1.getOfficeContentItemsList().get(0).getId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) shopAlbumBeanThree1.getOfficeContentItemsList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", shopAlbumBeanThree1.getOfficeContentItemsList().get(0).getResourceUrl());
                    if (urlList.size() >= 1)// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    mContext.startActivity(bIt);
                }
            });

            final ShopAlbumBean shopAlbumBeanThree2 = mShopAlbumList.get(2);
            ImageManager.getInstance().displayImg(img_photo_three2, shopAlbumBeanThree2.getOfficeContentItemsList().get(0).getResourceUrl());
            tv_photo_three_title2.setText(shopAlbumBeanThree2.getTitle());
            tv_photo_three_num2.setText(shopAlbumBeanThree2.getViewNum());
            img_photo_three2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < shopAlbumBeanThree2.getOfficeContentItemsList().size(); j++) {
                        urlList.add(shopAlbumBeanThree2.getOfficeContentItemsList().get(j).getResourceUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", shopAlbumBeanThree2.getId());
                    bIt.putExtra("itemsId", shopAlbumBeanThree2.getOfficeContentItemsList().get(0).getId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) shopAlbumBeanThree2.getOfficeContentItemsList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", shopAlbumBeanThree2.getOfficeContentItemsList().get(0).getResourceUrl());
                    if (urlList.size() >= 1)// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    mContext.startActivity(bIt);
                }
            });
        }
    }

   /* *//**
     * 判断是否需要切换城市
     *//*
    private void judgeNeedChangeCity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String lCity = GlobalParam.locationCityStr;
                String cCity = GlobalParam.chooseCityStr;
                if (TextUtils.isEmpty(lCity) || TextUtils.isEmpty(cCity)) {
                    asyncGetClosestShop();
                    return;
                }
                if (CommonMethod.isCitySupportByName(lCity) && !CommonMethod.compareTwoCityName(lCity, cCity)) {
                    for (CityBean c : GlobalParam.cityList) {
                        if (CommonMethod.compareTwoCityName(c.getCityName(), GlobalParam.locationCityStr)) {
                            GlobalParam.chooseCityStr = c.getCityName();
                            GlobalParam.chooseCityCode = c.getId() + "";
                            break;
                        }
                    }
                    // 保存所选城市，下次启动直接使用该城市
                    SharedPreferences spf = getSharedPreferences(LoadingActivity.CITY_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("cityCode", GlobalParam.chooseCityCode);
                    editor.putString("cityName", GlobalParam.chooseCityStr);
                    editor.commit();
                }
                asyncGetClosestShop();
            }
        }, 100);
    }*/
}
