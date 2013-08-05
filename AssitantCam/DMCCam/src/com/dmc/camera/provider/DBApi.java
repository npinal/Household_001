package com.dmc.camera.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public final class DBApi {
	public static final String LOG_TAG = "DBApi";
	
	public static final int SUCCESS = 0;
	public static final int NOTSUCCESS = -1;
	public static final String AUTHORITY = "com.dmc.camera.provider";
	
	
	public static final class TblSystem {
		public final static String tableName = "system";
		public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +  tableName);
		
		public final static String FIELD_KEY = "name";
		public final static String FIELD_VALUE = "value";
		
		public static final boolean putBoolean(ContentResolver resolver, String key, boolean value){
			if(value == true){
				return putInt(resolver, key, 1);
			}
			else {
				return putInt(resolver, key, 0);
			}
		}
		
		public static final boolean getBoolean(ContentResolver resolver, String key, boolean def){
			int value = -1;
			
			if(def == true){
				value = getInt(resolver, key, 1);
			}
			else{
				value = getInt(resolver, key, 0);
			}
			
			if(value == 1){
				return true;
			}
			else {
				return false;
			}
		}
		
		public static final boolean putInt(ContentResolver resolver, String key, int value){
			return putString(resolver, key, Integer.toString(value));
		}
		
		public static final int getInt(ContentResolver resolver, String key, int def){
			String v = getString(resolver, key);
            try {
            	return v != null ? Integer.parseInt(v) : def;
            } catch (NumberFormatException e) {
                return def;
            }
		}
		
		public static final boolean putLong(ContentResolver resolver, String key, long value){
			return putString(resolver, key, Long.toString(value));
		}
		
		public static final long getLong(ContentResolver resolver, String key, long def){
			String v = getString(resolver, key);
            try {
            	return v != null ? Long.parseLong(v) : def;
            } catch (NumberFormatException e) {
                return def;
            }
		}
		
		public static final boolean putFloat(ContentResolver resolver, String key, float value){
			return putString(resolver, key, Float.toString(value));
		}
		
		public static final float getFloat(ContentResolver resolver, String key, float def){
			String v = getString(resolver, key);
            try {
            	return v != null ? Float.parseFloat(v) : def;
            } catch (NumberFormatException e) {
                return def;
            }
		}
		
		public static final boolean putString(ContentResolver resolver, String key, String value){
			ContentValues values =  new ContentValues();
			values.put(FIELD_KEY, key);
			values.put(FIELD_VALUE, value);
			
			//resolver.insert(CONTENT_URI, values);
			
			String result = null;
			result = getString(resolver, key);
			if(result == null){
				if(resolver.insert(CONTENT_URI, values) != null){
					return true;
				}
			}
			else {
				if(resolver.update(CONTENT_URI, values, FIELD_KEY + "=?", new String[] { key }) >= 0){
					return true;
				}
			}
			return false;
		}
		
		public static final String getString(ContentResolver resolver, String key){
			Cursor cur = resolver.query(CONTENT_URI, new String[] { FIELD_VALUE }, FIELD_KEY + "=?", new String[] { key }, null);
			String value = null;
			
			if (cur.moveToFirst()) {
				do {
					// Get the field values
					value = cur.getString(cur.getColumnIndex(FIELD_VALUE));
				} while (cur.moveToNext());
			}
			if (cur != null){
				cur.close();
			}
			
			return value;
		}
		
		public static final int deleteString(ContentResolver resolver, String Tablename, String key){
			return resolver.delete(CONTENT_URI, FIELD_KEY + "=?", new String[] { key });
		}
		
		// 카메라 설정 
		public static final String SAVE_TYPE = "save_type";	// 촬영시 저장방식 설정 
		public static final String SHOT_VOICE_GUIDE = "shot_voice_guide";	// 사진구도 음성가이드 
		public static final String SHOT_MODE = "shot_mode";	// 촬영모드 
		public static final String PERSON_VOICE_GUIDE = "persion_voice_guide";	// 인물정보 음성안내 제공 
		public static final String WEATHER_VOICE_GUIDE = "weather_voice_guide";	// 날씨정보 음성안내 제공 
		
		// 카메라 상세설정 
		public static final String VOICE_CONTROL = "voice_control";	// 음성제어 
		public static final String FLASH = "flash";	// 플래시 
		public static final String RESOLUTION = "resolution";	// 해상도 
		public static final String GPS_TAG = "gps_tag";	// GPS 태그 
		public static final String SOUND_BUTTON = "sound_button";	// 음량버튼을 다음으로 설정 
		public static final String VIEW_FINDER = "view_finder";	// 뷰파인더 안내선 표시 
		public static final String SAVE_PLACE = "save_place";	// 저장소 
		
	}
}
