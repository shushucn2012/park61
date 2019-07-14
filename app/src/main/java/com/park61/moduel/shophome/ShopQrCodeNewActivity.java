package com.park61.moduel.shophome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.QRCodeCreator;
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
 * 商铺二维码
 *
 * @author ouyangji
 */
public class ShopQrCodeNewActivity extends BaseActivity implements OnClickListener {

    public Tencent mTencent;
    public IWXAPI wxApi;

    private String shareUrl;
    private String picUrl;
    private Bitmap weixinBmp;
    private String shareTitle;
    private String description;

    private ImageView img_qrcode;// 二维码图
    private TextView shopNameTxt;
    private String strShopName;
    private String shopId;
    private String shopContactor;
    private View view_wx_friend, view_qq_friend, view_wx_friends,
            view_qq_space;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_qrcode_new);
    }

    @Override
    public void initView() {
        img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        shopNameTxt = (TextView) findViewById(R.id.txt_name);
        view_wx_friend = findViewById(R.id.view_wx_friend);
        view_wx_friends = findViewById(R.id.view_wx_friends);
        view_qq_friend = findViewById(R.id.view_qq_friend);
        view_qq_space = findViewById(R.id.view_qq_space);
    }

    @Override
    public void initData() {
        strShopName = getIntent().getStringExtra("shopName");
        shopId = getIntent().getStringExtra("shopId");
        shopContactor = getIntent().getStringExtra("shopContactor");
        shopNameTxt.setText(strShopName);

        weixinBmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_qujiawan);
        picUrl = "http://m.61park.cn/images/store_index/shop.jpg";
        shareTitle = mContext.getString(R.string.app_name);
        description = strShopName + "\n店铺详情";

        Glide.with(this).load(picUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                // 微信分享的图片有大小限制，必须压缩
                weixinBmp = ImageManager.getInstance().reduceBitmapForWx(resource);
            }
        });
        setDataToView();
    }

    public void setDataToView() {
        shareUrl = "http://m.61park.cn/merchant/merchantIndex.do?id=" + shopId;
        Bitmap b = QRCodeCreator.generateQRCode(shareUrl, DevAttr.dip2px(mContext, 130), DevAttr.dip2px(mContext, 130),
                0xFF000000, 0xffffffff, BitmapFactory.decodeResource(getResources(),
                        R.drawable.icon_qujiawan));
        img_qrcode.setImageBitmap(b);
    }

    @Override
    public void initListener() {
        view_wx_friends.setOnClickListener(this);
        view_wx_friend.setOnClickListener(this);
        view_qq_friend.setOnClickListener(this);
        view_qq_space.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_wx_friends:
                shareToWeiXin(1);
                break;
            case R.id.view_wx_friend:
                shareToWeiXin(0);
                break;
            case R.id.view_qq_friend:
                shareToQQ();
                break;
            case R.id.view_qq_space:
                shareToQzone();
                break;
        }
    }

    public void shareToWeiXin(int way) {
        if (wxApi == null) {
            // 实例化
            wxApi = WXAPIFactory.createWXAPI(this, GlobalParam.WX_APP_ID);
            wxApi.registerApp(GlobalParam.WX_APP_ID);
        }
        // 在需要分享的地方添加代码：
        wechatShare(way);// 0分享到微信好友;1分享到微信朋友圈
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
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
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID,
                    mContext);
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
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT,
                getString(R.string.app_name) + GlobalParam.TENCENT_APP_ID);
        mTencent.shareToQQ(this, bundle, new BaseUiListener());
    }

    public void shareToQzone() {
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID,
                    mContext);
        Bundle params = new Bundle();
        // 分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);// 必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);// 必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(picUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone(this, params, new BaseUiListener());
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
