package com.example.speechhelper.speech;

import com.example.speechhelper.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends Activity {
	
	private EditText editNoteContentText;
	private EditText editStartTime;
	private EditText editEndTime;
	private Button editNoteDone;
	private Button editNoteCancel;
	
	private int projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		
		final DatabaseHelper db = new DatabaseHelper(this); // db 
		
		projectId=this.getIntent().getIntExtra("pid", -1);
		
		final int noteId =this.getIntent().getIntExtra("noteId", -1);
		final String noteContent = this.getIntent().getStringExtra("noteContent");
		final int startTime = this.getIntent().getIntExtra("startTime", -1);
		final int endTime = this.getIntent().getIntExtra("endTime", -1);
		
		//final int actId =this.getIntent().getIntExtra("actId", -1);
		
		editNoteContentText =(EditText)this.findViewById(R.id.editNoteContentText);
		editStartTime=(EditText)this.findViewById(R.id.editStartTime);
		editEndTime =(EditText)this.findViewById(R.id.editEndTime);

		editNoteContentText.setText(noteContent);
		editStartTime.setText(String.valueOf(startTime));
		editEndTime.setText(String.valueOf(endTime));
		
		editNoteCancel=(Button)this.findViewById(R.id.editNoteCancel);
		editNoteCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		

					
					Intent intent = new Intent(EditNote.this,ProjectTabHost.class);
					intent.putExtra("projectIdBack", projectId);
		        	setResult(RESULT_OK, intent);
	                finish();			
					
			}
		});
		
		editNoteDone=(Button)this.findViewById(R.id.editNoteDone);
		editNoteDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String contentText =editNoteContentText.getText().toString();
				int st = Integer.parseInt(editStartTime.getText().toString());  // not null
				int et = Integer.parseInt(editEndTime.getText().toString());
				
				db.updateNote(contentText, st, et, noteId);
				
				Log.d("PID", String.valueOf(projectId));
				
				//branch


				
				Intent intent = new Intent(EditNote.this,ProjectTabHost.class);
				intent.putExtra("projectIdBack", projectId);
	        	setResult(RESULT_OK, intent);
                finish();			
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_note, menu);
		return true;
	}

}
