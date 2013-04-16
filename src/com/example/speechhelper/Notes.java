package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Notes extends Activity {
	
	private Button newNoteBack;
	private Button addNewNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		addNewNote = (Button)findViewById(R.id.addNewNote);
		newNoteBack = (Button)findViewById(R.id.notesBack);
		
		newNoteBack.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	
		        	Intent intent = new Intent(Notes.this, NewProject.class);  
	                startActivity(intent);  
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
		
		addNewNote.setOnClickListener(new OnClickListener()
	        {
	        	@Override
	        	public void onClick(View view)
	        	{
			        try 
			        {
			        	
			        	Intent intent = new Intent(Notes.this, NewNote.class);  
		                startActivity(intent);  
			        } catch (Exception e) 
			        {
			        	e.printStackTrace();
			        }
	        	}
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes, menu);
		return true;
	}

}
