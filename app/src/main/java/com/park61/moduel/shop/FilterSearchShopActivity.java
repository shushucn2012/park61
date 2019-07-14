package com.park61.moduel.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.shop.adapter.ActNShopListAdapter;
import com.park61.moduel.shop.bean.ActNShopBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class FilterSearchShopActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 5;
    private String name;// 搜索关键字

    private PullToRefreshListView mPullRefreshListView;
    private List<ActNShopBean> actNShopDataList;
    private ActNShopListAdapter adapter;
    private EditText edit_shopname;
    private TextView tv_cancel, tv_null, tv_loading;
    private View sousuo_area;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_filter_search);
    }

    @Override
    public void initView() {
        edit_shopname = (EditText) findViewById(R.id.edit_shopname);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_null = (TextView) findViewById(R.id.tv_null);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncSearchShop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncSearchShop();
            }
        });
        ListView actualListView = mPullRefreshListView.getRefreshableView();
        actNShopDataList = new ArrayList<ActNShopBean>();
        adapter = new ActNShopListAdapter(mContext, actNShopDataList);
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Long shopId = actNShopDataList.get(position - 1).getId();
                String shopName = actNShopDataList.get(position - 1).getTitle();
                Intent data = new Intent();
                data.putExtra("shopId", shopId);
                data.putExtra("shopName", shopName);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        sousuo_area = findViewById(R.id.sousuo_area);
    }

    @Override
    public void initData() {
        edit_shopname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
                asyncSearchShop();
            }
        });
    }

    @Override
    public void initListener() {
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sousuo_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edit_shopname.setText("");
            }
        });
    }

    /**
     * 查询店铺
     */
    private void asyncSearchShop() {
        String wholeUrl = AppUrl.host + AppUrl.SEARCH_SHOP;
        String requestBodyData = ParamBuild.searchShop(name, null, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, searchShopLsner);
    }

    BaseRequestListener searchShopLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            //showDialog();
            tv_loading.setVisibility(View.VISIBLE);
            tv_null.setVisibility(View.GONE);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
            listStopLoadView();
            PAGE_NUM = 1;
            actNShopDataList.clear();
            adapter.notifyDataSetChanged();
            setPullToRefreshViewEnd();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            listStopLoadView();
            ArrayList<ActNShopBean> currentPageList = new ArrayList<ActNShopBean>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                actNShopDataList.clear();
                adapter.notifyDataSetChanged();
                tv_null.setVisibility(View.VISIBLE);
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                actNShopDataList.clear();
                setPullToRefreshViewBoth();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            // 解析数据
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ActNShopBean c = new ActNShopBean();
                c.setId(actJot.optLong("id"));
                c.setTitle(actJot.optString("name"));
                if (TextUtils.isEmpty(actJot.optString("name"))) {
                    c.setTitle(actJot.optString("actTitle")
                            + actJot.optString("shopName"));
                }
                currentPageList.add(c);
            }
            // 加载数据列表
            actNShopDataList.addAll(currentPageList);
            // 更新显示列表
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
        tv_loading.setVisibility(View.GONE);
    }

    /**
     * 将上下拉控件设为到底
     */
    protected void setPullToRefreshViewEnd() {
        mPullRefreshListView.setMode(Mode.DISABLED);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullRefreshListView.setMode(Mode.PULL_FROM_END);
    }

}
