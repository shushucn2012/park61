package com.park61.moduel.coupon;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.coupon.adapter.SelectCouponAdapter;
import com.park61.moduel.coupon.bean.CouponUser;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsSelectCouponActivity extends BaseActivity {
    public static boolean needRefresh = false;
    private SelectCouponAdapter adapter;
    private ArrayList<CouponUser> mlist;
    private ListView lv_coupon;
    private Long userId;
    private double amount;
    private double couponAmount;
    private String sCouponUseId="";// 已选中优惠券的id
    private TextView tv_help;
    private Button btn_confirm;
    private ArrayList<CouponUser> selectList = new ArrayList<CouponUser>();
    private List<Long> couponUseList;
    private String couponUseIds;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_select_coupon);

    }

    @Override
    public void initView() {
        tv_help = (TextView) findViewById(R.id.tv_help);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        lv_coupon = (ListView) findViewById(R.id.lv_coupon);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh)
            asyncAccountCouponList();
    }

    @Override
    public void initData() {
        couponUseList = new ArrayList<Long>();
        userId = GlobalParam.currentUser.getId();
        amount = getIntent().getDoubleExtra("amount", amount);
        couponUseIds = getIntent().getStringExtra("couponUseIds");
        logout("======couponUseIds========"+couponUseIds);
        if (!TextUtils.isEmpty(couponUseIds)) {
            String[] arr = couponUseIds.split(",");
            for (int i = 0; i < arr.length; i++) {
                couponUseList.add(Long.parseLong(arr[i]));
            }
        }
        mlist = new ArrayList<CouponUser>();
        adapter = new SelectCouponAdapter( mContext, mlist);
        lv_coupon.setAdapter(adapter);
        asyncAccountCouponList();

    }

    @Override
    public void initListener() {
        lv_coupon.setOnItemClickListener(new OnItemClickListener() {

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

        tv_help.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CouponHelpAcitivity.class));

            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                couponUseIds = "";
                for(int i=0;i<selectList.size();i++){
                    if(selectList.get(i).isChosen()){
                        couponUseIds += selectList.get(i).getId()+",";
                    }
                }
                if(couponUseIds.length()>0&&couponUseIds.endsWith(",")){
                    couponUseIds = couponUseIds.substring(0,couponUseIds.length()-1);
                }
                if(TextUtils.isEmpty(couponUseIds)){
                    showShortToast("请选择优惠券");
                }else{
                    asyncSaveCoupon();
                }
            }
        });

    }

    /**
     * 保存优惠券到结算页面
     */
    private void asyncSaveCoupon() {
//        String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_SAVERE;
//        String requestBodyData = ParamBuild.getSelectCoupon(Long
//                .parseLong(sCouponUseId));
        String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_SAVERE_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("couponUseIds", couponUseIds);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                saveListener);
    }

    BaseRequestListener saveListener = new JsonRequestListener() {

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
            couponAmount = FU.paseDb(jsonResult.optString("data"));
            logout("=========couponAmount=========="+couponAmount);
            Intent returnData = new Intent();
            returnData.putExtra("couponUseId", couponUseIds);
            logout("======couponUseIds222222222222222222========"+couponUseIds);
            setResult(RESULT_OK, returnData);
            finish();
        }
    };


    /**
     * 订单结算可用优惠券列表
     */
    private void asyncAccountCouponList() {
//        String wholeUrl = AppUrl.host + AppUrl.ACCOUNT_CONPON_LIST;
//        String requestBodyData = ParamBuild.getAccountCoupon(userId, amount);
        String wholeUrl = AppUrl.host + AppUrl.ORDER_COUNPON_LIST_V2;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
        listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
                ViewInitTool.setListEmptyView(mContext, lv_coupon,
                        "呜呜！没有可使用的优惠券！", R.drawable.quexing,
                        null, 100, 95);
            }
            adapter.notifyDataSetChanged();
        }
    };
}
