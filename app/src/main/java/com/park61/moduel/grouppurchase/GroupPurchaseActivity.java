package com.park61.moduel.grouppurchase;

import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.grouppurchase.adapter.GroupPurchaseGoodsListAdapter;
import com.park61.moduel.grouppurchase.bean.GroupGoodsCombine;
import com.park61.moduel.grouppurchase.bean.GroupProductLimit;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼团列表页面
 */
public class GroupPurchaseActivity extends BaseActivity {
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private List<GroupProductLimit> goodsDataList;
    private List<GroupGoodsCombine> goodsCombList;
    private GroupPurchaseGoodsListAdapter mAdapter;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_grouppurchse);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetGroupGoodsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetGroupGoodsList();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_page_title);
        String titleStr = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(titleStr)) {
            tvTitle.setText("");
        } else {
            tvTitle.setText(titleStr);
        }
        goodsDataList = new ArrayList<GroupProductLimit>();
        goodsCombList = new ArrayList<GroupGoodsCombine>();
        setGoodsToCombList();
        mAdapter = new GroupPurchaseGoodsListAdapter(mContext, goodsCombList);
        actualListView.setAdapter(mAdapter);
        GlobalParam.GroupPurchaseActivityNeedRefresh = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GlobalParam.GroupPurchaseActivityNeedRefresh){
            GlobalParam.GroupPurchaseActivityNeedRefresh = false;
            asyncGetGroupGoodsList();
        }
    }

    /**
     * 把商品列表的数据填充到商品联合bean列表
     */
    public void setGoodsToCombList() {
        goodsCombList.clear();
        for (int i = 0; i < goodsDataList.size(); i = i + 2) {
            GroupGoodsCombine comb = new GroupGoodsCombine();
            if (goodsDataList.get(i) != null)
                comb.setGoodsLeft(goodsDataList.get(i));
            if (i + 1 < goodsDataList.size()
                    && goodsDataList.get(i + 1) != null)
                comb.setGoodsRight(goodsDataList.get(i + 1));
            goodsCombList.add(comb);
        }
    }

    @Override
    public void initListener() {

    }

    /**
     * 请求拼团商品列表数据
     */
    private void asyncGetGroupGoodsList() {
        String wholeUrl = AppUrl.host + AppUrl.FIGHT_GROUP_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<GroupProductLimit> currentPageList = new ArrayList<GroupProductLimit>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                goodsDataList.clear();
                goodsCombList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyView(mContext, actualListView, "暂无数据",
                        R.drawable.quexing, null, 100, 95);
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                goodsDataList.clear();
                goodsCombList.clear();
                setPullToRefreshViewBoth();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                GroupProductLimit pl = gson.fromJson(actJot.toString(), GroupProductLimit.class);
                currentPageList.add(pl);
            }
            goodsDataList.addAll(currentPageList);
            setGoodsToCombList();
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * 将上下拉控件设为到底
     */
    protected void setPullToRefreshViewEnd() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

}
