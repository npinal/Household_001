package com.dmc.grip.setting;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.dmc.grip.R;
import com.dmc.grip.provider.DBApi;
import com.dmc.grip.setting.GripTouchSetting;

public class SettingActivity extends Activity {
	
	RelativeLayout mGripTouch;
	RelativeLayout mLockTest;
	
	Switch mGripTouchSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getBaseContext().getString(R.string.action_settings));
		
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		
		mGripTouch = (RelativeLayout) findViewById(R.id.setting_grip_touch);
		mGripTouch.setOnClickListener(mClick);
		
		mLockTest = (RelativeLayout) findViewById(R.id.setting_lock_test);
		mLockTest.setOnClickListener(mClick);
		
		mGripTouchSwitch = (Switch) findViewById(R.id.grip_switch);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mGripTouchSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.GRIP_TOUCH, false));
		mGripTouchSwitch.setOnCheckedChangeListener(mCheckChange);
		
	};
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.setting_grip_touch:
				Intent intent = new Intent(SettingActivity.this, GripTouchSetting.class);
				startActivity(intent);
				
				break;
				
			case R.id.setting_lock_test:
				Intent i = new Intent(SettingActivity.this, LockTestActivity.class);
				startActivity(i);
				break;

			default:
				break;
			}
			
		}
	};
	
	OnCheckedChangeListener mCheckChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.grip_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.GRIP_TOUCH, isChecked);
				break;

			default:
				break;
			}
			
		}
	};
	

}
