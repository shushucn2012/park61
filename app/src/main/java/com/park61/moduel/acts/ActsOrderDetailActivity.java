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
import com.park61.moduel.acts.course.CourseListInOrderActivity;
import com.park61.moduel.me.ApplyBabyActivity;
import com.park61.moduel.me.GameEvaluateActivity;
import com.park61.moduel.me.actlist.ActOrderListActivity;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.TimeTextView;
import com.park61.widget.textview.TimeTextView.OnTimeEnd;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ActsOrderDetailActivity extends BaseActivity implements OnTimeEnd, OnClickListener {
    private ApplyActItem curApply;
    private Long curApplyId, orderId;
    private String orderState = "";// 报名订单状态
    private String actBussinessType;//活动类别

    private ImageView img_act, img_downarrow_child, img_leftarrow_session, img_actorder_state;
    private Button btn_share;// 分享
    private Button btn_comt;// 评论
    private Button btn_cancel_apply;// 取消报名
    private Button btn_pay;// 继续支付
    private Button btn_evaluate;// 评价
    private TextView tv_countdown_day;
    private View act_info_area;// 游戏信息区域
    private View left_day_area, area_adult_num, area_session;
    private View left_time_area, area_baby;
    private TextView tv_show_tip;
    private TimeTextView tv_left_time;
    private TextView tv_act_totalmoney, tv_coupon_money, tv_total_money,
            tv_receiver_name, tv_receiver_phone, tv_act_title, tv_act_addr, tv_session_label,
            tv_act_price, tv_child_name, tv_adult_num, tv_order_number, tv_order_time, tv_session;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_act_orderdetail);
    }

    @Override
    public void initView() {
        img_act = (ImageView) findViewById(R.id.img_act);
        tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        tv_act_addr = (TextView) findViewById(R.id.tv_act_addr);
        tv_act_totalmoney = (TextView) findViewById(R.id.tv_act_totalmoney);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_receiver_name = (TextView) findViewById(R.id.tv_receiver_name);
        tv_receiver_phone = (TextView) findViewById(R.id.tv_receiver_phone);
        tv_act_price = (TextView) findViewById(R.id.tv_act_price);
        tv_child_name = (TextView) findViewById(R.id.tv_child_name);
        tv_adult_num = (TextView) findViewById(R.id.tv_adult_num);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_left_time = (TimeTextView) findViewById(R.id.tv_left_time);
        left_time_area = findViewById(R.id.left_time_area);
        tv_show_tip = (TextView) findViewById(R.id.tv_show_tip);
        left_time_area.setVisibility(View.GONE);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_comt = (Button) findViewById(R.id.btn_comt);
        btn_cancel_apply = (Button) findViewById(R.id.btn_cancel_apply);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        act_info_area = findViewById(R.id.act_info_area);
        left_day_area = findViewById(R.id.left_day_area);
        tv_countdown_day = (TextView) findViewById(R.id.tv_countdown_day);
        tv_session = (TextView) findViewById(R.id.tv_session);
        img_downarrow_child = (ImageView) findViewById(R.id.img_downarrow_child);
        area_baby = findViewById(R.id.area_baby);
        area_adult_num = findViewById(R.id.area_adult_num);
        tv_session_label = (TextView) findViewById(R.id.tv_session_label);
        img_leftarrow_session = (ImageView) findViewById(R.id.img_leftarrow_session);
        area_session = findViewById(R.id.area_session);
        img_actorder_state = (ImageView) findViewById(R.id.img_actorder_state);
    }

    @Override
    public void initData() {
        orderId = getIntent().getLongExtra("orderId", -1l);
        curApplyId = getIntent().getLongExtra("applyId", -1l);
        orderState = getIntent().getStringExtra("orderState");
        actBussinessType = getIntent().getStringExtra("actBussinessType");
        if (orderState.equals("waitforpay")) {
            tv_show_tip.setText("等待买家付款");
            btn_cancel_apply.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.VISIBLE);
            img_actorder_state.setImageResource(R.drawable.dengdaifukuan);
        } else if (orderState.equals("applied")) {
            tv_show_tip.setVisibility(View.GONE);
            left_day_area.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.VISIBLE);
            btn_cancel_apply.setVisibility(View.VISIBLE);
        } else if (orderState.equals("waitforcomt")) {
            img_actorder_state.setImageResource(R.drawable.daicanjia);
            tv_show_tip.setText("游戏已参加");
            btn_share.setVisibility(View.VISIBLE);
            btn_evaluate.setVisibility(View.VISIBLE);
            img_actorder_state.setImageResource(R.drawable.daipingjia);
        } else if (orderState.equals("done")) {
            tv_show_tip.setText("游戏已参加");
            btn_share.setVisibility(View.VISIBLE);
            img_actorder_state.setImageResource(R.drawable.yicanjia);
        }
        asyncGetApplyInfo();
    }

    public void fillData() {
//        if (orderState.equals("done") && "0".equals(curApply.getIsEvaluate())) {
//            btn_evaluate.setVisibility(View.VISIBLE);
//        } else if (orderState.equals("done") && "1".equals(curApply.getIsEvaluate())) {
//            btn_evaluate.setVisibility(View.GONE);
//        }

        if (curApply.getIsPayment() == 0) {
            initTimes();
        }

        int leftDay = 0;
        try {
            leftDay = DateTool.daysBetween(new Date(), new Date(Long.parseLong(curApply.getActStartTime())));
            if (leftDay < 0 && curApply.getIsPayment() != 0) {
                tv_show_tip.setText("游戏已参加");
                tv_show_tip.setVisibility(View.VISIBLE);
                left_day_area.setVisibility(View.GONE);
                btn_share.setVisibility(View.VISIBLE);
                btn_cancel_apply.setVisibility(View.GONE);
                if ( "0".equals(curApply.getIsEvaluate())) {
                    btn_evaluate.setVisibility(View.VISIBLE);
                } else if ("1".equals(curApply.getIsEvaluate())) {
                    btn_evaluate.setVisibility(View.GONE);
                }
                leftDay = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_countdown_day.setText(leftDay + "");
        ImageManager.getInstance().displayImg(img_act, curApply.getActCover());
        tv_act_title.setText(curApply.getActTitle());
        tv_act_addr.setText("地址:" + curApply.getActAddress());

        String dateStr = DateTool.L2SByDay2(curApply.getActStartTime()) + " - "
                + DateTool.L2SByDay2(curApply.getActEndTime());
        tv_session.setText(dateStr);

        tv_act_price.setText(FU.isNumEmpty(curApply.getApplyPrice()) ? "免费" : "￥" + FU.strDbFmt(curApply.getApplyPrice()));
        tv_act_totalmoney.setText("￥" + FU.strDbFmt(curApply.getPreTotalPrice()));
        tv_coupon_money.setText("-￥" + FU.strDbFmt(Math.abs(curApply.getAmount())));
        tv_total_money.setText("￥" + FU.strDbFmt(curApply.getTotalPrice()));
        tv_order_time.setText("下单时间：" + DateTool.L2S(curApply.getRegTime()));
        tv_order_number.setText("订单编号：" + (curApply.getOrderId() == null ? curApply.getId() : curApply.getOrderId()));
        tv_receiver_phone.setText(curApply.getUserTel());
        tv_receiver_name.setText("联系人：" + (curApply.getUserName() == null ? "" : curApply.getUserName()));

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
        if (GlobalParam.SMALL_CLASS_CODE.equals(actBussinessType)) {
            tv_session_label.setText("课程安排");
            img_leftarrow_session.setVisibility(View.VISIBLE);
            area_session.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseListInOrderActivity.class);
                    it.putExtra("applyId", curApplyId);
                    it.putExtra("orderId", orderId);
                    startActivity(it);
                }
            });
        }
    }

    private void initTimes() {
        tv_show_tip.setVisibility(View.VISIBLE);
        left_time_area.setVisibility(View.VISIBLE);

        Calendar cEnd = Calendar.getInstance();
        cEnd.setTimeInMillis(Long.parseLong(curApply.getRegTime()));
        long endMillis = cEnd.getTimeInMillis() + 45 * 60 * 1000;
        if(!TextUtils.isEmpty(curApply.getTimeExpireDate())){
            endMillis = Long.parseLong(curApply.getTimeExpireDate());
        }

        Calendar cNow = Calendar.getInstance();
        long nowMillis = cNow.getTimeInMillis();
        if (endMillis < nowMillis) {
            tv_show_tip.setText("支付已超时");
            left_time_area.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            /*if (curApply.getOrderId() == null) {
                asyncCancelApplyActs(curApply.getId());
            } else {
                asyncCancelOrder(curApply.getOrderId());
            }*/
            return;
        }

        long betweenMin = (endMillis - nowMillis) / (60 * 1000);
        long betweenSec = ((endMillis - nowMillis) / 1000) % 60;

        tv_left_time.setTimes(new long[]{0, 0, betweenMin, betweenSec});
        if (!tv_left_time.isRun()) {
            tv_left_time.run();
        }
    }

    @Override
    public void initListener() {
        tv_left_time.setOnTimeEndLsner(this);
        btn_share.setOnClickListener(this);
        btn_comt.setOnClickListener(this);
        btn_cancel_apply.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        act_info_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (curApply == null)
                    return;
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("id", Long.parseLong(curApply.getActId()));
                mContext.startActivity(it);
            }
        });
    }

    /**
     * 获取报名游戏订单详情
     */
    protected void asyncGetApplyInfo() {
        String wholeUrl = "";
        String requestBodyData = "";
        if (orderId == null || orderId == -1l) {
            wholeUrl = AppUrl.host + AppUrl.GET_ACT_APPLY_DETAIL;
            requestBodyData = ParamBuild.getApplyInfo(curApplyId);
        } else {
            wholeUrl = AppUrl.host + AppUrl.GET_ACT_ORDER_DETAIL;
            requestBodyData = ParamBuild.getOrderDetail(orderId);
        }
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
        }
    };

    /**
     * 请求取消已报名的游戏
     */
    private void asyncCancelApplyActs(Long applyId) {
        String wholeUrl = AppUrl.host + AppUrl.CANCEL_APPLY_ACTS_END;
        String requestBodyData = ParamBuild.cancelApply(applyId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, cancellistener);
    }

    BaseRequestListener cancellistener = new JsonRequestListener() {

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
            showShortToast("取消订单成功");
            ActOrderListActivity.needRefresh = true;
            finish();
        }
    };

    /**
     * 取消商品订单
     */
    private void asyncCancelOrder(Long orderId) {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_CANCEL;
        String requestBodyData = ParamBuild.getOrderDetail(orderId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, canceOrderListner);
    }

    BaseRequestListener canceOrderListner = new JsonRequestListener() {

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
            showShortToast("取消订单成功");
            ActOrderListActivity.needRefresh = true;
            finish();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                String shareUrl = String.format(AppUrl.ACT_SHARE_URL, curApply.getActId());
                String picUrl = curApply.getActCover();
                String title = getString(R.string.app_name);
                String description = curApply.getActTitle() + "\n 游戏详情";
                showShareDialog(shareUrl, picUrl, title, description);
                break;
            case R.id.btn_comt:
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("id", Long.parseLong(curApply.getActId()));
                it.putExtra("isToComt", true);
                startActivity(it);
                break;
            case R.id.btn_cancel_apply:
                dDialog.showDialog("提醒", "确认取消游戏报名吗？", "确认", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (curApply.getOrderId() == null) {
                                    asyncCancelApplyActs(curApply.getId());
                                } else {
                                    asyncCancelOrder(curApply.getOrderId());
                                }
                                dDialog.dismissDialog();
                            }
                        }, null);
                break;
            case R.id.btn_pay:
                if (curApply.getOrderId() == null) {
                    Intent payActIt = new Intent(mContext, PayConfirmActivity.class);
                    payActIt.putExtra("applyId", curApply.getId());
                    startActivity(payActIt);
                } else {
                    Intent payIt = new Intent(mContext, PayGoodsConfirmActivity.class);
                    payIt.putExtra("orderId", curApply.getOrderId());
                    startActivity(payIt);
                }
                break;
            case R.id.btn_evaluate:
//                Intent evaIt = new Intent(mContext, ActsEvaluateActivity.class);
                Intent evaIt = new Intent(mContext, GameEvaluateActivity.class);
                evaIt.putExtra("actCover", curApply.getActCover());
                evaIt.putExtra("applyId", curApply.getId());
                startActivity(evaIt);
                break;
        }
    }

    @Override
    public void onEnd() {
        tv_show_tip.setText("支付超时，订单已自动取消，请重新下单!");
        left_time_area.setVisibility(View.GONE);
        if (curApply.getOrderId() == null) {
            asyncCancelApplyActs(curApply.getId());
        } else {
            asyncCancelOrder(curApply.getOrderId());
        }
    }

}
