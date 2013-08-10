package com.dmc.camera.settings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmc.camera.assist.R;
import com.dmc.camera.provider.DBApi;
import com.dmc.camera.util.Utils;
import com.dmc.camera.widget.SingleModeDialog;

public final class CameraDetailSettingDialog {
	
	Dialog mDialog;
	Context mContext;
	
	Button mSave;
	
	RelativeLayout mVoiceControl;
	RelativeLayout mFlash;
	RelativeLayout mResolution;
	RelativeLayout mGpsTag;
	RelativeLayout mSoundButton;
	RelativeLayout mViewFinder;
//	RelativeLayout mSavePlace;
	
	TextView mVoiceControlValue;
	TextView mFlashValue;
	TextView mResolutionValue;
	TextView mGpsTagValue;
	TextView mSoundButtonValue;
	TextView mViewFinderValue;
	TextView mSavePlaceValue;
	
	String mVoiceControlMenuTitle[];
	String mFlashMenuTitle[];
	String mResolutionMenuTitle[];
	String mGpsTagMenuTitle[];
	String mSoundButtonMenuTitle[];
	String mViewFinderMenuTitle[];
	String mSavePlaceMenuTitle[];
	
	String mVoiceControlMenuValue[];
	String mFlashMenuValue[];
	String mResolutionMenuValue[];
	String mGpsTagMenuValue[];
	String mSoundButtonMenuValue[];
	String mViewFinderMenuValue[];
	String mSavePlaceMenuValue[];
	
	int mVoiceControlPosition;
	int mFlashPosition;
	int mResolutionPosition;
	int mGpsTagPosition;
	int mSoundButtonPosition;
	int mViewFinderPosition;
	int mSavePlacePosition;
	
	public CameraDetailSettingDialog(Context context) {
		mContext = context;
		
		mVoiceControlMenuTitle = mContext.getResources().getStringArray(R.array.setting_common_mode_array);
		mVoiceControlMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
		
		mFlashMenuTitle = mContext.getResources().getStringArray(R.array.setting_detail_flash_array);
		mFlashMenuValue = mContext.getResources().getStringArray(R.array.setting_detail_flash_value);
		
		mResolutionMenuTitle = mContext.getResources().getStringArray(R.array.setting_detail_resolution_array);
		mResolutionMenuValue = mContext.getResources().getStringArray(R.array.setting_detail_resolution_value);
		
		mGpsTagMenuTitle = mContext.getResources().getStringArray(R.array.setting_common_mode_array);
		mGpsTagMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
		
		mSoundButtonMenuTitle = mContext.getResources().getStringArray(R.array.setting_detail_sound_button_array);
		mSoundButtonMenuValue = mContext.getResources().getStringArray(R.array.setting_detail_sound_button_value);
		
		mViewFinderMenuTitle = mContext.getResources().getStringArray(R.array.setting_common_mode_array);
		mViewFinderMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
		
//		mSavePlaceMenuTitle = mContext.getResources().getStringArray(R.array.setting_detail);
//		mSavePlaceMenuValue = mContext.getResources().getStringArray(R.array.setting_common_mode_value);
	}
	
	public void show(){
		mDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(R.layout.camera_detail_setting);
		
		mSave = (Button) mDialog.findViewById(R.id.camera_detail_setting_save);
		mSave.setOnClickListener(mClick);
		
		mVoiceControl = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_voice_control);
		mVoiceControl.setOnClickListener(mClick);
		mVoiceControlValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_voice_control_value);
		mVoiceControlPosition = Utils.findPosition(mVoiceControlMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.VOICE_CONTROL));
		if(mVoiceControlPosition != -1){
			mVoiceControlValue.setText(mVoiceControlMenuTitle[mVoiceControlPosition]);
		}
		
		mFlash = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_flash);
		mFlash.setOnClickListener(mClick);
		mFlashValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_flash_value);
		mFlashPosition = Utils.findPosition(mFlashMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.FLASH));
		if(mFlashPosition != -1){
			mFlashValue.setText(mFlashMenuTitle[mFlashPosition]);
		}
		
		mResolution = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_resolution);
		mResolution.setOnClickListener(mClick);
		mResolutionValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_resolution_value);
		mResolutionPosition = Utils.findPosition(mResolutionMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.RESOLUTION));
		if(mResolutionPosition != -1){
			mResolutionValue.setText(mResolutionMenuTitle[mResolutionPosition]);
		}
		
		mGpsTag = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_gps_tag);
		mGpsTag.setOnClickListener(mClick);
		mGpsTagValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_gps_tag_value);
		mGpsTagPosition = Utils.findPosition(mGpsTagMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.GPS_TAG));
		if(mGpsTagPosition != -1){
			mGpsTagValue.setText(mGpsTagMenuTitle[mGpsTagPosition]);
		}
		
		mSoundButton = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_sound_button);
		mSoundButton.setOnClickListener(mClick);
		mSoundButtonValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_sound_button_value);
		mSoundButtonPosition = Utils.findPosition(mSoundButtonMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.SOUND_BUTTON));
		if(mSoundButtonPosition != -1){
			mSoundButtonValue.setText(mSoundButtonMenuTitle[mSoundButtonPosition]);
		}
		
		mViewFinder = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_view_finder);
		mViewFinder.setOnClickListener(mClick);
		mViewFinderValue = (TextView) mDialog.findViewById(R.id.camera_detail_setting_view_finder_value);
		mViewFinderPosition = Utils.findPosition(mViewFinderMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.VIEW_FINDER));
		if(mViewFinderPosition != -1){
			mViewFinderValue.setText(mViewFinderMenuTitle[mSoundButtonPosition]);
		}
		
		/*
		mSavePlace = (RelativeLayout) mDialog.findViewById(R.id.camera_detail_setting_save_place);
		mSavePlace.setOnClickListener(mClick);
		mSavePlaceValue = (TextView) mDialog.findViewById(R.id.camera_setting_weather_voice_guide_value);
		mSavePlacePosition = Utils.findPosition(mSavePlaceMenuValue, DBApi.TblSystem.getString(mContext.getContentResolver(), DBApi.TblSystem.WEATHER_VOICE_GUIDE));
		if(mSavePlacePosition != -1){
			mSavePlaceValue.setText(mSavePlaceMenuTitle[mSavePlacePosition]);
		}*/
		
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}
	
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.camera_detail_setting_save:
				dismiss();
				break;
				
			case R.id.camera_detail_setting_voice_control:
				SingleModeDialog voiceControlDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_voice_control_title), mVoiceControlMenuTitle, mVoiceControlMenuValue, mVoiceControlPosition);
				voiceControlDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mVoiceControlPosition = position;
						mVoiceControlValue.setText(mVoiceControlMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.VOICE_CONTROL, mVoiceControlMenuValue[position]);
					}
				});
				
				voiceControlDialog.show();
				break;
				
			case R.id.camera_detail_setting_flash:
				SingleModeDialog flashDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_flash_title), mFlashMenuTitle, mFlashMenuValue, mFlashPosition);
				flashDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mFlashPosition = position;
						mFlashValue.setText(mFlashMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.FLASH, mFlashMenuValue[position]);
					}
				});
				
				flashDialog.show();
				break;
				
			case R.id.camera_detail_setting_resolution:
				SingleModeDialog resolutionDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_resolution_title), mResolutionMenuTitle, mResolutionMenuValue, mResolutionPosition);
				resolutionDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mResolutionPosition = position;
						mResolutionValue.setText(mResolutionMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.RESOLUTION, mResolutionMenuValue[position]);
					}
				});
				
				resolutionDialog.show();
				break;
				
			case R.id.camera_detail_setting_gps_tag:
				SingleModeDialog gpsTagDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_gps_tag_title), mGpsTagMenuTitle, mGpsTagMenuValue, mGpsTagPosition);
				gpsTagDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mGpsTagPosition = position;
						mGpsTagValue.setText(mGpsTagMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.GPS_TAG, mGpsTagMenuValue[position]);
					}
				});
				
				gpsTagDialog.show();
				break;
			case R.id.camera_detail_setting_sound_button:
				SingleModeDialog soundButtonDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_sound_button_title), mSoundButtonMenuTitle, mSoundButtonMenuValue, mSoundButtonPosition);
				soundButtonDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mSoundButtonPosition = position;
						mSoundButtonValue.setText(mSoundButtonMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.SOUND_BUTTON, mSoundButtonMenuValue[position]);
					}
				});
				
				soundButtonDialog.show();
				break;
				
			case R.id.camera_detail_setting_view_finder:
				SingleModeDialog viewFinderDialog = new SingleModeDialog(mContext, mContext.getString(R.string.setting_detail_view_finder_title), mViewFinderMenuTitle, mViewFinderMenuValue, mViewFinderPosition);
				viewFinderDialog.setOnDismissListener(new SingleModeDialog.OnDismissListener() {
					@Override
					public void onDismissed(int position) {
						mViewFinderPosition = position;
						mViewFinderValue.setText(mViewFinderMenuTitle[position]);
						DBApi.TblSystem.putString(mContext.getContentResolver(), DBApi.TblSystem.VIEW_FINDER, mViewFinderMenuValue[position]);
					}
				});
				
				viewFinderDialog.show();
				break;
				
			case R.id.camera_detail_setting_save_place:
				break;
			default:
				break;
			}
			
		}
	};

}
