package com.park61.moduel.shophome;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.shophome.adapter.AlbumCommentListAdapter;
import com.park61.moduel.shophome.bean.CommentInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017/3/8.
 */
public class ShopCommentActivity extends BaseActivity {
    private AlbumCommentListAdapter mAdapter;
    private List<CommentInfo> mList;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView actualListView;
    private Long albumId;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 10;// 评论列表每页条数
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_comment);
    }

    @Override
    public void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullToRefreshListView, mContext);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetCommentList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetCommentList();
            }
        });
        actualListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        albumId = getIntent().getLongExtra("albumId",-1l);
        mList = new ArrayList<CommentInfo>();
        mAdapter = new AlbumCommentListAdapter(mContext,mList);
        actualListView.setAdapter(mAdapter);
        PAGE_NUM = 1;
        asyncGetCommentList();
    }

    @Override
    public void initListener() {

    }
    /**
     * 获取梦想评论列表
     */
    protected void asyncGetCommentList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALBUM_COMMENT_LIST;
        String requestBodyData = ParamBuild.getAlbumComts(albumId,PAGE_NUM,PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {
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
            mPullToRefreshListView.onRefreshComplete();
            ArrayList<CommentInfo> currentPageList = new ArrayList<CommentInfo>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                ViewInitTool.setListEmptyView(mContext, actualListView, "暂无评论", R.drawable.quexing, null,
                        100, 95);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setPullToRefreshViewEnd(mPullToRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullToRefreshListView);
            } else {
                ViewInitTool.setPullToRefreshViewBoth(mPullToRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                CommentInfo c = gson.fromJson(actJot.toString(), CommentInfo.class);
                currentPageList.add(c);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
}
