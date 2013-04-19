package com.example.speechhelper;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProjectNoteList extends Activity {
	
	private Button projectNoteBack;
	private Button addNote;
	
	private int projectId;
	private ListView tabHostListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_note_list);
		
		projectId=this.getIntent().getIntExtra("projectId", -1);
		
		projectNoteBack =(Button)this.findViewById(R.id.projectNoteBack);
		addNote = (Button)this.findViewById(R.id.addNote);
		tabHostListView =(ListView)this.findViewById(R.id.tabHostListView);
		
		final DatabaseHelper db = new DatabaseHelper(this);
		
		noteUpdateAdapter() ;
		
		//long click to delete
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		tabHostListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final long tempId = id;
				builder.setMessage("Delete?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										db.deleteData("NoteTable", (int) tempId);
										noteUpdateAdapter();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});

				AlertDialog ad = builder.create();
				ad.show();
				return true;
			}
		});
		
		// click event
		tabHostListView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Cursor c = db.getWritableDatabase()
								.query("NoteTable",
										new String[] { "note_content", "start_time",
												"end_time" }, "_id=?",
										new String[] { String.valueOf(id) }, null,
										null, null, null);
						c.moveToFirst();
						Intent intent = new Intent(ProjectNoteList.this,EditNote.class);
						
						 //
						intent.putExtra("noteId", (int)id);
						intent.putExtra("noteContent", c.getString(0));
						intent.putExtra("startTime", c.getInt(1));
						intent.putExtra("endTime", c.getInt(2));
						
						//should be the same
						intent.putExtra("pid", projectId);
						//intent.putExtra("actId", R.layout.activity_project_note_list);  // activity id

						startActivityForResult(intent, 0);

					}
				});

		
		projectNoteBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent (ProjectNoteList.this, MyProjects.class);
				startActivityForResult(intent, 0);
			}
		});
		
		addNote.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProjectNoteList.this, NewNote.class);

				intent.putExtra("project_id", projectId);
				
				startActivityForResult(intent, 0);
				
			}
		});
	}
	
	public void noteUpdateAdapter() {
		DatabaseHelper db = new DatabaseHelper(this);
		
		Cursor c = db.query("NoteTable", projectId);
		Log.d("PID 2", String.valueOf(projectId));
		if (c != null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							"note_content", "project_id" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			tabHostListView.setAdapter(la);

		}

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		noteUpdateAdapter() ;

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
		getMenuInflater().inflate(R.menu.project_note_list, menu);
		return true;
	}

}
