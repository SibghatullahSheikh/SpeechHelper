package com.example.speechhelper.relax.games;

import com.example.speechhelper.R;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.view.Menu;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory; 
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorManager;  
import android.view.SurfaceHolder;  
import android.view.WindowManager; 

public class LittleGameActivity extends Activity {
	
    MyView myView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		myView = new MyView(this);  
		setContentView(myView);  
	
	}
    public class MyView extends SurfaceView implements Callback,Runnable ,SensorEventListener{  

       public static final int TIME_IN_FRAME = 50;   

       Paint myPaint = null;  
       SurfaceHolder mySurfaceHolder = null;  
      //canvas
       Canvas myCanvas = null;  
       boolean isRunning = false;  
       private SensorManager sensorManager = null;      
       Sensor sensor = null;      
       int ScreenWidth = 0;  
       int ScreenHeight = 0;  
       private int Width = 0;  
       private int Height = 0;  
       private Bitmap background;  //background
       private Bitmap mario;  //mario icon
       private float MarioX = 0;  
       private float MarioY = 0;  
       private float GX = 0;  
       private float GY = 0;  
       private float GZ = 0;  

       public MyView(Context context) {  

           super(context);  
           this.setFocusable(true);  
           this.setFocusableInTouchMode(true);  
           mySurfaceHolder = this.getHolder();  
           mySurfaceHolder.addCallback(this);  
           myCanvas = new Canvas();  
           myPaint = new Paint();  
           myPaint.setColor(Color.WHITE);  
           mario = BitmapFactory.decodeResource(this.getResources(), R.drawable.mario2);  
           background = BitmapFactory.decodeResource(this.getResources(), R.drawable.back1);  
           sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);     
           sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);     
           sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);    
       }  

       private void DrawText() {         
           myCanvas.drawBitmap(background,0,0, myPaint);  
           myCanvas.drawBitmap(mario, MarioX,MarioY, myPaint);  
           myCanvas.drawText("X-axis Gravity:" + GX, 20, 20, myPaint);  
           myCanvas.drawText("Y-axis Gravity:" + GY, 20, 40, myPaint);  
           myCanvas.drawText("Z-axis Gravity:" + GZ, 20, 60, myPaint);  
       }  

       @Override  

       public void surfaceChanged(SurfaceHolder holder, int format, int width,  

           int height) {  

       }  

       @Override  

       public void surfaceCreated(SurfaceHolder holder) {  
         
           isRunning = true;  
           new Thread(this).start(); 
           ScreenWidth = this.getWidth();  
           ScreenHeight = this.getHeight();  
           Width = ScreenWidth - mario.getWidth();  
           Height = ScreenHeight - mario.getHeight();  

       }  

       @Override  

       public void surfaceDestroyed(SurfaceHolder holder) {  
           isRunning = false;  
       }  
       @Override  
       public void run() {  
           while (isRunning) {  
           long startTime = System.currentTimeMillis();  
           synchronized (mySurfaceHolder) {  
               myCanvas = mySurfaceHolder.lockCanvas();  
               DrawText();  
               mySurfaceHolder.unlockCanvasAndPost(myCanvas);  
           }  
           long endTime = System.currentTimeMillis();  
           int diffTime = (int) (endTime - startTime);  
           
           while (diffTime <= TIME_IN_FRAME) {  
               diffTime = (int) (System.currentTimeMillis() - startTime);  
               Thread.yield();  
           }  
           }  
       }  
          
       @Override  
       public void onAccuracyChanged(Sensor arg0, int arg1) {  

           // TODO Auto-generated method stub  
             
       }  
       @SuppressWarnings("deprecation")
	   @Override  
       public void onSensorChanged(SensorEvent event) {  

           GX = event.values[SensorManager.DATA_X];  
           GY= event.values[SensorManager.DATA_Y];  
           GZ = event.values[SensorManager.DATA_Z];  
           MarioX -= GX;  
           MarioY += GY;  
           //check boundary 
           if (MarioX < 0) {  
        	   MarioX = 0;  
           } else if (MarioX > Width) {  
        	   MarioX = Width;  
           }  
           if (MarioY < 0) {  
        	   MarioY = 0;  
           } else if (MarioY > Height) {  
        	   MarioY = Height;  
           }  
       }  
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.little_game, menu);
		return true;
	}

}
