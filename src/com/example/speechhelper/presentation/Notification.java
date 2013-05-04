package com.example.speechhelper.presentation;

import java.util.Calendar;

import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Notification extends Activity {

	private String title;
	private int selectedYear;
	private int month;
	private int day;

	private int startHour = -1;
	private int startMinute = -1;

	private int endHour = -1;
	private int endMinute = -1;

	private Button doneBtn;
	private Button cancelBtn;
	private EditText titleText;
	private EditText descText;
	private EditText dateText;
	private EditText stText;
	private EditText etText;

	private static String calendarURL;
	private static String calendarEventURL;
	private static String calendarRemiderURL;

	static {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			calendarURL = "content://com.android.calendar/calendars";
			calendarEventURL = "content://com.android.calendar/events";
			calendarRemiderURL = "content://com.android.calendar/reminders";

		} else {
			calendarURL = "content://calendar/calendars";
			calendarEventURL = "content://calendar/events";
			calendarRemiderURL = "content://calendar/reminders";
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		doneBtn = (Button) this.findViewById(R.id.doneBtn);
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		titleText = (EditText) this.findViewById(R.id.titleText);
		descText = (EditText) this.findViewById(R.id.descText);
		dateText = (EditText) this.findViewById(R.id.dateText);
		stText = (EditText) this.findViewById(R.id.stText);
		etText = (EditText) this.findViewById(R.id.etText);

		final int projectId = this.getIntent().getIntExtra("project_id", -1); // get
		final Calendar mCalendar = Calendar.getInstance(); // project
		// id
		final DatabaseHelper db = new DatabaseHelper(this); // db helper
		Cursor c = db.getWritableDatabase().query("ProjectTable",
				new String[] { "project_name" }, "_id=?",
				new String[] { String.valueOf(projectId) }, null, null, null,
				null);
		Log.d("C", String.valueOf(c.getCount())); // log
		if (c.getCount() > 0) {
			c.moveToFirst();
			title = c.getString(0);
			Log.d("title", title); // log
			titleText.setText(title); // set title
		}

		dateText.setOnClickListener(new View.OnClickListener() { // set date
			@Override
			public void onClick(View v) { // set a default date
				// TODO Auto-generated method stub
				int mYear = mCalendar.get(Calendar.YEAR);
				int mMonth = mCalendar.get(Calendar.MONTH);
				int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
				new DatePickerDialog(Notification.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								selectedYear = year;
								month = monthOfYear; // get year month day
								day = dayOfMonth;
								dateText.setText(year + ", "
										+ (monthOfYear + 1) + ", " + dayOfMonth);
							}
						}, mYear, mMonth, mDay).show();
			}
		});

		stText.setOnClickListener(new View.OnClickListener() { // set start time

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = mCalendar.get(Calendar.MINUTE);
				new TimePickerDialog(Notification.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								startHour = hourOfDay;
								startMinute = minute;
								stText.setText(hourOfDay + ": " + minute);
							}
						}, mHour, mMinute, true).show();
			}
		});

		etText.setOnClickListener(new View.OnClickListener() { // set end time
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = mCalendar.get(Calendar.MINUTE);
				new TimePickerDialog(Notification.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								endHour = hourOfDay;
								endMinute = minute;
								etText.setText(hourOfDay + ": " + minute);
							}
						}, mHour, mMinute, true).show();
			}
		});

		final Cursor userCursor = getContentResolver().query(
				Uri.parse(calendarURL), null, null, null, null);

		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (userCursor.getCount() > 0) {
					userCursor.moveToFirst();
					String calId = userCursor.getString(userCursor
							.getColumnIndex("_id")); // get calendar id
					Log.d("calid", calId + " + ");
					Log.d("selectedYear", String.valueOf(selectedYear));
					Log.d("month", String.valueOf(month));
					Log.d("day", String.valueOf(day));
					Log.d("startHour", String.valueOf(startHour));
					Log.d("startMinute", String.valueOf(startMinute));
					Log.d("endHour", String.valueOf(endHour));
					Log.d("endMinute", String.valueOf(endMinute));

					ContentValues event = new ContentValues();
					event.put("calendar_id", calId);
					event.put("eventTimezone", Time.getCurrentTimezone());
					if (!titleText.getText().toString().equals("")) {
						event.put("title", titleText.getText().toString());
						if (!descText.getText().toString().equals("")) {
							event.put("description", descText.getText()
									.toString());
							if ((selectedYear != 0) && (day != 0)) {
								if ((startHour != -1) && (startMinute != -1)) {
									if ((endHour != -1) && (endMinute != -1)) {
										
											if (((startHour==endHour)&&(startMinute<endMinute))||(startHour < endHour))
										{
											Calendar start = Calendar
													.getInstance();
											start.set(selectedYear, month, day,
													startHour, startMinute);
											long startTime = start
													.getTimeInMillis();
											Calendar end = Calendar
													.getInstance();
											end.set(selectedYear, month, day,
													endHour, endMinute);
											long endTime = end
													.getTimeInMillis();

											event.put("dtstart", startTime);
											event.put("dtend", endTime);
											event.put("hasAlarm", 1);

											Uri newEvent = getContentResolver()
													.insert(Uri
															.parse(calendarEventURL),
															event);
											long id = Long.parseLong(newEvent
													.getLastPathSegment());
											ContentValues values = new ContentValues();
											values.put("event_id", id);

											values.put("minutes", 30); // 30
																		// minute
											getContentResolver()
													.insert(Uri
															.parse(calendarRemiderURL),
															values);
											Toast t = Toast
													.makeText(
															Notification.this,
															"Event succesfully inserted",
															Toast.LENGTH_SHORT);
											t.setGravity(Gravity.CENTER, 0, 0);
											t.show();
										} else {
											Toast t = Toast
													.makeText(
															Notification.this,
															"End time should be after start time",
															Toast.LENGTH_SHORT);
											t.setGravity(Gravity.CENTER, 0, 0);
											t.show();
										}
									} else {
										Toast t = Toast.makeText(
												Notification.this,
												"Please select a end time",
												Toast.LENGTH_SHORT);
										t.setGravity(Gravity.CENTER, 0, 0);
										t.show();
									}
								} else {
									Toast t = Toast.makeText(Notification.this,
											"Please select a start time",
											Toast.LENGTH_SHORT);
									t.setGravity(Gravity.CENTER, 0, 0);
									t.show();
								}
							} else {
								Toast t = Toast.makeText(Notification.this,
										"Please select a date",
										Toast.LENGTH_SHORT);
								t.setGravity(Gravity.CENTER, 0, 0);
								t.show();
							}
						} else {
							Toast t = Toast.makeText(Notification.this,
									"Please enter a description",
									Toast.LENGTH_SHORT);
							t.setGravity(Gravity.CENTER, 0, 0);
							t.show();
						}

					} else {
						Toast t = Toast.makeText(Notification.this,
								"Please enter a title", Toast.LENGTH_SHORT);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

					}
				}
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Notification.this,
						Presentation.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}
}
