package com.dmc.grip.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;

import com.dmc.grip.R;
import com.dmc.grip.data.OnSensorDataListner;
import com.dmc.grip.data.PatternData;
import com.dmc.grip.data.SensorDataEvent;
import com.dmc.grip.sensor.GripSensorEventManager;
import com.dmc.grip.type.Define;
import com.dmc.grip.utils.CustomLog;
import com.dmc.grip.utils.FileUtils;

public class LockTestActivity extends Activity {
	
	public static final String LOG_TAG = "LockTestActivity";
	
	GripSensorEventManager mGripSensorEventManager;
	
	String mInputData = "";
	String mCompareData;
	PatternData mPatternData[] = new PatternData[3];
	int mPatternType;
	
	int mTime = 0;
	int mToken = 1;
	int mBeforePower = -1;
	int mAfterPower = -1;
	int mAllToken = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_test);
		
		mGripSensorEventManager = new GripSensorEventManager(getBaseContext());
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mGripSensorEventManager.registerCALstner();
		mGripSensorEventManager.setOnSensorDataListner(mSensorDataListener);
	}
	
	protected void onPause() {
		super.onPause();
		mGripSensorEventManager.removeCollectSensorDataHandler();
		mGripSensorEventManager.unregisterCAListener();
	}
	
	OnSensorDataListner mSensorDataListener = new OnSensorDataListner() {
		
		@Override
		public void OnSensorDataListner(SensorDataEvent sensorData) {
			String log = "mValue : ";
			for(int i=0; i < sensorData.mValue.length; i++){
				log = log + sensorData.mValue[i];
			}
			log = log + " / result : " + sensorData.mResult + ", power : " + sensorData.mPower + ", hand : " + sensorData.mHand;
			CustomLog.d(LOG_TAG, "mSensorDataListener " + log);
			
			if(sensorData.mResult == false){
				CustomLog.e(LOG_TAG, "resetData");
				resetData();
			}
			else{
				if(mInputData.equals("")){
					if(sensorData.mPower == Define.POWER_FULL){
						Boolean compareResult = false;
						for (int i=0; i < mPatternData.length; i++){
							if(mPatternData[i] != null){
								compareResult = mGripSensorEventManager.compareSensorValue(Define.WRONG_MAX_COUNT, mPatternData[i].value, sensorData.mValue);
								CustomLog.d(LOG_TAG, "compareResult : " + compareResult);
								if(compareResult == true){
									mPatternType = i;
									break;
								}
							}
						}
						
						if(compareResult == true){
							for(int i=0; i < sensorData.mValue.length; i++){
								mInputData = mInputData + sensorData.mValue[i];
								if(i != sensorData.mValue.length-1){
									mInputData = mInputData + ",";
								}
							}
							mInputData = mInputData + Define.FILE_SEPARATOR;
							mInputData = mInputData + sensorData.mHand;
							
							CustomLog.e(LOG_TAG, "save !!! value : " + mInputData);
						}
					}
				}
				else{
					int power = sensorData.mPower;
					
					mTime = mTime + Define.GRIP_SETTING_THRESHOLD;
					if(mBeforePower == -1){
						mBeforePower = power;
						mAfterPower = power;
						mTime = Define.GRIP_SETTING_THRESHOLD;
						mToken = 1;
						mAllToken = 1;
					}
					else{
						mAfterPower = power;
						
						CustomLog.e(LOG_TAG, "mBeforePower : " + mBeforePower + ", mAfterPower : " + mAfterPower);
						
						if(mBeforePower != mAfterPower){
							mInputData = mInputData + Define.FILE_SEPARATOR;
							mInputData = mInputData + mBeforePower;
							mInputData = mInputData + Define.FILE_SEPARATOR;
							mInputData = mInputData + mToken;
							
							mAllToken = mAllToken + 1;
							
							CustomLog.e(LOG_TAG, "save !!! mBeforePower != mAfterPower : " + mInputData);
							
							mToken = 1;
							mTime = Define.GRIP_SETTING_THRESHOLD;
						}
						else{
							if(mAllToken < mPatternData[mPatternType].all_token){
								if(mTime >= Define.GRIP_THRESHOLD){
									mTime = 0;
									mToken = mToken + 1;
									mAllToken = mAllToken + 1;
									
								}
							}
							else{
								if(mTime > 0){
									if(mToken == 0){
										mToken = mToken + 1;
									}
									mInputData = mInputData + Define.FILE_SEPARATOR;
									mInputData = mInputData + mBeforePower;
									mInputData = mInputData + Define.FILE_SEPARATOR;
									mInputData = mInputData + mToken;
									CustomLog.e(LOG_TAG, "save !!! mTime > 0 mInputData : " + mInputData);
									
//									mAllToken = mAllToken + 1;
								}
								
								mInputData = mInputData + Define.FILE_SEPARATOR;
								mInputData = mInputData + mAllToken;
								CustomLog.e(LOG_TAG, "save !!! mAllToken : " + mInputData);
								
								CustomLog.d(LOG_TAG, "mInputData end : " + mInputData);
								
								PatternData resultPatternData = PatternDataParser(mInputData);
								
								Boolean pattern_compare_result = false;
								LogDisplay("inputData : ", resultPatternData);
								LogDisplay("mPatternData[mPatternType] : ", mPatternData[mPatternType]);
								
								pattern_compare_result = mGripSensorEventManager.checkPatterData(resultPatternData, mPatternData[mPatternType]);
								CustomLog.e(LOG_TAG, "pattern_compare_result : " + pattern_compare_result);
								
								if(pattern_compare_result == true){
									if(mPatternType == 0){
										// lock
										finish();
									}
									else if(mPatternType == 1){
										// camera
										Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										startActivity(cameraIntent);
										finish();
										
									}
									else if(mPatternType == 2){
										// ebook
										Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.books");
										startActivity(intent);
										finish();
									}
								}
								else{
									resetData();
								}
							}
						}
					}
					
					mBeforePower = mAfterPower;
				}
				
			}
		}
	};
	
	private void LogDisplay (String msg , PatternData data){
		CustomLog.v(LOG_TAG, msg + "value : " + Arrays.toString(data.value) + ", hand : " + data.hand + ", power : " + Arrays.toString(data.power) 
				+ ", token : " + Arrays.toString(data.token) + ", all_token : " + data.all_token);
	}
	
	
	private void resetData(){
		mInputData = "";
		mTime = 0;
		mToken = 1;
		mBeforePower = -1;
		mAfterPower = -1;
		mAllToken = 1;
	}
	
	private void init(){
		
		String settingData[] = new String[3];
		
		File lock_data = new File(Define.PATTERN_QUICK_LOCK);
		File camera_data = new File(Define.PATTERN_QUICK_CAMERA);
		File ebook_data = new File(Define.PATTERN_QUICK_EBOOK);
		
		settingData[0] = FileUtils.readFile(lock_data);
		settingData[1] = FileUtils.readFile(camera_data);
		settingData[2] = FileUtils.readFile(ebook_data);
		
		CustomLog.d(LOG_TAG, "init lock_data : " + settingData[0]);
		CustomLog.d(LOG_TAG, "init camera_data : " + settingData[1]);
		CustomLog.d(LOG_TAG, "init ebook_data : " + settingData[2]);
		
		for(int i=0; i < settingData.length; i++){
			if(settingData[i] != null){
				mPatternData[i] = PatternDataParser(settingData[i]);
			}
		}
	}
	
	public PatternData PatternDataParser(String data){
		PatternData result = new PatternData();
		String settingValue[] = data.split(Define.FILE_SEPARATOR);
		String settingPatternValue[] = settingValue[0].split(",");
		int pattern_value[] = new int[settingPatternValue.length];
		for(int j=0; j < settingPatternValue.length; j++){
			pattern_value[j] = Integer.parseInt(settingPatternValue[j]);
		}

		result.value = pattern_value;
		result.hand = Integer.parseInt(settingValue[1]);
		result.all_token = Integer.parseInt(settingValue[settingValue.length-1]);
		
		result.power = new int[(settingValue.length-3)/2];
		result.token = new int[(settingValue.length-3)/2];
		
		for(int k=2; k < settingValue.length-1; k++){
			if(k % 2 == 0){
				result.power[(k-2)/2] = Integer.parseInt(settingValue[k]);
			}
			else{
				result.token[(k-2)/2] = Integer.parseInt(settingValue[k]);
			}
		}
		
		CustomLog.d(LOG_TAG, "result : " + "value : " + Arrays.toString(result.value) + ", hand : " + result.hand 
				+ ", power : " + Arrays.toString(result.power) + ", token : " + Arrays.toString(result.token) 
				+ ", all_token : " + result.all_token);
		
		return result;
		
	}
	
}
