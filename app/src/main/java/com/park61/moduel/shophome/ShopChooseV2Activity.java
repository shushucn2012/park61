package com.park61.moduel.shophome;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.CityChooseActivity;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.moduel.shophome.adapter.ChooseShopListAdapter;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shushucn2012 on 2017/3/16.
 */
public class ShopChooseV2Activity extends BaseActivity {

    private ListViewForScrollView lv_shop_suggest;
    private View area_city, area_cur_shop, area_suggust_title;
    private TextView tv_cur_shopname, tv_cur_shopdistance, tv_city_now;
    private ImageView img_cur_shop;

    private List<ShopBean> shopBeanList;
    private ChooseShopListAdapter mChooseShopListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_choose_v2);
    }

    @Override
    public void initView() {
        lv_shop_suggest = (ListViewForScrollView) findViewById(R.id.lv_shop_suggest);
        lv_shop_suggest.setFocusable(false);
        area_city = findViewById(R.id.area_city);
        tv_cur_shopname = (TextView) findViewById(R.id.tv_cur_shopname);
        tv_cur_shopdistance = (TextView) findViewById(R.id.tv_cur_shopdistance);
        img_cur_shop = (ImageView) findViewById(R.id.img_cur_shop);
        tv_city_now = (TextView) findViewById(R.id.tv_city_now);
        area_cur_shop = findViewById(R.id.area_cur_shop);
        area_suggust_title = findViewById(R.id.area_suggust_title);
    }

    @Override
    public void initData() {
        shopBeanList = new ArrayList<>();
        mChooseShopListAdapter = new ChooseShopListAdapter(mContext, shopBeanList);
        lv_shop_suggest.setAdapter(mChooseShopListAdapter);
        tv_cur_shopname.setText(GlobalParam.CUR_SHOP_NAME == null ? "" : GlobalParam.CUR_SHOP_NAME);
        if (GlobalParam.CUR_SHOP_IMG != null) {
            ImageManager.getInstance().displayImg(img_cur_shop, GlobalParam.CUR_SHOP_IMG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetOfficeByCity();
        if (TextUtils.isEmpty(GlobalParam.chooseCityStr)) {
            tv_city_now.setText("湖州市");
            if (!CommonMethod.isListEmpty(GlobalParam.cityList)) {
                for (CityBean cityBean : GlobalParam.cityList) {
                    if ((cityBean.getId() + "").equals(GlobalParam.chooseCityCode)) {
                        GlobalParam.chooseCityStr = cityBean.getCityName();
                        tv_city_now.setText(GlobalParam.chooseCityStr);
                    }
                }
            }
        } else {
            tv_city_now.setText(GlobalParam.chooseCityStr);
        }
    }

    @Override
    public void initListener() {
        area_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CityChooseActivity.class));
            }
        });
        lv_shop_suggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalParam.CUR_SHOP_ID = shopBeanList.get(position).getId();
                finish();
            }
        });
        area_cur_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取城市下的店铺
     */
    protected void asyncGetOfficeByCity() {
        String wholeUrl = AppUrl.host + AppUrl.getOfficeByCity;
        Map<String, Object> map = new HashMap<String, Object>();
        if (TextUtils.isEmpty(GlobalParam.chooseCityCode)) {
            map.put("cityId", "138106");
        } else {
            map.put("cityId", GlobalParam.chooseCityCode);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getDistrictShopsByCountryId);
    }

    BaseRequestListener getDistrictShopsByCountryId = new JsonRequestListener() {

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
            if (jay == null) {
                shopBeanList.clear();
                mChooseShopListAdapter.notifyDataSetChanged();
                return;
            }
            shopBeanList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ShopBean p = gson.fromJson(jot.toString(), ShopBean.class);
                if (p.getId() == GlobalParam.CUR_SHOP_ID) {
                    tv_cur_shopname.setText(p.getName());
                    tv_cur_shopdistance.setText(p.getDistanceNum());
                    ImageManager.getInstance().displayImg(img_cur_shop, p.getPicUrl());
                } else {
                    shopBeanList.add(p);
                }
            }
            if (CommonMethod.isListEmpty(shopBeanList)) {
                area_suggust_title.setVisibility(View.GONE);
            } else {
                area_suggust_title.setVisibility(View.VISIBLE);
            }
            mChooseShopListAdapter.notifyDataSetChanged();
        }
    };
}
