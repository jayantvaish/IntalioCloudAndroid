package com.intalio.cloud.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.intalio.cloud.android.common.Constants;

/**
 * It defines view of Calendar type.
 *
 */
public class Calendar extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalendarView calendar = new CalendarView(this);
		calendar.setClickable(true);
		
		calendar.setShowWeekNumber(false);
		calendar.setOnDateChangeListener(dateSetListener);
		setContentView(calendar);
	}
	
	/**
	 * Defines event to happen on date change of Calendar.
	 */
	private CalendarView.OnDateChangeListener dateSetListener = new CalendarView.OnDateChangeListener() {
		public void onSelectedDayChange(CalendarView view, int year,
				int monthOfYear, int dayOfMonth) {
			int selectedYear = year;
			int selectedMonth = monthOfYear;
			int selectedDay = dayOfMonth;
			String selectedDate = new StringBuilder().append(selectedYear).append("-").append(selectedMonth + 1).append("-")
					.append(selectedDay).toString();
	
			Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.putExtra(Constants.TASK_SELECTED_DATE, selectedDate);
			intent.putExtra(Constants.OBJECT, Constants.OBJECT_TASKS);
			intent.setClass(view.getContext(), ListRecords.class);
			startActivity(intent);   //Issue: There is no onDateClickListener.
		}
	};

}
