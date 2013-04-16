package com.example.speechhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private ImageButton myProjects;
	private ImageButton newProject;
	private ImageButton presentation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myProjects = (ImageButton)findViewById(R.id.myProjects);
		newProject = (ImageButton)findViewById(R.id.newProject);
		presentation = (ImageButton)findViewById(R.id.presentation);

		myProjects.setOnClickListener(new OnClickListener()
	        {
	        	@Override
	        	public void onClick(View view)
	        	{
			        try 
			        {
			        	Intent intent = new Intent(MainActivity.this, MyProjects.class);  
		                startActivity(intent);  
			        } catch (Exception e) 
			        {
			        	e.printStackTrace();
			        }
	        	}
	        });
		
	    presentation.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	
		        	Intent intent = new Intent(MainActivity.this, Presentation.class);  
	                startActivity(intent);  
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
		
		newProject.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	
		        	Intent intent = new Intent(MainActivity.this, NewProject.class);  
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
