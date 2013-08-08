package com.dmc.grip.sensor;

import com.dmc.grip.data.GripSensorDataApi;
import com.dmc.grip.data.OnSensorDataListner;
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
	public OnSensorDataListner mSensorDataListner = null;

	public GripSensorEventManager(Context context) {
		// TODO Auto-generated constructor stub

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

				mGripData = context.getIntArray("Grip");

				/*
				 * int i = 0; for (i = 0; i < 30; i++) { Log.e(TAG, "grip [" + i
				 * + "] = " + Integer.toHexString(grip[i])); }
				 */

				/*
				 * int count = 0; for(int i : grip){
				 * //str.append(Integer.toString(i) + ", ");
				 * printBit(grip[count]); count++; }
				 */

				/*
				 * Log.e(TAG, "START"); printBit(mGripData[0]);
				 * printBit(mGripData[1]); printBit(mGripData[2]);
				 * printBit(mGripData[3]); printBit(mGripData[4]); Log.e(TAG,
				 * "END");
				 */

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
			} else
				return;
		}
	};

	public void registerCALstner(){
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
	
	public void setOnSensorDataListner(OnSensorDataListner sl){
		mSensorDataListner = sl;
		
		if (sl != null)
			collectSensorDataHandler.sendEmptyMessage(1000);
	}

	public void removeCollectSensorDataHandler() {
		collectSensorDataHandler.removeMessages(0);
		isStop = true;
	}
}
