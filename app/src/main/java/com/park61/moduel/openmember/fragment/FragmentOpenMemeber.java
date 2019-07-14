package com.park61.moduel.openmember.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.MemberOrderConfirmActivity;
import com.park61.moduel.openmember.MyBabyListActivity;
import com.park61.moduel.openmember.OpenMemberActivity;
import com.park61.moduel.openmember.adapter.MemberCartListAdapter;
import com.park61.moduel.openmember.adapter.MyBabyListAdapter;
import com.park61.moduel.openmember.bean.ChildInfo;
import com.park61.moduel.pay.bean.MemberCardLengthVO;
import com.park61.moduel.pay.bean.MemberCardTypeVO;
import com.park61.moduel.shop.FilterShopActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentOpenMemeber extends BaseFragment implements MemberCartListAdapter.OpenMemberListener {
    private View area_baby, area_shop;
    private ListViewForScrollView listview;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private TextView tv_change_baby, tv_change_store, tv_baby_name, tv_store_name, tv_date,tv_range;
    private ImageView img_baby;
    private String shopName = "";// 输入的店铺名称
    private long shopId;
    private Long childId = null;
    private Long memberTypeId;
    private String cardName;
    private String cardCode;//卡编码：61区会员001，VIP会员002，天使卡003，八月大礼包004，妈妈合伙人005，
    //次卡006，小时卡007
    private MyBabyListAdapter mAdapter;
//    private List<BabyItem> babyList;
    private String timeExpireDate;//有效时间
    private List<MemberCardLengthVO> memberCardLenList;// 卡长度类型列表
    private MemberCartListAdapter memberCardAdapter;
    private BabyItem selectedChild, childItem;
    private ChildInfo child;
    private List<ChildInfo> childInfoList;
    private String type;// 充值方式(0:开通;1:续费;2:升级)
    private int cardType;//会员卡卡类型，1：次卡，2：小时卡
    private int isBindChild;//（0：无需绑定宝宝，1：需绑定宝宝）
    private MemberCardTypeVO m;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_open_member, container, false);
        cardName = getArguments().getString("cardName");
        cardCode = getArguments().getString("cardCode");
        memberTypeId = getArguments().getLong("memberTypeId");
        selectedChild = (BabyItem) getArguments().getSerializable("child");
        child = (ChildInfo) getArguments().getSerializable("childInfo");
        type = getArguments().getString("type");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        img_baby = (ImageView) parentView.findViewById(R.id.img_baby);
        area_baby = parentView.findViewById(R.id.area_baby);
        area_shop = parentView.findViewById(R.id.area_shop);
        tv_change_baby = (TextView) parentView.findViewById(R.id.tv_change_baby);
        tv_change_store = (TextView) parentView.findViewById(R.id.tv_change_store);
        tv_baby_name = (TextView) parentView.findViewById(R.id.tv_baby_name);
        tv_store_name = (TextView) parentView.findViewById(R.id.tv_store_name);
        tv_range = (TextView) parentView.findViewById(R.id.tv_range);
        tv_date = (TextView) parentView.findViewById(R.id.tv_date);
        listview = (ListViewForScrollView) parentView.findViewById(R.id.listview);
        memberCardLenList = new ArrayList<MemberCardLengthVO>();
        memberCardAdapter = new MemberCartListAdapter(parentActivity, memberCardLenList);
        memberCardAdapter.setListener(this);
        listview.setAdapter(memberCardAdapter);
        listview.setFocusable(false);
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView
                .findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, parentActivity);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetCardLenListByType(cardCode);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetCardLenListByType(cardCode);
            }
        });

    }

    @Override
    public void initData() {
        childInfoList = new ArrayList<ChildInfo>();
        if (child == null && selectedChild == null) {
            if(!CommonMethod.isListEmpty(((OpenMemberActivity)parentActivity).babyList)){
                childItem = ((OpenMemberActivity)parentActivity).babyList.get(0);
                childId = childItem.getId();
                Log.out("=====childItem.getId()===="+childItem.getId()+"");
                setData(childItem);
            }
        } else if (child != null) {
            Log.i("=====childId======", childId + "");
            childInfoList.add(child);
            tv_date.setText(child.getExpireDate());
            tv_baby_name.setText(child.getPetName());
            ImageManager.getInstance().displayCircleImg(img_baby, child.getPicUrl());
        } else if (selectedChild != null) {
            setData(selectedChild);
        }
        // 有过来的id，设置其第一个店
//        shopName = parentActivity.getIntent().getStringExtra("transShopName");
//        tv_store_name.setText(shopName);
//        shopId = parentActivity.getIntent().getLongExtra("transShopId", 0l);
//        if (shopId <= 0) {
//            asyncGetClosestShop();
//        }
        asyncGetCardLenListByType(cardCode);
    }

    private void setData(BabyItem selectedChild) {
        timeExpireDate = selectedChild.getTimeExpireDate();
        tv_baby_name.setText(selectedChild.getPetName());
        ImageManager.getInstance().displayCircleImg(img_baby, selectedChild.getPictureUrl());
        tv_date.setText(selectedChild.getMemberExpireStr());
    }

    @Override
    public void initListener() {
        tv_change_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity, MyBabyListActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        tv_change_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(parentActivity, FilterShopActivity.class);
                it.putExtra("curShop", shopName);
                startActivityForResult(it, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            selectedChild = (BabyItem) data.getSerializableExtra("child");
            if (selectedChild == null) {
                parentActivity.finish();
            } else {
                setData(selectedChild);
            }
        } else if (requestCode == 0 && data != null) {
            shopId = data.getLongExtra("shopId", 0l);
            shopName = data.getStringExtra("shopName");
            if(TextUtils.isEmpty(shopName)){
                tv_change_store.setText("选择店铺");
            }else{
                tv_range.setText("(全国通用)");
                tv_store_name.setText(shopName);
                tv_store_name.setTextColor(getResources().getColor(R.color.g333333));
                tv_change_store.setText("更换店铺");
            }
        }
    }

    /**
     * 获取可用店铺列表
     */
    private void asyncGetClosestShop() {
        String wholeUrl = AppUrl.host + AppUrl.GET_SHOPS_BY_NAME;
        Long childId = null;
//        if (!CommonMethod.isListEmpty(babyList)) {
//            childId = babyList.get(mChildGvAdapter.getSelectedPos()).getId();
//        }
        String requestBodyData = ParamBuild.searchShop(shopName, childId, 1, 10);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getClosestShopLsner);
    }

    BaseRequestListener getClosestShopLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            parentActivity.finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            shopName = jsonResult.optString("name");
            tv_store_name.setText(shopName);
            shopId = jsonResult.optLong("id");
        }
    };

    /**
     * 查询所有充值类型
     */
    private void asyncGetCardLenListByType(String cardCode) {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALL_RECHARGE_CARD;
        String requestBodyData = "";
        if (selectedChild != null) {
            requestBodyData = ParamBuild.getCardLenListByType(cardCode, selectedChild.getId());
        } else if (child != null) {
            requestBodyData = ParamBuild.getCardLenListByType(cardCode, child.getChildId());
        } else {
            requestBodyData = ParamBuild.getCardLenListByType(cardCode, null);
        }
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getRechargeTypeLsner);
    }

    BaseRequestListener getRechargeTypeLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            parentActivity.finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            memberCardLenList.clear();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                m = gson.fromJson(jot.toString(), MemberCardTypeVO.class);
            }
            memberCardLenList.addAll(m.getCardTypeLengthList());
            isBindChild = m.getIsBindChild();
            if(isBindChild == 1){
                if(!CommonMethod.isListEmpty(((OpenMemberActivity)parentActivity).babyList)){
                    area_baby.setVisibility(View.VISIBLE);
                    if(((OpenMemberActivity)parentActivity).babyList.size()==1){
                        tv_change_baby.setVisibility(View.GONE);
                    }else{
                        tv_change_baby.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                area_baby.setVisibility(View.GONE);
            }
            memberCardAdapter.notifyDataSetChanged();
        }

    };

    @Override
    public void openMember(int pos) {
        if (shopId == 0l) {
            showShortToast("请选择店铺！");
            return;
        }
        if (selectedChild != null) {
            childId = selectedChild.getId();
        } else if (child != null) {
            childId = child.getChildId();
        } else if (childItem != null) {
            childId = childItem.getId();
        }
        asyncStartMember(childId, shopId, memberTypeId,
                memberCardLenList.get(pos).getId(), cardCode);
    }

    /**
     * 开通会员
     *
     * @param cardId
     * @param memberTypeId
     * @param shopId
     * @param childId      充值方式(0:开通;1:续费;2:升级)
     */
    private void asyncStartMember(Long childId, Long shopId, Long memberTypeId,
                                  Long cardId, String cardCode) {
        String wholeUrl = AppUrl.host + AppUrl.START_MEMBER;
        String requestBodyData = ParamBuild.startMember(childId, shopId,
                memberTypeId, cardId, type, cardCode);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, startMemberLsner);
    }

    BaseRequestListener startMemberLsner = new JsonRequestListener() {

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
            Long rechargeOrderId = jsonResult.optLong("data");
            Intent it = new Intent(parentActivity, MemberOrderConfirmActivity.class);
            it.putExtra("rechargeId", rechargeOrderId);
            it.putExtra("isBindChild", isBindChild);
            startActivity(it);
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }


}
