package com.example.speechhelper;

import com.example.speechhelper.facebook.FacebookActivity;
import com.example.speechhelper.presentation.Presentation;
import com.example.speechhelper.project.MyProjects;
import com.example.speechhelper.relax.RelaxActivity;
import com.example.speechhelper.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageButton myProjects;
    private ImageButton presentation;
	private Button facebook;
	private Button relax;
	private Button email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myProjects = (ImageButton)findViewById(R.id.myProjects);
		presentation = (ImageButton)findViewById(R.id.presentation);
		facebook = (Button)findViewById(R.id.facebook);
		relax = (Button)findViewById(R.id.relax);
		email = (Button)findViewById(R.id.email);
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
		
		facebook.setOnClickListener(new OnClickListener()
                                    {
        	@Override
        	public void onClick(View view)
        	{
		        try
		        {
		        	Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
	                startActivity(intent);
                    
		        } catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
        	}
        });
		  email.setOnClickListener(new OnClickListener()
	        {
	        	@Override
	        	public void onClick(View view)
	        	{
	        		Intent i = new Intent(Intent.ACTION_SEND);
	        		i.setType("plain/text");	
	        		i.putExtra(Intent.EXTRA_SUBJECT, "Invitation of my speech/presentation");
	        		i.putExtra(Intent.EXTRA_TEXT   , "Hi there,\n \nI am going to have a speech/presentation. Please join me if you are available.\n \nLocation:\nTime:\n\nBest,\n");
	        		try {
	        		startActivity(i);
	        		} catch (android.content.ActivityNotFoundException ex) {
	        		  Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
	        		}
	        	}
	        });	
		relax.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               try
               {
                  Intent intent = new Intent(MainActivity.this, RelaxActivity.class);
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
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
