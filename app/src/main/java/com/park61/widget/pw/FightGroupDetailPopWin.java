package com.park61.widget.pw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.park61.R;
import com.park61.common.tool.DevAttr;

/**
 * 拼团商品详情二级目录
 */
public class FightGroupDetailPopWin extends PopupWindow {
    private View view_home;// 首页
    private View view_cart;// 分享
    private View view_conactor;// 联系
    private View toolView;
    public int vWidth = 120;// 宽度DP
    public int vHeight = 130;// 高度DP
    private OnPopWinDisLsner mOnPopWinDisLsner;

    public FightGroupDetailPopWin(Context context, View.OnClickListener itemsOnClick,
                          OnPopWinDisLsner onPopWinDisLsner, boolean  isGoods) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_fightgroup_detail_layout, null);
        view_home = toolView.findViewById(R.id.view_home);
        view_cart = toolView.findViewById(R.id.view_cart);
        view_conactor = toolView.findViewById(R.id.view_conactor);
        if(isGoods){
            view_conactor.setVisibility(View.GONE);
        }
        // 设置按钮监听
        view_home.setOnClickListener(itemsOnClick);
        view_cart.setOnClickListener(itemsOnClick);
        view_conactor.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.dip2px(context, vWidth));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mOnPopWinDisLsner = onPopWinDisLsner;
        toolView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                    if (mOnPopWinDisLsner != null)
                        mOnPopWinDisLsner.onDismiss();
                }
                return true;
            }
        });

    }

    public interface OnPopWinDisLsner {
        public void onDismiss();
    }
}
