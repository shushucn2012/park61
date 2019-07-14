package com.park61.moduel.acts.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.park61.R;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.child.bean.CapacityActVO;
import com.park61.moduel.shophome.bean.MyCourseDetailsBean;
import com.park61.widget.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

public class ActCapabilityChatAdapter {

    /**
     * 初始化能力维度图表
     */
    public static void initChart(RadarChart mChart, Context mContext, ActItem curAct) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");// 自定义文字样式
        mChart.setDescription("");

        mChart.setWebLineWidth(1.5f);
        mChart.setWebColor(Color.parseColor("#d9d9d9"));
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebColorInner(Color.parseColor("#d9d9d9"));
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        setData(mChart, tf, curAct);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(9f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(tf); // 文字样式
        yAxis.setTextSize(9f);// 文字大小

        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }

    private static void setData(RadarChart mChart, Typeface tf, ActItem curAct) {
        String[] mParties = new String[]{"语言智能  ", "逻辑智能  ", "空间智能  ",
                "运动智能  ", "音乐智能  ", "人际智能  ", "自我认知  ", "自然认知  "};
        // 维度数量
        int cnt = mParties.length;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        List<CapacityActVO> clist = curAct.getCapacityList();
        Collections.sort(clist, new Comparator<CapacityActVO>() {

            @Override
            public int compare(CapacityActVO lhs, CapacityActVO rhs) {
                try {
                    return lhs.getId().compareTo(rhs.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (int i = 0; i < cnt; i++) {
            CapacityActVO c = clist.get(i);
            try {
                yVals1.add(new Entry(c.getCapacityIncreaseValue().floatValue(), c.getId().intValue() - 1));
            } catch (Exception e) {
                e.printStackTrace();
                yVals1.add(new Entry(c.getCapacityIncreaseValue().floatValue(), i));
            }
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry(9f, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++) {
            xVals.add(mParties[i % mParties.length]);
        }

        RadarDataSet set1 = new RadarDataSet(yVals1, "");
        set1.setColor(Color.parseColor("#569FD0"));
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "");
        set2.setColor(Color.parseColor("#00000000"));
        set2.setDrawFilled(false);
        set2.setLineWidth(0f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * 初始化能力维度图表
     */
    public static void initChart(RadarChart mChart, Context mContext, MyCourseDetailsBean curAct) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");// 自定义文字样式
        mChart.setDescription("");

        mChart.setWebLineWidth(1.5f);
        mChart.setWebColor(Color.parseColor("#d9d9d9"));
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebColorInner(Color.parseColor("#d9d9d9"));
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        setData(mChart, tf, curAct);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(9f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(tf); // 文字样式
        yAxis.setTextSize(9f);// 文字大小

        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }

    private static void setData(RadarChart mChart, Typeface tf, MyCourseDetailsBean curAct) {
        String[] mParties = new String[curAct.getCapacityList().size()];
        for (int i = 0; i < curAct.getCapacityList().size(); i++) {
            mParties[i] = curAct.getCapacityList().get(i).getName();
        }
        // 维度数量
        int cnt = mParties.length;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        List<MyCourseDetailsBean.CapacityListBean> clist = curAct.getCapacityList();
        Collections.sort(clist, new Comparator<MyCourseDetailsBean.CapacityListBean>() {

            @Override
            public int compare(MyCourseDetailsBean.CapacityListBean lhs, MyCourseDetailsBean.CapacityListBean rhs) {
                try {
                    return lhs.getDimensionId().compareTo(rhs.getDimensionId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (int i = 0; i < cnt; i++) {
            MyCourseDetailsBean.CapacityListBean c = clist.get(i);
            try {
                yVals1.add(new Entry(c.getDimensionValue().floatValue(), c.getDimensionId().intValue() - 1));
            } catch (Exception e) {
                e.printStackTrace();
                yVals1.add(new Entry(c.getDimensionValue().floatValue(), i));
            }
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry(9f, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++) {
            xVals.add(mParties[i % mParties.length]);
        }

        RadarDataSet set1 = new RadarDataSet(yVals1, "");
        set1.setColor(Color.parseColor("#569FD0"));
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "");
        set2.setColor(Color.parseColor("#00000000"));
        set2.setDrawFilled(false);
        set2.setLineWidth(0f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.invalidate();
    }

}
