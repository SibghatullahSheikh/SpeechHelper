package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Notes extends Activity {

	private Button newNoteBack;
	private Button addNewNote;
	private ListView noteListView;
	private int projectId;
	
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		noteUpdateAdapter();

		 // get id
						// from new
																	// project

		addNewNote = (Button) findViewById(R.id.addNewNote);
		newNoteBack = (Button) findViewById(R.id.notesBack);
		

		newNoteBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					Intent intent = new Intent(Notes.this, NewProject.class);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		addNewNote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					Intent intent = new Intent(Notes.this, NewNote.class);

					intent.putExtra("project_id", projectId);

					startActivityForResult(intent, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void noteUpdateAdapter() {
		projectId = this.getIntent().getIntExtra("projectId", -1);
		Log.d("tag2", Integer.toString(projectId));	 //log 
		DatabaseHelper db = new DatabaseHelper(this);
		//String[] columns = {"_id","note_content","start_time","end_time"};
		//Cursor c = db.query("NoteTable", projectId,columns);
		Cursor c =db.query("NoteTable",projectId );
		//Cursor c = db.query("NoteTable");
		Log.d("tag3", Integer.toString(c.getCount()));  //log
		if (c!= null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
					"note_content","project_id" }, new int[] {
					android.R.id.text1, android.R.id.text2 });
			noteListView = (ListView) this.findViewById(R.id.noteListView);
			noteListView.setAdapter(la);
			
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		noteUpdateAdapter();
		switch (resultCode) { // resultCode
		case RESULT_OK:
			Bundle b = data.getExtras(); //
			int pid = b.getInt("projectIdBack");
			projectId = pid;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes, menu);
		return true;
	}

}
