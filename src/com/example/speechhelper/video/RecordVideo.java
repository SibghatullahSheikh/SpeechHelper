package com.example.speechhelper.video;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.speechhelper.R;
import com.example.speechhelper.database.DatabaseHelper;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecordVideo extends Activity implements Callback {
	private int projectId;

	private Button startRecord;
	private Button stopRecord;
	private Button recordBack;

	private SurfaceView mSurfaceview;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder mSurfaceHolder;
	private Camera cam;

	private String filename;
	private String path;
	
	private boolean isRecording = false;
	private boolean isDone =false;
	// ********** display control***********//

	private TextView noteText;
	private TextView totalTime;
	private TextView nextNote;
	private int i;
	private Cursor c;
	private Cursor cursor;

	private int startTime;
	private int endTime;
	private int projectTime;

	private Timer timer;
	private Handler handler;
	private TimerTask task;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_video);

		projectId = this.getIntent().getIntExtra("project_id", -1); // get
																	// project
																	// id
		startRecord = (Button) this.findViewById(R.id.startRecord);
		stopRecord = (Button) this.findViewById(R.id.stopRecord);
		recordBack = (Button) this.findViewById(R.id.recordBack);

		mSurfaceview = (SurfaceView) this.findViewById(R.id.surfaceView);
		
		cam= Camera.open();
		if(cam==null){
		cam = findFrontCam();// find camera
		}
		cam.setDisplayOrientation(90);

		mSurfaceHolder = mSurfaceview.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ************display control init*************************//
		final DatabaseHelper db = new DatabaseHelper(this);
		cursor = db.getReadableDatabase().query(
				"ProjectTable", // cursor for getting project time
				new String[] { "project_time" }, "_id=?",
				new String[] { String.valueOf(projectId) }, null, null, null,
				null);
		cursor.moveToFirst();
		projectTime = cursor.getInt(0); // get project time

		Log.d("id+time",
				String.valueOf(projectTime) + " " + String.valueOf(projectId));

		noteText = (TextView) this.findViewById(R.id.currenNoteText);
		totalTime = (TextView) this.findViewById(R.id.recordTime);
		nextNote = (TextView) this.findViewById(R.id.nextNoteText);

		noteText.setText("");
		nextNote.setText("");
		totalTime.setText("0");
		totalTime.setTextColor(android.graphics.Color.RED);

		// cursor for notes//
		c = db.getReadableDatabase().query("NoteTable",
				new String[] { "note_content", "start_time", "end_time" },
				"project_id=?", new String[] { String.valueOf(projectId) },
				null, null, null, null);

		Log.d("count+++", String.valueOf(c.getCount()));

		// Start recording
		startRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isRecording == false) {
					isDone=false;
					isRecording = true;
					startRecordVideo();
					if(c.getCount()!=0)
					noteDisplayInit();
				}
			}
		});

		// stop
		stopRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isRecording == true) {
					if (timer != null) {
						timer.cancel();
						task.cancel();

						timer = null;
						task = null;
						handler = null;
					}
					
					mMediaRecorder.stop();
					mMediaRecorder.release();
					mMediaRecorder = null;
					isRecording = false;
					
					if(isDone==false){
						isDone=db.addVideoData(filename, path, projectId);
					}
				}
			}
		});

		// back
		recordBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (timer != null) {
					timer.cancel();
					task.cancel();

					timer = null;
					task = null;
					handler = null;
				}
				if(isDone==false){
					isDone=db.addVideoData(filename, path, projectId);
				}

				Intent intent = new Intent(RecordVideo.this,
						ProjectVideoList.class);
				intent.putExtra("projectIdBack", projectId);
				setResult(RESULT_OK, intent);
				finish();

			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void noteDisplayInit() {
		if (timer == null) {
			c.moveToFirst();
			startTime = c.getInt(1);
			endTime = c.getInt(2);
			timer = new Timer();
			handler = new  Handler() {     //  ???????????
				String content;

				public void handleMessage(Message msg) {
					// Log.d("count22", String.valueOf(msg.what));

					totalTime.setText(String.valueOf(msg.what));

					if (startTime == msg.what) {
						content = c.getString(0);
						noteText.setText(content);

						if (!c.isLast()) {
							c.moveToNext();

							content = c.getString(0);
							nextNote.setText(content);
						} else {
							nextNote.setText(null);
						}
					}
					if (endTime == msg.what) {
						noteText.setText(null);

						startTime = c.getInt(1);
						endTime = c.getInt(2);
					}
					if (projectTime * 60 == msg.what) {
						Toast t = Toast.makeText(RecordVideo.this,
								"Presentation Completed!", Toast.LENGTH_SHORT);
						t.show();
						i = 0;

						timer.cancel();
						task.cancel(); // close timer

						timer = null;
						task = null;
						handler = null;

						mMediaRecorder.stop(); // time up, stop recording
						mMediaRecorder.release();
						mMediaRecorder = null;
						isRecording = false;
						final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
						if(isDone==false){
							isDone=db.addVideoData(filename, path, projectId);
						}

					}
					super.handleMessage(msg);
				}

			};

			task = new TimerTask() {

				public void run() {
					i++;
					Message message = new Message();
					message.what = i;
					handler.sendMessage(message);
				}
			};
			timer.schedule(task, 1000, 1000);
		}
	}
	
	//*************touch the screen to auto focus*******************//
	public boolean onTouchEvent(MotionEvent event) {         
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		            cam.autoFocus(null);		        }
		        return true;
		    }

	// @Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			cam.lock();
			cam.release();
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_video, menu);
		return true;
	}

	public Camera findFrontCam() {
		int cameraCount = 0;
		Camera cam = null;

		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camera info
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		}
		return cam;
	}

	public void startRecordVideo() {
		if (cam == null) {
			cam= Camera.open();
			if(cam==null){
			cam = findFrontCam();// find camera
			}
			//cam = findFrontCam();
		}

		path = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString();

		Date date = new Date();
		filename = "/rec" + date.toString().replace(" ", "_").replace(":", "_")
				+ ".mp4";

		@SuppressWarnings("unused")
		File file = new File(path, filename);

		mMediaRecorder = new MediaRecorder();

		cam.lock();
		cam.unlock();
		

		mMediaRecorder.setCamera(cam);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		// mMediaRecorder.setVideoFrameRate(15);
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

		mMediaRecorder.setOutputFile(path + filename);
		try {
			mMediaRecorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMediaRecorder.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		if (cam != null) {
			Parameters params = cam.getParameters();
			List<String> focusModes = params.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))  // set auto focus
			{
			    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			    Log.d("af", "yes");
			}
			cam.setParameters(params);
			Log.d("surface", "created");
		} else {
			Toast.makeText(getApplicationContext(), "Camera not available!",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}

	}

}
