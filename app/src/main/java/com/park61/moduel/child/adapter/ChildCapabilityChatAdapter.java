package com.park61.moduel.child.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.park61.R;
import com.park61.moduel.child.bean.CapacityChildAllVO;
import com.park61.moduel.child.bean.CapacityChildVO;
import com.park61.widget.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

public class ChildCapabilityChatAdapter {

	/**
	 * 初始化能力维度图表
	 */
	public static void initChart(RadarChart mChart, Context mContext,
			CapacityChildAllVO co) {
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
				"OpenSans-Regular.ttf");// 自定义文字样式
		mChart.setDescription("");

		mChart.setWebLineWidth(1.5f);
//		mChart.setWebColor(Color.parseColor("#d9d9d9"));
		mChart.setWebColor(Color.parseColor("#ff6b69"));
		mChart.setWebLineWidthInner(0.75f);
//		mChart.setWebColorInner(Color.parseColor("#d9d9d9"));
		mChart.setWebColorInner(Color.parseColor("#ff6b69"));
		mChart.setWebAlpha(100);

		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		MyMarkerView mv = new MyMarkerView(mContext,
				R.layout.custom_marker_view);

		// set the marker to the chart
		mChart.setMarkerView(mv);

		setData(mChart, tf, co);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setTypeface(tf);
		xAxis.setTextSize(9f);

		YAxis yAxis = mChart.getYAxis();
		yAxis.setTypeface(tf); // 文字样式
		yAxis.setTextSize(9f);// 文字大小

		Legend l = mChart.getLegend();
		l.setEnabled(false);
	}

	private static void setData(RadarChart mChart, Typeface tf,
			CapacityChildAllVO co) {
		// String[] mParties = new String[] { "语言智能  ", "逻辑智能  ", "空间智能  ",
		// "运动智能  ", "音乐智能  ", "人际智能  ", "自我认知  ", "自然认知  " };
		// 维度数量

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		ArrayList<Entry> yVals2 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		List<CapacityChildVO> clist = co.getList();
		Collections.sort(clist, new Comparator<CapacityChildVO>() {

			@Override
			public int compare(CapacityChildVO lhs, CapacityChildVO rhs) {
				try {
					return lhs.getId().compareTo(rhs.getId());
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		});

		int cnt = clist.size();

		for (int i = 0; i < cnt; i++) {
			CapacityChildVO c = clist.get(i);
			try {
				yVals1.add(new Entry(c.getCapacityValue().floatValue(), c
						.getId().intValue() - 1));
			} catch (Exception e) {
				e.printStackTrace();
				yVals1.add(new Entry(c.getCapacityValue().floatValue(), i));
			}
		}

		for (int i = 0; i < cnt; i++) {
			yVals2.add(new Entry(9f, i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < cnt; i++)
			xVals.add(clist.get(i).getCapacityDimensionName());

		RadarDataSet set1 = new RadarDataSet(yVals1, "");
//		set1.setColor(Color.parseColor("#569FD0"));
		set1.setColor(Color.parseColor("#ff6a6a"));
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
