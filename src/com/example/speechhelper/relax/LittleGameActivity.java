package com.example.speechhelper.relax;

import com.example.speechhelper.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LittleGameActivity extends Activity {
	private Button littleGameBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_little_game);
		
		littleGameBack = (Button) this.findViewById(R.id.littleGameBack);
		
		littleGameBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent = new Intent(LittleGameActivity.this,
						RelaxActivity.class);
			
	        	setResult(RESULT_OK, intent);
                finish();	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.little_game, menu);
		return true;
	}

}
