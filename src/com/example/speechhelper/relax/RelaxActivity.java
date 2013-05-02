package com.example.speechhelper.relax;

import com.example.speechhelper.R;
import android.os.Bundle;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class RelaxActivity extends ActivityGroup {

    private TabHost relaxTabHost;
	private int projectId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relax_tab_host);
		
		projectId =this.getIntent().getIntExtra("projectId", -1);
	
		
		relaxTabHost = (TabHost) findViewById(android.R.id.tabhost);
		relaxTabHost.setup(this.getLocalActivityManager());
		
		Intent musicIntent =new Intent (this, MusicActivity.class);
		musicIntent.putExtra("projectId", projectId);   //pass project id
		
		Intent gameIntent = new Intent(this, GameActivity.class);
		gameIntent.putExtra("projectId", projectId);
		
		relaxTabHost.addTab(relaxTabHost.newTabSpec("soundscape")
				.setIndicator("Soundscape").setContent(musicIntent));
		relaxTabHost.addTab(relaxTabHost.newTabSpec("gamecenter")
				.setIndicator("Gamecenter").setContent(gameIntent));
		relaxTabHost.setCurrentTab(0);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relax, menu);
		return true;
	}


}





