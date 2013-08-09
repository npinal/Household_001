package com.dmc.grip.type;

import android.os.Environment;

public class Define {
	public static final String SETTING_SAVE_PATH = Environment.getExternalStorageDirectory() + "/Grip_Pattern/";
	
	public static final String SETTING_QUICK_LOCK = SETTING_SAVE_PATH + "quick_lock.txt";
	public static final String SETTING_QUICK_CAMERA = SETTING_SAVE_PATH + "quick_camera.txt";
	public static final String SETTING_QUICK_EBOOK = SETTING_SAVE_PATH + "quick_ebook.txt";
	
	public static final String PATTERN_QUICK_LOCK = SETTING_SAVE_PATH + "quick_lock_pattern.txt"; 	
	public static final String PATTERN_QUICK_CAMERA = SETTING_SAVE_PATH + "quick_camera_pattern.txt"; 	
	public static final String PATTERN_QUICK_EBOOK = SETTING_SAVE_PATH + "quick_ebook_pattern.txt"; 	
	
	public static final String FILE_SEPARATOR = "-";

	public static final int POWER_MIDDLE = 0x00;
	public static final int POWER_FULL = 0x20;

	public static final int HAND_RIGHT = 0xC0;
	public static final int HAND_LEFT = 0x80;
	public static final int HAND_NO = 0x00;
	public static final int HAND_IMPOSSIBLE = 0x40;

	public static int MASK_BIT_HAND = 0xC0;
	public static int MASK_BIT_POWER = 0x20;
	public static int MASK_BIT_SENSOR = 0x01;

	public static int WRONG_MAX_COUNT = 5;
	
	public static final int HAND_STATUS_GRIP_START = 0x01;
	public static final int HAND_STATUS_GRIP_PUSH = 0x02;
	public static final int HAND_STATUS_GRIP_RELEASE = 0x03;	
	public static final int HAND_STATUS_GRIP_ERROR = 0x04;
	
	public static final long GRIP_REGIST_TIMEOUT = 1000 * 60;	// 1분
	
	public static final long GRIP_THRESHOLD = 1000 * 3; 	// 3초 
	public static final int GRIP_SETTING_THRESHOLD = 500; 	// 0.5초 
	
	public static enum HandStatus {
		HAND_STATUS_GRIP_PUSH, HAND_STATUS_GRIP_RELEASE, HAND_STATUS_GRIP_ERROR,
	};
	
	public static enum IndexShortcutPattern{
		INDEX_NONE, INDEX_LOCK, INDEX_CAMERA, INDEX_EBOOK,
	};
}
