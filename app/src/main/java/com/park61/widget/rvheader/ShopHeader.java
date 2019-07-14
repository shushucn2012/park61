package com.park61.widget.rvheader;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActMapActivity;
import com.park61.moduel.shop.ShopQrCodeActivity;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.moduel.shophome.ShopQrCodeNewActivity;
import com.park61.moduel.shophome.ShowBigPhotoActivity;
import com.park61.moduel.shophome.bean.TempleteData;

import java.util.ArrayList;

/**
 * RecyclerView的HeaderView(店铺首页)
 */
public class ShopHeader extends RelativeLayout {

    private ImageView shop_bg_img;
    private TextView tv_num;
    private TextView tv_title;
    private TextView tv_content;
    private ImageView arrow_img;
    private TextView tv_store_name;
    private ImageView super_store_img;
    private ImageView erweima_img;
    private TextView tv_addr;
    private ImageView phone_img;
    private ImageView store_img;

    private Context mContext;

    public ShopHeader(Context context) {
        super(context);
        init(context);
    }

    public ShopHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        View rootView = inflate(context, R.layout.shop_main_head_layout, this);
        shop_bg_img = (ImageView) rootView.findViewById(R.id.shop_bg_img);
        tv_num = (TextView) rootView.findViewById(R.id.tv_num);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        arrow_img = (ImageView) rootView.findViewById(R.id.arrow_img);
        store_img = (ImageView) rootView.findViewById(R.id.store_img);

        tv_store_name = (TextView) rootView.findViewById(R.id.tv_store_name);
        super_store_img = (ImageView) rootView.findViewById(R.id.super_store_img);
        erweima_img = (ImageView) rootView.findViewById(R.id.erweima_img);
        tv_addr = (TextView) rootView.findViewById(R.id.tv_addr);
        phone_img = (ImageView) rootView.findViewById(R.id.phone_img);
    }

    public void fillPhotoData(final TempleteData templeteData) {
        if (templeteData == null) {
            shop_bg_img.setBackgroundResource(R.drawable.img_default_v);
            tv_num.setText("0");
            tv_title.setText("");
            tv_content.setText("");
            arrow_img.setOnClickListener(null);
            return;
        }
        if (templeteData.getOfficeContentItemsList().size() > 0) {
            ImageManager.getInstance().displayImg(shop_bg_img,
                    templeteData.getOfficeContentItemsList().get(0).getResourceUrl(),
                    R.drawable.img_default_v);
        }
        tv_num.setText(templeteData.getViewNum());
        tv_title.setText(templeteData.getTitle());
        tv_content.setText(templeteData.getDesc());
        arrow_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> urlList = new ArrayList<String>();
                for (int j = 0; j < templeteData.getOfficeContentItemsList().size(); j++) {
                    urlList.add(templeteData.getOfficeContentItemsList().get(j).getResourceUrl());
                }
                Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                bIt.putExtra("albumId", templeteData.getId());
                bIt.putExtra("itemsId", templeteData.getOfficeContentItemsList().get(0).getId());
                bIt.putParcelableArrayListExtra("picList", (ArrayList) templeteData.getOfficeContentItemsList());
                bIt.putExtra("position", 0);
                bIt.putExtra("picUrl", templeteData.getOfficeContentItemsList().get(0).getResourceUrl());
                if (urlList.size() >= 1)// url集合
                    bIt.putStringArrayListExtra("urlList", urlList);
                mContext.startActivity(bIt);
            }
        });
    }


    public void setPhoneImgClickLsner(View.OnClickListener lsner) {
        phone_img.setOnClickListener(lsner);
    }

}
