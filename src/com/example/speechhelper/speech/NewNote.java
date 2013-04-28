package com.example.speechhelper.speech;

import com.example.speechhelper.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewNote extends Activity {

	private EditText noteContentText; // accepts user input for new note content
	private EditText startTimeText; // accepts user input for start time
	private EditText endTimeText; // accepts user input for end time

	// private String noteContent ;
	// private int startTime;
	// private int endTime;

	private Button newNoteBack;
	private Button newNoteCancel;

	private int projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);

		projectId = this.getIntent().getIntExtra("project_id", -1);

		noteContentText = (EditText) findViewById(R.id.noteContentText);
		startTimeText = (EditText) findViewById(R.id.startTimeText);
		endTimeText = (EditText) findViewById(R.id.endTimeText);
		newNoteCancel = (Button) this.findViewById(R.id.newNoteCancel);

		//final int actId = this.getIntent().getIntExtra("actId", -1);

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
					Log.d("tag4", Integer.toString(projectId)); // log 4
					if (projectId != -1) {
						String noteContent = noteContentText.getText()
								.toString();
						int startTime = Integer.parseInt(startTimeText
								.getText().toString());
						int endTime = Integer.parseInt(endTimeText.getText()
								.toString());

						DatabaseHelper db = new DatabaseHelper(
								getApplicationContext());

						db.addNoteData(noteContent, startTime, endTime,
								projectId);
					}

			

					
						Intent intent = new Intent(NewNote.this,
								ProjectNoteList.class);
						intent.putExtra("projectIdBack", projectId);
						setResult(RESULT_OK, intent);
						finish();
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// get value from edittext
		/* *****************
		 * 
		 * ********************
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

}
