package com.park61.moduel.openmember;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.coupon.CouponHelpAcitivity;
import com.park61.moduel.coupon.bean.CouponUser;
import com.park61.moduel.openmember.adapter.MemberCardCouponListAdapter;
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
 * 会员选择优惠券列表界面
 */
public class MemberChooseCouponActivity extends BaseActivity {
    private ArrayList<CouponUser> mlist;
    private MemberCardCouponListAdapter adapter;
    private String couponUseIds;

    private ListView lv_act_coupon;
    private Button btn_confirm;
    private TextView tv_coupon_rule;
    private long childId;//宝宝id
    private double amount;//金额
    private long cardId;//卡id
    private ArrayList<CouponUser> selectList = new ArrayList<CouponUser>();//选中优惠券集合
    private List<Long> couponUseList;//前一次选中优惠券id集合
    private long rechargeId;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_member_choose_coupon);
    }

    @Override
    public void initView() {
        tv_coupon_rule = (TextView) findViewById(R.id.tv_coupon_rule);
        lv_act_coupon = (ListView) findViewById(R.id.lv_act_coupon);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        rechargeId = getIntent().getLongExtra("rechargeId",0l);
        logout("=========会员卡优惠券======="+rechargeId);
        couponUseIds = getIntent().getStringExtra("couponUseId");
        childId = getIntent().getLongExtra("childId",0l);
        amount = getIntent().getDoubleExtra("amount",amount);
        cardId = getIntent().getLongExtra("cardId",0l);
        logout("=========会员卡优惠券==cardId====="+cardId);
        couponUseList = new ArrayList<Long>();
        logout("======couponUseId========"+couponUseIds);
        if (!TextUtils.isEmpty(couponUseIds)) {
            String[] arr = couponUseIds.split(",");
            for (int i = 0; i < arr.length; i++) {
                couponUseList.add(Long.parseLong(arr[i]));
            }
        }
        mlist = new ArrayList<CouponUser>();
        adapter = new MemberCardCouponListAdapter(mContext, mlist);
        lv_act_coupon.setAdapter(adapter);
        asyncGetActCoupons();
    }

    @Override
    public void initListener() {
        tv_coupon_rule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CouponHelpAcitivity.class));
            }
        });
        lv_act_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CouponUser coupon = mlist.get(position);
                if(coupon.isChosen()){
                    coupon.setChosen(false);
                    for(int i=0;i<selectList.size();i++){
                        if(coupon.getId()==selectList.get(i).getId()){
                            selectList.remove(i) ;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    return;
                }

                if(coupon.getCoupon().getType()==3){//满减券不可叠加不可组合，且只能选一张
                    if(selectList.size()>0){
                        showShortToast("该券不能与其他券叠加使用");
                        return;
                    }
                    selectList.add(coupon);
                    coupon.setChosen(true);
                }else{
                    for(int i=0;i<selectList.size();i++){//若已选了一张满减券则不能再选其他任何券
                        if(selectList.get(i).getCoupon().getType()==3){
                            showShortToast("满减券不能与其他券叠加使用");
                            return;
                        }
                    }

                    if(coupon.getCoupon().getCanGroup()==1){//可以组合
                        if(coupon.getCoupon().getCanRepeat()==1){//可以叠加
                            selectList.add(coupon);
                            coupon.setChosen(true);
                        }else{//不可以叠加
                            boolean isCanAdd = true;
                            for(int i=0;i<selectList.size();i++){
                                if(selectList.get(i).getCoupon().getType()==coupon.getCoupon().getType()){
                                    isCanAdd = false;
                                    break;
                                }
                            }
                            if(isCanAdd){
                                selectList.add(coupon);
                                coupon.setChosen(true);
                            }
                        }
                    }else{//不可以组合
                        if(coupon.getCoupon().getCanRepeat()==1){//可以叠加
                            for(int i=0;i<selectList.size();i++){
                                if(selectList.get(i).getCoupon().getType()!=coupon.getCoupon().getType()){
                                    showShortToast("该券不能与其他类型券组合使用");
                                    return;
                                }
                            }
                            selectList.add(coupon);
                            coupon.setChosen(true);
                        }else{//不可以叠加
                            if(selectList.size()>0){
                                showShortToast("该券不能与其他券一起使用");
                                return;
                            }
                            selectList.add(coupon);
                            coupon.setChosen(true);
                        }
                    }

                }
                adapter.notifyDataSetChanged();

            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                couponUseIds = "";
                for(int i=0;i<selectList.size();i++){
                    if(selectList.get(i).isChosen()){
                        couponUseIds += selectList.get(i).getId()+",";
                    }
                }
                if(couponUseIds.length()>0&&couponUseIds.endsWith(",")){
                    couponUseIds =  couponUseIds.substring(0,couponUseIds.length()-1);
                }
                logout("======id========"+couponUseIds);
                if(TextUtils.isEmpty(couponUseIds)){
                    showShortToast("请选择优惠券");
                }else{
                    asyncGetMemberOrder();
                }
            }
        });
    }
    /**
     * 获取可用优惠券列表
     */
    protected void asyncGetActCoupons() {
        String wholeUrl = AppUrl.host + AppUrl.GET_VIP_CONPON_LIST_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("amount", amount);
        map.put("cardType", cardId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getActCouponsLsner);
    }

    BaseRequestListener getActCouponsLsner = new JsonRequestListener() {

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
            ArrayList<CouponUser> currentPageList = new ArrayList<CouponUser>();
            mlist.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject couponJot = actJay.optJSONObject(i);
                CouponUser g = gson.fromJson(couponJot.toString(),
                        CouponUser.class);
                mlist.add(g);
            }
            mlist.addAll(currentPageList);
            logout("========优惠券总数======"+mlist.size());
            for(int i =0;i<mlist.size();i++){
                if(couponUseList.contains(mlist.get(i).getId())){
                    mlist.get(i).setChosen(true);
                    selectList.add(mlist.get(i));
                }
            }
            if(mlist.size()<1){
                ViewInitTool.setListEmptyView(mContext, lv_act_coupon,
                        "呜呜！没有可使用的优惠券！", R.drawable.quexing,
                        null, 100, 95);
            }
            adapter.notifyDataSetChanged();
        }
    };


    /**
     * 获取订单详情
     */
    private void asyncGetMemberOrder() {
//        String wholeUrl = AppUrl.host + AppUrl.GET_MEMBER_ORDER;
        String wholeUrl = AppUrl.host + AppUrl.GET_SAVE_COUPON_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rechargeId", rechargeId);
        if (!TextUtils.isEmpty(couponUseIds)) {
            map.put("couponUseIds", couponUseIds);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getMemberOrderLsner);
    }

    BaseRequestListener getMemberOrderLsner = new JsonRequestListener() {

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
//            MemberConfirmOrderVO info = new Gson().fromJson(jsonResult.toString(), MemberConfirmOrderVO.class);
//            double useCouponPrice = FU.paseDb(info.getUseCouponPrice()+"");
            Intent returnData = new Intent();
            returnData.putExtra("couponUseId", couponUseIds);
//            returnData.putExtra("useCouponPrice", useCouponPrice);
            setResult(RESULT_OK, returnData);
            finish();
        }
    };
}
