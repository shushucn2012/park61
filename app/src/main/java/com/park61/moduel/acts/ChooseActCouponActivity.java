package com.park61.moduel.acts;

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
import com.park61.common.set.Log;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.adapter.ActCouponListAdapter;
import com.park61.moduel.coupon.CouponHelpAcitivity;
import com.park61.moduel.coupon.bean.CouponUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseActCouponActivity extends BaseActivity {

    // private String amount;// 总金额
    private String couponListJsonStr;// 优惠券列表json
    private List<CouponUser> couponList;
    private ActCouponListAdapter adapter;
    private List<Long> couponUseList;
    private ArrayList<CouponUser> selectList = new ArrayList<CouponUser>();
    private String totalPrice, classCouponPrice;

    private ListView lv_act_coupon;
    private Button btn_confirm;
    private TextView tv_coupon_rule;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_act_choose_coupon);
    }

    @Override
    public void initView() {
        tv_coupon_rule = (TextView) findViewById(R.id.tv_coupon_rule);
        lv_act_coupon = (ListView) findViewById(R.id.lv_act_coupon);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        totalPrice = getIntent().getStringExtra("totalPrice");
        Log.out("totalPrice0======" + totalPrice);
        classCouponPrice = getIntent().getStringExtra("classCouponPrice");

        couponUseList = new ArrayList<Long>();
        String couponUseIds = getIntent().getStringExtra("couponUseIds");
        if (!TextUtils.isEmpty(couponUseIds)) {
            String[] arr = couponUseIds.split(",");
            for (int i = 0; i < arr.length; i++) {
                couponUseList.add(Long.parseLong(arr[i]));
            }
        }

        couponList = new ArrayList<CouponUser>();
        couponListJsonStr = getIntent().getStringExtra("couponListJsonStr");
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult = new JSONObject(couponListJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jay = jsonResult.optJSONArray("list");
        if (jay != null && jay.length() > 0) {
            btn_confirm.setBackgroundColor(getResources().getColor(R.color.com_orange));
            btn_confirm.setEnabled(true);
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CouponUser c = gson.fromJson(jot.toString(), CouponUser.class);
                couponList.add(c);
                if (couponUseList.contains(c.getId())) {//把已选择的优惠券加进已选列表
                    selectList.add(c);
                }
            }
            adapter = new ActCouponListAdapter(mContext, couponList, couponUseList,
                    FU.paseDb(totalPrice), FU.paseDb(classCouponPrice));
            lv_act_coupon.setAdapter(adapter);
        } else {
            btn_confirm.setBackgroundColor(getResources().getColor(R.color.gaaaaaa));
            btn_confirm.setEnabled(false);
        }
    }

    @Override
    public void initListener() {
        tv_coupon_rule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CouponHelpAcitivity.class));
            }
        });
        lv_act_coupon.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CouponUser coupon = couponList.get(position);

                for (int i = 0; i < selectList.size(); i++) {
                    //如果是已选择，把它从已选列表移除，并置灰，返回
                    if (coupon.getId() == selectList.get(i).getId()) {
                        selectList.remove(i);
                        adapter.selectItem(coupon.getId());
                        return;
                    }
                }

                if (coupon.getCoupon().getType() == 3) {//满减券不可叠加不可组合，且只能选一张
                    if (selectList.size() > 0) {
                        showShortToast("该券不能与其他券叠加使用");
                        return;
                    }
                    selectList.add(coupon);
                    adapter.selectItem(coupon.getId());
                } else {
                    for (int i = 0; i < selectList.size(); i++) {//若已选了一张满减券则不能再选其他任何券
                        if (selectList.get(i).getCoupon().getType() == 3) {
                            showShortToast("满减券不能与其他券叠加使用");
                            return;
                        }
                        //如果之前已选了一张不可组合的券 并当前券与它不是同一类型
                        if(selectList.get(i).getCoupon().getCanGroup() == 0
                                && coupon.getCoupon().getType() != selectList.get(i).getCoupon().getType()){
                            showShortToast("该券不能与已选的券组合使用");
                            return;
                        }
                        //如果之前已选了一张不可叠加的券 并当前券与它是同一类型
                        if(selectList.get(i).getCoupon().getCanRepeat() == 0
                                && coupon.getCoupon().getType() == selectList.get(i).getCoupon().getType()){
                            showShortToast("该券不能与已选的券叠加使用");
                            return;
                        }
                    }
                    if (coupon.getCoupon().getCanGroup() == 1) {//可以组合
                        if (coupon.getCoupon().getCanRepeat() == 1) {//可以叠加
                            selectList.add(coupon);
                            adapter.selectItem(coupon.getId());
                        } else {//不可以叠加
                            boolean isCanAdd = true;
                            for (int i = 0; i < selectList.size(); i++) {
                                if (selectList.get(i).getCoupon().getType() == coupon.getCoupon().getType()) {
                                    isCanAdd = false;
                                    break;
                                }
                            }
                            if (isCanAdd) {
                                selectList.add(coupon);
                                adapter.selectItem(coupon.getId());
                            }
                        }
                    } else {//不可以组合
                        if (coupon.getCoupon().getCanRepeat() == 1) {//可以叠加
                            for (int i = 0; i < selectList.size(); i++) {
                                if (selectList.get(i).getCoupon().getType() != coupon.getCoupon().getType()) {
                                    showShortToast("该券不能与其他类型券组合使用");
                                    return;
                                }
                            }
                            selectList.add(coupon);
                            adapter.selectItem(coupon.getId());
                        } else {//不可以叠加
                            if (selectList.size() > 0) {
                                showShortToast("该券不能与其他券一起使用");
                                return;
                            }
                            selectList.add(coupon);
                            adapter.selectItem(coupon.getId());
                        }
                    }
                }
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (adapter == null)
                    return;
                Intent data = new Intent();
                data.putExtra("couponUseIds", adapter.getSelectItems());
                //data.putExtra("leftAmout", adapter.getLeftAmount());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
