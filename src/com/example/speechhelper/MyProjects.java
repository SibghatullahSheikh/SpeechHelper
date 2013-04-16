package com.example.speechhelper;

import com.example.speechhelper.facebook.FacebookActivity;
import com.facebook.Session;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.facebook.*;
import com.facebook.model.*;
public class MyProjects extends Activity {
	
	private Button newNoteBack;
	private Button go;
	private Button facebook;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_projects);
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_projects, menu);
		return true;
	}

}
