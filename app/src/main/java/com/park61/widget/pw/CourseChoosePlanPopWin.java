package com.park61.widget.pw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.acts.adapter.CourseSessionListAdapter;
import com.park61.moduel.acts.bean.CourseSessionBean;
import com.park61.moduel.shop.adapter.ActNShopListAdapter;
import com.park61.moduel.shop.bean.ActNShopBean;
import com.park61.moduel.shop.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

public class CourseChoosePlanPopWin extends PopupWindow {
    private View toolView;
    private ListView lv_teachers;
    private CourseSessionListAdapter mCourseSessionListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CourseChoosePlanPopWin(Context context, List<CourseSessionBean> _list) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_course_choose_plan_layout, null);
        lv_teachers = (ListView) toolView.findViewById(R.id.lv_teachers);
        mCourseSessionListAdapter = new CourseSessionListAdapter(context, _list, false);
        lv_teachers.setAdapter(mCourseSessionListAdapter);
        // 设置按钮监听
//		lv_teachers.setOnItemClickListener(itemClickListener);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(context.getDrawable(R.drawable.planchoose_list_bg));
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.toolView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = toolView.findViewById(R.id.menuview_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        this.setAnimationStyle(R.style.AnimBottom);
    }

    public ListView getLvTeachers(){
        return lv_teachers;
    }

}
