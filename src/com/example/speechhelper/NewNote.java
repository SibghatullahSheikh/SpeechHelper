package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewNote extends Activity {
	
	 private EditText noteContentText; // accepts user input for new note content
	 private EditText startTimeText; // accepts user input for start time
	 private EditText endTimeText; // accepts user input for end time
	 
	 private String noteContent ;
	 private int  startTime; 
	 private int  endTime;
	 
	 private Button newNoteBack;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		
		newNoteBack = (Button)findViewById(R.id.newNoteBack);
		
		newNoteBack.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	Intent intent = new Intent(NewNote.this, Notes.class);  
	                startActivity(intent);  
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
		
			noteContentText = (EditText) findViewById(R.id.noteContentText);
	        startTimeText = (EditText) findViewById(R.id.startTimeText);
	        endTimeText = (EditText) findViewById(R.id.endTimeText);
	         
	         // get value from edittext
	        /* *****************
	         noteContent  = noteContentText.getText().toString();
	         startTime = Integer.parseInt(startTimeText.getText().toString());
	         endTime = Integer.parseInt(endTimeText.getText().toString());
	        ********************* */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

}
