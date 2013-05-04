package com.example.speechhelper.note;

import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewNote extends Activity {

	private EditText noteContentText; // accepts user input for new note content
	private EditText startTimeText; // accepts user input for start time
	private EditText endTimeText; // accepts user input for end time

	private Button newNoteBack;
	private Button newNoteCancel;

	private int projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);

		projectId = this.getIntent().getIntExtra("project_id", -1);
		final DatabaseHelper db = new DatabaseHelper(
				getApplicationContext());
		Cursor c =db.getWritableDatabase().query("ProjectTable", new String[]{"project_time"}, "_id=?", new String[]{String.valueOf(projectId)}, null, null, null);
		c.moveToFirst();
		final int totalTime = c.getInt(0);
		db.close();


		noteContentText = (EditText) findViewById(R.id.noteContentText);
		startTimeText = (EditText) findViewById(R.id.startTimeText);
		endTimeText = (EditText) findViewById(R.id.endTimeText);
		newNoteCancel = (Button) this.findViewById(R.id.newNoteCancel);


		newNoteCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
					Intent intent = new Intent(NewNote.this,
							ProjectNoteList.class);
					intent.putExtra("projectIdBack", projectId);
					setResult(RESULT_OK, intent);
					finish();
			}
		});

		newNoteBack = (Button) findViewById(R.id.newNoteBack);
		newNoteBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					//Log.d("tag4", Integer.toString(projectId)); // log 4
					if (projectId != -1) {
						String noteContent = noteContentText.getText()
								.toString();
						if(!noteContent.equals("")){
							if(!startTimeText
									.getText().toString().equals("")){
								if(!endTimeText.getText()
										.toString().equals("")){
									//to do
									int startTime = Integer.parseInt(startTimeText
											.getText().toString());
									int endTime = Integer.parseInt(endTimeText.getText()
											.toString());

								if(startTime>totalTime*60||endTime>totalTime*60){
										
										Toast t4 = Toast.makeText(NewNote.this,
												"Start time or end time exceeds total time, check agian",
												Toast.LENGTH_SHORT);
										t4.setGravity(Gravity.CENTER, 0, 0);
										t4.show();
										
									}else{
										if(startTime<endTime){
									db.addNoteData(noteContent, startTime, endTime,
											projectId);
									
									Intent intent = new Intent(NewNote.this,
											ProjectNoteList.class);
									intent.putExtra("projectIdBack", projectId);
									setResult(RESULT_OK, intent);
									finish();
										}else{
											Toast t4 = Toast.makeText(NewNote.this,
													"End time should follow start time, please check agian",
													Toast.LENGTH_SHORT);
											t4.setGravity(Gravity.CENTER, 0, 0);
											t4.show();
										}
									}

								}else{
									Toast t3 = Toast.makeText(NewNote.this,
											"Please enter end time",
											Toast.LENGTH_SHORT);
									t3.setGravity(Gravity.CENTER, 0, 0);
									t3.show();
								}
								
							}else{
								Toast t2 = Toast.makeText(NewNote.this,
										"Please enter start time",
										Toast.LENGTH_SHORT);
								t2.setGravity(Gravity.CENTER, 0, 0);
								t2.show();
							}
							
						}else{
							Toast t = Toast.makeText(NewNote.this,
									"Please enter note content",
									Toast.LENGTH_SHORT);
							t.setGravity(Gravity.CENTER, 0, 0);
							t.show();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

}
