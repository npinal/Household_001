package com.dmc.grip.utils;

import android.util.Log;

public class PrintUtils {
	private static final String TAG = "PrintUtils";

	public static void printBit(String string, int value) {
		String binaryString = Integer.toBinaryString(value);
		while (binaryString.length() % 4 != 0) {
			binaryString = "0" + binaryString;
		}

		Log.e(TAG,
				string + " : " + binaryString + "(ox"
						+ Integer.toHexString(value) + ")");

		/*
		 * for (int i = 0; i < binaryString.length(); i++) { Log.e(TAG, "\tbit "
		 * + i + " : " + ((value >> i & 1) == 1)); }
		 */
	}
}
