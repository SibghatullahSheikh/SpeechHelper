package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ProjectVideoList extends Activity {
	
	private Button projectVideoBack;
	private Button addVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_video_list);
		
		projectVideoBack =(Button)this.findViewById(R.id.projectVideoBack);
		addVideo = (Button)this.findViewById(R.id.addVideo);
		
		projectVideoBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent (ProjectVideoList.this, MyProjects.class);
				startActivityForResult(intent, 0);
			}
		});
		
		//addVideo **************
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_video_list, menu);
		return true;
	}

}
