package com.park61.moduel.me.actlist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.MainTabActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.MainHomeActivity;
import com.park61.moduel.growing.GrowingActivity;
import com.park61.moduel.me.MeActivity;
import com.park61.moduel.me.ParentalTicketDetailsActivity;
import com.park61.moduel.me.adapter.ParentalTicketListAdapter;
import com.park61.moduel.me.bean.ParentalTicketBean;
import com.park61.moduel.sales.SalesMainV2Activity;
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
 * 亲子活动报名列表页面
 * Created by shubei on 2017/11/2.
 */
public class ParentalTicketListActivity extends BaseActivity {

    private int PAGE_NUM = 0; //当前页码
    private final int PAGE_SIZE = 10;//每页数量
    private List<ParentalTicketBean> dList;//列表数据

    private PullToRefreshListView mPullRefreshListView;//拉下上拉列表控件
    private ParentalTicketListAdapter adapter;//列表数据适配器

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_parental_ticketlist);
    }

    @Override
    public void initView() {
        setPagTitle("我的活动");

        //初始化拉下上拉列表控件
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                PAGE_NUM = 0;
                asyncMyActivityApplyList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                PAGE_NUM++;
                asyncMyActivityApplyList();
            }
        });
    }

    @Override
    public void initData() {
        //初始化列表数据
        dList = new ArrayList<>();
        adapter = new ParentalTicketListAdapter(mContext, dList);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncMyActivityApplyList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //列表item点击到详情
                Intent it = new Intent(mContext, ParentalTicketDetailsActivity.class);
                it.putExtra("id", dList.get(position - 1).getId());
                startActivity(it);
            }
        });
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMe();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMe();
    }

    /**
     * 请求列表数据
     */
    private void asyncMyActivityApplyList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.findMyPartyApplyList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<ParentalTicketBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                dList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                findViewById(R.id.area_root).setBackgroundColor(mContext.getResources().getColor(R.color.bg_color));
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                dList.clear();
                findViewById(R.id.area_root).setBackgroundColor(mContext.getResources().getColor(R.color.gf9f0f5));
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), ParentalTicketBean.class));
            }
            dList.addAll(currentPageList);
            //数据加载完后刷新列表
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 返回“我的”页面
     */
    public void backToMe() {
        for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
            if (!(mActivity instanceof MainTabActivity)
                    && !(mActivity instanceof MainHomeActivity)
                    && !(mActivity instanceof SalesMainV2Activity)
                    && !(mActivity instanceof GrowingActivity)
                    && !(mActivity instanceof MeActivity)) {
                if (mActivity != null) {// 关闭所有页面，置回首页
                    mActivity.finish();
                    Intent changeIt = new Intent();
                    changeIt.setAction("ACTION_TAB_CHANGE");
                    changeIt.putExtra("TAB_NAME", "tab_me");
                    sendBroadcast(changeIt);
                }
            }
        }
    }
}
