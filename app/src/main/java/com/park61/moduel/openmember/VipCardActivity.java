package com.park61.moduel.openmember;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.openmember.adapter.VipCardBabyListAdapter;
import com.park61.moduel.openmember.adapter.VipCardListAdapter;
import com.park61.moduel.openmember.bean.CardInfo;
import com.park61.moduel.openmember.bean.ChildInfo;
import com.park61.moduel.openmember.bean.VipCardInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员卡明细界面
 */
public class VipCardActivity extends BaseActivity {
    private TextView tv, tv_top_title, tv_member_right, tv_rest, tv_count, tv_description, tv_unit, tv_card_msg;
    private View area_baby, area_count_hour, block_detail, block_baby;
    private Button btn_recharge;
    private String cardName;//卡名称
    private int cardType;//会员卡卡类型，1：次卡，2：小时卡,3:会员卡
    private String cardId;//卡id
    private Long childId;
    private List<ChildInfo> babyItemsList;
    private VipCardBabyListAdapter babyListAdapter;
    private ListViewForScrollView lv_child, lv_member_detail;
    private ArrayList<VipCardInfo> vipCardList;
    private VipCardListAdapter vipCardListAdapter;
    private String hasChild;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_vipcard);
    }

    @Override
    public void initView() {
        tv = (TextView) findViewById(R.id.tv);
        tv_unit = (TextView) findViewById(R.id.tv_unit);
        tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        tv_rest = (TextView) findViewById(R.id.tv_rest);
        tv_member_right = (TextView) findViewById(R.id.tv_member_right);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_card_msg = (TextView) findViewById(R.id.tv_card_msg);
        area_baby = findViewById(R.id.area_baby);
        block_baby = findViewById(R.id.block_baby);
        block_detail = findViewById(R.id.block_detail);
        area_count_hour = findViewById(R.id.area_count_hour);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        lv_child = (ListViewForScrollView) findViewById(R.id.lv_child);
        lv_member_detail = (ListViewForScrollView) findViewById(R.id.lv_member_detail);
//        ViewInitTool.setListEmptyView(mContext,lv_child,"好冷清呀，您还没有使用记录哦",
//                R.drawable.quexing,null,100,95);
//        ViewInitTool.setListEmptyView(mContext,lv_member_detail,"好冷清呀，您还没有使用记录哦",
//                R.drawable.quexing,null,100,95);
    }

    @Override
    public void initData() {
        cardName = getIntent().getStringExtra("cardName");
        tv_top_title.setText(cardName);
        tv_card_msg.setText(cardName + "服务明细");
        cardType = getIntent().getIntExtra("cardType", 0);
        cardId = getIntent().getStringExtra("cardId");
        hasChild = getIntent().getStringExtra("hasChild");
        if (hasChild.equals("1")) {
            lv_child.setVisibility(View.VISIBLE);
        } else {
            lv_child.setVisibility(View.GONE);
        }
        babyItemsList = new ArrayList<ChildInfo>();
        babyListAdapter = new VipCardBabyListAdapter(mContext, babyItemsList);
        lv_child.setAdapter(babyListAdapter);
        vipCardList = new ArrayList<VipCardInfo>();
        vipCardListAdapter = new VipCardListAdapter(mContext, vipCardList);
        lv_member_detail.setAdapter(vipCardListAdapter);
        if (cardType == 1 || cardType == 2) {
            area_baby.setVisibility(View.GONE);
            area_count_hour.setVisibility(View.VISIBLE);
            asyncGetCardDetail(cardType);
        } else {
            area_baby.setVisibility(View.VISIBLE);
            area_count_hour.setVisibility(View.GONE);
            asyncGetUserChilds();
        }
    }

    @Override
    public void initListener() {
        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OpenMemberActivity.class);
                intent.putExtra("cardId", cardId);
                startActivity(intent);
            }
        });
        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(mContext, OpenMemberActivity.class);
                intent.putExtra("childInfo", babyItemsList.get(position));
//                intent.putExtra("cardCode","001");
                startActivity(intent);
            }
        });
        tv_member_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MemberRightsActivity.class));
            }
        });
    }

    /**
     * 获取会员卡孩子列表
     */
    private void asyncGetUserChilds() {
//        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String wholeUrl = AppUrl.host + AppUrl.GET_CHILDREN_LIST;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getChildsLsner);
    }

    BaseRequestListener getChildsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray jay = jsonResult.optJSONArray("list");
            babyItemsList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ChildInfo info = gson.fromJson(jot.toString(), ChildInfo.class);
                babyItemsList.add(info);
            }
            if (CommonMethod.isListEmpty(babyItemsList)) {
                showShortToast("您还没有添加孩子信息!");
                finish();
                return;
            }
            if (babyItemsList.size() > 0) {
                block_baby.setVisibility(View.GONE);
            } else {
                block_baby.setVisibility(View.VISIBLE);
            }
            babyListAdapter.notifyDataSetChanged();
            asyncGetCardDetail(cardType);
        }
    };

    /**
     * 查询小时卡/次卡剩余次数
     */
    private void asyncGetRestTimes(int cardType) {
        String wholeUrl = AppUrl.host + AppUrl.GET_REST_TIMES;
        String requestBodyData = ParamBuild.getCardDetail(cardType);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getTimesLsner);
    }

    BaseRequestListener getTimesLsner = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CardInfo cardItem = gson.fromJson(jot.toString(),
                        CardInfo.class);// 把Json数据解析成对象
                tv_rest.setText(cardItem.getText() + "：");
                tv_count.setText(cardItem.getAllTimes());
                tv_unit.setText(cardItem.getType());
                tv_description.setText(cardItem.getDate());
            }
        }
    };

    /**
     * 卡明细
     */
    private void asyncGetCardDetail(int cardType) {
//        String wholeUrl = AppUrl.host + AppUrl.GET_VIP_DETAIL;
        String wholeUrl = AppUrl.host + AppUrl.GET_CARD_DETAIL;
        String requestBodyData = ParamBuild.getCardDetail(cardType);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getDetailLsner);
    }

    BaseRequestListener getDetailLsner = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            vipCardList.clear();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                VipCardInfo cardItem = gson.fromJson(jot.toString(), VipCardInfo.class);
                vipCardList.add(cardItem);
            }
            if (vipCardList.size() > 0) {
                block_detail.setVisibility(View.GONE);
            } else {
                block_detail.setVisibility(View.VISIBLE);
            }
            if (vipCardList.size() < 6) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
            }
            vipCardListAdapter.notifyDataSetChanged();
            if(cardType == 1 || cardType == 2){
                asyncGetRestTimes(cardType);
            }
        }
    };
}
