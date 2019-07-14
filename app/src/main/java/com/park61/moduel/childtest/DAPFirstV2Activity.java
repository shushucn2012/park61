package com.park61.moduel.childtest;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.bean.BannerItem;
import com.park61.moduel.childtest.adapter.EaListAdapter;
import com.park61.moduel.childtest.bean.EaListBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.MyBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shushucn2012 on 2017/3/1.
 */
public class DAPFirstV2Activity extends BaseActivity {

    private Button btn_right;
    private ListView lv_big_sys;
    private View banner_area;
    private ViewPager top_viewpager;
    private LinearLayout top_vp_dot;
    private TextView tv_record;

    private EaListBean mEaListBean;
    private List<EaListBean.SysListBean> sysList;
    private EaListAdapter mEaListAdapter;
    private MyBanner mp;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dap_first);
    }

    @Override
    public void initView() {
        btn_right = (Button) findViewById(R.id.btn_right);
        lv_big_sys = (ListView) findViewById(R.id.lv_big_sys);
        banner_area = findViewById(R.id.banner_area);
        tv_record = (TextView) findViewById(R.id.tv_record);
        top_viewpager = (ViewPager) findViewById(R.id.top_viewpager);
        top_vp_dot = (LinearLayout) findViewById(R.id.top_vp_dot);

        if (mp != null) {
            mp.clear();
        }
        mp = new MyBanner(mContext, top_viewpager, top_vp_dot);
    }

    @Override
    public void initData() {
        sysList = new ArrayList<>();
        mEaListAdapter = new EaListAdapter(mContext, sysList);
        lv_big_sys.setAdapter(mEaListAdapter);
        asyncGetEaItemList();
        asyncGetBanner();
    }

    @Override
    public void initListener() {
       /* btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/ea/toEaIntroduce.do?eaKey=31c7f6b070ebfa101660a4bd4df75063";
                String title = "DAP测评系统";
                String description = "多元智慧发展教育";
                showShareDialog(shareUrl, AppUrl.SHARE_APP_ICON, title, description);
            }
        });*/
        tv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TestRecordListActivity.class));
            }
        });
    }

    private void asyncGetBanner() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROMOTION_BANNER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 4);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            List<BannerItem> bannerlList = new ArrayList<>();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject bannerItemJot = actJay.optJSONObject(i);
                BannerItem bItem = new BannerItem();
                bItem.setImg(bannerItemJot.optString("bannerPositionPic"));
                bItem.setUrl(bannerItemJot.optString("bannerPositionWebsite"));
                bItem.setDescription(bannerItemJot.optString("name"));
                bannerlList.add(bItem);
            }
            if (bannerlList.size() < 1) {
                banner_area.setVisibility(View.GONE);
            } else {
                banner_area.setVisibility(View.VISIBLE);
                mp.init(bannerlList);
            }
        }
    };

    private void asyncGetEaItemList() {
        String wholeUrl = AppUrl.host + AppUrl.ea_itemList;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getBabysLsner);
    }

    BaseRequestListener getBabysLsner = new JsonRequestListener() {

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
            mEaListBean = gson.fromJson(jsonResult.toString(), EaListBean.class);
            if (mEaListBean == null || CommonMethod.isListEmpty(mEaListBean.getSysList())) {
                showShortToast("暂无数据！");
                return;
            }
            sysList.addAll(mEaListBean.getSysList());
            mEaListAdapter.notifyDataSetChanged();
        }
    };

}
