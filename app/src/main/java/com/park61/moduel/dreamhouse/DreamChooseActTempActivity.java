package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.dreamhouse.adapter.DreamChooseActTempListAdapter;
import com.park61.moduel.dreamhouse.bean.DreamChooseActTempBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 梦想发布-选择现有游戏模板
 * Created by shushucn2012 on 2017/1/17.
 */
public class DreamChooseActTempActivity extends BaseActivity {

    private PullToRefreshListView mPullRefreshListView;
    private View serach_area;
    private TextView tv_keyword;

    private DreamChooseActTempListAdapter mAdapter;
    private ArrayList<DreamChooseActTempBean> mList;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 10;// 评论列表每页条数
    private String actBussinessType;
    private String actTitle;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dream_choose_acttemp);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetActTempList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetActTempList();
            }
        });
        serach_area = findViewById(R.id.serach_area);
        tv_keyword = (TextView) findViewById(R.id.tv_keyword);
    }

    @Override
    public void initData() {
        actTitle = getIntent().getStringExtra("KEY_WORD");
        if (!TextUtils.isEmpty(actTitle)) {
            tv_keyword.setText(actTitle);
        }
        mList = new ArrayList<DreamChooseActTempBean>();
        mAdapter = new DreamChooseActTempListAdapter(mContext, mList);
        mPullRefreshListView.setAdapter(mAdapter);

        PAGE_NUM = 1;
        asyncGetActTempList();
    }

    @Override
    public void initListener() {
        serach_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, DreamActTempSearchActivity.class);
                it.putExtra("KEY_WORD", actTitle);
                startActivityForResult(it, 11);
                //CommonMethod.startOnlyNewActivity(mContext, DreamActTempSearchActivity.class, it);
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnData = new Intent();
                logout("mList.get(position).getActDetail()=======" + mList.get(position - 1).getActDetail());
                returnData.putExtra("ActContent", mList.get(position - 1).getActDetail());
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    /**
     * 游戏模板列表
     */
    protected void asyncGetActTempList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ACTIVITY_TEMPLATE_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        map.put("actBussinessType", actBussinessType);
        map.put("actTitle", actTitle);
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
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<DreamChooseActTempBean> currentPageList = new ArrayList<DreamChooseActTempBean>();
            JSONArray jay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyTipByDefaultPic(mContext, mPullRefreshListView.getRefreshableView(), "暂无数据");
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
                DreamChooseActTempBean itemInfo = gson.fromJson(jot.toString(), DreamChooseActTempBean.class);
                currentPageList.add(itemInfo);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        if (requestCode == 11) {
            actTitle = data.getStringExtra("KEY_WORD");
            if (!TextUtils.isEmpty(actTitle)) {
                tv_keyword.setText(actTitle);
            }
            PAGE_NUM = 1;
            asyncGetActTempList();
        }
    }
}
