package com.dmc.camera.settings;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmc.camera.assist.R;
import com.dmc.camera.provider.DBApi;
import com.dmc.camera.provider.DBHelper;
import com.dmc.camera.util.Utils;
import com.dmc.camera.widget.SingleModeDialog;
import com.dmc.camera.widget.CustomAlertDialog;;

public final class CameraSettingDialog {
	
	Dialog mDialog;
	Context mContext;
	
	Button mSave;
	
	RelativeLayout mSaveType;
	RelativeLayout mShotVoiceGuide;
	RelativeLayout mShotMode;
	RelativeLayout mDetailSetting;
	RelativeLayout mPersonVoiceGuide;
	RelativeLayout mWeatherVoiceGuide;
	RelativeLayout mReset;
	RelativeLayout mHelp;
	
	TextView mSaveTypeValue;
	TextView mShotVoiceGuideValue;
	TextView mShotModeValue;
	TextView mPersonVoiceGuideValue;
	TextView mWeatherVoiceGuideValue;
	
	String mSaveTypeMenuTitle[];
	String mShotVoiceGuideMenuTitle[];
	String mShotModeMenuTitle[];
	String mPersonVoiceGuideMenuTitle[];
	String mWeatherVoiceGuideMenuTitle[];
	
	String mSaveTypeMenuValue[];
	String mShotVoiceGuideMenuValue[];
	String mShotModeMenuValue[];
	String mPersonVoiceGuideMenuValue[];
	String mWeatherVoiceGuideMenuValue[];
	
	int mSaveTypePosition;
	int mShotVoiceGuidePosition;
	int mShotModePosition;
	int mPersonVoiceGuidePosition;
	int mWeatherVoiceGuidePosition;
	
	public CameraSettingDialog(Context context) {
		mContext = context;
		
		mSaveTypeMenuTitle = mContext.getResources().getStringArray(R.array.setting_save_type_array);
		mSaveTypeMenuValue = mContext.getResources().getStringArray(R.array.setting_save_type_value);
		
		mShotVoiceGuideMenuTitle = mContext.getResources().getStringArray(R.array.setting_shot_voice_guide_array);
		mShotVoiceGuideMenuValue = mContext.getResources().getStringArray(R.array.setting_shot_voice_guide_value);
		
		mShotModeMenuTitle = mContext.getResources().getStringArray(R.array.setting_shot_mode_array);
		mShotModeMenuValue = mContext.getResources().getStringArray(R.array.setting_shot_mode_value);
		
		mPersonVoiceGuideMenuTitle = mContext.getResources().getStringArray(R.array.setting_common_mode_array);
		mPersonVoiceGuideMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
		
		mWeatherVoiceGuideMenuTitle = mContext.getResources().getStringArray(R.array.setting_common_mode_array);
		mWeatherVoiceGuideMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
	}
	
	public void show(){
		mDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
		mDialog.show();
		mDialog.setContentView(R.layout.camera_setting);
		
		mSave = (Button) mDialog.findViewById(R.id.camera_setting_save);
		mSave.setOnClickListener(mClick);
		
		mSaveType = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_save_type);
		mSaveType.setOnClickListener(mClick);
		mSaveTypeValue = (TextView) mDialog.findViewById(R.id.camera_setting_save_type_value);
		mSaveTypePosition = Utils.findPosition(mSaveTypeMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SAVE_TYPE));
		if(mSaveTypePosition != -1){
			mSaveTypeValue.setText(mSaveTypeMenuTitle[mSaveTypePosition]);
		}
		
		mShotVoiceGuide = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_shot_voice_guide);
		mShotVoiceGuide.setOnClickListener(mClick);
		mShotVoiceGuideValue = (TextView) mDialog.findViewById(R.id.camera_setting_shot_voice_guide_value);
		mShotVoiceGuidePosition = Utils.findPosition(mShotVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_VOICE_GUIDE));
		if(mShotVoiceGuidePosition != -1){
			mShotVoiceGuideValue.setText(mShotVoiceGuideMenuTitle[mShotVoiceGuidePosition]);
		}
		
		mShotMode = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_shot_mode);
		mShotMode.setOnClickListener(mClick);
		mShotModeValue = (TextView) mDialog.findViewById(R.id.camera_setting_shot_mode_value);
		mShotModePosition = Utils.findPosition(mShotModeMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_MODE));
		if(mShotModePosition != -1){
			mShotModeValue.setText(mShotModeMenuTitle[mShotModePosition]);
		}
		
		mDetailSetting = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_detail);
		mDetailSetting.setOnClickListener(mClick);
		
		mPersonVoiceGuide = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_person_voice_guide);
		mPersonVoiceGuide.setOnClickListener(mClick);
		mPersonVoiceGuideValue = (TextView) mDialog.findViewById(R.id.camera_setting_person_voice_guide_value);
		mPersonVoiceGuidePosition = Utils.findPosition(mPersonVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.PERSON_VOICE_GUIDE));
		if(mPersonVoiceGuidePosition != -1){
			mPersonVoiceGuideValue.setText(mPersonVoiceGuideMenuTitle[mPersonVoiceGuidePosition]);
		}
		
		mWeatherVoiceGuide = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_weather_voice_guide);
		mWeatherVoiceGuide.setOnClickListener(mClick);
		mWeatherVoiceGuideValue = (TextView) mDialog.findViewById(R.id.camera_setting_weather_voice_guide_value);
		mWeatherVoiceGuidePosition = Utils.findPosition(mWeatherVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.WEATHER_VOICE_GUIDE));
		if(mWeatherVoiceGuidePosition != -1){
			mWeatherVoiceGuideValue.setText(mWeatherVoiceGuideMenuTitle[mWeatherVoiceGuidePosition]);
		}
		
		mReset = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_reset);
		mReset.setOnClickListener(mClick);
		
		mHelp = (RelativeLayout) mDialog.findViewById(R.id.camera_setting_help);
		mHelp.setOnClickListener(mClick);
		
		
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}
	
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.camera_setting_save:
				dismiss();
				break;
				
			case R.id.camera_setting_save_type:
				SingleModeDialog saveTypeDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_save_type_title), mSaveTypeMenuTitle, mSaveTypeMenuValue, mSaveTypePosition);
				saveTypeDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mSaveTypePosition = position;
						mSaveTypeValue.setText(mSaveTypeMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SAVE_TYPE, mSaveTypeMenuValue[position]);
					}
				});
				
				saveTypeDialog.show();
				break;
				
			case R.id.camera_setting_shot_voice_guide:
				SingleModeDialog shotVoiceGuideDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_shot_voice_guide_title), mShotVoiceGuideMenuTitle, mShotVoiceGuideMenuValue, mShotVoiceGuidePosition);
				shotVoiceGuideDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mShotVoiceGuidePosition = position;
						mShotVoiceGuideValue.setText(mShotVoiceGuideMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_VOICE_GUIDE, mShotVoiceGuideMenuValue[position]);
					}
				});
				
				shotVoiceGuideDialog.show();
				break;
				
			case R.id.camera_setting_shot_mode:
				SingleModeDialog shotModeDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_shot_mode_setting_title), mShotModeMenuTitle, mShotModeMenuValue, mShotModePosition);
				shotModeDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mShotModePosition = position;
						mShotModeValue.setText(mShotModeMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_MODE, mShotModeMenuValue[position]);
					}
				});
				
				shotModeDialog.show();
				break;
				
			case R.id.camera_setting_detail:
				CameraDetailSettingDialog detailSettingDialog = new CameraDetailSettingDialog(mContext);
				detailSettingDialog.show();
				
				break;
			case R.id.camera_setting_person_voice_guide:
				SingleModeDialog personVoiceGuideDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_person_voice_guide_setting_title), mPersonVoiceGuideMenuTitle, mPersonVoiceGuideMenuValue, mPersonVoiceGuidePosition);
				personVoiceGuideDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mPersonVoiceGuidePosition = position;
						mPersonVoiceGuideValue.setText(mPersonVoiceGuideMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.PERSON_VOICE_GUIDE, mPersonVoiceGuideMenuValue[position]);
					}
				});
				
				personVoiceGuideDialog.show();
				break;
				
			case R.id.camera_setting_weather_voice_guide:
				SingleModeDialog weatherVoiceGuideDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_weather_voice_guide_title), mWeatherVoiceGuideMenuTitle, mWeatherVoiceGuideMenuValue, mWeatherVoiceGuidePosition);
				weatherVoiceGuideDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mWeatherVoiceGuidePosition = position;
						mWeatherVoiceGuideValue.setText(mWeatherVoiceGuideMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.WEATHER_VOICE_GUIDE, mWeatherVoiceGuideMenuValue[position]);
					}
				});
				
				weatherVoiceGuideDialog.show();
				break;
				
			case R.id.camera_setting_reset:
				CustomAlertDialog resetDialog = new CustomAlertDialog(mContext, mContext.getString(R.string.setting_reset_message));
				resetDialog.setOnButtonClickListener(new CustomAlertDialog.OnButtonClickListener() {
					@Override
					public void onPositiveButtonClick(View v) {
						SettingReset();
					}
					
					@Override
					public void onNegativeButtonClick(View v) {
						
					}
				});
				resetDialog.show();
				
				break;
			case R.id.camera_setting_help:
				break;
			default:
				break;
			}
			
		}
	};
	
	private void SettingReset(){
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SAVE_TYPE, mContext.getString(R.string.save_type));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_VOICE_GUIDE, mContext.getString(R.string.shot_voice_guide));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_MODE, mContext.getString(R.string.shot_mode));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.PERSON_VOICE_GUIDE, mContext.getString(R.string.persion_voice_guide));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.WEATHER_VOICE_GUIDE, mContext.getString(R.string.weather_voice_guide));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.VOICE_CONTROL, mContext.getString(R.string.voice_control));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.FLASH, mContext.getString(R.string.flash_setting));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.RESOLUTION, mContext.getString(R.string.resolution));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.GPS_TAG, mContext.getString(R.string.gps_tag));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SOUND_BUTTON, mContext.getString(R.string.sound_button));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.VIEW_FINDER, mContext.getString(R.string.view_finder));
		DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SAVE_PLACE, mContext.getString(R.string.save_place));
		
		mSaveTypePosition = Utils.findPosition(mSaveTypeMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SAVE_TYPE));
		if(mSaveTypePosition != -1){
			mSaveTypeValue.setText(mSaveTypeMenuTitle[mSaveTypePosition]);
		}
		
		mShotVoiceGuidePosition = Utils.findPosition(mShotVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_VOICE_GUIDE));
		if(mShotVoiceGuidePosition != -1){
			mShotVoiceGuideValue.setText(mShotVoiceGuideMenuTitle[mShotVoiceGuidePosition]);
		}
		
		mShotModePosition = Utils.findPosition(mShotModeMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SHOT_MODE));
		if(mShotModePosition != -1){
			mShotModeValue.setText(mShotModeMenuTitle[mShotModePosition]);
		}
		
		mPersonVoiceGuidePosition = Utils.findPosition(mPersonVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.PERSON_VOICE_GUIDE));
		if(mPersonVoiceGuidePosition != -1){
			mPersonVoiceGuideValue.setText(mPersonVoiceGuideMenuTitle[mPersonVoiceGuidePosition]);
		}
		
		mWeatherVoiceGuidePosition = Utils.findPosition(mWeatherVoiceGuideMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.WEATHER_VOICE_GUIDE));
		if(mWeatherVoiceGuidePosition != -1){
			mWeatherVoiceGuideValue.setText(mWeatherVoiceGuideMenuTitle[mWeatherVoiceGuidePosition]);
		}
	}

}
