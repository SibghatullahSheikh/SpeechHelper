package com.example.speechhelper.relax;

import com.example.speechhelper.MainActivity;
import com.example.speechhelper.R;
import com.example.speechhelper.R.layout;
import com.example.speechhelper.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class GameActivity extends Activity {
	private Button gameBack;
	private int projectId;
	private ListView gameListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		projectId = this.getIntent().getIntExtra("projectId", -1);
		gameBack = (Button) this.findViewById(R.id.gameBack);
		gameListView = (ListView) this.findViewById(R.id.gameListView);
		
		gameBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GameActivity.this,
						MainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
