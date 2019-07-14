package com.park61.moduel.sales;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.SecKillListNewAdapter;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionPageBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SecKillActivityNew extends BaseActivity {

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;

    private String promotionType;
    private List<ProductLimit> plist;
    private List<PromotionPageBean> ppbList;
    private List<Long> timerList; //计时到时列表 单位秒
    private SecKillListNewAdapter mAdapter;
    private boolean isTimerStart;//计时器是否开始
    private long timerCount = 0;//计时数字

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sec_kill_new);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_page_title);
        String titleStr = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(titleStr)) {
            tvTitle.setText("限时秒杀");
        } else {
            tvTitle.setText(titleStr);
        }
        plist = new ArrayList<ProductLimit>();
        ppbList = new ArrayList<PromotionPageBean>();
        timerList = new ArrayList<Long>();
        mAdapter = new SecKillListNewAdapter(mContext, plist);
        actualListView.setAdapter(mAdapter);
        promotionType = getIntent().getStringExtra("promotionType");
        asyncGetPromotionMerge();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取限时秒杀所有列表
     */
    public void asyncGetPromotionMerge() {
        String wholeUrl = AppUrl.host + AppUrl.PROMOTION_MERGE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("promotionType", promotionType);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, pmlistener);
    }

    BaseRequestListener pmlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ppbList.clear();
            plist.clear();
            timerList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            /******测试代码start******/
            /* String resultJson = CommonMethod.readAssetsFile(mContext, "seckill_list_json_file");
            logout("resultJson======"+resultJson);
            try {
                JSONObject mJSONObject = new JSONObject(resultJson);
                actJay = mJSONObject.optJSONArray("data");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            /******测试代码end******/
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                PromotionPageBean ppb = gson.fromJson(jot.toString(), PromotionPageBean.class);
                String isStart = ppb.getIsStart();
                String isCurrent = ppb.getIsCurrent();
                String msg = "";
                if (isStart.equals("1") && isCurrent.equals("1")) {
                    msg = "抢购中";
                } else if (isStart.equals("1") && isCurrent.equals("")) {
                    msg = "即将开始";
                } else if (isStart.equals("0") && isCurrent.equals("")) {
                    msg = "已开抢";
                }
                ppb.setMsg(msg);
                long leftSecond = Long.parseLong(ppb.getCountDownTime())/1000;
                logout("leftMSecond======"+ppb.getCountDownTime());
                logout("leftSecond======"+leftSecond);
                timerList.add(leftSecond);
                ppbList.add(ppb);
            }
            for (int j = 0; j < ppbList.size(); j++) {
                PromotionPageBean promotionPageBean = ppbList.get(j);
                if (!CommonMethod.isListEmpty(promotionPageBean.getProductLimit())) {
                    for (int k = 0; k < promotionPageBean.getProductLimit().size(); k++) {
                        ProductLimit productLimit = promotionPageBean.getProductLimit().get(k);
                        productLimit.setCountDownTime(promotionPageBean.getCountDownTime());
                        productLimit.setMsg(promotionPageBean.getMsg());
                    }
                    plist.addAll(promotionPageBean.getProductLimit());
                }
            }
            mAdapter.notifyDataSetChanged();
            stopMyTimer();
            startMyTimer();
        }
    };

    private final Handler timerHanlder = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerCount++;
                timerHanlder.sendEmptyMessageDelayed(0, 1000);
                for(int j=0;j<plist.size();j++){
                    long leftSec = Long.parseLong(plist.get(j).getCountDownTime())/1000;
                    plist.get(j).setCountDownTime((leftSec-1)*1000+"");
                }
                mAdapter.notifyDataSetChanged();
                for (int i = 0; i < timerList.size(); i++) {
                    if (timerList.get(i).equals(timerCount)) {
                        asyncGetPromotionMerge();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开始计时
     */
    public void startMyTimer() {
        if (!isTimerStart) {
            timerCount = 0;
            timerHanlder.sendEmptyMessageDelayed(0, 1000);
            isTimerStart = true;
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopMyTimer() {
        if (isTimerStart) {
            timerHanlder.removeMessages(0);
            isTimerStart = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMyTimer();
    }
}
