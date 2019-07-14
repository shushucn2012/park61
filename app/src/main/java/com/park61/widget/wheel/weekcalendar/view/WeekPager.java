package com.park61.widget.wheel.weekcalendar.view;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.widget.wheel.weekcalendar.WeekCalendar;
import com.park61.widget.wheel.weekcalendar.adapter.ViewPagerAdapter;
import com.park61.widget.wheel.weekcalendar.eventbus.BusProvider;
import com.park61.widget.wheel.weekcalendar.eventbus.Event;
import com.park61.widget.wheel.weekcalendar.fragment.WeekFragment;
import com.squareup.otto.Subscribe;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by nor on 12/5/2015.
 */
public class WeekPager extends ViewPager {
	private static final String TAG = "WeekCalendar";
	private ViewPagerAdapter adapter;
	private int pos;
	private boolean check;
	public static int NUM_OF_PAGES;
	private TypedArray typedArray;

	public WeekPager(Context context) {
		super(context);
		initialize(null);
	}

	public WeekPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(attrs);
	}

	private void initialize(AttributeSet attrs) {
		WeekFragment.selectedDateTime = new DateTime();
		WeekFragment.CalendarStartDate = WeekFragment.selectedDateTime;
		WeekFragment.EnabelStartDate = WeekFragment.selectedDateTime;
		WeekFragment.EnabelEndDate = WeekFragment.EnabelStartDate.plusDays(WeekCalendar.PAGE_SIZE * 7 - 1);
		if (attrs != null) {
			typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
//			NUM_OF_PAGES = typedArray.getInt(R.styleable.WeekCalendar_numOfPages, 100);
			NUM_OF_PAGES = WeekCalendar.PAGE_SIZE;
		}
		setId(idCheck());
		if (!isInEditMode()) {
			initPager(new DateTime());
			BusProvider.getInstance().register(this);
		}
	}

	private void initPager(DateTime dateTime) {
		// pos = NUM_OF_PAGES / 2;
		pos = 0;
		adapter = new ViewPagerAdapter(getContext(), ((BaseActivity) getContext()).getSupportFragmentManager(),
				dateTime, typedArray);
		setAdapter(adapter);
		this.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (!check) {
					if (position < pos) {
						adapter.swipeBack();
						WeekFragment.selectedDateTime = WeekFragment.selectedDateTime.plusDays(-7);
					} else if (position > pos) {
						adapter.swipeForward();
						WeekFragment.selectedDateTime = WeekFragment.selectedDateTime.plusDays(+7);
					}
				}
				pos = position;
				check = false;
				// if
				// (WeekFragment.selectedDateTime.isBefore(WeekFragment.EnabelStartDate)
				// ||
				// WeekFragment.selectedDateTime.isAfter(WeekFragment.EnabelEndDate))
				// {
				// return;
				// }
				if (WeekFragment.selectedDateTime.isBefore(WeekFragment.EnabelStartDate)) {
					WeekFragment.selectedDateTime = WeekFragment.EnabelStartDate;
				} else if (WeekFragment.selectedDateTime.isAfter(WeekFragment.EnabelEndDate)) {
					WeekFragment.selectedDateTime = WeekFragment.EnabelEndDate;
				}
				int dayPosition = Days.daysBetween(
						DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(new DateTime().toString("yyyy-MM-dd")),
						DateTimeFormat.forPattern("yyyy-MM-dd")
								.parseDateTime(WeekFragment.selectedDateTime.toString("yyyy-MM-dd")))
						.getDays();
				BusProvider.getInstance().post(new Event.OnDateClickEvent(WeekFragment.selectedDateTime, dayPosition));
				BusProvider.getInstance().post(new Event.InvalidateEvent());
			}
		});
		setOverScrollMode(OVER_SCROLL_NEVER);
		setCurrentItem(pos);
		if (typedArray != null)
			setBackgroundColor(typedArray.getColor(R.styleable.WeekCalendar_daysBackgroundColor,
					getContext().getResources().getColor(R.color.gededf7)));
		if (WeekFragment.selectedDateTime == null)
			WeekFragment.selectedDateTime = new DateTime();
	}

	@Subscribe
	public void setCurrentPage(Event.SetCurrentPageEvent event) {
		check = true;
		if (event.getDirection() == 1)
			adapter.swipeForward();
		else
			adapter.swipeBack();
		setCurrentItem(getCurrentItem() + event.getDirection());

	}

	@Subscribe
	public void reset(Event.ResetEvent event) {
		WeekFragment.selectedDateTime = new DateTime(WeekFragment.CalendarStartDate);
		// WeekFragment.CalendarStartDate = new DateTime();
		initPager(WeekFragment.CalendarStartDate);
	}

	@Subscribe
	public void setSelectedDate(Event.SetSelectedDateEvent event) {
		WeekFragment.selectedDateTime = event.getSelectedDate();
		initPager(event.getSelectedDate());
	}

	@Subscribe
	public void setStartDate(Event.SetStartDateEvent event) {
		WeekFragment.CalendarStartDate = event.getStartDate();
		WeekFragment.selectedDateTime = event.getStartDate();
		initPager(event.getStartDate());
	}

	private int idCheck() {
		int id = 0;
		while (findViewById(++id) != null)
			;
		return id;
	}
}
