package com.dmc.grip.data;

import com.dmc.grip.type.Define;
import com.dmc.grip.type.Define.HandStatus;
import com.dmc.grip.utils.PrintUtils;

import android.util.Log;

public class GripSensorDataApi {
	private static final String TAG = "GripSensorDataApi[";

	private HandStatus mSensorDriveStatus = HandStatus.HAND_STATUS_GRIP_RELEASE;

	private GripSensorData mGripSensorData;
	private int mFirstHand;
	private int firstTouch = 0;
	protected int[] mFistGripSensorDataValue;

	public GripSensorDataApi() {
		// TODO Auto-generated constructor stub
		mGripSensorData = new GripSensorData();
		mGripSensorData.mValue = new int[30];
		mGripSensorData.mResult = true;

		mFistGripSensorDataValue = new int[30];
	}

	public int[] getSersorValue() {
		return mGripSensorData.mValue;
	}

	public int getSensorHand() {
		return mGripSensorData.mHand;
	}

	public int getSensorPower() {
		return mGripSensorData.mPower;
	}

	public boolean getSensorResult() {
		return mGripSensorData.mResult;
	}

	private boolean getCheckChangedFinger(int maxCount, GripSensorData grip) {
		boolean result = true;
		int wrongCountNum = 0;

		if (grip == null)
			return true;

		if (getSensorDriveStatus() == HandStatus.HAND_STATUS_GRIP_PUSH) {
			if (grip.mHand != mFirstHand) {
				result = false;
				return result;
			}

			for (int i = 0; i < mGripSensorData.mValue.length; i++) {
				if (mFistGripSensorDataValue[i] != mGripSensorData.mValue[i]) {
					wrongCountNum++;
				}
			}
		}

		if (Define.FEATURE_POWER_MODE != Define.FEATURE_POWER_MODE_POWER_ALL) {

			Log.e(TAG, "wrongCount = " + wrongCountNum);

			if (wrongCountNum > maxCount) {
				result = false;
			}

		}

		return result;
	}

	public boolean isSensorTouched(int[] grip) {
		boolean result = true;

		int hand = grip[0] & Define.MASK_BIT_HAND;
		int power = grip[0] & Define.MASK_BIT_POWER;

		switch (hand) {
		case Define.HAND_LEFT:
		case Define.HAND_RIGHT:
			if (Define.FEATURE_POWER_MODE == Define.FEATURE_POWER_MODE_POWER_ALL) {
				result = true;
			} else {
				if (power == Define.POWER_FULL) {
					result = true;
				} else
					result = false;
			}
			break;
		case Define.HAND_NO:
			result = false;
			break;
		case Define.HAND_IMPOSSIBLE:
			result = false;
			break;
		default:
			result = false;
			break;
		}

		return result;
	}

	public void parseSensorData(int grip[]) {
		int sensorNum = 0;

		if (grip == null) {
			Log.e(TAG, "Grip Data is NULL");
			return;
		}

		// PrintUtils.printBit("grip", grip[0]);

		if (isSensorTouched(grip)) {
			setSensorDriveStatus(HandStatus.HAND_STATUS_GRIP_PUSH);
		} else {
			setSensorDriveStatus(HandStatus.HAND_STATUS_GRIP_RELEASE);
		}

		for (int i = 7; i >= 0; i--) {
			mGripSensorData.mValue[sensorNum] = ((grip[1] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}

		for (int i = 7; i >= 0; i--) {
			mGripSensorData.mValue[sensorNum] = ((grip[2] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}

		for (int i = 7; i >= 0; i--) {
			mGripSensorData.mValue[sensorNum] = ((grip[3] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}

		for (int i = 7; i >= 2; i--) {
			mGripSensorData.mValue[sensorNum] = ((grip[4] >> i) & Define.MASK_BIT_SENSOR);
			sensorNum++;
		}

		/*
		 * for (int i=0 ; i<30; i++){ PrintUtils.printBit("value",
		 * mGripSensorData.mValue[i]); }
		 */

		mGripSensorData.mHand = grip[0] & Define.MASK_BIT_HAND;
		// PrintUtils.printBit("hand", mGripSensorData.mHand);

		mGripSensorData.mPower = grip[0] & Define.MASK_BIT_POWER;
		// PrintUtils.printBit("power", mGripSensorData.mPower);

		if (Define.FEATURE_POWER_MODE == Define.FEATURE_POWER_MODE_POWER_ALL) {
			if (mGripSensorData.mPower == 0x00)
				mGripSensorData.mPower = 0x20;
		}

		if (firstTouch != 0) {
			mGripSensorData.mResult = getCheckChangedFinger(
					Define.WRONG_MAX_COUNT, mGripSensorData);
		}

		Log.e(TAG, "handResult = " + mGripSensorData.mResult);

		if (mGripSensorData.mResult == false)
			firstTouch = 0;

		if (firstTouch == 0
				&& (getSensorDriveStatus() == HandStatus.HAND_STATUS_GRIP_PUSH)) {
			Log.e(TAG, "FirstTouched!!!");

			// --- 최초 인식된 hand값 저장
			mFirstHand = mGripSensorData.mHand;

			// --- 최초 인식된 SensorVale값 저장
			for (int i = 0; i < mGripSensorData.mValue.length; i++) {
				mFistGripSensorDataValue[i] = mGripSensorData.mValue[i];
			}

			firstTouch = 1;
		}
	}

	public void clearSensorData() {
		for (int i = 0; i < mGripSensorData.mValue.length; i++) {
			mGripSensorData.mValue[i] = 0;
		}
		mGripSensorData.mHand = 0;
		mGripSensorData.mPower = 0;
		mGripSensorData.mResult = true;

	}

	public HandStatus getSensorDriveStatus() {
		return mSensorDriveStatus;
	}

	public void setSensorDriveStatus(HandStatus sensorDriveStatus) {
		this.mSensorDriveStatus = sensorDriveStatus;
	}
}
