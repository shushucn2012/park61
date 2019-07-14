package com.park61.moduel.shophome;

import android.content.Intent;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.shophome.adapter.SampleRecordListAdapter;
import com.park61.moduel.shophome.bean.OfficeContentAlbums;
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
 * Created by HP on 2017/3/6.
 */
public class ShopSampleActivity extends BaseActivity implements SampleRecordListAdapter.RecordOnCallBack {
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private SampleRecordListAdapter mAdapter;
    private List<OfficeContentAlbums> mList;
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 6;
    private Long officeId;//店铺id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_sample);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetList();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        mList = new ArrayList<OfficeContentAlbums>();
        mAdapter = new SampleRecordListAdapter(mContext, mList);
        actualListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
//        officeId = GlobalParam.CUR_SHOP_ID;
//        PAGE_NUM = 1;
//        asyncGetList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        officeId = GlobalParam.CUR_SHOP_ID;
        PAGE_NUM = 1;
        asyncGetList();
    }

    @Override
    public void initListener() {

    }

//    @Override
//    public void onPraiseClicked(int position) {
//        asyncPraise();
//    }

    @Override
    public void onShareClicked(int position) {
//        String shareUrl = String.format(AppUrl.GROW_SHARE_URL,  "");
//        String picUrl = "";
//        String title = getString(R.string.app_name);
//        String description = "";
//        showShareDialog(shareUrl, picUrl, title, description);

        final ArrayList<String> urlList = new ArrayList<String>();
        for (int j = 0; j < mList.get(position).getOfficeContentItemsList().size(); j++) {
            urlList.add(mList.get(position).getOfficeContentItemsList().get(j).getResourceUrl());
        }
        Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
        bIt.putExtra("albumId", mList.get(position).getId());
        bIt.putExtra("itemsId", mList.get(position).getOfficeContentItemsList().get(0).getId());
        bIt.putParcelableArrayListExtra("picList", (ArrayList) mList.get(position).getOfficeContentItemsList());
        bIt.putExtra("position", 0);
        bIt.putExtra("picUrl", mList.get(position).getOfficeContentItemsList().get(0).getResourceUrl());
        if (urlList.size() > 1)// url集合
            bIt.putStringArrayListExtra("urlList", urlList);
        startActivity(bIt);
    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_OFFICE_CONTENT_ALBUMS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("officeId", officeId);
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<OfficeContentAlbums> currentPageList = new ArrayList<OfficeContentAlbums>();
            JSONArray jay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyView(mContext, actualListView, "暂无数据", R.drawable.quexing, null,
                        100, 95);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            if (PAGE_NUM == 1) {
                mList.clear();
            }
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            } else {
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                OfficeContentAlbums itemInfo = gson.fromJson(jot.toString(), OfficeContentAlbums.class);
                currentPageList.add(itemInfo);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    private void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
