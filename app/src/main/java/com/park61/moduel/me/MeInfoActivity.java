package com.park61.moduel.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.AliyunUploadUtils.OnUploadFinish;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.accountsafe.ModifyPhoneNumActivity;
import com.park61.moduel.address.AddressManageActivity;
import com.park61.moduel.login.ForgetPwdInputPhoneActivity;
import com.park61.moduel.login.UnionWxBindActivity;
import com.park61.moduel.me.bean.AuthInfoBean;
import com.park61.net.base.Request;
import com.park61.net.base.Request.Method;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AuthFailedDialog;
import com.park61.widget.dialog.AuthSuccessDialog;
import com.park61.widget.picselect.utils.SelectPicPopupWindow;
import com.park61.wxapi.HttpUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class MeInfoActivity extends BaseActivity implements OnClickListener {

    private static final int REQ_UNION_BIND_PHONE = 111;
    private static final int REQ_GET_PIC = 0;
    private static final int REQ_GET_NICKNAME = 1;
    private static final int REQ_GET_NAME = 2;
    private static final int REQ_GET_ADDR = 3;
    private static final int REQ_GET_CONTACT = 4;
    private static final int REQ_GET_RESUME = 5;
    private View me_pic_area;
    private View nickname_area;
    private View name_area;
    private View addr_area;
    private View contact_area;
    private ImageView img_me_pic;
    private View address_manage_area, pinfo_area, auth_area, area_modify_pwd, pwd_area, area_cancel, tv_modify_by_phone, tv_modify_by_pwd, weixin_area;
    private TextView tv_nickname_value, tv_name_value, tv_addr_value,
            tv_contact_value, tv_pinfo_value, tv_auth_value;
    private String backData = "";

    public static boolean isWXLogin = false;// 是否微信登陆
    public static String WX_CODE = "";// 微信登陆授权返回code
    private IWXAPI wxApi;
    private String mopenId, nickname, sex, headimgurl, unionQQorWx;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_meinfo);
    }

    @Override
    public void initView() {
        me_pic_area = findViewById(R.id.me_pic_area);
        nickname_area = findViewById(R.id.nickname_area);
        name_area = findViewById(R.id.name_area);
        addr_area = findViewById(R.id.addr_area);
        contact_area = findViewById(R.id.contact_area);
        address_manage_area = findViewById(R.id.address_manage_area);
        pinfo_area = findViewById(R.id.pinfo_area);
        auth_area = findViewById(R.id.auth_area);
        area_modify_pwd = findViewById(R.id.area_modify_pwd);
        pwd_area = findViewById(R.id.pwd_area);
        area_cancel = findViewById(R.id.area_cancel);
        tv_modify_by_phone = findViewById(R.id.tv_modify_by_phone);
        tv_modify_by_pwd = findViewById(R.id.tv_modify_by_pwd);
        weixin_area = findViewById(R.id.weixin_area);

        img_me_pic = (ImageView) findViewById(R.id.img_me_pic);
        tv_nickname_value = (TextView) findViewById(R.id.tv_nickname_value);
        tv_name_value = (TextView) findViewById(R.id.tv_name_value);
        tv_addr_value = (TextView) findViewById(R.id.tv_addr_value);
        tv_contact_value = (TextView) findViewById(R.id.tv_contact_value);
        tv_pinfo_value = (TextView) findViewById(R.id.tv_pinfo_value);
        tv_auth_value = (TextView) findViewById(R.id.tv_auth_value);
    }

    @Override
    public void initData() {
        if (GlobalParam.currentUser != null) {
            ImageManager.getInstance().displayCircleImg(img_me_pic, GlobalParam.currentUser.getPictureUrl());
            if (!TextUtils.isEmpty(GlobalParam.currentUser.getPetName()))
                tv_nickname_value.setText(GlobalParam.currentUser.getPetName());
            if (!TextUtils.isEmpty(GlobalParam.currentUser.getName()))
                tv_name_value.setText(GlobalParam.currentUser.getName());
            if (!TextUtils.isEmpty(GlobalParam.currentUser.getAddress()))
                tv_addr_value.setText(GlobalParam.currentUser.getAddress());
            if (!TextUtils.isEmpty(GlobalParam.currentUser.getMobile()))
                tv_contact_value.setText(GlobalParam.currentUser.getMobile().substring(0, 3) + "****" + GlobalParam.currentUser.getMobile().substring(7));
            if (!TextUtils.isEmpty(GlobalParam.currentUser.getResume())) {
                String resumeStr = GlobalParam.currentUser.getResume();
                if (resumeStr.length() > 15) {
                    resumeStr = resumeStr.substring(0, 15) + "...";
                }
                tv_pinfo_value.setText(resumeStr);
            }
            if (GlobalParam.currentUser.isExpert()) {
                tv_auth_value.setText("已认证");
            } else {
                tv_auth_value.setText("未认证");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(GlobalParam.currentUser.getMobile())) {
            tv_contact_value.setText(GlobalParam.currentUser.getMobile().substring(0, 3) + "****" + GlobalParam.currentUser.getMobile().substring(7));
        }
        if (isWXLogin && !WX_CODE.equals("")) {// 如果是登录，并且返回不为空
            loadWXUserInfo();
        } else {
            dismissDialog();
        }
    }

    @Override
    public void initListener() {
        me_pic_area.setOnClickListener(this);
        nickname_area.setOnClickListener(this);
        name_area.setOnClickListener(this);
        addr_area.setOnClickListener(this);
        contact_area.setOnClickListener(this);
        pinfo_area.setOnClickListener(this);
        auth_area.setOnClickListener(this);
        address_manage_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addrIntent = new Intent(MeInfoActivity.this, AddressManageActivity.class);
                startActivity(addrIntent);
            }
        });
        pwd_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                area_modify_pwd.setVisibility(View.VISIBLE);
            }
        });
        area_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                area_modify_pwd.setVisibility(View.GONE);
            }
        });
        tv_modify_by_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgetPwdInputPhoneActivity.class));
                area_modify_pwd.setVisibility(View.GONE);
            }
        });
        tv_modify_by_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ChangePasswordActivity.class));
                area_modify_pwd.setVisibility(View.GONE);
            }
        });
        weixin_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalParam.LOGIN_TYPE = 1;
                isWXLogin = true;
                showDialog();
                unionWXLogin();
            }
        });
    }

    /**
     * 微信联合登陆
     */
    public void unionWXLogin() {
        wxApi = WXAPIFactory.createWXAPI(this, GlobalParam.WX_APP_ID, true);
        wxApi.registerApp(GlobalParam.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        wxApi.sendReq(req);
    }

    /**
     * @methods: 获得微信用户信息
     * @author: lianzhi
     * @Date: 2015-3-5
     */
    private void loadWXUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + GlobalParam.WX_APP_ID
                        + "&secret="
                        + GlobalParam.WX_APP_SECRET
                        + "&code="
                        + WX_CODE
                        + "&grant_type=authorization_code";
                Log.e("loadWXUserInfo", "accessTokenUrl======" + accessTokenUrl);
                String tokenResult = HttpUtil.httpsGet(accessTokenUrl);
                Log.e("loadWXUserInfo", "tokenResult======" + tokenResult);
                if (null != tokenResult) {
                    JSONObject tokenObj = null;
                    JSONObject wxUserInfoJot = null;
                    try {
                        tokenObj = new JSONObject(tokenResult);
                        String accessToken = tokenObj.getString("access_token");
                        String openId = tokenObj.getString("openid");
                        String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
                        String wxUserInfo = HttpUtil.httpsGet(userUrl);
                        Log.e("loadWXUserInfo", "wxUserInfo======" + wxUserInfo);
                        wxUserInfoJot = new JSONObject(wxUserInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mopenId = wxUserInfoJot.optString("unionid");
                    nickname = wxUserInfoJot.optString("nickname");
                    sex = wxUserInfoJot.optInt("sex") + "";
                    headimgurl = wxUserInfoJot.optString("headimgurl");
                    unionQQorWx = "weixin";
                    asyncUnionLoginCheckMobile(mopenId, unionQQorWx, "", null, nickname, sex, headimgurl);
                }
                // 请求后重置
                isWXLogin = false;
                WX_CODE = "";
            }
        }).start();
    }

    /**
     * 联合登录
     */
    private void asyncUnionLoginCheckMobile(String loginName, String unionLoginType, String name, String email, String petName, String sex, String pictureUrl) {
        String wholeUrl = AppUrl.host + AppUrl.UNION_LOGIN_CHECK_MOBILE;
        String requestBodyData = ParamBuild.unionLogin(loginName, unionLoginType, name, email, petName, sex, pictureUrl);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, checkMobileloginLsner);
    }

    BaseRequestListener checkMobileloginLsner = new JsonRequestListener() {

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
            String needMobile = jsonResult.optString("needMobile");
            if ("0".equals(needMobile)) {
                showShortToast("手机号已经绑定！");
            } else if ("1".equals(needMobile)) {
                Intent it = new Intent(mContext, UnionWxBindActivity.class);
                it.putExtra("mopenId", mopenId);
                it.putExtra("unionQQorWx", unionQQorWx);
                it.putExtra("nickname", nickname);
                it.putExtra("sex", sex);
                it.putExtra("headimgurl", headimgurl);
                startActivityForResult(it, REQ_UNION_BIND_PHONE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int reqCode = -1;
        Intent editIt = new Intent(mContext, MeEditActivity.class);
        if (v.getId() == R.id.me_pic_area) {// 头像
            Intent it = new Intent(mContext, SelectPicPopupWindow.class);
            startActivityForResult(it, REQ_GET_PIC);
            return;
        } else if (v.getId() == R.id.nickname_area) {// 昵称
            reqCode = REQ_GET_NICKNAME;
            editIt.putExtra("current_data", tv_nickname_value.getText());
        } else if (v.getId() == R.id.name_area) {// 姓名
            reqCode = REQ_GET_NAME;
            editIt.putExtra("current_data", tv_name_value.getText());
        } else if (v.getId() == R.id.addr_area) {// 地址
            reqCode = REQ_GET_ADDR;
            editIt.putExtra("current_data", tv_addr_value.getText());
        } else if (v.getId() == R.id.contact_area) {// 绑定手机号
            reqCode = REQ_GET_CONTACT;
            // editIt.putExtra("current_data", tv_contact_value.getText());
            String oldPhone = tv_contact_value.getText().toString().trim().replace("未填写", "");
            if (!TextUtils.isEmpty(oldPhone)) {
                dDialog.showDialog("提示", "您确定更换手机号吗？", "确定", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(mContext, ModifyPhoneNumActivity.class);
                                startActivityForResult(it, REQ_GET_CONTACT);
                                dDialog.dismissDialog();
                            }
                        }, null);
            } else {
                startActivityForResult(new Intent(mContext, BindPhoneActivity.class), REQ_GET_CONTACT);
            }
            return;
        } else if (v.getId() == R.id.pinfo_area) {
            Intent it = new Intent(mContext, ResumeInputActivity.class);
            it.putExtra("resume", GlobalParam.currentUser.getResume());
            startActivityForResult(it, REQ_GET_RESUME);
            return;
        } else if (v.getId() == R.id.auth_area) {
            asyncGetAuthInfo();
            return;
        }
        startActivityForResult(editIt, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        String requestBodyData = "";
        backData = data.getStringExtra("new_data");
        if (requestCode == REQ_GET_PIC) {
            String picPath = data.getStringExtra("path");
            logout("picPath======" + picPath);
            //Bitmap bmp = ImageManager.getInstance().readFileBitMap(picPath);
            //Glide.with(this).load(picPath).placeholder(R.drawable.headimg_default_img).transform(new GlideCircleTransform(this)).into(img_me_pic);
            ImageManager.getInstance().displayCircleImg(img_me_pic, picPath);
            new AliyunUploadUtils(mContext).uploadPic(picPath,
                    new OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            String requestBodyData = ParamBuild.updateUser(
                                    picUrl, null, null, null, null);
                            asyncUpdateUser(requestBodyData, 99);
                        }

                        @Override
                        public void onError() {
                            showShortToast("头像上传失败请重试！");
                        }
                    });
            return;
        } else if (requestCode == REQ_GET_NICKNAME) {
            requestBodyData = ParamBuild.updateUser("", backData, "", "", "");
        } else if (requestCode == REQ_GET_NAME) {
            requestBodyData = ParamBuild.updateUser("", "", backData, "", "");
        } else if (requestCode == REQ_GET_ADDR) {
            requestBodyData = ParamBuild.updateUser("", "", "", backData, "");
        } else if (requestCode == REQ_GET_CONTACT) {
            // requestBodyData = ParamBuild.updateUser("", "", "", "",
            // backData);
            tv_contact_value.setText(backData.substring(0, 3) + "****" + backData.substring(7));
            return;
        } else if (requestCode == REQ_GET_RESUME) {
            tv_pinfo_value.setText(GlobalParam.currentUser.getResume());
            return;
        }
        asyncUpdateUser(requestBodyData, requestCode);
    }

    /**
     * 请求更新用户数据
     */
    private void asyncUpdateUser(String requestBodyData, int requestCode) {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_USER_INFO_END;
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData,
                requestCode, listener);
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
            showShortToast("修改用户信息成功");
            switch (requestId) {
                case REQ_GET_NICKNAME:
                    tv_nickname_value.setText(backData);
                    break;
                case REQ_GET_NAME:
                    tv_name_value.setText(backData);
                    break;
                case REQ_GET_ADDR:
                    tv_addr_value.setText(backData);
                    break;
                case REQ_GET_CONTACT:
                    tv_contact_value.setText(backData.substring(0, 3) + "****" + backData.substring(7));
                    break;
            }
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
