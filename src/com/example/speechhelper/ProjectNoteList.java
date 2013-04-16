package com.example.speechhelper;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ProjectNoteList extends Activity {
	
	private Button projectNoteBack;
	private Button addNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_note_list);
		
		projectNoteBack =(Button)this.findViewById(R.id.projectNoteBack);
		addNote = (Button)this.findViewById(R.id.addNote);
		
		projectNoteBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent (ProjectNoteList.this, MyProjects.class);
				startActivityForResult(intent, 0);
			}
		});
		
		//addNote **************
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_note_list, menu);
		return true;
	}

}
