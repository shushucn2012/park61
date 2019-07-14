package com.park61.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.park61.GuideViewPagerAdapter;
import com.park61.R;
import com.park61.common.tool.ImageManager;

public class GoodsBanner {

    private Context context;
    private ViewPager vp;
    private LinearLayout dotlayout;
    private List<View> views;
    private GuideViewPagerAdapter vpAdapter;
    private TextView tvPos;

    // 图片数量
    private int picNum = 0;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    public GoodsBanner(Context context, ViewPager viewpager,
                       LinearLayout dotlayout) {
        this.context = context;
        this.vp = viewpager;
        this.dotlayout = dotlayout;
    }

    public GoodsBanner(Context context, ViewPager viewpager,
                       LinearLayout dotlayout, TextView tvPos) {
        this.context = context;
        this.vp = viewpager;
        this.dotlayout = dotlayout;
        this.tvPos = tvPos;
    }

    /**
     * 设置当前图片
     *
     * @param num
     */
    public void setCurrentImtem(int num) {
        vp.setCurrentItem(num);
    }

    /**
     * 通过url加载banner
     */
    public void init(List<String> bannerPicUrlList) {
        picNum = bannerPicUrlList.size();
        views = new ArrayList<View>();
        // 初始化引导图片列表
        for (int i = 0; i < bannerPicUrlList.size(); i++) {
            View viewItem = LayoutInflater.from(context).inflate(R.layout.main_banner_vp_item, null);
            ImageView img_big_photo = (ImageView) viewItem.findViewById(R.id.img_big_photo);
            ImageManager.getInstance().displayImg(img_big_photo, bannerPicUrlList.get(i), R.drawable.img_default_v);
            views.add(viewItem);
        }
        // 初始化Adapter
        vpAdapter = new GuideViewPagerAdapter(views, picNum);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(mOnPageChangeListener);

        if (tvPos != null) {
            tvPos.setText("1/" + views.size());
        } else {
            // 初始化底部小点
            if (picNum <= 30) {
                initDots();
            }
        }
        vp.setCurrentItem(0);
    }

    /**
     * 页面切换监听
     */
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (tvPos != null) {
                tvPos.setText((position + 1) + "/" + views.size());
            } else {
                if (picNum <= 30) {
                    setCurDot(position % views.size());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动页面时执行此处
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

}
