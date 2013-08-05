package com.dmc.camera.assist;

import com.dmc.camera.provider.DBApi;
import com.dmc.camera.util.Utils;
import com.dmc.camera.settings.CameraSettingDialog;
import com.dmc.camera.widget.SingleModeDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {

	private final static String TAG = "MainActivity";

	private Preview mPreview = null;
	private CameraSettings mCameraSettings = null;
	private TestCameraApi mTestCameraApi = null;
	private int mCameraFacing;
	static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		// Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
		mCameraSettings = new CameraSettings(context);

		// ---test by hkkwon
		mTestCameraApi = new TestCameraApi(mCameraSettings);

		// Create our Preview view and set it as the content of our activity
		mPreview = new Preview(context, mCameraFacing, mCameraSettings);

		LayoutInflater inflater = getLayoutInflater();
		View buttonView = (View) inflater.inflate(R.layout.activity_main, null);

		RelativeLayout mainLayout = new RelativeLayout(context);
		mainLayout.setBackgroundColor(Color.BLACK);
		mainLayout.addView(mPreview);
		mainLayout.addView(buttonView);
		setContentView(mainLayout);
		
//		mCameraModeView = (View) inf

		ImageButton mainGalleryButton = (ImageButton) findViewById(R.id.mainGalleryButton);
		mainGalleryButton.setOnClickListener(this);

		ImageButton mainSettingButton = (ImageButton) findViewById(R.id.mainSettingButton);
		mainSettingButton.setOnClickListener(this);

		ImageButton mainModeButton = (ImageButton) findViewById(R.id.mainModeButton);
		mainModeButton.setOnClickListener(this);

		ImageButton mainCloseButton = (ImageButton) findViewById(R.id.mainCloseButton);
		mainCloseButton.setOnClickListener(this);

		ImageButton mainShutterButton = (ImageButton) findViewById(R.id.mainShutterButton);
		mainShutterButton.setOnClickListener(this);

		OrientationEventListener oel = new OrientationEventListener(this) {
			int mOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
			int mOrientationCompensation = 0;

			@Override
			public void onOrientationChanged(int orientation) {

				// We keep the last known orientation. So if the user first
				// orient
				// the camera then point the camera to floor or sky, we still
				// have
				// the correct orientation.
				if (orientation == ORIENTATION_UNKNOWN)
					return;

				mOrientation = Util.roundOrientation(orientation, mOrientation);
				// When the screen is unlocked, display rotation may change.
				// Always
				// calculate the up-to-date orientationCompensation.
				int orientationCompensation = mOrientation
						+ Util.getDisplayRotation(context);
				if (mOrientationCompensation != orientationCompensation) {
					mOrientationCompensation = orientationCompensation;
					Util.setOrientationCompensation(mOrientationCompensation);
					Log.e(TAG, "mOrientationCompensation = "
							+ mOrientationCompensation);
				}

			}
		};
		oel.enable();
		
	}

	public void onClick(View v) {
		if (v.getId() == R.id.mainShutterButton) {
			mPreview.capture();
		} else if (v.getId() == R.id.mainSettingButton) {
//			mTestCameraApi.testSetCameraFlashMode();
			// 카메라 설정 view
			CameraSettingDialog csd = new CameraSettingDialog(MainActivity.this);
			csd.show();
			
			
		} else if (v.getId() == R.id.mainModeButton) {
			/*
			mTestCameraApi.testSetCameraPictureSize();
			setPreviewPostionChange();
			*/
			// 촬영모드 설정팝업 
			String[] menuTitle = getBaseContext().getResources().getStringArray(R.array.setting_shot_mode_array);
			final String[] menuValue = getBaseContext().getResources().getStringArray(R.array.setting_shot_mode_value);
			int setting_position = Utils.findPosition(menuValue, DBApi.TblSystem.getString(getContentResolver(), DBApi.TblSystem.SHOT_MODE));
			SingleModeDialog sm = new SingleModeDialog(MainActivity.this, getBaseContext().getString(R.string.setting_shot_mode_setting_title), menuTitle, menuValue, setting_position);
			sm.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
				@Override
				public void onDismissed(int position) {
					DBApi.TblSystem.putString(getContentResolver(), DBApi.TblSystem.SHOT_MODE, menuValue[position]);
				}
			});
			
			sm.show();
		} else if (v.getId() == R.id.mainGalleryButton) {

		} else {
			finish();
		}
	}

	private void setPreviewPostionChange() {
		int margin = 0;

		int width = Util.getPreviewSizeWidth();
		int height = Util.getPreviewSizeHeigth();

		int myRatioSize = Util.getMyRatioSize();

		if ((myRatioSize == Util.PREVIEW_GALAXY_S4_RATIO_4_3)
				|| (myRatioSize == Util.PREVIEW_GALAXY_NOTE2_RATIO_4_3))
			margin = Util.PREVIEW_LEFT_MARGIN;

		RelativeLayout.LayoutParams layoutParms = new RelativeLayout.LayoutParams(
				width, height); // width, height
		layoutParms.setMargins(margin, 0, 0, 0); // left, // top, // 0,// 0
		mPreview.setLayoutParams(layoutParms);
	}
	
}