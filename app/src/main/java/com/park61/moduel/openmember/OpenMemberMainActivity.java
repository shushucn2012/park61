package com.park61.moduel.openmember;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.me.AddBabyActivity;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.adapter.CardTypeListAdapter;
import com.park61.moduel.openmember.bean.MemberCartBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 开通会员主界面
 */
public class OpenMemberMainActivity extends BaseActivity implements View.OnClickListener {
    private View area_right,area_my_service;
    private ImageView img_open_member;
    private ListViewForScrollView lv_cardtype;
    private CardTypeListAdapter cardTypeListAdapter;
    private ArrayList<MemberCartBean> cardTypeList;
    private int cardType;//会员卡卡类型，1：次卡，2：小时卡,3:会员卡
    private String cardId;//卡id
    private List<BabyItem> babyList;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_openmember_main);
    }

    @Override
    public void initView() {
        img_open_member = (ImageView) findViewById(R.id.img_open_member);
        area_my_service = findViewById(R.id.area_my_service);
        area_right = findViewById(R.id.area_right);
        lv_cardtype = (ListViewForScrollView) findViewById(R.id.lv_cardtype);
    }

    @Override
    public void initData() {
        cardTypeList = new ArrayList<MemberCartBean>();
        cardTypeListAdapter = new CardTypeListAdapter(mContext,cardTypeList);
        lv_cardtype.setAdapter(cardTypeListAdapter);
        asyncGetCardType();
    }

    @Override
    public void initListener() {
        img_open_member.setOnClickListener(this);
        area_right.setOnClickListener(this);
        area_my_service.setOnClickListener(this);
        lv_cardtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cardType = cardTypeList.get(position).getType();
                Intent it = new Intent(mContext,VipCardActivity.class);
                    it.putExtra("cardType",cardType);
                    it.putExtra("cardName",cardTypeList.get(position).getName());
                    it.putExtra("cardId",cardTypeList.get(position).getCardId());
                    it.putExtra("hasChild",cardTypeList.get(position).getHasChild());
                    startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_open_member:
                asyncGetUserChilds();
//                Intent intent = new Intent(this,OpenMemberActivity.class);
//                intent.putExtra("cardType",cardType);
//                startActivity(intent);
                break;
            case R.id.area_right:
                startActivity(new Intent(this,MemberRightsActivity.class));
                break;
            case R.id.area_my_service:
                startActivity(new Intent(this,ConsumptionDetailActivity.class));
                break;

        }
    }
    /**
     * 查询会员卡类型
     */
    private void asyncGetCardType() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CARD_TYPE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getCardTypeLsner);
    }
    BaseRequestListener getCardTypeLsner = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
//            finish();
        }
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            cardTypeList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                MemberCartBean cardItem = gson.fromJson(jot.toString(),
                        MemberCartBean.class);// 把Json数据解析成对象
                cardTypeList.add(cardItem);
            }
            cardTypeListAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
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
            babyList = new ArrayList<BabyItem>();
            babyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                babyList.add(b);
            }
            if(babyList.size()<=0){
                dDialog.showDialog("提醒", "您还未添加宝宝，是否先去添加宝宝", "否", "是", null,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mContext, AddBabyActivity.class));
                                dDialog.dismissDialog();
                            }
                        });
            }else{
                Intent intent = new Intent(mContext,OpenMemberActivity.class);
                intent.putExtra("cardType",cardType);
                startActivity(intent);
            }
        }
    };
}
