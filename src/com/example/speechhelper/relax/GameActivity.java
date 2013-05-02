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

public class GameActivity extends Activity {
	private Button gameBack;
//	private int projectId;
	private ListView gameListView;
	private List<BaseItem> listElements;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
	//	projectId = this.getIntent().getIntExtra("projectId", -1);
		gameBack = (Button) this.findViewById(R.id.gameBack);
		gameListView = (ListView) this.findViewById(R.id.gameListView);
		
		listElements = new ArrayList<BaseItem>();
		
		listElements.add(new littleGame(0));
		listElements.add(new newGame(1));
	
		// Set the list view adapter
		gameListView.setAdapter(new ActionListAdapter(GameActivity.this, 
		                    R.id.gameListView, listElements));
		gameBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GameActivity.this,
						MainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
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
	                    (LayoutInflater) GameActivity.this
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
	private class littleGame extends BaseItem {

	    public littleGame(int requestCode) {
	        super( GameActivity.this.getResources().getDrawable(R.drawable.sea_icon),
	        		 GameActivity.this.getResources().getString(R.string.little_game),
	        		 GameActivity.this.getResources().getString(R.string.little_game2),
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Do nothing for now
	            	Intent intent = new Intent(GameActivity.this,
							LittleGameActivity.class);
					startActivityForResult(intent, 0);
	            }
	        };
	    }	
	}
	
	private class newGame extends BaseItem {

	    public newGame(int requestCode) {
	        super( GameActivity.this.getResources().getDrawable(R.drawable.new_game_icon),
	        		 GameActivity.this.getResources().getString(R.string.some_new_game),
	        		 GameActivity.this.getResources().getString(R.string.some_new_game2),
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Do nothing for now
	            	Intent intent = new Intent(GameActivity.this,
							NewGameActivity.class);
					startActivityForResult(intent, 0);
	            }
	        };
	    }	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
