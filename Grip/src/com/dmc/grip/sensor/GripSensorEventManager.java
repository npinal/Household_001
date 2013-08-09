package com.dmc.grip.sensor;

import com.dmc.grip.data.GripSensorDataApi;
import com.dmc.grip.data.OnSensorDataListner;
import com.dmc.grip.data.PatternData;
import com.dmc.grip.data.SensorDataEvent;
import android.content.Context;
import android.hardware.contextaware.ContextAwareConstants;
import android.hardware.contextaware.ContextAwareManager;
import android.hardware.contextaware.manager.ContextAwareListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GripSensorEventManager {
	private ContextAwareManager mContextAwareManager;
	public static final String TAG = "GripSensorEventManager";
	private GripSensorDataApi mGripSensorDataApi;
	private boolean isStop = false;
	private int[] mGripData;
	private int[] mLibGripData;
	public OnSensorDataListner mSensorDataListner = null;

	public GripSensorEventManager(Context context) {
		// TODO Auto-generated constructor stub

		mGripData = new int[30];
		mLibGripData = new int[30];
		mGripSensorDataApi = new GripSensorDataApi();

		mContextAwareManager = (ContextAwareManager) context
				.getSystemService("context_aware");

		mContextAwareManager.setCAProperty(
				ContextAwareManager.GRIP_SENSOR_SERVICE,
				ContextAwareManager.PROPERTY_GRIP_SENSOR_FLAG,
				ContextAwareConstants.GRIP_SENSOR_FLAG_EVENT_DATA);
	}

	private ContextAwareListener mCAListener = new ContextAwareListener() {

		@Override
		public void onContextChanged(final int type, final Bundle context) {
			if (type == ContextAwareManager.GRIP_SENSOR_SERVICE) {
				// TODO : receive the context

				for (int i = 0 ; i < mLibGripData.length ; i++){
					mLibGripData[i] = 0;
				}
				
				mLibGripData = context.getIntArray("Grip");
				mGripData = mLibGripData;

				/*
				 * int i = 0; for (i = 0; i < 30; i++) { Log.e(TAG, "grip [" + i
				 * + "] = " + Integer.toHexString(mLibGripData[i])); }
				 */

				/*
				 * int count = 0; for(int i : mLibGripData){
				 * //str.append(Integer.toString(i) + ", ");
				 * printBit(mLibGripData[count]); count++; }
				 */
/*
				Log.e(TAG, "START");
				PrintUtils.printBit("org", mLibGripData[0]);
				PrintUtils.printBit("org", mLibGripData[1]);
				PrintUtils.printBit("org", mLibGripData[2]);
				PrintUtils.printBit("org", mLibGripData[3]);
				PrintUtils.printBit("org", mLibGripData[4]);
				Log.e(TAG, "END");
*/
				//PrintUtils.printBit("org", mLibGripData[0]);						
			}
		}
	};

	private Handler collectSensorDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (!isStop) {
				mGripSensorDataApi.parseSensorData(mGripData);
				collectSensorDataHandler.sendEmptyMessageDelayed(msg.what + 1,
						500);
				// Log.e(TAG, "what = " + msg.what);

				mSensorDataListner.OnSensorDataListner(new SensorDataEvent(
						mGripSensorDataApi.getSersorValue(), mGripSensorDataApi
								.getSensorHand(), mGripSensorDataApi
								.getSensorPower(), mGripSensorDataApi
								.getSensorResult()));
				
				mGripSensorDataApi.clearSensorData();
			} else
				return;
		}
	};

	public void registerCALstner() {
		Log.e(TAG, "registerCAListner!");
		mContextAwareManager.registerListener(mCAListener,
				ContextAwareManager.GRIP_SENSOR_SERVICE);
	}

	public void unregisterCAListener() {
		if (mContextAwareManager != null) {
			Log.e(TAG, "unregisterCALListner!");
			mContextAwareManager.unregisterListener(mCAListener,
					ContextAwareManager.GRIP_SENSOR_SERVICE);
		}
	}

	public void setOnSensorDataListner(OnSensorDataListner sl) {
		mSensorDataListner = sl;
		isStop = false;
		if (sl != null)
			collectSensorDataHandler.sendEmptyMessage(1000);
	}

	public void removeCollectSensorDataHandler() {
		collectSensorDataHandler.removeMessages(0);
		isStop = true;
	}
	
	public boolean compareSensorValue(int maxCompareCount, final int[] gripOrg, final int[]gripTmp){	
		boolean result = true;
		int wrongCount = 0;
		
		for (int i = 0 ; i < gripOrg.length ; i++){
			if (gripOrg[i] != gripTmp[i]){
				wrongCount++;
			}
		}
		
		if (wrongCount > maxCompareCount)
			result = false;
		
		return result;
	}
	
public boolean checkPatterData(PatternData patternData, PatternData savedPatternData){	
		boolean result = true;
		
		/*
		PatternData patterData = new PatternData();
		patterData.hand = Define.HAND_LEFT;
		patterData.all_token = 5;	
		patterData.power = new int[4];
		patterData.power[0] = 0x20;
		patterData.power[1] = 0x00;
		patterData.power[2] = 0x20;
		patterData.power[3] = 0x00;		
		
		patterData.token = new int [4];
		patterData.token[0] = 2;
		patterData.token[1] = 1;
		patterData.token[2] = 1;
		patterData.token[3] = 1;
		
	
		PatternData savedPatternData = new PatternData();
		savedPatternData.hand = Define.HAND_LEFT;
		savedPatternData.all_token = 5;	
		savedPatternData.power = new int[4];
		savedPatternData.power[0] = 0x20;
		savedPatternData.power[1] = 0x00;
		savedPatternData.power[2] = 0x20;
		savedPatternData.power[3] = 0x00;
			
		savedPatternData.token = new int [4];
		savedPatternData.token[0] = 2;
		savedPatternData.token[1] = 1;
		savedPatternData.token[2] = 1;
		savedPatternData.token[3] = 1;
		*/
		
		//---	Sensor All Token Check
		if (patternData.all_token != savedPatternData.all_token){
			result = false;
			return result;
		}
				
		//---	Sensor Hand Check
		if (patternData.hand != savedPatternData.hand){
			result = false;
			return result;
		}
		
		//---	Sensor Power Check
		for (int i = 0 ; i < savedPatternData.power.length; i++){
			if (savedPatternData.power[i] != patternData.power[i]){
				result = false;
				return result;
			}
		}
		
		//---	Sensor Token Check
		for (int i = 0 ; i < patternData.token.length; i++){
			if (savedPatternData.token[i] != patternData.token[i]){
				result = false;
				return result;
			}
		}
	
		return result;
	}
}
