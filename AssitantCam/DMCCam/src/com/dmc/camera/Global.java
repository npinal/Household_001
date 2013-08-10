package com.dmc.camera;

import com.dmc.camera.type.Define;

import android.app.Application;

public class Global extends Application{
	
	private String basePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	
	public void setBasePath(String base_path){
		basePath = base_path;
	}

	public String getPhotoPath() {
		return basePath + Define.PHOTO_PATH;
	}
	
	public String getSoundShotPath() {
		return basePath + Define.PHOTO_SOUND_SHOT_PATH;
	}
	
	public String getThumbPath() {
		return Define.PHOTO_THUMB_PATH;
	}
	
	public String getSoundPath() {
		return basePath + Define.PHOTO_SOUND_PATH;
	}

}