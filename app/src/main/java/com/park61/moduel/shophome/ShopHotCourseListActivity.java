package com.park61.moduel.shophome;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.CourseDetailsActivity;
import com.park61.moduel.acts.adapter.ShopCourseListAdapter;
import com.park61.moduel.shophome.bean.MyCourseBean;
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
 * Created by shubei on 2017/7/24.
 */

public class ShopHotCourseListActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private List<MyCourseBean> dataList;
    private ShopCourseListAdapter mShopCourseListAdapter;

    private PullToRefreshListView mPullRefreshListView;
    private View area_right_kebiao;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_hotcourse_list);
    }

    @Override
    public void initView() {
        setPagTitle("课程");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetListCourses();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetListCourses();
            }
        });
        area_right_kebiao = findViewById(R.id.area_right_kebiao);
    }

    @Override
    public void initData() {
        dataList = new ArrayList<MyCourseBean>();
        mShopCourseListAdapter = new ShopCourseListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mShopCourseListAdapter);
        asyncGetListCourses();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("courseId", dataList.get(position - 1).getId());
                it.putExtra("priceSale", dataList.get(position - 1).getPriceSale());
                it.putExtra("priceOriginal", dataList.get(position - 1).getPriceOriginal());
                it.putExtra("className", dataList.get(position - 1).getClassName());
                it.putExtra("courseNum", dataList.get(position - 1).getCost());
                mContext.startActivity(it);
            }
        });
        area_right_kebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShopPlanActivity.class));
            }
        });
    }

    private void asyncGetListCourses() {
        String wholeUrl = AppUrl.host + AppUrl.listCourses;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("officeId", GlobalParam.CUR_SHOP_ID);
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getActListLsner);
    }

    BaseRequestListener getActListLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                dataList.clear();
                mShopCourseListAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                dataList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            ArrayList<MyCourseBean> currentPageList = new ArrayList<MyCourseBean>();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                MyCourseBean c = gson.fromJson(actJot.toString(), MyCourseBean.class);
                currentPageList.add(c);
            }
            dataList.addAll(currentPageList);
            mShopCourseListAdapter.notifyDataSetChanged();
        }
    };
}
