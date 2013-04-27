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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyProjects extends Activity {

	private Button newNoteBack;
	private ListView projectListView;

	private Button addNewProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_projects);
		projectListView = (ListView) this.findViewById(R.id.projectListView);

		final DatabaseHelper db = new DatabaseHelper(this);

		projectUpdateAdapter();

		// click event
		projectListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(MyProjects.this,
						ProjectTabHost.class);

				intent.putExtra("projectId", (int) id); // pass project_id
				

				startActivityForResult(intent, 0);
			}
		});

		// long click to delete

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		projectListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						final long tempId = id;
						builder.setMessage("Delete?")
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												db.deleteData("ProjectTable",
														(int) tempId);
												db.getWritableDatabase()
														.delete("NoteTable",
																"project_id=?",
																new String[] { String
																		.valueOf(tempId) });
												projectUpdateAdapter();

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										});

						AlertDialog ad = builder.create();
						ad.show();
						return true;
					}
				});

		newNoteBack = (Button) findViewById(R.id.myProjectsBack);

		newNoteBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					Intent intent = new Intent(MyProjects.this,
							MainActivity.class);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		addNewProject = (Button) this.findViewById(R.id.addNewProject);
		addNewProject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubIntent intent = new
				// Intent(Notes.this, NewNote.class);
				Intent intent = new Intent(MyProjects.this, NewProject.class);
				startActivityForResult(intent, 0);

			}
		});
	}

	public void projectUpdateAdapter() {
		DatabaseHelper db = new DatabaseHelper(this);
		Cursor c = db.getReadableDatabase().query("ProjectTable", null, null,
				null, null, null, "_id desc", null);
		if (c != null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							"project_name", "_id" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			projectListView.setAdapter(la);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		projectUpdateAdapter();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_projects, menu);
		return true;
	}

}
