package com.park61.moduel.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.childtest.TestHistoryActivity;
import com.park61.moduel.coupon.MyCouponActiity;
import com.park61.moduel.firsthead.MyFocusMenListActivity;
import com.park61.moduel.firsthead.MyFunsListActivity;
import com.park61.moduel.login.bean.UserBean;
import com.park61.moduel.login.bean.UserManager;
import com.park61.moduel.me.actlist.ActOrderListActivity;
import com.park61.moduel.me.actlist.FocusActsActivity;
import com.park61.moduel.me.actlist.ParentalTicketListActivity;
import com.park61.moduel.me.bean.AuthInfoBean;
import com.park61.moduel.me.bean.MeInfo;
import com.park61.moduel.msg.MsgActivity;
import com.park61.moduel.okdownload.MyDownLoadActivity;
import com.park61.moduel.openmember.OpenMemberActivity;
import com.park61.moduel.order.MeOrderActivity;
import com.park61.moduel.toyshare.TSMyApplyToyListActivity;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.circleimageview.GlideCircleTransform;
import com.park61.widget.dialog.AuthFailedDialog;
import com.park61.widget.dialog.AuthSuccessDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的界面
 */
public class MeActivity extends BaseActivity implements View.OnClickListener {
    private static final int ASYNCREQ_GET_USER = 0;
    private UserBean ub;
    private TextView tv_me_name, tv_pay_num, tv_applyed_num, tv_msg_remind, tv_topic_num, tv_collect_num, tv_my_deposit, tv_funs_num, tv_focusme_num, tv_coupon_num;
    private ImageView img_me, img_setting, img_xiaoxi, partner_area;

    private View waitfor_pay_area, apply_area, waitfor_comt_area, finish_area, area_my_ts_apply, area_deposit, course_area, area_parental, area_coupon, download_area,
            sales_area, member_area, baby_area, service_area, collected_area, area_my_fabu, area_my_collect, area_myfuns, area_my_focusman, auth_area;
    private MeInfo meInfo;
    private RelativeLayout baby_test;
    private String depositStr;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lowSdkLayout();
        }
    }

    public void lowSdkLayout() {
        findViewById(R.id.top_space_area).setVisibility(View.GONE);
        findViewById(R.id.top_bar).setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void initView() {
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_xiaoxi = (ImageView) findViewById(R.id.img_xiaoxi);
        img_me = (ImageView) findViewById(R.id.img_me);
        tv_me_name = (TextView) findViewById(R.id.tv_me_name);
        tv_pay_num = (TextView) findViewById(R.id.tv_pay_num);
        tv_applyed_num = (TextView) findViewById(R.id.tv_applyed_num);
        tv_msg_remind = (TextView) findViewById(R.id.tv_msg_remind);
        waitfor_pay_area = findViewById(R.id.waitfor_pay_area);
        apply_area = findViewById(R.id.apply_area);
        waitfor_comt_area = findViewById(R.id.waitfor_comt_area);
        finish_area = findViewById(R.id.finish_area);
        sales_area = findViewById(R.id.sales_area);
        member_area = findViewById(R.id.member_area);
        baby_area = findViewById(R.id.baby_area);
        service_area = findViewById(R.id.service_area);
        download_area = findViewById(R.id.download_area);
        collected_area = findViewById(R.id.collected_area);
        partner_area = (ImageView) findViewById(R.id.partner_area);
        area_my_fabu = findViewById(R.id.area_my_fabu);
        tv_topic_num = (TextView) findViewById(R.id.tv_topic_num);
        tv_collect_num = (TextView) findViewById(R.id.tv_collect_num);
        area_my_collect = findViewById(R.id.area_my_collect);
        area_my_ts_apply = findViewById(R.id.area_my_ts_apply);
        tv_my_deposit = (TextView) findViewById(R.id.tv_my_deposit);
        area_deposit = findViewById(R.id.area_deposit);
        tv_funs_num = (TextView) findViewById(R.id.tv_funs_num);
        tv_focusme_num = (TextView) findViewById(R.id.tv_focusme_num);
        area_myfuns = findViewById(R.id.area_myfuns);
        area_my_focusman = findViewById(R.id.area_my_focusman);
        auth_area = findViewById(R.id.auth_area);
        course_area = findViewById(R.id.course_area);
        baby_test = (RelativeLayout) findViewById(R.id.baby_test);
        baby_test.setOnClickListener(this);
        area_parental = findViewById(R.id.area_parental);
        area_coupon = findViewById(R.id.area_coupon);
        tv_coupon_num = (TextView) findViewById(R.id.tv_coupon_num);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetUserInfo();
    }

    @Override
    public void initListener() {
        img_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SettingActivity.class));
            }
        });
        img_xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MsgActivity.class));
            }
        });
        img_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MeInfoActivity.class));
            }
        });
        collected_area.setOnClickListener(this);
        waitfor_pay_area.setOnClickListener(this);
        apply_area.setOnClickListener(this);
        waitfor_comt_area.setOnClickListener(this);
        finish_area.setOnClickListener(this);
        sales_area.setOnClickListener(this);
        member_area.setOnClickListener(this);
        baby_area.setOnClickListener(this);
        service_area.setOnClickListener(this);
        download_area.setOnClickListener(this);
        area_my_fabu.setOnClickListener(this);
        area_my_collect.setOnClickListener(this);
        area_my_ts_apply.setOnClickListener(this);
        area_deposit.setOnClickListener(this);
        area_myfuns.setOnClickListener(this);
        area_my_focusman.setOnClickListener(this);
        auth_area.setOnClickListener(this);
        course_area.setOnClickListener(this);
        area_parental.setOnClickListener(this);
        area_coupon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(mContext, ActOrderListActivity.class);
        switch (v.getId()) {
            case R.id.waitfor_pay_area:
                it.putExtra("ACT_ORDER_STATE", GlobalParam.ActOrderState.WAITFORPAY);
                startActivity(it);
                break;
            case R.id.apply_area:
                it.putExtra("ACT_ORDER_STATE", GlobalParam.ActOrderState.APPLIED);
                startActivity(it);
                break;
            case R.id.waitfor_comt_area:
                it.putExtra("ACT_ORDER_STATE", GlobalParam.ActOrderState.WAITFORCOMT);
                startActivity(it);
                break;
            case R.id.finish_area:
                it.putExtra("ACT_ORDER_STATE", GlobalParam.ActOrderState.DONE);
                startActivity(it);
                break;
            case R.id.collected_area:
                startActivity(new Intent(mContext, FocusActsActivity.class));
                break;
            case R.id.area_coupon:
                startActivity(new Intent(mContext, MyCouponActiity.class));
                break;
            case R.id.sales_area:
                //startActivity(new Intent(mContext, MyOrderActivty.class));
                startActivity(new Intent(mContext, MeOrderActivity.class));
                break;
            case R.id.member_area:
//                startActivity(new Intent(mContext, OpenMemberMainActivity.class));
                startActivity(new Intent(this, OpenMemberActivity.class));
                break;
            case R.id.baby_area:
                startActivity(new Intent(mContext, MeBabyListActivity.class));
                break;
            case R.id.service_area:
                startActivity(new Intent(mContext, CustomerServiceActivity.class));
                break;
            case R.id.download_area:
                startActivity(new Intent(mContext, MyDownLoadActivity.class));
                break;
            case R.id.invite_area:
                String shareUrl = AppUrl.APP_3W_URL;
                String picUrl = "http://m.61park.cn/images/logoIcon.png";
                String title = "61区";
                String description = "一起61区吧！";
                showShareDialog(shareUrl, picUrl, title, description);
                break;
            case R.id.area_my_fabu:
                startActivity(new Intent(mContext, MyPublishListActivity.class));
                break;
            case R.id.area_my_collect:
                startActivity(new Intent(mContext, MyCollectListActivity.class));
                break;
            case R.id.area_my_ts_apply:
                startActivity(new Intent(mContext, TSMyApplyToyListActivity.class));
                break;
            case R.id.area_deposit:
                if (TextUtils.isEmpty(depositStr) || FU.isNumEmpty(depositStr)) {
                    showShortToast("申领玩具才需要缴纳押金哦");
                } else {
                    dDialog.showDialog("押金提取",
                            "申请成功后，押金在两小时内到账。提取成功后再次申领需要缴纳押金，是否确定提取？",
                            "取消", "确认", null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    asyncBackUserDeposit();
                                    dDialog.dismissDialog();
                                }
                            });
                }
                break;
            case R.id.area_myfuns:
                startActivity(new Intent(mContext, MyFunsListActivity.class));
                break;
            case R.id.area_my_focusman:
                startActivity(new Intent(mContext, MyFocusMenListActivity.class));
                break;
            case R.id.auth_area:
//                startActivity(new Intent(mContext, AuthFirstActivity.class));
                asyncGetAuthInfo();
                break;
            case R.id.course_area:
                startActivity(new Intent(mContext, MyBabyCourseActivity.class));
                break;
            case R.id.baby_test:
                startActivity(new Intent(mContext, TestHistoryActivity.class));
                break;
            case R.id.area_parental:
                startActivity(new Intent(mContext, ParentalTicketListActivity.class));
                break;
        }
    }

    /**
     * 我的界面信息
     */
    private void asyncGetUserInfo() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_INFO;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getTopTypeLsner);
    }

    BaseRequestListener getTopTypeLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            meInfo = gson.fromJson(jsonResult.toString(), MeInfo.class);
            GlobalParam.MSG_NUM = meInfo.getMessageNum();
            if (meInfo.getMessageNum() <= 0) {
                tv_msg_remind.setVisibility(View.GONE);
            } else {
                tv_msg_remind.setVisibility(View.VISIBLE);
            }
            if (meInfo.getToPaymentActivityNum() <= 0) {
                tv_pay_num.setVisibility(View.GONE);
            } else {
                tv_pay_num.setVisibility(View.VISIBLE);
                tv_pay_num.setText(meInfo.getToPaymentActivityNum() + "");
            }
            tv_topic_num.setText(meInfo.getMyDynamicNum() + "");
            tv_collect_num.setText(meInfo.getMyCollectNum() + "");
            tv_funs_num.setText(meInfo.getMyFansNum() + "");
            tv_focusme_num.setText(meInfo.getMyFocusesNum() + "");
            if (meInfo.getAppliedActivityNum() <= 0) {
                tv_applyed_num.setVisibility(View.GONE);
            } else {
                tv_applyed_num.setVisibility(View.VISIBLE);
                tv_applyed_num.setText(meInfo.getAppliedActivityNum() + "");
            }
            if (meInfo.getIsValidMember() == 0) {
                partner_area.setVisibility(View.GONE);
            } else {
                partner_area.setVisibility(View.VISIBLE);
            }
            asyncGetUserData();
            asyncGetUserInfoNew();
        }
    };

    /**
     * 用户数据信息
     */
    private void asyncGetUserInfoNew() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getUserInfoByUserId;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, unlistener);
    }

    BaseRequestListener unlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            int couponNum = jsonResult.optInt("couponNum");
            tv_coupon_num.setText(couponNum + "张可用");
        }
    };

    /**
     * 用户数据信息
     */
    private void asyncGetUserData() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getUser;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ub = gson.fromJson(jsonResult.toString(), UserBean.class);
            GlobalParam.currentUser = ub;
            UserManager.getInstance().setAccountInfo(GlobalParam.currentUser, mContext);
            fillDataToView();
        }
    };

    protected void fillDataToView() {
        tv_me_name.setText(GlobalParam.currentUser.getPetName());
        Glide.with(this).load(GlobalParam.currentUser.getPictureUrl()).placeholder(R.drawable.headimg_default_img).
                transform(new GlideCircleTransform(this)).into(img_me);
        asyncGetUserDeposit();
    }

    /**
     * 获取用户押金
     */
    private void asyncGetUserDeposit() {
        String wholeUrl = AppUrl.host + AppUrl.SHOWDEPOSITAMOUNT;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            depositStr = jsonResult.optString("data");
            if (FU.isNumEmpty(depositStr)) {
                tv_my_deposit.setText("未缴纳");
            } else {
                tv_my_deposit.setText(depositStr + "元 押金退款");
            }
        }
    };

    /**
     * 退用户押金
     */
    private void asyncBackUserDeposit() {
        String wholeUrl = AppUrl.host + AppUrl.EXTRACTDEPOSITAMOUNT;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

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
            showShortToast("押金退还申请成功，预计2小时内到账");
            asyncGetUserData();
        }
    };

    private void asyncGetAuthInfo() {
        String wholeUrl = AppUrl.host + AppUrl.authInfo;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, infoLsner);
    }

    BaseRequestListener infoLsner = new JsonRequestListener() {

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
            String data;
            try {
                data = jsonResult.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
                data = null;
            }
            if (data != null) {
                startActivity(new Intent(mContext, AuthFirstActivity.class));
            } else {
                AuthInfoBean authInfoBean = gson.fromJson(jsonResult.toString(), AuthInfoBean.class);
                if (authInfoBean.getStatus() == 0) {//已申请认证，但是还没有审核，显示待审核页面，可以做取消认证操作
                    startActivity(new Intent(mContext, AuthCheckInfoActivity.class));
                } else if (authInfoBean.getStatus() == 1) {//认证已通过，显示证书
                    if (authInfoBean.getAuditNoticeSign() == 0) {
                        AuthSuccessDialog mDialog = new AuthSuccessDialog(mContext);
                        mDialog.showDialog();
                        mDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                netRequest.startRequest(AppUrl.host + AppUrl.getAuthNotice, Request.Method.POST, "", 0, new SimpleRequestListener());
                            }
                        });
                    } else {
                        Intent it = new Intent(mContext, AuthPaperActivity.class);
                        it.putExtra("authInfoBean", authInfoBean);
                        startActivity(it);
                    }
                } else if (authInfoBean.getStatus() == 2) {//表示认证不通过
                    if (authInfoBean.getAuditNoticeSign() == 0) {
                        AuthFailedDialog mAuthFailedDialog = new AuthFailedDialog(mContext, authInfoBean.getAuditOpinion());
                        mAuthFailedDialog.showDialog();
                        mAuthFailedDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                netRequest.startRequest(AppUrl.host + AppUrl.getAuthNotice, Request.Method.POST, "", 0, new SimpleRequestListener());
                            }
                        });
                    } else {
                        Intent it = new Intent(mContext, AuthFirstActivity.class);
                        startActivity(it);
                    }
                }
            }
        }
    };


}
