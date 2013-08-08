package com.dmc.grip.data;

import java.lang.reflect.Array;

import com.dmc.grip.type.Define;
import com.dmc.grip.utils.PrintUtils;

import android.util.Log;

public class GripSensorDataApi {
	private static final String TAG = "GripSensorDataApi[";
	private GripSensorData mGripSensorData;

	public GripSensorDataApi() {
		// TODO Auto-generated constructor stub
		mGripSensorData = new GripSensorData();
		mGripSensorData.mValue = new int[30];
	}

	public int[] getSersorValue() {
		return mGripSensorData.mValue;
	}
	
	public int getSensorHand(){
		return mGripSensorData.mHand;
	}
	
	public int getSensorPower(){
		return mGripSensorData.mPower;
	}
	
	public boolean getSensorResult(){
		return mGripSensorData.mResult;
	}

	// --- Feature Edited by jeanclad
	private boolean setCheckChangedHand() {
		return true;
	}

	public void parseSensorData(int grip[]) {
		int sensorNum = 0;
		if (grip == null){
			Log.e(TAG, "Grip Data is NULL");
			return;
		}
		
		//PrintUtils.printBit("grip", grip[0]);


		for (int i=7; i>=0; i--){
			mGripSensorData.mValue[sensorNum] = ((grip[1] >> i) & Define.MASK_BIT_SENSOR);			
			sensorNum++;
		}
		
		for (int i=7; i>=0; i--){
			mGripSensorData.mValue[sensorNum] = ((grip[2] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}
		
		for (int i=7; i>=0; i--){
			mGripSensorData.mValue[sensorNum] = ((grip[3] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}
		
		for (int i=7; i>=2; i--){
			mGripSensorData.mValue[sensorNum] = ((grip[4] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}
		
		for (int i=0 ; i<30; i++){
			PrintUtils.printBit("value", mGripSensorData.mValue[i]);
		}
		
		mGripSensorData.mHand = grip[0] & Define.MASK_BIT_HAND;
		//PrintUtils.printBit("hand", mGripSensorData.mHand);
		
		mGripSensorData.mPower = grip[0] & Define.MASK_BIT_POWER;
		//PrintUtils.printBit("power", mGripSensorData.mPower);
		
		mGripSensorData.mResult = setCheckChangedHand();
	}
}
