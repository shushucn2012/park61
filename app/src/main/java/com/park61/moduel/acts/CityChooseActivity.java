package com.park61.moduel.acts;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.LoadingActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.adapter.CityGvAdapter;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityChooseActivity extends BaseActivity {

    private TextView tv_now_city;// 武汉205杭州50郑州187
    private GridView gv_city;
    private CityGvAdapter adapter;
    private List<CityBean> mcityList = new ArrayList<CityBean>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_city_choose);
    }

    @Override
    public void initView() {
        tv_now_city = (TextView) findViewById(R.id.tv_now_city);
        gv_city = (GridView) findViewById(R.id.gv_city);
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(GlobalParam.chooseCityStr)) {
            tv_now_city.setText("湖州市");
        } else {
            tv_now_city.setText(GlobalParam.chooseCityStr);
        }
        if (CommonMethod.isListEmpty(GlobalParam.cityList)) {
            asyncGetSupportCitys();
        } else {
            fillCity();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    asyncGetSupportCitys();
                }
            }, 500);
        }
    }

    @Override
    public void initListener() {
        gv_city.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String chooseCityStr = mcityList.get(position).getCityName();
                String chooseCityCode = mcityList.get(position).getId() + "";

                GlobalParam.chooseCityStr = chooseCityStr;
                GlobalParam.chooseCityCode = chooseCityCode;

                // 保存所选城市，下次启动直接使用该城市
                SharedPreferences spf = getSharedPreferences(
                        LoadingActivity.CITY_FILE_NAME, Context.MODE_PRIVATE);
                Editor editor = spf.edit();
                editor.putString("cityCode", GlobalParam.chooseCityCode);
                editor.putString("cityName", GlobalParam.chooseCityStr);
                editor.commit();

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    protected void fillCity() {
        mcityList.clear();
        for (int i = 0; i < GlobalParam.cityList.size(); i++) {
            CityBean item = GlobalParam.cityList.get(i);
            //if (!item.getCityName().equals(GlobalParam.chooseCityStr))
            mcityList.add(item);
        }

        adapter = new CityGvAdapter(mcityList, mContext);
        gv_city.setAdapter(adapter);
    }

    /**
     * 获取支持的城市列表
     */
    private void asyncGetSupportCitys() {
        String wholeUrl = AppUrl.host + AppUrl.GET_SUPPORT_CITYS;
        String requestBodyData = "";
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
            JSONArray jay = jsonResult.optJSONArray("list");
            if (jay == null || jay.length() <= 0) {
                return;
            }
            GlobalParam.cityList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean b = gson.fromJson(jot.toString(), CityBean.class);
                GlobalParam.cityList.add(b);
            }
            fillCity();
        }
    };

}
