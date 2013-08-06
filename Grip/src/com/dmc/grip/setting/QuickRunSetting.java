package com.dmc.grip.setting;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dmc.grip.R;
import com.dmc.grip.provider.DBApi;

public class QuickRunSetting extends Activity {
	
	public static final String QUICK_TYPE = "quick_type";
	
	public static final int QUICK_RUN_LOCK = 0;
	public static final int QUICK_RUN_CAMERA = 1;
	public static final int QUICK_RUN_EBOOK = 2;
	
	Switch mActionBarSwitch;
	
	RelativeLayout mLock;
	RelativeLayout mCamera;
	RelativeLayout mEbook;
	
	Switch mLockSwitch;
	Switch mCameraSwitch;
	Switch mEbookSwitch;
	
	TextView mLockTitle;
	TextView mCameraTitle;
	TextView mEbookTitle;
	
	TextView mLockSummary;
	TextView mCameraSummary;
	TextView mEbookSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quick_run_setting);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getBaseContext().getString(R.string.setting_quick_run_title));
		
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mActionBarSwitch = new Switch(getBaseContext());
		
		mActionBarSwitch.setPaddingRelative(0, 0, getResources().getDimensionPixelSize(R.dimen.actionbar_switch_right_margin), 0);
		
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(mActionBarSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.END));
		
		mLock = (RelativeLayout) findViewById(R.id.setting_quick_run_lock);
		mCamera = (RelativeLayout) findViewById(R.id.setting_quick_run_camera);
		mEbook = (RelativeLayout) findViewById(R.id.setting_quick_run_ebook);
		
		mLock.setOnClickListener(mClick);
		mCamera.setOnClickListener(mClick);
		mEbook.setOnClickListener(mClick);
		
		mLockSwitch = (Switch) findViewById(R.id.quick_run_lock_switch);
		mLockSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_LOCK, false));
		mLockSwitch.setOnCheckedChangeListener(mCheckChange);
		mLockTitle = (TextView) findViewById(R.id.setting_quick_run_lock_title);
		mLockSummary = (TextView) findViewById(R.id.setting_quick_run_lock_summary);
		
		mCameraSwitch = (Switch) findViewById(R.id.quick_run_camera_switch);
		mCameraSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_CAMERA, false));
		mCameraSwitch.setOnCheckedChangeListener(mCheckChange);
		mCameraTitle = (TextView) findViewById(R.id.setting_quick_run_camera_title);
		mCameraSummary = (TextView) findViewById(R.id.setting_quick_run_camera_summary);
		
		mEbookSwitch = (Switch) findViewById(R.id.quick_run_ebook_switch);
		mEbookSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_EBOOK, false));
		mEbookSwitch.setOnCheckedChangeListener(mCheckChange);
		mEbookTitle = (TextView) findViewById(R.id.setting_quick_run_ebook_title);
		mEbookSummary = (TextView) findViewById(R.id.setting_quick_run_ebook_summary);
		
		mActionBarSwitch.setOnCheckedChangeListener(mActionCheckChange);
		mActionBarSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, false));
		
		setEnable(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, false));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mLockSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_LOCK, false));
		mCameraSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_CAMERA, false));
		mEbookSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_EBOOK, false));
	};
	
	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QuickRunSetting.this, GripRegister.class);

			switch (v.getId()) {
			case R.id.setting_quick_run_lock:
				intent.putExtra(QUICK_TYPE, QUICK_RUN_LOCK);
				
				break;
			case R.id.setting_quick_run_camera:
				intent.putExtra(QUICK_TYPE, QUICK_RUN_CAMERA);

				break;
			case R.id.setting_quick_run_ebook:
				intent.putExtra(QUICK_TYPE, QUICK_RUN_EBOOK);

				break;

			default:
				break;
			}
			
			startActivity(intent);

		}
	};
	
	OnCheckedChangeListener mCheckChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.quick_run_lock_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_LOCK, isChecked);
				break;
			case R.id.quick_run_camera_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_CAMERA, isChecked);
				break;
			case R.id.quick_run_ebook_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_EBOOK, isChecked);
				break;

			default:
				break;
			}
			
		}
	};
	
	OnCheckedChangeListener mActionCheckChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, isChecked);
			setEnable(isChecked);
		}
	};
	
	
	public void setEnable(Boolean enabled){
		mLock.setEnabled(enabled);
		mLockSwitch.setEnabled(enabled);
		mLockTitle.setEnabled(enabled);
		mLockSummary.setEnabled(enabled);
		
		mCamera.setEnabled(enabled);
		mCameraSwitch.setEnabled(enabled);
		mCameraTitle.setEnabled(enabled);
		mCameraSummary.setEnabled(enabled);
		
		mEbook.setEnabled(enabled);
		mEbookSwitch.setEnabled(enabled);
		mEbookTitle.setEnabled(enabled);
		mEbookSummary.setEnabled(enabled);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	        	break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}

}
