package com.dmc.grip.sensor;

import com.dmc.grip.data.GripSensorDataApi;
import com.dmc.grip.data.OnSensorDataListner;
import com.dmc.grip.data.PatternData;
import com.dmc.grip.data.SensorDataEvent;
import com.dmc.grip.utils.PrintUtils;

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

				for (int i = 0; i < mLibGripData.length; i++) {
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
				 * Log.e(TAG, "START"); PrintUtils.printBit("org",
				 * mLibGripData[0]); PrintUtils.printBit("org",
				 * mLibGripData[1]); PrintUtils.printBit("org",
				 * mLibGripData[2]); PrintUtils.printBit("org",
				 * mLibGripData[3]); PrintUtils.printBit("org",
				 * mLibGripData[4]); Log.e(TAG, "END");
				 */
				//Log.e(TAG, "power x = " + mLibGripData[0]);
				//PrintUtils.printBit("power b = ", mLibGripData[0]);
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

	public boolean compareSensorValue(int maxCompareCount, final int[] gripOrg,
			final int[] gripTmp) {
		boolean result = true;
		int wrongCount = 0;

		for (int i = 0; i < gripOrg.length; i++) {
			if (gripOrg[i] != gripTmp[i]) {
				wrongCount++;
			}
		}

		if (wrongCount > maxCompareCount)
			result = false;

		return result;
	}

	public boolean checkPatterData(PatternData patternData,
			PatternData savedPatternData) {
		boolean result = true;
		
		// --- Sensor Token Check
		if (savedPatternData.token.length != patternData.token.length)
			return result;

		// --- Sensor All Token Check
		if (patternData.all_token != savedPatternData.all_token) {
			result = false;
			return result;
		}

		// --- Sensor Hand Check
		if (patternData.hand != savedPatternData.hand) {
			result = false;
			return result;
		}

		// --- Sensor Power Check
		for (int i = 0; i < savedPatternData.power.length; i++) {
			if (savedPatternData.power[i] != patternData.power[i]) {
				result = false;
				return result;
			}
		}

		for (int i = 0; i < patternData.token.length; i++) {
			if (savedPatternData.token[i] != patternData.token[i]) {
				result = false;
				return result;
			}
		}

		return result;
	}
}
