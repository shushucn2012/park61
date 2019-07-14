package com.park61.common.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by shubei on 2017/6/17.
 */

public class ShareTool {

    public static Tencent mTencent;
    public static IWXAPI wxApi;

    public static String shareUrl;
    public static String picUrl;
    public static String shareTitle;
    public static String description;

    public static void init(String _shareUrl, String _picUrl, String _shareTitle, String _description) {
        shareUrl = _shareUrl;
        picUrl = _picUrl;
        shareTitle = _shareTitle;
        description = _description;
    }

    public static void shareToWeiXin(Context context, final int way) {
        if (wxApi == null) {
            // 实例化
            wxApi = WXAPIFactory.createWXAPI(context, GlobalParam.WX_APP_ID);
            wxApi.registerApp(GlobalParam.WX_APP_ID);
        }

        if(TextUtils.isEmpty(picUrl)){
            picUrl = AppUrl.SHARE_APP_ICON;
        }

        Glide.with(context).load(picUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                // 微信分享的图片有大小限制，必须压缩
                wechatShare(way, ImageManager.createBitmapThumbnail(resource, false));
            }
        });
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    private static void wechatShare(int flag, Bitmap weixinBmp) {
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
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    public static void shareToQQ(Context context) {
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID, context);
        Bundle bundle = new Bundle();
        // 这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        // 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        // 分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
        // 分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareTitle);
        // 标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, context.getString(R.string.app_name) + GlobalParam.TENCENT_APP_ID);
        mTencent.shareToQQ((Activity) context, bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public static void shareToQzone(Context context) {
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID, context);
        Bundle params = new Bundle();
        // 分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);// 必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);// 必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(picUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone((Activity) context, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
