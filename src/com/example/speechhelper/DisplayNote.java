package com.example.speechhelper;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayNote extends Activity {

	private Button displayBack;
	private Button start;

	private TextView text;
	private TextView totalTime;
	private int i;
	private Cursor c;
	private TextView nextNote;
	private int startTime;
	private int endTime;
	private int projectTime;
	private Timer timer;
	private Handler handler;
	private TimerTask task;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_note);

		projectTime = this.getIntent().getIntExtra("projectTime", -1);

		final int id = this.getIntent().getIntExtra("projectId", -1);
		Log.d("pid", String.valueOf(id));

		displayBack = (Button) this.findViewById(R.id.displayBack);
		start = (Button) this.findViewById(R.id.start);
		text = (TextView) this.findViewById(R.id.text);
		nextNote = (TextView) this.findViewById(R.id.nextNote);
		totalTime = (TextView) this.findViewById(R.id.totalTime);

		text.setText("");
		nextNote.setText("");
		totalTime.setText("0");

		// get cursor
		DatabaseHelper db = new DatabaseHelper(this);
		c = db.getReadableDatabase().query("NoteTable",
				new String[] { "note_content", "start_time", "end_time" },
				"project_id=?", new String[] { String.valueOf(id) }, null,
				null, null, null);

		Log.d("count", String.valueOf(c.getCount()));

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (timer == null) {
					c.moveToFirst();
					startTime = c.getInt(1);
					endTime = c.getInt(2);
					timer = new Timer();
					handler = new Handler() {
						String content;

						public void handleMessage(Message msg) {
							//Log.d("count22", String.valueOf(msg.what));
							
							totalTime.setText(String.valueOf(msg.what));

							if (startTime == msg.what) {
								content = c.getString(0);
								text.setText(content);

								if (!c.isLast()) {
									c.moveToNext();

									content = c.getString(0);
									nextNote.setText(content);
								} else {
									nextNote.setText(null);
								}
							}
							if (endTime == msg.what) {
								text.setText(null);

								startTime = c.getInt(1);
								endTime = c.getInt(2);
							}
							if (projectTime * 60 == msg.what) {
								Toast t = Toast.makeText(DisplayNote.this,
										"Presentation Completed!",
										Toast.LENGTH_SHORT);
								t.show();
								i = 0;

								timer.cancel();
								task.cancel();  //close timer

								timer = null;
								task = null;
								handler = null;

							}
							super.handleMessage(msg);
						}

					};

					task = new TimerTask() {

						public void run() {
							i++;
							Message message = new Message();
							message.what = i;
							handler.sendMessage(message);
						}
					};

					timer.schedule(task, 1000, 1000);
				}
			}
		});

		displayBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DisplayNote.this, Presentation.class);
				if(timer!=null){
				timer.cancel();
				task.cancel();
				
				timer = null;
				task = null;
				handler = null;
				}
				
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_note, menu);
		return true;
	}
}
