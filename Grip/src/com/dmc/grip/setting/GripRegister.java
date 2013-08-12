package com.dmc.grip.setting;

import java.io.File;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dmc.grip.R;
import com.dmc.grip.provider.DBApi;
import com.dmc.grip.type.Define;

public class GripRegister extends Activity {
	
	public static final int VIEW_TYPE_REGIST = 0;
	public static final int VIEW_TYPE_REREGIST = 1;
	public static final int VIEW_TYPE_VIEW = 2;
	
	public static final String VIEW_MODE = "view_mode";
	
	LinearLayout mRegistFirst;
	LinearLayout mRegistMore;
	
	TextView mRegistMsg;
	
	Button mRegist;
	Button mGripView;
	Button mReRegist;
	
	Switch mActionBarSwitch;
	
	int mQuickRunType;
	Boolean mViewMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grip_register);
		
		Intent intent = getIntent();
		
		ActionBar actionBar = getActionBar();
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
		
		mRegistFirst = (LinearLayout) findViewById(R.id.grip_regist_first);
		mRegistMore = (LinearLayout) findViewById(R.id.grip_regist_more);
		
		mRegistMsg = (TextView) findViewById(R.id.grip_regist_message);
		
		mRegist = (Button) findViewById(R.id.btn_grip_regist);
		mGripView = (Button) findViewById(R.id.btn_grip_view);
		mReRegist = (Button) findViewById(R.id.btn_grip_re_regist);
		
		mRegist.setOnClickListener(mClick);
		mGripView.setOnClickListener(mClick);
		mReRegist.setOnClickListener(mClick);
		
		mQuickRunType = intent.getIntExtra(QuickRunSetting.QUICK_TYPE, QuickRunSetting.QUICK_RUN_LOCK);
		if(mQuickRunType == QuickRunSetting.QUICK_RUN_LOCK){
			actionBar.setTitle(getString(R.string.setting_quick_run_lock_title));
			mRegistMsg.setText(R.string.setting_grip_register_quick_lock);
			mActionBarSwitch.setOnCheckedChangeListener(mActionCheckChange);
			mActionBarSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_LOCK, false));
			setEnable(mActionBarSwitch.isChecked());
			
			File file = new File(Define.SETTING_QUICK_LOCK);
			if(file.isFile()){
				mViewMode = true;
			}
		}
		else if(mQuickRunType == QuickRunSetting.QUICK_RUN_CAMERA){
			actionBar.setTitle(getString(R.string.setting_quick_run_camera_title));
			mRegistMsg.setText(R.string.setting_grip_register_quick_camera);
			mActionBarSwitch.setOnCheckedChangeListener(mActionCheckChange);
			mActionBarSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_CAMERA, false));
			setEnable(mActionBarSwitch.isChecked());
			
			File file = new File(Define.SETTING_QUICK_CAMERA);
			if(file.isFile()){
				mViewMode = true;
			}
		}
		else {
			actionBar.setTitle(getString(R.string.setting_quick_run_ebook_title));
			mRegistMsg.setText(R.string.setting_grip_register_quick_ebook);
			mActionBarSwitch.setOnCheckedChangeListener(mActionCheckChange);
			mActionBarSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_EBOOK, false));
			setEnable(mActionBarSwitch.isChecked());
			
			File file = new File(Define.SETTING_QUICK_EBOOK);
			if(file.isFile()){
				mViewMode = true;
			}
		}
		
		setViewMode(mViewMode);

	}
	
	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(v.getId() == R.id.btn_grip_regist){
				Intent intent = new Intent(GripRegister.this, GripActivity.class);
				intent.putExtra(QuickRunSetting.QUICK_TYPE, mQuickRunType);
				intent.putExtra(VIEW_MODE, false);
				startActivityForResult(intent, VIEW_TYPE_REGIST);
			}
			else if(v.getId() == R.id.btn_grip_view){
				Intent intent = new Intent(GripRegister.this, GripActivity.class);
				intent.putExtra(QuickRunSetting.QUICK_TYPE, mQuickRunType);
				intent.putExtra(VIEW_MODE, true);
				startActivityForResult(intent, VIEW_TYPE_VIEW);
			}
			else if(v.getId() == R.id.btn_grip_re_regist){
				Intent intent = new Intent(GripRegister.this, GripActivity.class);
				intent.putExtra(QuickRunSetting.QUICK_TYPE, mQuickRunType);
				intent.putExtra(VIEW_MODE, false);
				startActivityForResult(intent, VIEW_TYPE_REREGIST);
			}
		}
	};
	
	OnCheckedChangeListener mActionCheckChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			if(mQuickRunType == QuickRunSetting.QUICK_RUN_LOCK){
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_LOCK, isChecked);
			}
			else if(mQuickRunType == QuickRunSetting.QUICK_RUN_CAMERA){
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_CAMERA, isChecked);
			}
			else {
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN_EBOOK, isChecked);
			}
			
			setEnable(isChecked);
		}
	};
	
	
	public void setEnable(Boolean enabled){
		mRegist.setEnabled(enabled);
		mGripView.setEnabled(enabled);
		mReRegist.setEnabled(enabled);
	}
	
	public void setViewMode(Boolean view_mode){
		if(view_mode == true){
			mRegistFirst.setVisibility(View.GONE);
			mRegistMore.setVisibility(View.VISIBLE);
		}
		else{
			mRegistFirst.setVisibility(View.VISIBLE);
			mRegistMore.setVisibility(View.GONE);
		}
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == VIEW_TYPE_REGIST){
			File file = new File(Define.SETTING_QUICK_LOCK);
			if(file.isFile()){
				mViewMode = true;
				setViewMode(mViewMode);
			}
		}
		else if(requestCode == VIEW_TYPE_VIEW){
			File file = new File(Define.SETTING_QUICK_CAMERA);
			if(file.isFile()){
				mViewMode = true;
				setViewMode(mViewMode);
			}
		}
		else if(requestCode == VIEW_TYPE_REREGIST){
			File file = new File(Define.SETTING_QUICK_EBOOK);
			if(file.isFile()){
				mViewMode = true;
				setViewMode(mViewMode);
			}
		}
	}


}
