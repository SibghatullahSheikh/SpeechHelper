package com.example.speechhelper.relax.sounds;

import com.example.speechhelper.R;
import com.example.speechhelper.relax.GameActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

@SuppressWarnings("deprecation")
public class ForestActivity extends Activity implements ViewFactory,OnItemSelectedListener {  
	private Button forestBack;
    ImageSwitcher mSwitcher;  
    private Integer[] mThumbIds = {  R.drawable.forest1,R.drawable.forest2,R.drawable.forest3,R.drawable.forest4,R.drawable.forest5,R.drawable.forest6};  
    private Integer[] mImageIds = {  R.drawable.forest1,R.drawable.forest2,R.drawable.forest3,R.drawable.forest4,R.drawable.forest5,R.drawable.forest6};  
    private MediaPlayer mediaPlayer;
    private Button forestStartButton;
    private Button forestPauseButton;
    private Button forestRestartButton;
	private int playbackPosition = 0;
	private int projectId;

    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_forest);  
        forestBack = (Button) this.findViewById(R.id.forestBack);
        forestStartButton = (Button)findViewById(R.id.forestStartButton);
        forestPauseButton = (Button)findViewById(R.id.forestPauseButton);
        forestRestartButton = (Button)findViewById(R.id.forestRestartButton);
		projectId=this.getIntent().getIntExtra("pid", -1);

        forestBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDestroy(mediaPlayer);
				Intent intent = new Intent(ForestActivity.this,
						GameActivity.class);
				intent.putExtra("projectIdBack", projectId);
	        	setResult(RESULT_OK, intent);
                finish();	
			}
		});
        
        forestStartButton.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
		        try 
		        {
		        	playLocalAudio_UsingDescriptor();
		        } catch (Exception e) 
		        {
		        	e.printStackTrace();
		        }
        	}
        });
        
	    forestPauseButton.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	     	public void onClick(View view)
	        {
		        	if(mediaPlayer!=null)
		        	{
		        		playbackPosition = mediaPlayer.getCurrentPosition();
		        		mediaPlayer.pause();
		        	}
	        }
	    });
	        	
	    forestRestartButton.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	        public void onClick(View view)
	        {
		        	if(mediaPlayer!=null && !mediaPlayer.isPlaying())
		        	{
		        		mediaPlayer.seekTo(playbackPosition);
		        		mediaPlayer.start();
		        	}
		    }
	    });
        mSwitcher = (ImageSwitcher) findViewById(R.id.forestImageSwitcher);   
        mSwitcher.setFactory(this);  
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));  
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));  

        Gallery g = (Gallery) findViewById(R.id.forestGallery);  

        g.setAdapter(new ImageAdapter(this));  
        g.setOnItemSelectedListener(this);  

    }  

    @SuppressWarnings("rawtypes")
	public void onItemSelected(AdapterView parent, View v, int position, long id) {  
        mSwitcher.setImageResource(mImageIds[position]);  
    }  
    
	protected void onDestroy(MediaPlayer mediaPlayer)
	{
	       	super.onDestroy();
	       	killMediaPlayer(mediaPlayer);
	}
	
	private void killMediaPlayer(MediaPlayer mediaPlayer)
	{
	   	if(mediaPlayer!=null)
	   	{
	       	try
	       	{
	       		mediaPlayer.release();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	    }
	 } 	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void playLocalAudio_UsingDescriptor() throws Exception 
	{   AssetFileDescriptor fileDesc = null;
		
			fileDesc = getResources().openRawResourceFd(R.raw.forest);
		
		if (fileDesc != null) 
		{
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc.getStartOffset(), fileDesc.getLength());
			fileDesc.close();
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
	}
	
	

    @SuppressWarnings("rawtypes")
	public void onNothingSelected(AdapterView parent) {  
    }  
    @Override
   	public void onResume() {
   	    super.onResume();
   	    
   	}

@Override  
    public View makeView() {  
    ImageView i = new ImageView(this);  
    i.setScaleType(ImageView.ScaleType.FIT_CENTER);  
    i.setLayoutParams(new ImageSwitcher.LayoutParams(  
    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
    return i;  
    }  
 
    public class ImageAdapter extends BaseAdapter {  
        private Context mContext;  
        public ImageAdapter(Context c) {  
             mContext = c;  
          }  

    public int getCount() {  
        return mThumbIds.length;  
    }  

   public Object getItem(int position) {  
    return position;  
   }  

   public long getItemId(int position) {  
    return position;  
   }  
  
  
  @Override
      public View getView(int position, View convertView, ViewGroup parent) {  
        ImageView i = new ImageView(mContext);
        i.setImageResource(mThumbIds[position]);  
        i.setAdjustViewBounds(true);  
        i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));   
        return i;  
    }  

  }  

}  
