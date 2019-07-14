package com.park61.widget.wheel.weekcalendar.fragment;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import com.park61.R;
import com.park61.moduel.shop.bean.ShopActWeekDateBean;
import com.park61.widget.wheel.weekcalendar.WeekCalendar;
import com.park61.widget.wheel.weekcalendar.eventbus.BusProvider;
import com.park61.widget.wheel.weekcalendar.eventbus.Event;
import com.squareup.otto.Subscribe;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by nor on 12/4/2015.
 */
public class WeekFragment extends Fragment {
	private static final String TAG = "WeekFragment";
	public static final String TEXT_SIZE_KEY = "text_size";
	public static final String TEXT_COLOR_KEY = "text_color";
	public static final String SELECTED_DATE_COLOR_KEY = "selected_color";
	public static final String TODAYS_DATE_COLOR_KEY = "todays_color";
	public static String DATE_KEY = "date_key";
	public static String WEEk_KEY = "week_key";
	private GridView gridView;
	private WeekAdapter weekAdapter;
	public static DateTime selectedDateTime = new DateTime();
	public static DateTime CalendarStartDate = new DateTime();
	public static DateTime EnabelStartDate = new DateTime();
	public static DateTime EnabelEndDate = new DateTime();
	private DateTime startDate, endDate, midDate;
	private boolean isVisible;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_week, container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		init();
		return rootView;
	}

	private void init() {
		ArrayList<ShopActWeekDateBean> days = new ArrayList<>();
		midDate = (DateTime) getArguments().getSerializable(DATE_KEY);
		midDate = midDate.withDayOfWeek(DateTimeConstants.THURSDAY);
		// Getting all seven days

		for (int i = -4; i <= 2; i++) {
			ShopActWeekDateBean shopActWeekDateBean = new ShopActWeekDateBean();
			shopActWeekDateBean.setDateNormal(midDate.plusDays(i));
			shopActWeekDateBean.setStrDate(String.valueOf(shopActWeekDateBean.getDateNormal().getDayOfMonth()));

			int day = Days.daysBetween(
					DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(new DateTime().toString("yyyy-MM-dd")),
					DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(midDate.plusDays(i).toString("yyyy-MM-dd")))
					.getDays();
			shopActWeekDateBean.setPosition(day);
			days.add(shopActWeekDateBean);
		}

		startDate = days.get(0).getDateNormal();
		endDate = days.get(days.size() - 1).getDateNormal();

		weekAdapter = new WeekAdapter(getActivity(), days);
		gridView.setAdapter(weekAdapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (weekAdapter.getItem(position).getDateNormal().isBefore(EnabelStartDate)
						|| weekAdapter.getItem(position).getDateNormal().isAfter(EnabelEndDate)) {
					return;
				}
				BusProvider.getInstance().post(new Event.OnDateClickEvent(weekAdapter.getItem(position).getDateNormal(),
						weekAdapter.getItem(position).getPosition()));
				selectedDateTime = weekAdapter.getItem(position).getDateNormal();
				BusProvider.getInstance().post(new Event.InvalidateEvent());
			}
		});
	}

	@Subscribe
	public void updateSelectedDate(Event.UpdateSelectedDateEvent event) {
		if (isVisible) {
			selectedDateTime = selectedDateTime.plusDays(event.getDirection());
			if (selectedDateTime.toLocalDate().equals(endDate.plusDays(1).toLocalDate())
					|| selectedDateTime.toLocalDate().equals(startDate.plusDays(-1).toLocalDate())) {
				if (!(selectedDateTime.toLocalDate().equals(startDate.plusDays(-1).toLocalDate())
						&& event.getDirection() == 1)
						&& !(selectedDateTime.toLocalDate().equals(endDate.plusDays(1).toLocalDate())
								&& event.getDirection() == -1))
					BusProvider.getInstance().post(new Event.SetCurrentPageEvent(event.getDirection()));
			}
			BusProvider.getInstance().post(new Event.InvalidateEvent());
		}
	}

	@Subscribe
	public void invalidate(Event.InvalidateEvent event) {
		gridView.invalidateViews();
	}

	@Override
	public void onResume() {
		BusProvider.getInstance().register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		BusProvider.getInstance().unregister(this);
		super.onPause();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		isVisible = isVisibleToUser;
		super.setUserVisibleHint(isVisibleToUser);
	}

	private class WeekAdapter extends BaseAdapter {
		private ArrayList<ShopActWeekDateBean> days;
		private Context context;

		public WeekAdapter(Context context, ArrayList<ShopActWeekDateBean> days) {
			this.days = days;
			this.context = context;
		}

		@Override
		public int getCount() {
			return days.size();
		}

		@Override
		public ShopActWeekDateBean getItem(int position) {
			return days.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.grid_item, null);
			}

			TextView day = (TextView) convertView.findViewById(R.id.daytext);
			DateTime dateTime = getItem(position).getDateNormal();
			// DateTime dt = new DateTime();

			// Drawable holoCircle =
			// context.getDrawable(R.drawable.holo_circle);
			// 选中后的背景颜色
			Drawable solidCircle = context.getResources().getDrawable(R.drawable.solid_circle);

			// holoCircle.setColorFilter(getArguments().getInt(SELECTED_DATE_COLOR_KEY),
			// PorterDuff.Mode.SRC_ATOP);
			solidCircle.setColorFilter(getArguments().getInt(TODAYS_DATE_COLOR_KEY), PorterDuff.Mode.SRC_ATOP);
			// solidCircle.mutate().setAlpha(200);
			// holoCircle.mutate().setAlpha(200);
			if (dateTime.toLocalDate().equals(CalendarStartDate.toLocalDate())) {
				day.setBackground(solidCircle);
				day.setTextColor(Color.WHITE);
			}
			if (selectedDateTime != null) {
				if (selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {
					if (!selectedDateTime.toLocalDate().equals(CalendarStartDate.toLocalDate()))
						day.setBackground(solidCircle);
				} else {
					day.setBackground(null);
				}
			}
			day.setText(String.valueOf(dateTime.getDayOfMonth()));
			day.setTextColor(getArguments().getInt(TEXT_COLOR_KEY));

			if (DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dateTime.toString("yyyy-MM-dd"))
					.isBefore(DateTimeFormat.forPattern("yyyy-MM-dd")
							.parseDateTime(CalendarStartDate.toString("yyyy-MM-dd")))
					|| DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dateTime.toString("yyyy-MM-dd"))
							.isAfter(DateTimeFormat.forPattern("yyyy-MM-dd")
									.parseDateTime(CalendarStartDate.toString("yyyy-MM-dd"))
									.plusDays(WeekCalendar.PAGE_SIZE * 7 - 1))) {
				day.setTextColor(Color.GRAY);
			}
			float size = getArguments().getFloat(TEXT_SIZE_KEY);
			if (size == -1)
				size = day.getTextSize();
			day.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
			return convertView;
		}
	}
}
