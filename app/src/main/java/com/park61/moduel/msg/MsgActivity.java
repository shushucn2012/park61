package com.park61.moduel.msg;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.msg.adapter.MsgListAdapter;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;

    private MsgListAdapter mAdapter;
    private List<MsgItem> msgDataList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_msg);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        msgDataList = new ArrayList<MsgItem>();
        mAdapter = new MsgListAdapter(mContext, msgDataList);
        actualListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, actualListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetMsgList();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetMsgList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetMsgList();
            }
        });
//        actualListView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // 下拉list的position会多1
//                MsgItem item = msgDataList.get(position - 1);
//                if (item.getType() == 0 || item.getType() == 1) {
//                    Intent it = new Intent(mContext, ActDetailsActivity.class);
//                    it.putExtra("id", item.getActivityId());
//                    it.putExtra("isToComt", true);
//                    startActivity(it);
//                } else if (item.getType() == 4 || item.getType() == 5 || item.getType() == 6) {
//                    Intent it = new Intent(mContext, ActsOrderDetailActivity.class);
//                    it.putExtra("applyId", item.getActivityId());
//                    it.putExtra("orderState", "applied");
//                    startActivity(it);
//                } else if (item.getType() == 9) {
//                    Intent it = new Intent(mContext, BalanceActivity.class);
//                    startActivity(it);
//                } else {
//                    Intent changeIt = new Intent();
//                    changeIt.setAction("ACTION_TAB_CHANGE");
//                    changeIt.putExtra("TAB_NAME", "tab_grow");
//                    sendBroadcast(changeIt);
//                    finish();
//                }
//                asyncSetMsgRead(msgDataList.get(position - 1).getId());
//            }
//        });
    }

    /**
     * 请求消息数据
     */
    private void asyncGetMsgList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_MSG_LIST;
        String requestBodyData = ParamBuild.getMsgList(PAGE_NUM, PAGE_SIZE);
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
            listStopLoadView();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<MsgItem> currentPageList = new ArrayList<MsgItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                msgDataList.clear();
                mAdapter.notifyDataSetChanged();
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                msgDataList.clear();
                setPullToRefreshViewBoth();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                MsgItem c = gson.fromJson(actJot.toString(), MsgItem.class);
                currentPageList.add(c);
            }
            msgDataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 将消息设为已读
     */
    private void asyncSetMsgRead(Long id) {
        // 请求之前隐藏为空提示
        String wholeUrl = AppUrl.host + AppUrl.SET_MSG_READ;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
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
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullRefreshListView.setMode(Mode.BOTH);
    }

}
