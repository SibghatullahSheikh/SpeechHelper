package com.example.speechhelper.project;

import com.example.speechhelper.R;
import com.example.speechhelper.note.ProjectNoteList;
import com.example.speechhelper.video.ProjectVideoList;

import android.os.Bundle;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ProjectTabHost extends ActivityGroup {
	
	private TabHost projectTabHost;
	
	private int projectId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_tab_host);
		
		projectId =this.getIntent().getIntExtra("projectId", -1);
	
		
		projectTabHost = (TabHost) findViewById(android.R.id.tabhost);
		projectTabHost.setup(this.getLocalActivityManager());
		
		Intent noteIntent =new Intent (this, ProjectNoteList.class);
		noteIntent.putExtra("projectId", projectId);   //pass project id
		
		Intent videoIntent = new Intent(this, ProjectVideoList.class);
		videoIntent.putExtra("projectId", projectId);
		
		projectTabHost.addTab(projectTabHost.newTabSpec("note")
				.setIndicator("Note").setContent(noteIntent));
		projectTabHost.addTab(projectTabHost.newTabSpec("video")
				.setIndicator("Video").setContent(videoIntent));
		projectTabHost.setCurrentTab(0);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_tab_host, menu);
		return true;
	}

}
