package de.seideman.app.activities;

import java.io.IOException;

import de.seideman.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePicture extends Activity implements SurfaceHolder.Callback, OnClickListener {
    /** Called when the activity is first created. */
	
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Boolean mPreviewRunning = false;
	private Camera.PictureCallback mPictureCallback;
	private Camera mCamera;
	private Button btnTake;

	
	
    public void onCreate(Bundle savedInstanceState) {
    	
    	
    	
    	super.onCreate(savedInstanceState);
    	
    	
    	getWindow().setFormat(PixelFormat.TRANSLUCENT);

    	requestWindowFeature(Window.FEATURE_NO_TITLE);

    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

    	//WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
    	
    	
    	setContentView(R.layout.main_take);
    	
    	btnTake = (Button)findViewById(R.id.Button_Take);
	    btnTake.setOnClickListener(this);
	    
	    
    	mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
    	mSurfaceView.setClickable(true);

    	mSurfaceHolder = mSurfaceView.getHolder();

    	mSurfaceHolder.addCallback(this);

    	mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      	mSurfaceView.setOnClickListener(this);
      	
      	mPictureCallback = new Camera.PictureCallback() {

			
    		public void onPictureTaken(byte[] imageData, Camera c) {
    		
    			
    			if (imageData != null){
    				convertImage(imageData);
    			}
    			
    			
    			
    			
    		}
    		
    	};
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w,
			int h) {
		
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		
		//mCamera.setParameters(p);
		
		mCamera.setDisplayOrientation(90);
		
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.setParameters(p);} 
		catch (IOException e) {
			e.printStackTrace();
		}

		mCamera.startPreview();
		mPreviewRunning = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();

		mPreviewRunning = false;

		mCamera.release();
		
	}

	@Override
	public void onClick(final View v) {
	
		//Toast.makeText(this,"Bearbeite Foto!", 10).show();
	
		mCamera.takePicture(null, mPictureCallback, mPictureCallback);
		
		
	}
	public boolean convertImage(byte[] imageData){
		
		
		
	
		
		Toast.makeText(this,"Bytes: "+imageData.length, 10).show();
		
		Intent intent = new Intent();
		
		
		if (imageData.length >0){
			intent.putExtra("bit", imageData);
			setResult(RESULT_OK,intent);
			finish();
		}
		
		return true;
	}
	
}