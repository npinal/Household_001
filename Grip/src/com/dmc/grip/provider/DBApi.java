package com.dmc.grip.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public final class DBApi {
	public static final String LOG_TAG = "DBApi";
	
	public static final int SUCCESS = 0;
	public static final int NOTSUCCESS = -1;
	public static final String AUTHORITY = "com.dmc.grip.provider";
	
	
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
		
		public static final String GRIP_TOUCH = "grip_touch";	// 그립 터치 
		public static final String QUICK_RUN = "quick_run";	// 빠른 실행 
		public static final String QUICK_RUN_LOCK = "quick_run_lock";	// 빠른실행 > 화면잠금해제 
		public static final String QUICK_RUN_CAMERA = "quick_run_camera";	// 빠른실행 > 카메라 
		public static final String QUICK_RUN_EBOOK = "quick_run_ebook";	// 빠른실행 > 이북 
		
		public static final String ONE_HAND_NAVI = "one_hand_navi";	// 한 손 네비게이션 
		public static final String MEDIA_EDIT = "media_edit";	// 간편 미디어 편집 
		
	}
}
