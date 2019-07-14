package com.park61.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.GuideViewPagerAdapter;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.WebViewActivity;
import com.park61.moduel.sales.bean.PromotionBannerData;

public class TeBuyBanner {

    private Context context;
    private ViewPager vp;
    private LinearLayout dotlayout;
    private List<View> views;
    private GuideViewPagerAdapter vpAdapter;
    private int num = 400;
    public boolean isAutoPlay;//是否自动播放
    private int count = 0;
    private int WHAT_AUTO_PLAY = 1000;
    private int DELAY_MILLIS = 3000;

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                vp.setCurrentItem(vp.getCurrentItem() + 1, true);
                viewHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开始自动轮播
     */
    public void startAutoPlay() {
        if (!isAutoPlay) {
            viewHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
            isAutoPlay = true;
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        if (isAutoPlay) {
            viewHandler.removeMessages(WHAT_AUTO_PLAY);
            isAutoPlay = false;
        }
    }

    // 图片数量
    private int picNum = 0;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    public TeBuyBanner(Context context, ViewPager viewpager,
                       LinearLayout dotlayout) {
        this.context = context;
        this.vp = viewpager;
        this.dotlayout = dotlayout;
    }

    /**
     * 通过url加载banner
     */
    public void init(final Context mContext, List<PromotionBannerData> bannerList) {
        picNum = bannerList.size();
        if (picNum < 3) {
            bannerList.addAll(bannerList);
        }
        views = new ArrayList<View>();
        // 初始化引导图片列表
        for (int i = 0; i < bannerList.size(); i++) {
            View viewItem = LayoutInflater.from(context).inflate(R.layout.main_banner_vp_item, null);
            ImageView img_big_photo = (ImageView) viewItem.findViewById(R.id.img_big_photo);
            PromotionBannerData bi = bannerList.get(i);
            img_big_photo.setOnClickListener(new OnBannerItemClickLsner(bi));
            ImageManager.getInstance().displayImg(img_big_photo, bannerList.get(i).getBannerPositionPic());
            views.add(viewItem);
        }
        if (picNum > 1) {
            // 初始化Adapter
            vpAdapter = new GuideViewPagerAdapter(views);
            vp.setAdapter(vpAdapter);
            // 绑定回调
            vp.setOnPageChangeListener(mOnPageChangeListener);
            // 初始化底部小点
            initDots();
            vp.setCurrentItem(num);
            startAutoPlay();
        } else {//只有一张图的时候隐藏掉底部小点
            dotlayout.setVisibility(View.GONE);
            vp.setAdapter(new SimpleVpAdapter(views.subList(0, 1)));
        }
    }

    /**
     * 停在轮播定时器线程
     */
    public void clear() {
        stopAutoPlay();
    }

    /**
     * 页面切换监听
     */
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            num = position;
            setCurDot(position % picNum);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动页面时执行此处
                    stopAutoPlay();
                    count++;
                    final int rec = count;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (!isAutoPlay && rec == count) {
                                startAutoPlay();
                            }
                        }
                    }, 5 * 1000);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:// 未拖动页面时执行此处
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化引导点
     */
    private void initDots() {
        dots = new ImageView[picNum];
        // 循环取得小点图片
        for (int i = 0; i < picNum; i++) {
            dots[i] = (ImageView) dotlayout.getChildAt(i);
            dots[i].setVisibility(View.VISIBLE);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
        for (int j = picNum; j < dotlayout.getChildCount(); j++) {
            dotlayout.getChildAt(j).setVisibility(View.GONE);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > picNum - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    private class OnBannerItemClickLsner implements OnClickListener {

        private PromotionBannerData bi;

        public OnBannerItemClickLsner(PromotionBannerData bi) {
            this.bi = bi;
        }

        @Override
        public void onClick(View view) {
            if (bi.getBannerPositionWebsite() != null
                    && !"".equals(bi.getBannerPositionWebsite())) {
                String url = bi.getBannerPositionWebsite();
                Log.out("url======" + url);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("picUrl", bi.getBannerPositionPic());
                intent.putExtra("PAGE_TITLE", bi.getName());
                context.startActivity(intent);
            }
        }
    }

}
