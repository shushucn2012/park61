package com.park61.moduel.acts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.ActCouponSelectChildCache;
import com.park61.moduel.acts.course.CourseListInOrderActivity;
import com.park61.moduel.address.UpdateSelfTakeInfoActivity;
import com.park61.moduel.me.ApplyBabyActivity;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.ChooseRecommendActivity;
import com.park61.moduel.pay.PayAllSuccessActivity;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActOrderConfirmActivity extends BaseActivity {

    private long applyId;// 订单id
    private ApplyActItem curApply;
    private String rname, rphone;// 联系人姓名电话
    private String couponUseIds;
    private String couponListJsonStr;
    private double finalMoney;
    private ArrayList<BabyItem> childList;// 获取选择的孩子列表
    // 推荐码
    private String recommendCode;

    private ImageView img_act, img_downarrow_child, img_leftarrow_session;
    private TextView tv_act_title, tv_act_addr, tv_goods_price, tv_adult_num,
            tv_no_contactor, tv_rname, tv_rphone, tv_total_price, tv_session_label,
            tv_final_money, tv_total_money, tv_canuse_coupon_count, tv_child_name,
            tv_coupon_reduce_money, tv_coupon_money, tv_session, tv_invitecode;
    private View area_input_contactor, area_coupon_choose, area_baby, area_adult_num, area_session,
            area_invitecode_choose;
    private Button btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_act_orderconfirm);
    }

    @Override
    public void initView() {
        img_act = (ImageView) findViewById(R.id.img_act);
        tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        tv_act_addr = (TextView) findViewById(R.id.tv_act_addr);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        area_adult_num = findViewById(R.id.area_adult_num);
        area_input_contactor = findViewById(R.id.area_input_contactor);
        tv_no_contactor = (TextView) findViewById(R.id.tv_no_contactor);
        tv_rname = (TextView) findViewById(R.id.tv_rname);
        tv_rphone = (TextView) findViewById(R.id.tv_rphone);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        area_coupon_choose = findViewById(R.id.area_coupon_choose);
        tv_canuse_coupon_count = (TextView) findViewById(R.id.tv_canuse_coupon_count);
        tv_coupon_reduce_money = (TextView) findViewById(R.id.tv_coupon_reduce_money);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_session = (TextView) findViewById(R.id.tv_session);
        tv_child_name = (TextView) findViewById(R.id.tv_child_name);
        img_downarrow_child = (ImageView) findViewById(R.id.img_downarrow_child);
        area_baby = findViewById(R.id.area_baby);
        tv_adult_num = (TextView) findViewById(R.id.tv_adult_num);
        tv_session_label = (TextView) findViewById(R.id.tv_session_label);
        img_leftarrow_session = (ImageView) findViewById(R.id.img_leftarrow_session);
        area_session = findViewById(R.id.area_session);
        area_invitecode_choose = findViewById(R.id.area_invitecode_choose);
        tv_invitecode = (TextView) findViewById(R.id.tv_invitecode);
    }

    @Override
    public void initData() {
        if (GlobalParam.currentUser != null
                && !TextUtils.isEmpty(GlobalParam.currentUser.getMobile())) {
            if (GlobalParam.currentUser.getName() == null) {
                rname = "";
            } else {
                rname = GlobalParam.currentUser.getName();
            }
            rphone = GlobalParam.currentUser.getMobile();
            tv_no_contactor.setVisibility(View.GONE);
            tv_rname.setVisibility(View.VISIBLE);
            tv_rphone.setVisibility(View.VISIBLE);
            tv_rname.setText("联系人：" + rname);
            tv_rphone.setText("电话：" + rphone);
        }
        childList = (ArrayList<BabyItem>) getIntent().getSerializableExtra("childList");
        applyId = getIntent().getLongExtra("applyId", -1);
        asyncGetApplyInfo();
    }

    @Override
    public void initListener() {
        area_input_contactor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, UpdateSelfTakeInfoActivity.class);
                intent.putExtra("rname", rname);
                intent.putExtra("rphone", rphone);
                startActivityForResult(intent, 0);
            }
        });
        btn_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(rphone) || TextUtils.isEmpty(rname)) {
                    showShortToast("联系人姓名和电话必须填写");
                    return;
                }
                asyncUpdateActApplys();
            }
        });
        area_invitecode_choose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncGetShopInviteCode();
            }
        });
    }

    /**
     * 获取订单详情
     */
    protected void asyncGetApplyInfo() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ACT_ORDER_DETAILS;
        String requestBodyData = ParamBuild.getApplyInfo(applyId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getApplyInfolistener);
    }

    BaseRequestListener getApplyInfolistener = new JsonRequestListener() {

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
            curApply = gson.fromJson(jsonResult.toString(), ApplyActItem.class);
            fillData();
            asyncGetActCoupons();
        }
    };

    /**
     * 填充数据
     */
    public void fillData() {
//        ImageLoader.getInstance().displayImage(curApply.getActCover(), img_act, options);
        ImageManager.getInstance().displayImg(img_act, curApply.getActCover());
        tv_act_title.setText(curApply.getActTitle());
        tv_act_addr.setText(curApply.getActAddress());
        String dateStr = DateTool.L2SByDay2(curApply.getActStartTime()) + " - "
                + DateTool.L2SByDay2(curApply.getActEndTime());
        tv_session.setText(dateStr);
        tv_goods_price.setText("￥" + FU.strDbFmt(curApply.getApplyPrice()));
        tv_total_price.setText("￥" + FU.strDbFmt(curApply.getTotalPrice()));
        tv_final_money.setText("￥" + FU.strDbFmt(curApply.getTotalPrice()));
        tv_total_money.setText("￥" + FU.strDbFmt(curApply.getTotalPrice()));
        finalMoney = curApply.getTotalPrice();

        //显示小孩名称
        if (!CommonMethod.isListEmpty(curApply.getActivityApplyDetailsList())) {
            if (curApply.getActivityApplyDetailsList().size() == 1) {
                tv_child_name.setText(curApply.getActivityApplyDetailsList().get(0).getVisitorName());
            } else if (curApply.getActivityApplyDetailsList().size() == 2) {
                tv_child_name.setText(curApply.getActivityApplyDetailsList().get(0).getVisitorName() + " "
                        + curApply.getActivityApplyDetailsList().get(1).getVisitorName());
            } else {
                tv_child_name.setText(curApply.getActivityApplyDetailsList().get(0).getVisitorName() + " "
                        + curApply.getActivityApplyDetailsList().get(1).getVisitorName() + "...");
                img_downarrow_child.setVisibility(View.VISIBLE);
            }
            area_baby.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, ApplyBabyActivity.class);
                    it.putExtra("mList", curApply.getActivityApplyDetailsList());
                    startActivity(it);
                }
            });
        }
        //显示成人数量
        tv_adult_num.setText(curApply.getApplyAdultsNumber() + "位成人");
        //不显示成人人数，小课时场次变成课程安排，并可以查看场次
        if (GlobalParam.SMALL_CLASS_CODE.equals(curApply.getActBussinessType())) {
            tv_session_label.setText("课程安排");
            img_leftarrow_session.setVisibility(View.VISIBLE);
            area_session.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseListInOrderActivity.class);
                    it.putExtra("applyId", applyId);
                    startActivity(it);
                }
            });
        }
    }

    /**
     * 获取游戏可用优惠券列表
     */
    protected void asyncGetActCoupons() {
        String wholeUrl = "";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", GlobalParam.currentUser.getId());
        map.put("amount", curApply.getTotalPrice());
        if (GlobalParam.SMALL_CLASS_CODE.equals(curApply.getActBussinessType())) {
            wholeUrl = AppUrl.host + AppUrl.GET_CANUSE_CLASS_COUPONLIST;
            map.put("classSeries", curApply.getActClassSeries());
        } else {
            wholeUrl = AppUrl.host + AppUrl.GET_ACT_CONPON_LIST;
            map.put("activityType", curApply.getActBussinessType());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getActCouponsLsner);
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
            couponListJsonStr = jsonResult.toString();
            JSONArray jay = jsonResult.optJSONArray("list");
            int canUseCouponCount = jay.length();
            if (ActCouponSelectChildCache.canUseCouponAct.get(Long.parseLong(curApply.getActId()))) {
                tv_canuse_coupon_count.setText("优惠券 " + canUseCouponCount + "张可用");
                area_coupon_choose.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(mContext, ChooseActCouponActivity.class);
                        intent.putExtra("couponListJsonStr", couponListJsonStr);
                        intent.putExtra("couponUseIds", couponUseIds);
                        intent.putExtra("totalPrice", FU.strDbFmt(curApply.getTotalPrice()));
                        intent.putExtra("classCouponPrice", FU.strDbFmt(FU.paseDb(curApply.getApplyPrice()) / curApply.getActClassCount()));
                        startActivityForResult(intent, 1);
                    }
                });
            } else {
                tv_canuse_coupon_count.setText("优惠券");
                tv_coupon_reduce_money.setText("不可用");
            }
        }
    };

    /**
     * 游戏优惠券核销
     */
    protected void asyncUpdateActApplys() {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_ACT_APPLYS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("userTel", rphone);
        map.put("userName", rname);
        map.put("total_fee", finalMoney);
        if (!TextUtils.isEmpty(couponUseIds)) {
            map.put("couponUseId", couponUseIds);
        }
        map.put("recommendCode", recommendCode);
        map.put("uuid", UUID.randomUUID().toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, updateActApplysLsner);
    }

    BaseRequestListener updateActApplysLsner = new JsonRequestListener() {

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
            int isPay = jsonResult.optInt("isPay");
            Long orderId = jsonResult.optLong("orderId");
            if (isPay == 1) {// 不需要付款
                Intent paySuccessIt = new Intent(mContext, PayAllSuccessActivity.class);
                paySuccessIt.putExtra("applyId", orderId);
                startActivity(paySuccessIt);
                finish();
            } else if (isPay == 0) {
                showShortToast("订单提交成功，请继续支付！");
                Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
                it.putExtra("orderId", orderId);
                startActivity(it);
                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            rname = data.getStringExtra("rname");
            rphone = data.getStringExtra("rphone");
            tv_no_contactor.setVisibility(View.GONE);
            tv_rname.setVisibility(View.VISIBLE);
            tv_rphone.setVisibility(View.VISIBLE);
            tv_rname.setText("联系人：" + rname);
            tv_rphone.setText("电话：" + rphone);
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            couponUseIds = data.getStringExtra("couponUseIds");
            if (!TextUtils.isEmpty(couponUseIds)) {
                asyncCountTotal();
            } else {// 没有选择优惠券时
                tv_coupon_reduce_money.setText("-￥0.00");
                tv_coupon_money.setText("-￥0.00");
                finalMoney = curApply.getTotalPrice();
                tv_final_money.setText("￥" + FU.strDbFmt(finalMoney));
                tv_total_money.setText("￥" + FU.strDbFmt(finalMoney));
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            if (!TextUtils.isEmpty(data.getStringExtra("RETURN_DATA2"))) {
                recommendCode = data.getStringExtra("RETURN_DATA2");
                tv_invitecode.setText(data.getStringExtra("RETURN_DATA") + "  " + data.getStringExtra("RETURN_DATA2"));
            }
            if (!TextUtils.isEmpty(data.getStringExtra("recommendCode"))) {
                recommendCode = data.getStringExtra("recommendCode");
                tv_invitecode.setText(data.getStringExtra("recommendCode"));
            }
        }
    }

   /* private void asyncCountTotal(double leftAmout) {
        finalMoney = leftAmout;
        double couponReduceMoney = FU.sub(curApply.getTotalPrice(), finalMoney);
        tv_coupon_reduce_money.setText("-￥" + FU.strDbFmt(couponReduceMoney));
        tv_coupon_money.setText("-￥" + FU.strDbFmt(couponReduceMoney));
        tv_final_money.setText("￥" + FU.strDbFmt(finalMoney));
        tv_total_money.setText("￥" + FU.strDbFmt(finalMoney));
    }*/

    /**
     * 获取店铺店员推荐码列表
     */
    protected void asyncGetShopInviteCode() {
        String wholeUrl = AppUrl.host + AppUrl.GET_INVITECODES_BYSHOP;
        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("applyId", applyId);
        map.put("merchantId", FU.paseLong(curApply.getBackendShopId()));
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getInviteCodesLsner);
    }

    BaseRequestListener getInviteCodesLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
//            Intent it = new Intent(mContext, MySimpleChoosePage.class);
            Intent it = new Intent(mContext, ChooseRecommendActivity.class);
            it.putExtra("PAGE_TITLE", "选择推荐码");
            ArrayList<String> slist = new ArrayList<String>();
            ArrayList<String> slist2 = new ArrayList<String>();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject item = jay.optJSONObject(i);
                slist.add(item.optString("name"));
                slist2.add(item.optString("recommenderCode"));
            }
            it.putStringArrayListExtra("LIST_DATA", slist);
            it.putStringArrayListExtra("LIST_DATA2", slist2);
            recommendCode = tv_invitecode.getText().toString();
            it.putExtra("CHOSEN_ITEM", tv_invitecode.getText().toString());
            startActivityForResult(it, 2);
        }
    };

    /**
     * 计算核销金额
     */
    private void asyncCountTotal() {
		String wholeUrl = AppUrl.host + AppUrl.ACT_COUPON_COUNT_TOTAL;
		Map<String, Object> map = new HashMap<String, Object>();
		if (!TextUtils.isEmpty(couponUseIds)) {
			map.put("couponUseIds", couponUseIds);
		}
		String requestBodyData = ParamBuild.buildParams(map);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, countTotalLsner);
	}

	BaseRequestListener countTotalLsner = new JsonRequestListener() {

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
			finalMoney = jsonResult.optDouble("totalPrice");
			double couponReduceMoney = jsonResult.optDouble("amount");
			tv_coupon_reduce_money.setText("-￥" + FU.strDbFmt(couponReduceMoney));
			tv_coupon_money.setText("-￥" + FU.strDbFmt(couponReduceMoney));
			tv_final_money.setText("￥" + FU.strDbFmt(finalMoney));
			tv_total_money.setText("￥" + FU.strDbFmt(finalMoney));
		}
	};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActCouponSelectChildCache.selectChilds.clear();
    }
}
