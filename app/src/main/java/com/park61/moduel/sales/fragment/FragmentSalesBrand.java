package com.park61.moduel.sales.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.moduel.sales.adapter.LvRightItemAdapter;
import com.park61.moduel.sales.adapter.SimpleExpAdapter;
import com.park61.moduel.sales.bean.BrandBean;
import com.park61.moduel.sales.bean.DataDao;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.NetLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentSalesBrand extends BaseFragment {

    private ExpandableListView mExlv;
    private DataDao mDataDao = new DataDao();
    private ListView mLvRight; //右侧的字母栏
    private NetLoadingView view_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_brand_all, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mExlv = (ExpandableListView) parentView.findViewById(R.id.exlv);
        mLvRight = (ListView) parentView.findViewById(R.id.lv_right);
        view_loading = (NetLoadingView) parentView.findViewById(R.id.view_loading);
    }

    @Override
    public void initData() {
        asyncGetBrands();
    }

    @Override
    public void initListener() {
        view_loading.setOnRefreshClickedLsner(new NetLoadingView.OnRefreshClickedLsner() {
            @Override
            public void OnRefreshClicked() {
                asyncGetBrands();
            }
        });
        mExlv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mLvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mExlv.setSelectedGroup(position);
            }
        });
    }

    /**
     * 获取所有品牌
     */
    protected void asyncGetBrands() {
        String wholeUrl = AppUrl.host + AppUrl.FIND_PRODUCT_BRANDLIST;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getTopNodesLsner);
    }

    BaseRequestListener getTopNodesLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            view_loading.showLoading(mExlv);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            view_loading.showRefresh();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //view_loading.hideLoading(mExlv);
            new ParseTask().execute(jsonResult.toString());
        }
    };

    private class ParseTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.out("------starttime-----" + System.currentTimeMillis() + "");
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BrandBean bb = gson.fromJson(jot.toString(), BrandBean.class);
                mDataDao.add(bb);
            }
            Log.out("------endtime-----" + System.currentTimeMillis() + "");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            view_loading.hideLoading(mExlv);
            mExlv.setAdapter(new SimpleExpAdapter(mDataDao.getDataEntities(), parentActivity));
            //全都展开不能回缩
            for (int i = 0; i < mDataDao.getDataEntities().size(); i++) {
                mExlv.expandGroup(i);
            }
            mLvRight.setAdapter(new LvRightItemAdapter(parentActivity, mDataDao.getDataEntities()));
        }
    }

}
