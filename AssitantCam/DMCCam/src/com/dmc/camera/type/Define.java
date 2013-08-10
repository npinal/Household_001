package com.dmc.camera.type;

public class Define {
	
	public static final Boolean DEBUG = true;
	
	public static final String BASE_PATH = "/Assistant_Camera";
	public static final String PHOTO_PATH = BASE_PATH + "/photo/";
	public static final String PHOTO_TEMP_PATH = BASE_PATH + "./tmp_photo/";
	public static final String PHOTO_THUMB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "./thumb_path/";
	public static final String PHOTO_SOUND_PATH = BASE_PATH + "/sound/";
	public static final String PHOTO_SOUND_SHOT_PATH = BASE_PATH + "/soundshot/";
	
}
