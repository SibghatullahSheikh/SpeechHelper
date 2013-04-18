package com.example.speechhelper;

import com.example.speechhelper.facebook.FacebookActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyProjects extends Activity {
	
	private Button newNoteBack;
	private Button go;
	private ListView projectListView;
    private Button facebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_projects);
		
		projectUpdateAdapter();
		
		newNoteBack = (Button)findViewById(R.id.myProjectsBack);
		
		newNoteBack.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	
		        	Intent intent = new Intent(MyProjects.this, MainActivity.class);  
	                startActivity(intent);  
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
        facebook = (Button)findViewById(R.id.facebook);
		facebook.setOnClickListener(new OnClickListener()
                                    {
        	@Override
        	public void onClick(View view)
        	{
		        try
		        {
		        	Intent intent = new Intent(MyProjects.this, FacebookActivity.class);
	                startActivity(intent);
                    
		        } catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
        	}
        });

		
			go= (Button)findViewById(R.id.projectNoteBack);
		
		go.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	
		        	Intent intent = new Intent(MyProjects.this, ProjectTabHost.class);  
	                startActivity(intent);  
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
	}
	
	public void projectUpdateAdapter() {
		DatabaseHelper db = new DatabaseHelper(this);;
		Cursor c= db.getReadableDatabase().query("ProjectTable", null, null, null, null, null, null);
	//	Cursor c =db.query("NoteTable",projectId );
		//Cursor c = db.query("NoteTable");
		if (c!= null && c.getCount() >= 0) {
			ListAdapter la = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_2, c, new String[] {
					"project_name","_id" }, new int[] {
					android.R.id.text1, android.R.id.text2 });
			projectListView = (ListView) this.findViewById(R.id.projectListView);
			projectListView.setAdapter(la);
		}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_projects, menu);
		return true;
	}

}
