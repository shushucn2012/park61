package com.park61.moduel.child;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.ComWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.child.adapter.DapCommentRecordListAdapter;
import com.park61.moduel.child.bean.DapCommentInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 测评记录
 */
public class DapCommentRecordActivity extends BaseActivity {
    private Long childId;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 10;// 评论列表每页条数
    private DapCommentRecordListAdapter mAdapter;
    private ArrayList<DapCommentInfo> mList;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dap_comment_record);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView,mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetEaChildList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetEaChildList();
            }
        });
        mList = new ArrayList<DapCommentInfo>();
        mAdapter = new DapCommentRecordListAdapter(mContext,mList);
        actualListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        childId = getIntent().getLongExtra("childId",0l);
        PAGE_NUM = 1;
        asyncGetEaChildList();
    }

    @Override
    public void initListener() {
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Long resultId = mList.get(position-1).getId();
                Intent it = new Intent(mContext,ComWebViewActivity.class);
                it.putExtra("PAGE_TITLE","测评结果");
                String url = "http://m.61park.cn/ea/toGrowEaResult.do"+
                        "?eaKey=31c7f6b070ebfa101660a4bd4df75063&resultId="+ resultId;
                it.putExtra("url",url);
                it.putExtra("title",mList.get(position-1).getEaItemName());
                startActivity(it);
            }
        });
    }
    /**
     * 测评列表
     */
    private void asyncGetEaChildList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_LIST_EA_CHILD_RESULT;
        String requestBodyData = ParamBuild.getEaChildList(PAGE_NUM,PAGE_SIZE,childId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                listListener);
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
            ArrayList<DapCommentInfo> currentPageList = new ArrayList<DapCommentInfo>();
            JSONArray jay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyView(mContext, actualListView, "暂无测评记录", R.drawable.quexing, null,
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
                DapCommentInfo itemInfo = gson.fromJson(jot.toString(), DapCommentInfo.class);
                currentPageList.add(itemInfo);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
    private void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }
}
