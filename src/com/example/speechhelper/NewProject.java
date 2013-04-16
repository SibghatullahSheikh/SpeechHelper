package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewProject extends Activity {
	private EditText projectTitle; // accepts user input for project title
	private EditText time; // accepts user input for time
	private String _projectTitle;
	private int _time;
	
	private Button newProjectDone;
	private Button newProjectBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		try {
			projectTitle = (EditText) findViewById(R.id.projectNameText);
			time = (EditText) findViewById(R.id.speechLengthText);
			
			//_projectTitle = projectTitle.getText().toString();
			//_time = Double.parseDouble(time.getText().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		newProjectBack = (Button) findViewById(R.id.newProjectBack);
		newProjectDone = (Button) findViewById(R.id.newProjectDone);

		newProjectBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					Intent intent = new Intent(NewProject.this,
							MainActivity.class);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		newProjectDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					Intent intent = new Intent(NewProject.this, Notes.class);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_project, menu);
		return true;
	}

}
