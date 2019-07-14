package com.park61.moduel.child;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.adapter.PhotosViewPagerAdapter;
import java.util.ArrayList;

public class ShowBigPicActivity extends BaseActivity {

    private ImageView img_show;
    private Button btn_delete;
    private String bmpPath;
    private int position;
    private ViewPager viewpager_photos;// 照片浏览
    private ArrayList<String> urlList;
    private String url;
    private ArrayList<View> views = new ArrayList<View>();
    ;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_show_bigpic);
    }

    @Override
    public void initView() {
        img_show = (ImageView) findViewById(R.id.img_show);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        viewpager_photos = (ViewPager) findViewById(R.id.viewpager_photos);
    }

    @Override
    public void initData() {
        bmpPath = getIntent().getStringExtra("big_pic");
        if (!TextUtils.isEmpty(bmpPath)) {
            position = getIntent().getIntExtra("position", -1);
            Bitmap bmp = ImageManager.getInstance().readFileBitMap(bmpPath);
            img_show.setImageBitmap(bmp);
            scaleImageView(img_show, bmp);
        }
        url = getIntent().getStringExtra("picUrl");
        if (!TextUtils.isEmpty(url)) {
            logout("----------ShowBigPicActivity-----url--------------------------============" + url);
            ImageManager.getInstance().displayImg(img_show, url);
            btn_delete.setVisibility(View.GONE);
        }
        urlList = getIntent().getStringArrayListExtra("urlList");
        if (urlList != null && urlList.size() > 1) {
            viewpager_photos.setVisibility(View.VISIBLE);
            img_show.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
            for (int i = 0; i < urlList.size(); i++) {
                View viewItem = LayoutInflater.from(mContext).inflate(R.layout.showbigpic_vp_item, null);
                ImageView img_big_photo = (ImageView) viewItem.findViewById(R.id.img_big_photo);
                String path = urlList.get(i);
                ImageManager.getInstance().displayImg(img_big_photo, path);
                views.add(viewItem);
            }
            viewpager_photos.setAdapter(new PhotosViewPagerAdapter(views));
            int index = urlList.indexOf(url);
            viewpager_photos.setCurrentItem(index);
        }
    }

    @Override
    public void initListener() {
        btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dDialog.showDialog("提醒", "确定删除该图片吗？", "确定", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                Intent data = new Intent();
                                data.putExtra("position", position);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        }, null);
            }
        });
    }

    /**
     * 缩放到适应屏幕
     */
    public void scaleImageView(ImageView imgView, Bitmap bmp) {
        Matrix matrix = new Matrix();
        Matrix currentMaritx = new Matrix();
        currentMaritx.set(imgView.getImageMatrix());// 记录ImageView当期的移动位置
        float scale = DevAttr.getScreenWidth(mContext) / bmp.getWidth();// 放大倍数
        matrix.set(currentMaritx);
        matrix.postScale(scale, scale);
    }

}
