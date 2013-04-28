package com.example.speechhelper.project;

import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
							MyProjects.class);
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
					
					if(!projectTitle.getText().toString().equals("")){
						if(!time.getText().toString().equals("")){
							
							_projectTitle = projectTitle.getText().toString();
							_time =Integer.parseInt(time.getText().toString());
							
							DatabaseHelper db = new DatabaseHelper(getApplicationContext());
							db.addProjectData(_projectTitle, _time);
							
							Intent intent = new Intent(NewProject.this, MyProjects.class);
							setResult(RESULT_OK, intent);
			                finish();	
							
						}else{
							Toast t = Toast.makeText(NewProject.this,
									"Please enter project total time",
									Toast.LENGTH_SHORT);
							 t.setGravity(Gravity.CENTER, 0, 0);
							t.show();
						}
					}else{
						Toast t2 = Toast.makeText(NewProject.this,
								"Please enter project name",
								Toast.LENGTH_SHORT);
						t2.setGravity(Gravity.CENTER, 0, 0);
						t2.show();
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
		getMenuInflater().inflate(R.menu.new_project, menu);
		return true;
	}

}
