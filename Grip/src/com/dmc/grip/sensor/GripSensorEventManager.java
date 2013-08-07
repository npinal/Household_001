package com.dmc.grip.sensor;

import com.dmc.grip.R;

import android.content.Context;
import android.hardware.contextaware.ContextAwareConstants;
import android.hardware.contextaware.ContextAwareManager;
import android.hardware.contextaware.manager.ContextAwareListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GripSensorEventManager {
	private ContextAwareManager mContextAwareManager;
	public static final String TAG = "GripSensorEventManager";

	public GripSensorEventManager(Context context) {
		// TODO Auto-generated constructor stub

		mContextAwareManager = (ContextAwareManager) context
				.getSystemService("context_aware");

		mContextAwareManager.setCAProperty(
				ContextAwareManager.GRIP_SENSOR_SERVICE,
				ContextAwareManager.PROPERTY_GRIP_SENSOR_FLAG,
				ContextAwareConstants.GRIP_SENSOR_FLAG_EVENT_DATA);

		mContextAwareManager.registerListener(mCAListener,
				ContextAwareManager.GRIP_SENSOR_SERVICE);

		collectSensorDataHandler.sendEmptyMessage(1000);
	}
	
	private ContextAwareListener mCAListener = new ContextAwareListener() {

		@Override
		public void onContextChanged(final int type, final Bundle context) {
			if (type == ContextAwareManager.GRIP_SENSOR_SERVICE) {
				// TODO : receive the context

				final int[] grip = context.getIntArray("Grip");

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
				Log.e(TAG, "START");
				printBit(grip[0]);
				printBit(grip[1]);
				printBit(grip[2]);
				printBit(grip[3]);
				printBit(grip[4]);
				Log.e(TAG, "END");
				*/
			}
		}
	};

	private Handler collectSensorDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			collectSensorDataHandler.sendEmptyMessageDelayed(msg.what + 1, 500);
			Log.e(TAG, "what = " + msg.what);
		}
	};

	public void unregisterCAListener() {
		if (mContextAwareManager != null) {
			mContextAwareManager.unregisterListener(mCAListener,
					ContextAwareManager.GRIP_SENSOR_SERVICE);
		}
	}
	
	public void removeCollectSensorDataHandler(){
		collectSensorDataHandler.removeMessages(0);
	}

	private void printBit(int value) {
		String binaryString = Integer.toBinaryString(value);
		while (binaryString.length() % 4 != 0) {
			binaryString = "0" + binaryString;
		}

		Log.e(TAG,
				"Source : " + binaryString + "(ox" + Integer.toHexString(value)
						+ ")");

		/*
		 * for (int i = 0; i < binaryString.length(); i++) { Log.e(TAG, "\tbit "
		 * + i + " : " + ((value >> i & 1) == 1)); }
		 */
	}

}
