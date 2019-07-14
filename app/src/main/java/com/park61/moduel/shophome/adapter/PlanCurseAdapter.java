package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.CourseDetailsActivity;
import com.park61.moduel.shophome.bean.TimetableInfo;
import com.park61.moduel.shophome.bean.XplanInfo;
import com.park61.moduel.shophome.viewprotocol.LinkageScrollView;
import com.park61.moduel.shophome.viewprotocol.UpDownLeftRightScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ouyangji on 17/3/14.
 */

public class PlanCurseAdapter extends SimpleAdapter {
    private List<? extends Map<String, ?>> datas;
    private int res;
    private String[] from;
    private int[] to;
    private Context context;
    /**
     * 列表中各列的文字背景颜色集合
     */
    private List<String> planBgColorList = new ArrayList<String>();
    private int[] mParentFramentArr;
    private int[] mJoinImgArr;

    private LinkageScrollView linkageScrollView;

    public PlanCurseAdapter(Context context,
                            List<? extends Map<String, ?>> data, int resource,
                            String[] from, int[] to, int[] parentFramentArr, int[] joinImgArr,
                            LinkageScrollView pLinkageScrollView) {
        super(context, data, resource, from, to);
        this.context = context;
        this.datas = data;
        this.res = resource;
        this.from = from;
        this.to = to;
        mParentFramentArr = parentFramentArr;
        mJoinImgArr = joinImgArr;
        linkageScrollView = pLinkageScrollView;
        initColorList();
    }

    private void initColorList() {
        planBgColorList.add("#999999");
        planBgColorList.add("#ffc231");
        planBgColorList.add("#c282f3");
        planBgColorList.add("#40d7af");
        planBgColorList.add("#01b3ee");
        planBgColorList.add("#ff9b58");
        planBgColorList.add("#ff7272");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(res, null);
            linkageScrollView.addHViews((UpDownLeftRightScrollView) v.findViewById(R.id.item_scroll));
            View[] views = new View[to.length];
            for (int i = 0; i < to.length; i++) {
                View tv = v.findViewById(to[i]);
                tv.setOnClickListener(clickListener);
                views[i] = tv;
            }
            v.setTag(views);
        }
        View[] holders = (View[]) v.getTag();
        int len = holders.length;
        if (this.datas.get(position) != null) {
            if (position > 0 &&
                    !((TimetableInfo) this.datas.get(position).get(from[0])).getStartTime().equals(((TimetableInfo) this.datas.get(position - 1).get(from[0])).getStartTime())) {
                ((TextView) holders[0]).setText(((TimetableInfo) this.datas.get(position).get(from[0])).getStartTime().toString());
            } else if (position == 0) {
                ((TextView) holders[0]).setText(((TimetableInfo) this.datas.get(position).get(from[0])).getStartTime().toString());
            } else {
                ((TextView) holders[0]).setText(" ");
            }
            for (int i = 1; i < len; i++) {
                TimetableInfo columnPlan = (TimetableInfo) this.datas.get(position).get(from[i]);

//                ((TextView) holders[i]).setText(columnPlan == null ? ""
//                        : columnPlan.getActivityTypeName().toString()+"\n"
//                        +columnPlan.getSectionStartTime()+"-"+columnPlan.getSectionEndTime());

                ((TextView) holders[i]).setText(columnPlan == null ? "" : columnPlan.getTotalName());

                if (columnPlan == null) {
                    columnPlan = new TimetableInfo();
                    columnPlan.setWeekDay(i - 1);
                }
                holders[i].setTag(columnPlan);
                ImageView joinImg = null;
                if (i > 0) {
                    joinImg = (ImageView) v.findViewById(mJoinImgArr[i - 1]);
                    if (i - 1 == linkageScrollView.getSelectWeekIndex()) {
                        LinearLayout.LayoutParams lpNew = new LinearLayout.LayoutParams(
                                DevAttr.dip2px(context, 90), ViewGroup.LayoutParams.MATCH_PARENT
                        );
                        FrameLayout frameLayout = (FrameLayout) v.findViewById(mParentFramentArr[i - 1]);
                        frameLayout.setLayoutParams(lpNew);
                    } else {
                        LinearLayout.LayoutParams lpNew = new LinearLayout.LayoutParams(
                                DevAttr.dip2px(context, 60), ViewGroup.LayoutParams.MATCH_PARENT
                        );
                        FrameLayout frameLayout = (FrameLayout) v.findViewById(mParentFramentArr[i - 1]);
                        frameLayout.setLayoutParams(lpNew);
                    }
                }


                if (columnPlan.getStartTime() == null) {
                    ((TextView) holders[i]).setBackgroundColor(Color.TRANSPARENT);
                    if (joinImg != null) {
                        joinImg.setVisibility(View.GONE);
                    }

                } else {
                    ((TextView) holders[i]).setBackgroundColor(Color.parseColor(planBgColorList.get(i - 1)));
                    if (joinImg != null && columnPlan.isHadApply()) {
                        joinImg.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        return v;
    }

    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimetableInfo xplanInfo = null;
            if (v.getTag() == null)
                return;
            else {
                xplanInfo = (TimetableInfo) v.getTag();
            }

            int clickPlanWeek = xplanInfo.getWeekDay();
            if (clickPlanWeek != -1 && clickPlanWeek != linkageScrollView.getSelectWeekIndex()) {
                FrameLayout parentFlayout = (FrameLayout) linkageScrollView.getTopBarFlayout()[clickPlanWeek];
                View[] topBarFlayoutChild = (View[]) parentFlayout.getTag();
                linkageScrollView.changeTopbarWeek(clickPlanWeek, parentFlayout, topBarFlayoutChild);
                if (xplanInfo.getStartTime() != null) {
                    Intent it = new Intent(context, CourseDetailsActivity.class);
                    it.putExtra("courseId", xplanInfo.getCourseId());
                    it.putExtra("className", xplanInfo.getCourseName());
                    it.putExtra("priceSale", xplanInfo.getPriceSale());
                    it.putExtra("priceOriginal", xplanInfo.getPriceOriginal());
                    it.putExtra("courseNum", xplanInfo.getCourseNum());
                    context.startActivity(it);
                }
            } else if (clickPlanWeek != -1) {
                if (xplanInfo.getStartTime() != null) {
                    Intent it = new Intent(context, CourseDetailsActivity.class);
                    it.putExtra("courseId", xplanInfo.getCourseId());
                    it.putExtra("className", xplanInfo.getCourseName());
                    it.putExtra("priceSale", xplanInfo.getPriceSale());
                    it.putExtra("priceOriginal", xplanInfo.getPriceOriginal());
                    it.putExtra("courseNum", xplanInfo.getCourseNum());
                    context.startActivity(it);
                }
            }
        }
    };
}