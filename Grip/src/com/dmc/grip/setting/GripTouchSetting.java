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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dmc.grip.R;
import com.dmc.grip.provider.DBApi;

public class GripTouchSetting extends Activity {
	
	Switch mActionBarSwitch;
	
	RelativeLayout mQuickRun;
	RelativeLayout mOneHand;
	RelativeLayout mMediaEdit;
	
	Switch mQuickRunSwitch;
	Switch mOneHandSwitch;
	Switch mMediaEditSwitch;
	
	TextView mQuickRunTitle;
	TextView mOneHandTitle;
	TextView mMediaEditTitle;
	
	TextView mQuickRunSummary;
	TextView mOneHandSummary;
	TextView mMediaEditSummary;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grip_touch_setting);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getBaseContext().getString(R.string.setting_grip_touch_title));
		
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
		
		mQuickRun = (RelativeLayout) findViewById(R.id.setting_quick_run);
		mOneHand = (RelativeLayout) findViewById(R.id.setting_one_hand_navi);
		mMediaEdit = (RelativeLayout) findViewById(R.id.setting_media_edit);
		
		mQuickRun.setOnClickListener(mClick);
		mOneHand.setOnClickListener(mClick);
		mMediaEdit.setOnClickListener(mClick);
		
		mQuickRunSwitch = (Switch) findViewById(R.id.quick_run_switch);
		mQuickRunSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, false));
		mQuickRunSwitch.setOnCheckedChangeListener(mCheckChange);
		mQuickRunTitle = (TextView) findViewById(R.id.setting_quick_run_title);
		mQuickRunSummary = (TextView) findViewById(R.id.setting_quick_run_summary);
		
		mOneHandSwitch = (Switch) findViewById(R.id.one_hand_navi_switch);
		mOneHandSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.ONE_HAND_NAVI, false));
		mOneHandSwitch.setOnCheckedChangeListener(mCheckChange);
		mOneHandTitle = (TextView) findViewById(R.id.setting_one_hand_navi_title);
		mOneHandSummary = (TextView) findViewById(R.id.setting_one_hand_navi_summary);
		
		mMediaEditSwitch = (Switch) findViewById(R.id.media_edit_switch);
		mMediaEditSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.MEDIA_EDIT, false));
		mMediaEditSwitch.setOnCheckedChangeListener(mCheckChange);
		mMediaEditTitle = (TextView) findViewById(R.id.setting_media_edit_title);
		mMediaEditSummary = (TextView) findViewById(R.id.setting_media_edit_summary);
		
		mActionBarSwitch.setOnCheckedChangeListener(mActionCheckChange);
		mActionBarSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.GRIP_TOUCH, false));
		
		setEnable(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.GRIP_TOUCH, false));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mQuickRunSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, false));
		mOneHandSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.ONE_HAND_NAVI, false));
		mMediaEditSwitch.setChecked(DBApi.TblSystem.getBoolean(getContentResolver(), DBApi.TblSystem.MEDIA_EDIT, false));
		
	};
	
	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.setting_quick_run:
				Intent intent = new Intent(GripTouchSetting.this, QuickRunSetting.class);
				startActivity(intent);
				
				break;
			case R.id.setting_one_hand_navi:

				break;
			case R.id.setting_media_edit:

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
			case R.id.quick_run_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.QUICK_RUN, isChecked);
				break;
			case R.id.one_hand_navi_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.ONE_HAND_NAVI, isChecked);
				break;
			case R.id.media_edit_switch:
				DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.MEDIA_EDIT, isChecked);
				break;
				
			default:
				break;
			}
		}
	};
	
	OnCheckedChangeListener mActionCheckChange = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			DBApi.TblSystem.putBoolean(getContentResolver(), DBApi.TblSystem.GRIP_TOUCH, isChecked);
			setEnable(isChecked);
		}
	};
	
	
	public void setEnable(Boolean enabled){
		mQuickRun.setEnabled(enabled);
		mQuickRunSwitch.setEnabled(enabled);
		mQuickRunTitle.setEnabled(enabled);
		mQuickRunSummary.setEnabled(enabled);
		
		mOneHand.setEnabled(enabled);
		mOneHandSwitch.setEnabled(enabled);
		mOneHandTitle.setEnabled(enabled);
		mOneHandSummary.setEnabled(enabled);
		
		mMediaEdit.setEnabled(enabled);
		mMediaEditSwitch.setEnabled(enabled);
		mMediaEditTitle.setEnabled(enabled);
		mMediaEditSummary.setEnabled(enabled);
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
