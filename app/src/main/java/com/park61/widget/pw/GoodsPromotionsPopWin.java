package com.park61.widget.pw;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.sales.WebViewActivity;
import com.park61.moduel.sales.adapter.GpPopWinAdapter;
import com.park61.moduel.sales.bean.DiscountActivity;

import java.util.List;

/**
 * 二级导航窗口
 *
 * @author super
 */
public class GoodsPromotionsPopWin extends PopupWindow {
    private View toolView;
    private ListView lv_promotion;
    private View mcover,img_close_cover;

    private Context mContext;
    private List<DiscountActivity> promotionList;
    private GpPopWinAdapter mGpPopWinAdapter;

    public GoodsPromotionsPopWin(final Context context, View cover, List<DiscountActivity> list) {
        super(context);
        mContext = context;
        this.mcover = cover;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_goods_promotion_layout, null);
        // 初始化视图
        lv_promotion = (ListView) toolView.findViewById(R.id.lv_promotion);
        mcover.setVisibility(View.VISIBLE);
        img_close_cover = toolView.findViewById(R.id.img_close_cover);
        // 初始化数据
        promotionList = list;
        mGpPopWinAdapter = new GpPopWinAdapter(mContext, promotionList);
        lv_promotion.setAdapter(mGpPopWinAdapter);
        // 初始化监听
        lv_promotion.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent it = new Intent(mContext, WebViewActivity.class);
                String url = AppUrl.APP_INVITE_URL + promotionList.get(position).getUrl();
                //"/toDiscuntListDetail.do?discId="+promotionList.get(position).getId();
                it.putExtra("url", url);
                it.putExtra("PAGE_TITLE", promotionList.get(position).getTitle());
                CommonMethod.startOnlyNewActivity(mContext, WebViewActivity.class, it);
                //context.startActivity(it);
            }
        });
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.7));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        this.setAnimationStyle(R.style.AnimBottom);
        img_close_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mcover.setVisibility(View.GONE);
            }
        });
    }

}
