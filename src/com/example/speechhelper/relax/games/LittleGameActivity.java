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
       private int ScreenBallWidth = 0;  
       private int ScreenBallHeight = 0;  
       private Bitmap mbitmapBg;  //background
       private Bitmap mbitmapBall;  //ball
       private float PositionX = 0;  
       private float PositionY = 0;  
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
           mbitmapBall = BitmapFactory.decodeResource(this.getResources(), R.drawable.mario2);  
           mbitmapBg = BitmapFactory.decodeResource(this.getResources(), R.drawable.back1);  
           sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);     
           sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);     
           sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);    
       }  

       private void DrawText() {         
           myCanvas.drawBitmap(mbitmapBg,0,0, myPaint);  
           myCanvas.drawBitmap(mbitmapBall, PositionX,PositionY, myPaint);  
           myCanvas.drawText("X:" + GX, 0, 20, myPaint);  
           myCanvas.drawText("Y:" + GY, 0, 40, myPaint);  
           myCanvas.drawText("Z:" + GZ, 0, 60, myPaint);  
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
           ScreenBallWidth = ScreenWidth - mbitmapBall.getWidth();  
           ScreenBallHeight = ScreenHeight - mbitmapBall.getHeight();  

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
           PositionX -= GX;  
           PositionY += GY;  
           if (PositionX < 0) {  
        	   PositionX = 0;  
           } else if (PositionX > ScreenBallWidth) {  
        	   PositionX = ScreenBallWidth;  
           }  
           if (PositionY < 0) {  
        	   PositionY = 0;  
           } else if (PositionY > ScreenBallHeight) {  
        	   PositionY = ScreenBallHeight;  
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
