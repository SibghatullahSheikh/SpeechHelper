package com.example.speechhelper.relax.games;

import com.example.speechhelper.R;
import com.example.speechhelper.relax.RelaxActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class NewGameActivity extends Activity {
	private Button newGameBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
        newGameBack = (Button) this.findViewById(R.id.newGameBack);
		
		newGameBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent = new Intent(NewGameActivity.this,
						RelaxActivity.class);
			
	        	setResult(RESULT_OK, intent);
                finish();	
			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

}
