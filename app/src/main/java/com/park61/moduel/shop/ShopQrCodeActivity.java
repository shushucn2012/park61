package com.park61.moduel.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.QRCodeCreator;

/**
 * 商铺二维码
 *
 * @author ouyangji
 */
public class ShopQrCodeActivity extends BaseActivity {

    private ImageView img_qrcode, img_shop;// 二维码图
    private TextView shopNameTxt, tv_addr;
    private String strShopName, strShopAddr, shopPicUrl;
    private String shopId;
    private Button btnShare;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shop_qrcode);
    }

    @Override
    public void initView() {
        img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        img_shop = (ImageView) findViewById(R.id.img_shop);
        shopNameTxt = (TextView) findViewById(R.id.txt_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        btnShare = (Button) findViewById(R.id.btn_share);
    }

    @Override
    public void initData() {
        strShopName = getIntent().getStringExtra("shopName");
        strShopAddr = getIntent().getStringExtra("shopAddr");
        shopId = getIntent().getStringExtra("shopId");
        shopPicUrl = getIntent().getStringExtra("shopPicUrl");
        logout("shopPicUrl======" + shopPicUrl);
        shopNameTxt.setText(strShopName);
        tv_addr.setText(strShopAddr);
        ImageManager.getInstance().displayImg(img_shop, shopPicUrl, R.drawable.img_default_v);
        setDataToView();
    }

    public void setDataToView() {
        String shareUrl = "http://m.61park.cn/page/store-index/page.html?officeId=" + shopId;
        Bitmap b = QRCodeCreator.generateQRCode(shareUrl, DevAttr.dip2px(mContext, 250), DevAttr.dip2px(mContext, 250),
                0xff000000, 0xffffffff, BitmapFactory.decodeResource(getResources(), R.drawable.icon_qujiawan));
        img_qrcode.setImageBitmap(b);

        Glide.with(this).load(shopPicUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                String shareUrl = "http://m.61park.cn/merchant/merchantIndex.do?id=" + shopId;
                Bitmap b = QRCodeCreator.generateQRCode(shareUrl, DevAttr.dip2px(mContext, 250), DevAttr.dip2px(mContext, 250),
                        0xff000000, 0xffffffff, resource);
                img_qrcode.setImageBitmap(b);
            }
        });
    }

    @Override
    public void initListener() {
        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/page/store-index/page.html?officeId=" + shopId;
                String title = mContext.getString(R.string.app_name);
                String description = strShopName + "\n店铺详情";
                ((BaseActivity) mContext).showShareDialog(shareUrl, shopPicUrl, title, description);
            }
        });
    }

}
