package com.example.speechhelper.note;

import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;
import com.example.speechhelper.project.ProjectTabHost;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends Activity {

	private EditText editNoteContentText;
	private EditText editStartTime;
	private EditText editEndTime;
	private Button editNoteDone;
	private Button editNoteCancel;

	private int projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);

		final DatabaseHelper db = new DatabaseHelper(this); // db

		projectId = this.getIntent().getIntExtra("pid", -1);

		Cursor c = db.getWritableDatabase().query("ProjectTable",
				new String[] { "project_time" }, "_id=?",
				new String[] { String.valueOf(projectId) }, null, null, null);
		c.moveToFirst();
		final int totalTime = c.getInt(0);
		db.close();

		final int noteId = this.getIntent().getIntExtra("noteId", -1);
		final String noteContent = this.getIntent().getStringExtra(
				"noteContent");
		final int startTime = this.getIntent().getIntExtra("startTime", -1);
		final int endTime = this.getIntent().getIntExtra("endTime", -1);

		// final int actId =this.getIntent().getIntExtra("actId", -1);

		editNoteContentText = (EditText) this
				.findViewById(R.id.editNoteContentText);
		editStartTime = (EditText) this.findViewById(R.id.editStartTime);
		editEndTime = (EditText) this.findViewById(R.id.editEndTime);

		editNoteContentText.setText(noteContent);
		editStartTime.setText(String.valueOf(startTime));
		editEndTime.setText(String.valueOf(endTime));

		editNoteCancel = (Button) this.findViewById(R.id.editNoteCancel);
		editNoteCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(EditNote.this, ProjectTabHost.class);
				intent.putExtra("projectIdBack", projectId);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

		editNoteDone = (Button) this.findViewById(R.id.editNoteDone);
		editNoteDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!editNoteContentText.getText().toString().equals("")) {
					String contentText = editNoteContentText.getText()
							.toString();
					if (!editStartTime.getText().toString().equals("")) {
						int st = Integer.parseInt(editStartTime.getText()
								.toString()); // not null
						if (!editEndTime.getText().toString().equals("")) {
							int et = Integer.parseInt(editEndTime.getText()
									.toString());
							if (st < et) {
								if (st > totalTime * 60 || et > totalTime * 60) {
									Toast t4 = Toast
											.makeText(
													EditNote.this,
													"Start time or end time exceeds total time, check agian",
													Toast.LENGTH_SHORT);
									t4.setGravity(Gravity.CENTER, 0, 0);
									t4.show();
								} else {

									db.updateNote(contentText, st, et, noteId);

									Log.d("PID", String.valueOf(projectId));
									Intent intent = new Intent(EditNote.this,
											ProjectTabHost.class);
									intent.putExtra("projectIdBack", projectId);
									setResult(RESULT_OK, intent);
									finish();
								}
							} else {
								Toast t4 = Toast
										.makeText(
												EditNote.this,
												"End time should follow start time, please check agian",
												Toast.LENGTH_SHORT);
								t4.setGravity(Gravity.CENTER, 0, 0);
								t4.show();
							}
						} else {
							Toast t4 = Toast
									.makeText(EditNote.this,
											"Please enter end time",
											Toast.LENGTH_SHORT);
							t4.setGravity(Gravity.CENTER, 0, 0);
							t4.show();
						}
					} else {
						Toast t4 = Toast.makeText(EditNote.this,
								"Please enter start time", Toast.LENGTH_SHORT);
						t4.setGravity(Gravity.CENTER, 0, 0);
						t4.show();
					}
				} else {
					Toast t4 = Toast.makeText(EditNote.this,
							"Please enter note content", Toast.LENGTH_SHORT);
					t4.setGravity(Gravity.CENTER, 0, 0);
					t4.show();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_note, menu);
		return true;
	}

}
