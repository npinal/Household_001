package com.dmc.grip.type;

import android.os.Environment;

public class Define {
	public static final String SETTING_SAVE_PATH = Environment.getExternalStorageDirectory() + "/Grip_Pattern/";
	
	public static final String SETTING_QUICK_LOCK = SETTING_SAVE_PATH + "quick_lock.txt";
	public static final String SETTING_QUICK_CAMERA = SETTING_SAVE_PATH + "quick_camera.txt";
	public static final String SETTING_QUICK_EBOOK = SETTING_SAVE_PATH + "quick_ebook.txt";
	
}
