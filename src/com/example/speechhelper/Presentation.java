package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Presentation extends Activity {

	private Button presentationBack;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presentation);

		listView = (ListView) this.findViewById(R.id.listView);

		UpdateAdapter();
		// click event
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(
						getApplicationContext(),
						String.valueOf(id) + " short "
								+ String.valueOf(position), Toast.LENGTH_SHORT)
						.show();
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
		;
		Cursor c = db.getReadableDatabase().query("ProjectTable", null, null,
				null, null, null, "_id desc", null);
		if (c != null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							"project_name", "_id" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			listView.setAdapter(la);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.presentation, menu);
		return true;
	}

}
