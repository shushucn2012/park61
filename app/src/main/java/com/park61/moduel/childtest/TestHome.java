package com.park61.moduel.childtest;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.childtest.adapter.RecommendTestAdapter;
import com.park61.moduel.childtest.bean.BabylistBean;
import com.park61.moduel.childtest.bean.TestRecommedBean;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.me.AddBabyActivity;
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
 * Created by nieyu on 2017/12/1.
 */

public class TestHome extends BaseActivity implements View.OnClickListener {
    ListView rv_firsthead;
    ImageView iv_addbaby, iv_changebaby, iv_back, icon_user, tv_testbaby;
    TextView tv_logstate, tv_warm, tv_hisresult;
    private int addbaby = -1;
    private int babynumber = -1;
    List<BabylistBean> babylistBeenList;
    List<TestRecommedBean> sysRecommendList;
    BabylistBean babylistBean;
    private int curBabyIndex = 0;
    private RecommendTestAdapter recommendTestAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.actiivty_testhome);
    }

    @Override
    public void initView() {
        tv_testbaby = (ImageView) findViewById(R.id.tv_testbaby);
        tv_testbaby.setOnClickListener(this);
        icon_user = (ImageView) findViewById(R.id.icon_user);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        rv_firsthead = (ListView) findViewById(R.id.rv_firsthead);
        iv_addbaby = (ImageView) findViewById(R.id.iv_addbaby);
        iv_addbaby.setOnClickListener(this);
        tv_logstate = (TextView) findViewById(R.id.tv_logstate);
        tv_warm = (TextView) findViewById(R.id.tv_warm);
        tv_hisresult = (TextView) findViewById(R.id.tv_hisresult);
        tv_hisresult.setOnClickListener(this);
        iv_changebaby = (ImageView) findViewById(R.id.iv_changebaby);
//        tv_bottom=(TextView) findViewById(R.id.tv_bottom);
//        tv_bottom.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sysRecommendList = new ArrayList<TestRecommedBean>();
        recommendTestAdapter = new RecommendTestAdapter(sysRecommendList, TestHome.this);
        rv_firsthead.setAdapter(recommendTestAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initview();
    }

    @Override
    public void initListener() {
        iv_changebaby.setOnClickListener(v -> {
                    if (curBabyIndex < babylistBeenList.size() - 1) {//不是最后一个宝宝，切换下一个
                        curBabyIndex = curBabyIndex + 1;
                    } else {//是最后一个宝宝，切回第一个
                        curBabyIndex = 0;
                    }
                    babylistBean = babylistBeenList.get(curBabyIndex);
                    ImageManager.getInstance().displayImg(icon_user, babylistBean.getPictureUrl(), R.drawable.icon_user);
                    tv_warm.setText(babylistBean.getChildAge());
                    tv_logstate.setText(babylistBean.getPetName());
                    asyncChildRecommed();
                }
        );
        recommendTestAdapter.setBtnClickedLsner(new RecommendTestAdapter.OnBtnClickedLsner() {
            @Override
            public void onClick(int position) {
                if (sysRecommendList.get(position).getIsLogin() == 1 && TextUtils.isEmpty(GlobalParam.userToken)) {//是否需登录,0:不登录,1:登录	需要登录，并且没有登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (sysRecommendList.get(position).getBtnStatus() == 0 || sysRecommendList.get(position).getBtnStatus() == 1) {
                    if (babylistBean == null) {//没有孩子
                        Intent it = new Intent(mContext, TestBaseInfoInputActivity.class);
                        //it.putExtra("childId", babylistBean.getId());
                        it.putExtra("eaServiceId", sysRecommendList.get(position).getId());
                        startActivity(it);
                    } else {//有孩子
                        if (babylistBean.getHad5Sub() == 1) {
                            Intent it = new Intent(mContext, TestQuestionNewActivity.class);
                            it.putExtra("childId", babylistBean.getId());
                            it.putExtra("eaServiceId", sysRecommendList.get(position).getId());
                            startActivity(it);
                        } else if (babylistBean.getHad5Sub() == 0) {
                            Intent it = new Intent(mContext, TestBaseInfoInputActivity.class);
                            it.putExtra("childId", babylistBean.getId());
                            it.putExtra("eaServiceId", sysRecommendList.get(position).getId());
                            startActivity(it);
                        }
                    }
                } else if (sysRecommendList.get(position).getBtnStatus() == 2) {
                    if (GlobalParam.userToken == null) {
                        startActivity(new Intent(TestHome.this, LoginV2Activity.class));
                    } else {
                        Intent it = new Intent(mContext, TestOrderPaysActivity.class);
                        it.putExtra("eaServiceId", sysRecommendList.get(position).getId());
                        startActivity(it);
                    }
                }
            }
        });
       /* rv_firsthead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                it.putExtra("url", "http://m.61park.cn/#/dap/detail/3");
                startActivity(it);
            }
        });*/
    }

    public void initview() {
        if (GlobalParam.userToken != null) {
            asyncCheckHasChild();
            icon_user.setOnClickListener(v -> {
            });
            tv_logstate.setOnClickListener(v -> {
            });
        } else {
            //未登录，点击icon和logstate跳转到登录页面
            icon_user.setOnClickListener(v -> startActivity(new Intent(this, LoginV2Activity.class)));
            tv_logstate.setOnClickListener(v -> startActivity(new Intent(this, LoginV2Activity.class)));
            tv_logstate.setVisibility(View.VISIBLE);
            tv_logstate.setTextColor(getResources().getColor(R.color.color_text_red_deep));
            tv_logstate.setText("未登录");
            iv_addbaby.setVisibility(View.GONE);
            tv_warm.setText("登录可保存测评报告");
            asyncChildRecommed();
        }
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.tv_hisresult) {
            Intent intent = new Intent(TestHome.this, TestHistoryActivity.class);
            startActivity(intent);
        } else if (viewid == R.id.iv_back) {
            finish();
        } else if (viewid == R.id.iv_addbaby) {
            startActivity(new Intent(this, AddBabyActivity.class));
        } else if (R.id.tv_testbaby == viewid) {
            Intent intent = new Intent(TestHome.this, TestIntroActivity.class);
            startActivity(intent);
        } else if (R.id.tv_bottom == viewid) {

        }
    }

    public void asyncChildRecommed() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TestRecommend;
        Map<String, Object> map = new HashMap<String, Object>();
        if (babylistBean != null)
            map.put("childId", babylistBean.getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, childRecommend);
    }

    BaseRequestListener childRecommend = new JsonRequestListener() {

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
            sysRecommendList.clear();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            if (cmtJay != null) {
                for (int i = 0; i < cmtJay.length(); i++) {
                    JSONObject actJot = cmtJay.optJSONObject(i);
                    TestRecommedBean p = null;
                    try {
                        p = gson.fromJson(actJot.toString(), TestRecommedBean.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (p != null) {
                        sysRecommendList.add(p);
                    }
                }
            }
            recommendTestAdapter.notifyDataSetChanged();
        }
    };

    public void asyncCheckHasChild() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.childList;
        /*Map<String, Object> map = new HashMap<String, Object>();
        String requestBodyData = ParamBuild.buildParams(map);*/
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, checkedhasChildlisenter);
    }

    BaseRequestListener checkedhasChildlisenter = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            asyncChildRecommed();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            babylistBeenList = new ArrayList<BabylistBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            if (cmtJay != null) {
                for (int i = 0; i < cmtJay.length(); i++) {
                    JSONObject actJot = cmtJay.optJSONObject(i);
                    BabylistBean p = gson.fromJson(actJot.toString(), BabylistBean.class);
                    babylistBeenList.add(p);
                }
            }
            if (babylistBeenList.size() > curBabyIndex)
                babylistBean = babylistBeenList.get(curBabyIndex);
            asyncChildRecommed();
            if (babylistBeenList.size() >= 2) {//有2个以上宝宝
                ImageManager.getInstance().displayImg(icon_user, babylistBean.getPictureUrl(), R.drawable.icon_user);
                tv_logstate.setVisibility(View.VISIBLE);
                tv_logstate.setTextColor(getResources().getColor(R.color.bdp_dark_gray));
                tv_logstate.setText(babylistBean.getPetName());
                iv_changebaby.setVisibility(View.VISIBLE);
                iv_addbaby.setVisibility(View.GONE);
                tv_warm.setText(babylistBean.getChildAge());
            } else if (babylistBeenList.size() == 1) {//有一个宝宝
                ImageManager.getInstance().displayImg(icon_user, babylistBean.getPictureUrl(), R.drawable.icon_user);
                tv_warm.setText(babylistBean.getChildAge());
                tv_logstate.setVisibility(View.VISIBLE);
                tv_logstate.setTextColor(getResources().getColor(R.color.bdp_dark_gray));
                tv_logstate.setText(babylistBean.getPetName());
                iv_changebaby.setVisibility(View.GONE);
                iv_addbaby.setVisibility(View.VISIBLE);
            } else {//一个宝宝没有
                tv_logstate.setVisibility(View.GONE);
                iv_changebaby.setVisibility(View.GONE);
                iv_addbaby.setVisibility(View.VISIBLE);
                //为了更完整的测试报告，请完整填写资料
                tv_warm.setText("完善资料，测评报告更准确~");
            }
        }
    };

}
