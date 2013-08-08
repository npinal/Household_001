package com.dmc.grip.type;

import android.os.Environment;

public class Define {
	public static final String SETTING_SAVE_PATH = Environment.getExternalStorageDirectory() + "/Grip_Pattern/";
	
	public static final String SETTING_QUICK_LOCK = SETTING_SAVE_PATH + "quick_lock.txt";
	public static final String SETTING_QUICK_CAMERA = SETTING_SAVE_PATH + "quick_camera.txt";
	public static final String SETTING_QUICK_EBOOK = SETTING_SAVE_PATH + "quick_ebook.txt";
	
	public static final int POWER_MIDDLE = 0x00;
	public static final int POWER_FULL = 0x20;
	
	public static final int HAND_RIGHT = 0xC0;
	public static final int HAND_LEFT = 0x80;
	public static final int HAND_NO = 0x00;
	public static final int HAND_IMPOSSIBLE = 0x40;
	
	public static final String FILE_SEPARATOR = "-";
	
	public static int MASK_BIT_HAND = 0xC0;
	public static int MASK_BIT_POWER = 0x20;
	public static int MASK_BIT_SENSOR = 0x01;
}
