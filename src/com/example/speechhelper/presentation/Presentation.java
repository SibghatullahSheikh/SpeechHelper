package com.example.speechhelper.presentation;

import com.example.speechhelper.MainActivity;
import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Presentation extends Activity {

	private Button presentationBack;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presentation);

		listView = (ListView) this.findViewById(R.id.listView);
		final DatabaseHelper db = new DatabaseHelper(this);

		UpdateAdapter();
		// click event
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c = db.getWritableDatabase().query("ProjectTable",
						new String[] { "project_time" }, "_id=?",
						new String[] { String.valueOf(id) }, null, null, null,
						null);
				c.moveToFirst();
				db.close();  // close db   
				
				Intent intent = new Intent(Presentation.this, DisplayNote.class);

				intent.putExtra("projectTime", c.getInt(0)); // pass project
																// time
				intent.putExtra("projectId", (int) id); // pass project_id

				startActivityForResult(intent, 0);
			}
		});

		presentationBack = (Button) this.findViewById(R.id.presentationBack);
		presentationBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Presentation.this,
						MainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	public void UpdateAdapter() {
		DatabaseHelper db = new DatabaseHelper(this);
		
		Cursor c = db.getReadableDatabase().query("ProjectTable", null, null,
				null, null, null, "_id desc", null);
		if (c != null && c.getCount() >= 0) {
			@SuppressWarnings("deprecation")
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							"project_name", "project_time" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			listView.setAdapter(la);
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.presentation, menu);
		return true;
	}

}
