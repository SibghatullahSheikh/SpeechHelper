package com.example.speechhelper.speech;

import java.io.File;

import com.example.speechhelper.R;

import android.net.Uri;
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

public class ProjectVideoList extends Activity {

	private Button projectVideoBack;
	private Button addVideo;
	private int projectId;

	private ListView videoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_video_list);

		final DatabaseHelper db = new DatabaseHelper(this);

		projectId = this.getIntent().getIntExtra("projectId", -1);

		projectVideoBack = (Button) this.findViewById(R.id.projectVideoBack);
		addVideo = (Button) this.findViewById(R.id.addVideo);
		videoListView = (ListView) this.findViewById(R.id.videoListView);

		videoUpdateAdapter();

		// long click to delete
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		videoListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final long tempId = id;
				Cursor c = db.getWritableDatabase().query("VideoTable",
						new String[] { "video_name","video_path" }, "_id=?",
						new String[] { String.valueOf(tempId) }, null, null, null,
						null);
				Log.d("count cc", String.valueOf(c.getCount()));
				c.moveToFirst();
				
				final String name = c.getString(0);
				 final String path = c.getString(1);
				builder.setMessage("Delete?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										db.deleteData("VideoTable",
												(int) tempId);
										Log.d("count ccc", String.valueOf(tempId));
										
										
										File file = new File(path, name);
										if(file.exists()){
											file.delete();
										}
										videoUpdateAdapter();
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
		videoListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c = db.getWritableDatabase().query("VideoTable",
						new String[] { "video_name","video_path" }, "_id=?",
						new String[] { String.valueOf(id) }, null, null, null,
						null);
				Log.d("count cc", String.valueOf(c.getCount()));
				c.moveToFirst();
				
				final String name = c.getString(0);
				 final String path = c.getString(1);
				 Log.d("path", path+name);
				
				 Intent it = new Intent(Intent.ACTION_VIEW);
				 it.setDataAndType(Uri.fromFile(new File(path, name)), "video/mp4");
				 try{
				 startActivity(it);
				 }catch(Exception e){
					 e.printStackTrace();
				 }
			}
		});

		projectVideoBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProjectVideoList.this,
						MyProjects.class);
				startActivityForResult(intent, 0);
			}
		});

		addVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProjectVideoList.this,
						RecordVideo.class);

				intent.putExtra("project_id", projectId);

				startActivityForResult(intent, 0);

			}
		});
	}

	public void videoUpdateAdapter() {
		DatabaseHelper db = new DatabaseHelper(this);
		Cursor c = db.query("VideoTable", projectId);

		Log.d("video", String.valueOf(c.getCount()));
		if (c != null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
							"video_name", "_id" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			videoListView.setAdapter(la);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		videoUpdateAdapter();
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
		getMenuInflater().inflate(R.menu.project_video_list, menu);
		return true;
	}

}
