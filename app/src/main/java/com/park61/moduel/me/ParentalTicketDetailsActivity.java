package com.park61.moduel.me;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.ParentalDetailActivity;
import com.park61.moduel.firsthead.ToyNumActivity;
import com.park61.moduel.firsthead.adapter.ToysAdapter;
import com.park61.moduel.firsthead.bean.ToyBean;
import com.park61.moduel.me.bean.ParentalTicketDetailsBean;
import com.park61.moduel.sales.ParentalOrderConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 亲子活动入场证详情页面
 * Created by shubei on 2017/11/2.
 */
public class ParentalTicketDetailsActivity extends BaseActivity implements ToysAdapter.OnNumChanged {

    private int id;//入场证id
    private ParentalTicketDetailsBean mParentalTicketDetailsBean;//入场证详情bean
    private List<ToyBean> toyBeanList = new ArrayList<>();
    ;
    //private LRecyclerViewAdapter lAdapter;
    private ToysAdapter adapter;

    private TextView tv_state, browse_name_tv, browse_number_tv, tv_toy_name, tv_addr, tv_start_time, tv_apply_time, tv_sign_time, tv_one_key_buy;
    private ImageView img_state, img_toy;
    private View area_buy_title, area_sign_time, area_actdetail;
    private RecyclerView toys_lv;
    private boolean isInputNum = false;
    private boolean isFirstIn = true;
    private int activityId = -1;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_parental_ticket_details);
    }

    @Override
    public void initView() {
        setPagTitle("订单详情");
        tv_state = (TextView) findViewById(R.id.tv_state);
        img_state = (ImageView) findViewById(R.id.img_state);
        browse_name_tv = (TextView) findViewById(R.id.browse_name_tv);
        browse_number_tv = (TextView) findViewById(R.id.browse_number_tv);
        img_toy = (ImageView) findViewById(R.id.img_toy);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
        tv_sign_time = (TextView) findViewById(R.id.tv_sign_time);
        toys_lv = (RecyclerView) findViewById(R.id.toys_lv);
        area_buy_title = findViewById(R.id.area_buy_title);
        tv_one_key_buy = (TextView) findViewById(R.id.tv_one_key_buy);
        area_sign_time = findViewById(R.id.area_sign_time);
        area_actdetail = findViewById(R.id.area_actdetail);
    }

    @Override
    public void initData() {
        adapter = new ToysAdapter(this, toyBeanList);
        adapter.setOnNumChangedListener(this);
        //lAdapter = new LRecyclerViewAdapter(adapter);
        //FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        toys_lv.setNestedScrollingEnabled(false);
        //toys_lv.setLayoutManager(linearLayoutManager);
        toys_lv.setLayoutManager(new LinearLayoutManager(mContext));
        toys_lv.setAdapter(adapter);

        id = getIntent().getIntExtra("id", -1);
        asyncActivityApplyDetail();
    }

    @Override
    public void initListener() {
        tv_one_key_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一键购买
                goOrderActivity();
            }
        });
        area_actdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ParentalDetailActivity.class);
                it.putExtra("id", mParentalTicketDetailsBean.getPartyId());
                it.putExtra("classId", mParentalTicketDetailsBean.getClassId());
                startActivity(it);
            }
        });
    }

    private void goOrderActivity() {
        boolean hasSelected = false;
        StringBuilder sbId = new StringBuilder();
        StringBuilder sbCount = new StringBuilder();
        if (toyBeanList.size() > 0) {
            for (int i = 0; i < toyBeanList.size(); i++) {
                ToyBean b = toyBeanList.get(i);
                if (b.getNumSelect() > 0) {
                    hasSelected = true;
                    sbId.append(b.getPmInfoId());
                    sbCount.append(b.getNumSelect());
                } else {
                    continue;
                }
                if (i < toyBeanList.size() - 1) {
                    sbId.append(",");
                    sbCount.append(",");
                }
            }
        }
        if (hasSelected) {
            Intent it = new Intent(this, ParentalOrderConfirmActivity.class);
            it.putExtra("partyId", activityId);
            it.putExtra("linkedBusinessId", mParentalTicketDetailsBean.getId());
            it.putExtra("pmInfoIdList", sbId.toString());
            it.putExtra("pmInfoCountList", sbCount.toString());
            startActivity(it);
            finish();
        } else {
            showShortToast("请先选择要购买的商品");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInputNum && !isFirstIn) {
            //选完规格后刷新列表
            if (mParentalTicketDetailsBean != null && !mParentalTicketDetailsBean.isBoughtGoods()) {
                asyncToyList();
            }
        } else {
            isFirstIn = false;
            isInputNum = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ToyNumActivity.RESULT_CODE) {
            //更新填写的装备数量
            int p = data.getIntExtra("position", -1);
            int tempNum = data.getIntExtra("tempNum", -1);
            if (p != -1 && tempNum != -1) {
                onChanged(p, tempNum);
            }
            isInputNum = true;
        }
    }

    /**
     * 请求详情数据
     */
    private void asyncActivityApplyDetail() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.applyDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, elistener);
    }

    BaseRequestListener elistener = new JsonRequestListener() {
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
            mParentalTicketDetailsBean = gson.fromJson(jsonResult.toString(), ParentalTicketDetailsBean.class);
            //填充数据
            fillData();
        }
    };

    private void fillData() {
        //根据状态展示不同数据
        if (mParentalTicketDetailsBean.getStatus() == 0) { //待签到
            img_state.setImageResource(R.drawable.dengdaichuli);
            tv_state.setText("待签到");
            //不显示下单时间，签到时间，退款时间，和退款联系客服按钮
            area_sign_time.setVisibility(View.GONE);
        } else if (mParentalTicketDetailsBean.getStatus() == 1) {//已签到
            img_state.setImageResource(R.drawable.jiaoyichenggong);
            tv_state.setText("活动进行中");
            //不显示签到时间，退款时间，显示退款联系客服按钮
            area_sign_time.setVisibility(View.VISIBLE);
        } else if (mParentalTicketDetailsBean.getStatus() == 2) {//已结束
            img_state.setImageResource(R.drawable.jiaoyichenggong);
            tv_state.setText("已结束");
            //不显示退款时间，不显示退款联系客服按钮
            area_sign_time.setVisibility(View.VISIBLE);
        }

        browse_name_tv.setText("联系人：" + mParentalTicketDetailsBean.getUserName());//联系人姓名
        browse_number_tv.setText(mParentalTicketDetailsBean.getUserTel());//联系人电话
        ImageManager.getInstance().displayImg(img_toy, mParentalTicketDetailsBean.getCoverUrl());//封面
        tv_toy_name.setText(mParentalTicketDetailsBean.getTitle());//活动名称
        tv_addr.setText(mParentalTicketDetailsBean.getSchoolName() + " " + mParentalTicketDetailsBean.getClassName());//活动地址
        tv_start_time.setText(mParentalTicketDetailsBean.getStartDateStr());//活动时间
        tv_apply_time.setText(mParentalTicketDetailsBean.getApplyDate());//活动报名时间
        tv_sign_time.setText(mParentalTicketDetailsBean.getSignDate());//签到时间\
        if (TextUtils.isEmpty(mParentalTicketDetailsBean.getSignDate())) {
            area_sign_time.setVisibility(View.GONE);
        }

        activityId = mParentalTicketDetailsBean.getPartyId();
        adapter.setActivityId(activityId);
        if (!mParentalTicketDetailsBean.isBoughtGoods()) {
            asyncToyList();
        }
    }

    @Override
    public void onChanged(int position, int num) {
        ToyBean b = toyBeanList.get(position);
        b.setNumSelect(num);
        adapter.notifyDataSetChanged();

        if (num != 0) {
            String url = AppUrl.NEWHOST_HEAD + AppUrl.childNumSelect;
            Map<String, Object> map = new HashMap<>();
            map.put("numSelect", num);
            map.put("partyId", activityId);
            map.put("pmInfoIdList", b.getPmInfoIdList());
            String requestData = ParamBuild.buildParams(map);
            netRequest.startRequest(url, Request.Method.POST, requestData, 0, numListener);
        }
    }

    BaseRequestListener numListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) throws JSONException {
            dismissDialog();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    /**
     * 获取关联装备列表
     */
    private void asyncToyList() {
        String url = AppUrl.NEWHOST_HEAD + AppUrl.childActivityToys;
        Map<String, Object> map = new HashMap<>();
        map.put("id", mParentalTicketDetailsBean.getPartyId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, com.park61.net.base.Request.Method.POST, requestData, 0, toysListener);
    }

    BaseRequestListener toysListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                toyBeanList.clear();
                tv_one_key_buy.setVisibility(View.VISIBLE);
                area_buy_title.setVisibility(View.VISIBLE);
                toys_lv.setVisibility(View.VISIBLE);
                for (int i = 0; i < arr.length(); i++) {
                    ToyBean b = gson.fromJson(arr.optJSONObject(i).toString(), ToyBean.class);
                    if (b.getNumSelect() == 0) {
                        b.setNumSelect(1);
                    }
                    toyBeanList.add(b);
                }
                adapter.notifyDataSetChanged();
            } else {
                //隐藏一键购买按钮
                tv_one_key_buy.setVisibility(View.GONE);
                area_buy_title.setVisibility(View.GONE);
                toys_lv.setVisibility(View.GONE);
            }
        }
    };
}
