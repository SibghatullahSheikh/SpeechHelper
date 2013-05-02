package com.example.speechhelper.relax;

import java.util.ArrayList;
import java.util.List;

import com.example.speechhelper.MainActivity;
import com.example.speechhelper.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicActivity extends Activity {
	private Button musicBack;
//	private int projectId;
	private ListView musicListView;

	
	private List<BaseItem> listElements;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
	//	projectId = this.getIntent().getIntExtra("projectId", -1);
		musicBack = (Button) this.findViewById(R.id.musicBack);
		musicListView = (ListView) this.findViewById(R.id.musicListView);
		musicBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicActivity.this,
						MainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		
		listElements = new ArrayList<BaseItem>();
		// Add an item for the friend picker
		listElements.add(new seaSound(0));
		listElements.add(new forestSound(1));
		listElements.add(new nightSound(2));
		// Set the list view adapter
		musicListView.setAdapter(new ActionListAdapter(MusicActivity.this, 
		                    R.id.musicListView, listElements));

		// Check for an open session
	//	Session session = Session.getActiveSession();

		    	
	
	}	   
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music, menu);
		return true;
	}
	
	private class ActionListAdapter extends ArrayAdapter<BaseItem> {
	    private List<BaseItem> listElements;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<BaseItem> listElements) {
	        super(context, resourceId, listElements);
	        this.listElements = listElements;
	        // Set up as an observer for list item changes to
	        // refresh the view.
	        for (int i = 0; i < listElements.size(); i++) {
	            listElements.get(i).setAdapter(this);
	        }
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater =
	                    (LayoutInflater) MusicActivity.this
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.listitem, null);
	        }

	        BaseItem listElement = listElements.get(position);
	        if (listElement != null) {
	            view.setOnClickListener(listElement.getOnClickListener());
	            ImageView icon = (ImageView) view.findViewById(R.id.icon);
	            TextView text1 = (TextView) view.findViewById(R.id.text1);
	            TextView text2 = (TextView) view.findViewById(R.id.text2);
	            if (icon != null) {
	                icon.setImageDrawable(listElement.getIcon());
	            }
	            if (text1 != null) {
	                text1.setText(listElement.getText1());
	            }
	            if (text2 != null) {
	                text2.setText(listElement.getText2());
	            }
	        }
	        return view;
	    }

	}
	private class seaSound extends BaseItem {

	    public seaSound(int requestCode) {
	        super( MusicActivity.this.getResources().getDrawable(R.drawable.sea_icon),
	        		 MusicActivity.this.getResources().getString(R.string.sea_sound),
	        		 MusicActivity.this.getResources().getString(R.string.sea_sound2),
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Do nothing for now
	            	Intent intent = new Intent(MusicActivity.this,
							SeaActivity.class);
					startActivityForResult(intent, 0);
	            }
	        };
	    }	
	}
	private class forestSound extends BaseItem {

	    public forestSound(int requestCode) {
	        super( MusicActivity.this.getResources().getDrawable(R.drawable.forest_icon),
	        		 MusicActivity.this.getResources().getString(R.string.forest_sound),
	        		 MusicActivity.this.getResources().getString(R.string.forest_sound2),
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Do nothing for now
	            	Intent intent = new Intent(MusicActivity.this,
							ForestActivity.class);
					startActivityForResult(intent, 0);
	            }
	        };
	    }

		
	}
	private class nightSound extends BaseItem {

	    public nightSound(int requestCode) {
	        super( MusicActivity.this.getResources().getDrawable(R.drawable.night_icon),
	        		 MusicActivity.this.getResources().getString(R.string.night_sound),
	        		 MusicActivity.this.getResources().getString(R.string.night_sound2),
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Do nothing for now
	            	Intent intent = new Intent(MusicActivity.this,
							NightActivity.class);
					startActivityForResult(intent, 0);
	            }
	        };
	    }

		
	}


}

