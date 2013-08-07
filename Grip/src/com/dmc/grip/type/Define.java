package com.dmc.grip.type;

import android.os.Environment;

public class Define {
	public static final String SETTING_SAVE_PATH = Environment.getExternalStorageDirectory() + "/Grip_Pattern/";
	
	public static final String SETTING_QUICK_LOCK = SETTING_SAVE_PATH + "quick_lock.txt";
	public static final String SETTING_QUICK_CAMERA = SETTING_SAVE_PATH + "quick_camera.txt";
	public static final String SETTING_QUICK_EBOOK = SETTING_SAVE_PATH + "quick_ebook.txt";
	
	public static final int POWER_EMPTY = 0;
	public static final int POWER_MIDDLE = 1;
	public static final int POWER_FULL = 2;
	
	public static final int HAND_RIGHT = 11;
	public static final int HAND_LEFT = 10;
	public static final int HAND_NO = 00;
	public static final int HAND_IMPOSSIBLE = 01;
	
	public static final String FILE_SEPARATOR = "-";
	
}
