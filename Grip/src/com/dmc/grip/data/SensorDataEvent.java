package com.dmc.grip.data;

public class SensorDataEvent {
	public int [] mValue;
	public int mHand;
	public int mPower;
	public boolean mResult;
	public SensorDataEvent(int []value, int hand, int power, Boolean result) {
		// TODO Auto-generated constructor stub
		mValue = value;
		mHand = hand;
		mPower = power;
		mResult = result;
	}
}
