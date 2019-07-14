package com.park61.moduel.child;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ImageManager;
import com.park61.widget.dialog.QRcodeInviteDialog;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 邀请方式的界面
 */
public class InviteMethodActivity extends BaseActivity {
    private View area_qq_invite, area_weixin_invite, area_qr_invite;
    private Long curChildId;//小孩id
    private String childName;//小孩名字
    private String picUrl;
    private String id;//关系人id
    private String relationName;//关系名称
    private String shareUrl;
    private String shareTitle;
    private String description;
    private Bitmap weixinBmp;
    public IWXAPI wxApi;
    public Tencent mTencent;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_invite_method);
    }

    @Override
    public void initView() {
        area_qq_invite = findViewById(R.id.area_qq_invite);
        area_weixin_invite = findViewById(R.id.area_weixin_invite);
        area_qr_invite = findViewById(R.id.area_qr_invite);
    }

    @Override
    public void initData() {
        curChildId = getIntent().getLongExtra("childId", -1l);
        logout("======curChildId=====" + curChildId);
        picUrl = getIntent().getStringExtra("childPic");
        childName = getIntent().getStringExtra("childName");
        id = getIntent().getStringExtra("id");
        relationName = getIntent().getStringExtra("relationName");

        Glide.with(this).load(picUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                // 微信分享的图片有大小限制，必须压缩
                weixinBmp = ImageManager.getInstance().reduceBitmapForWx(resource);
            }
        });
        shareUrl = String.format(AppUrl.INVITE_URL, GlobalParam.currentUser.getInviteCode(),
                curChildId + "", id);
        logout("=====shareUrl=====" + shareUrl);
    }

    @Override
    public void initListener() {
        area_qr_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRcodeInviteDialog dialog = new QRcodeInviteDialog(mContext, shareUrl);
                dialog.showDialog();
            }
        });
        area_qq_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUrl = String.format(AppUrl.INVITE_URL, GlobalParam.currentUser.getInviteCode(),
                        curChildId + "", id);
                shareTitle = "邀请亲";
                description = childName + "的" + relationName + "邀请您61区！";
                shareToQQ();
            }
        });
        area_weixin_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUrl = String.format(AppUrl.INVITE_URL, GlobalParam.currentUser.getInviteCode(),
                        curChildId + "", id);
                shareTitle = "邀请亲";
                description = childName + "的" + relationName + "邀请您61区！";
                wechatShare(0);
            }
        });
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    public void wechatShare(int flag) {
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(mContext, GlobalParam.WX_APP_ID);
            wxApi.registerApp(GlobalParam.WX_APP_ID);
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = description;
        // 这里替换一张自己工程里的图片资源
        msg.setThumbImage(weixinBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    public void shareToQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID, mContext);
        }
        Bundle bundle = new Bundle();
        // 这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        // 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        // 分享的图片URL
        logout("picUrl======" + picUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
        // 分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        // 手QQ客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareTitle);
        // 标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT,
                getString(R.string.app_name) + GlobalParam.TENCENT_APP_ID);
        mTencent.shareToQQ(this, bundle, new BaseUiListener());
    }

    public class BaseUiListener implements IUiListener {

        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object arg0) {
        }

        @Override
        public void onError(UiError arg0) {
        }
    }
}
