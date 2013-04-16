package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.TabHost;

public class ProjectTabHost extends ActivityGroup {
	
	private TabHost projectTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_tab_host);
		
		projectTabHost = (TabHost) findViewById(android.R.id.tabhost);
		projectTabHost.setup(this.getLocalActivityManager());
		
		projectTabHost.addTab(projectTabHost.newTabSpec("note")
				.setIndicator("Note").setContent(new Intent (this, ProjectNoteList.class)));
		projectTabHost.addTab(projectTabHost.newTabSpec("video")
				.setIndicator("Video").setContent(new Intent(this, ProjectVideoList.class)));
		projectTabHost.setCurrentTab(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_tab_host, menu);
		return true;
	}

}
