package com.park61.widget.wheel.weekcalendar.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.widget.wheel.weekcalendar.WeekCalendar;
import com.park61.widget.wheel.weekcalendar.fragment.WeekFragment;

import org.joda.time.DateTime;

import java.util.Calendar;

import static com.park61.widget.wheel.weekcalendar.fragment.WeekFragment.DATE_KEY;
import static com.park61.widget.wheel.weekcalendar.view.WeekPager.NUM_OF_PAGES;


/**
 * Created by nor on 12/4/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private static final String TAG = "PagerAdapter";
	private int currentPage = 0;
	private DateTime date;
	private TypedArray typedArray;
	private Context context;

	public ViewPagerAdapter(Context context, FragmentManager fm, DateTime date, TypedArray typedArray) {
		super(fm);
		this.date = date;
		this.typedArray = typedArray;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		WeekFragment fragment = new WeekFragment();
		Bundle bundle = new Bundle();

		// if (position - currentPage > 0) {
		// fragment.fragmentPageIndex = position - currentPage;
		// } else {
		// fragment.fragmentPageIndex = 0;
		// }

		if (position < currentPage)
			bundle.putSerializable(DATE_KEY, getPerviousDate());
		else if (position > currentPage)
			bundle.putSerializable(DATE_KEY, getNextDate());
		else
			bundle.putSerializable(DATE_KEY, getTodaysDate());

		bundle.putFloat(WeekFragment.TEXT_SIZE_KEY, typedArray.getDimension(R.styleable.WeekCalendar_daysTextSize, -1));
		bundle.putInt(WeekFragment.TEXT_COLOR_KEY,
				typedArray.getColor(R.styleable.WeekCalendar_daysTextColor, Color.WHITE));
		bundle.putInt(WeekFragment.TODAYS_DATE_COLOR_KEY, typedArray.getColor(
				R.styleable.WeekCalendar_todaysDateBgColor, context.getResources().getColor(R.color.colorAccent)));
		bundle.putInt(WeekFragment.SELECTED_DATE_COLOR_KEY, typedArray.getColor(
				R.styleable.WeekCalendar_selectedBgColor, context.getResources().getColor(R.color.colorAccent)));
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		Calendar c = Calendar.getInstance();
		String weekStr = DateTool.getWeekOfDate(c.getTime());
		if (weekStr.equals("周日")) {
			return NUM_OF_PAGES;
		} else {
			return NUM_OF_PAGES + 1;
		}
	}

//	private DateTime getTodaysDate() {
//		return date;
//	}
//
//	private DateTime getPerviousDate() {
//		return date.plusDays(-7);
//	}
//
//	private DateTime getNextDate() {
//		return date.plusDays(7);
//	}

	private DateTime getTodaysDate() {
		return getCount()== WeekCalendar.PAGE_SIZE?date.plusDays(7):date;
	}

	private DateTime getPerviousDate() {
		return getCount()== WeekCalendar.PAGE_SIZE?date.plusDays(-14): date.plusDays(-7);
	}

	private DateTime getNextDate() {
		return getCount()== WeekCalendar.PAGE_SIZE?date.plusDays(14):date.plusDays(7);
	}

	/**
	 * 向后翻日历
	 */
	public void swipeBack() {
		date = date.plusDays(-7);
		currentPage--;
		currentPage = currentPage <= 1 ? 0 : currentPage;
	}

	/**
	 * 向前翻日历
	 */
	public void swipeForward() {
		date = date.plusDays(7);
		currentPage++;
		currentPage = currentPage >= NUM_OF_PAGES - 1 ? 0 : currentPage;

	}

	/*
	 * public DateTime getDate() { return date; }
	 * 
	 * public int getCurrentPage() { return currentPage; }
	 * 
	 * public void setCurrentPage(int currentPage) { this.currentPage =
	 * currentPage; }
	 * 
	 * public void setDate(DateTime date) { this.date = date; }
	 */

}
