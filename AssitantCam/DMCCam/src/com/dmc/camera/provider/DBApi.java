package com.dmc.camera.provider;

import java.util.ArrayList;

import com.dmc.camera.type.Type.Category;
import com.dmc.camera.type.Type.Gallery;
import com.dmc.camera.util.Utils;

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
	
	public static class TblGallery {
		public final static String tableName = "gallery";
		public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +  tableName);
		
		public final static String FIELD_ID = "_id";
		public final static String FIELD_PATH = "path";
		public final static String FIELD_FILENAME = "filename";
		public final static String FIELD_MEMO = "meno";
		public final static String FIELD_CATEGORY = "category";
		public final static String FIELD_SOUND = "sound";
		public final static String FIELD_RECORDING = "recording";
		public final static String FIELD_WEATHER = "weather";
		public final static String FIELD_GPS = "gps";
		
		public final static int GALLERY_ID_NULL = -1;
		
		public static final boolean insert (ContentResolver resolver, String path, String filename, String memo, int category, String sound, String recording, 
				String weather, String gps){
			
			ContentValues values = new ContentValues();
			values.put(FIELD_PATH, path);
			values.put(FIELD_FILENAME, filename);
			values.put(FIELD_MEMO, memo);
			values.put(FIELD_CATEGORY, category);
			values.put(FIELD_SOUND, sound);
			values.put(FIELD_RECORDING, recording);
			values.put(FIELD_WEATHER, weather);
			values.put(FIELD_GPS, gps);
			
			if(resolver.insert(CONTENT_URI, values) != null){
				return true;
			}
			
			return false;
		}
		
		public static final int select (ContentResolver resolver, ArrayList<Gallery> galleryInfoList, int gallery_id){
			
			String gallery_id_string = null;
			
			if(gallery_id != GALLERY_ID_NULL){
				gallery_id_string = Utils.intToString(gallery_id);
			}
			
			String where = "";
			String terms[] = null;
			
			where = getWhere(gallery_id_string);
			terms = getTerms(gallery_id_string);
			
			Cursor cur = resolver.query(CONTENT_URI, new String[] { FIELD_ID, FIELD_PATH, FIELD_FILENAME, FIELD_MEMO, FIELD_CATEGORY, FIELD_SOUND, FIELD_RECORDING, 
					FIELD_WEATHER, FIELD_GPS }, where, terms, null);
			
			if(cur == null){
				return NOTSUCCESS;
			}
			else if(cur.getCount() <= 0){
				cur.close();
				return SUCCESS;
			}
			
			if (cur.moveToFirst()) {
				
				do {
					
					Gallery galleryInfo = new Gallery();
					
					galleryInfo.id = cur.getInt(cur.getColumnIndex(FIELD_ID));
					galleryInfo.path = cur.getString(cur.getColumnIndex(FIELD_PATH));
					galleryInfo.filename = cur.getString(cur.getColumnIndex(FIELD_FILENAME));
					galleryInfo.memo = cur.getString(cur.getColumnIndex(FIELD_MEMO));
					galleryInfo.category_id = cur.getInt(cur.getColumnIndex(FIELD_CATEGORY));
					galleryInfo.sound = cur.getString(cur.getColumnIndex(FIELD_SOUND));
					galleryInfo.recording = cur.getString(cur.getColumnIndex(FIELD_RECORDING));
					galleryInfo.weather = cur.getString(cur.getColumnIndex(FIELD_WEATHER));
					galleryInfo.gps = cur.getString(cur.getColumnIndex(FIELD_GPS));
					
					galleryInfoList.add(galleryInfo);
					
					
				} while (cur.moveToNext());
		
			} 
			cur.close();
			
			return SUCCESS;
		}
		
		public static final int delete (ContentResolver resolver, int gallery_id){
			String where = "";
			String terms[] = null;
			
			String gallery_id_string = null;
			if(gallery_id != GALLERY_ID_NULL){
				gallery_id_string = Utils.intToString(gallery_id);
			}
			
			where = getWhere(gallery_id_string);
			terms = getTerms(gallery_id_string);

			return resolver.delete(CONTENT_URI, where, terms);
		}
		
		private static final int getCount(ContentResolver resolver, String gallery_id){
			int count = 0;
			Cursor cur = resolver.query(CONTENT_URI, new String[] { FIELD_ID }, 
					FIELD_ID + "=?", new String[] { gallery_id }, null);
			if(cur == null){
				return NOTSUCCESS;
			}
			count = cur.getCount();
			cur.close();
			return count;
		}
		
		public static final String getWhere(String gallery_id){
			String where = "";
			
			if(gallery_id != null){
				where = where + FIELD_ID + "=?";
			}

			if (where.equals("")){
				where = null;
			}
			
			return where;
		}
		
		public static final String[] getTerms(String gallery_id){
			String terms[] = new String[1];
			String returnTerms[] = null;
			
			int i = 0;
			
			if(gallery_id != null){
				terms[i] = gallery_id;
				i = i+1;
			}
			
			if(i != 0){
				returnTerms = new String[i];
				for (int j = 0;j < i; j++){
					returnTerms[j] = terms[j];
				}
			}
			
			return returnTerms;
		}
	}
	
	
	public static class TblCategory {
		public final static String tableName = "category";
		public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +  tableName);
		
		public final static String FIELD_ID = "_id";
		public final static String FIELD_NAME = "name";
		
		public final static int CATEGORY_ID_NULL = -1;
		
		public static final boolean insert (ContentResolver resolver, String category_name){
			
			ContentValues values = new ContentValues();
			values.put(FIELD_NAME, category_name);
			
			if(resolver.insert(CONTENT_URI, values) != null){
				return true;
			}
			
			return false;
		}
		
		public static final int select (ContentResolver resolver, ArrayList<Category> categoryInfoList, int category_id){
			
			String category_id_string = null;
			
			if(category_id != CATEGORY_ID_NULL){
				category_id_string = Utils.intToString(category_id);
			}
			
			String where = "";
			String terms[] = null;
			
			where = getWhere(category_id_string);
			terms = getTerms(category_id_string);
			
			Cursor cur = resolver.query(CONTENT_URI, new String[] { FIELD_ID, FIELD_NAME }, where, terms, null);
			
			if(cur == null){
				return NOTSUCCESS;
			}
			else if(cur.getCount() <= 0){
				cur.close();
				return SUCCESS;
			}
			
			if (cur.moveToFirst()) {
				
				do {
					
					Category categoryInfo = new Category();
					
					categoryInfo.id = cur.getInt(cur.getColumnIndex(FIELD_ID));
					categoryInfo.category_name = cur.getString(cur.getColumnIndex(FIELD_NAME));
					
					categoryInfoList.add(categoryInfo);
					
					
				} while (cur.moveToNext());
		
			} 
			cur.close();
			
			return SUCCESS;
		}
		
		public static final int delete (ContentResolver resolver, int category_id){
			String where = "";
			String terms[] = null;
			
			String category_id_string = null;
			if(category_id != CATEGORY_ID_NULL){
				category_id_string = Utils.intToString(category_id);
			}
			
			where = getWhere(category_id_string);
			terms = getTerms(category_id_string);

			return resolver.delete(CONTENT_URI, where, terms);
		}
		
		private static final int getCount(ContentResolver resolver, String gallery_id){
			int count = 0;
			Cursor cur = resolver.query(CONTENT_URI, new String[] { FIELD_ID }, 
					FIELD_ID + "=?", new String[] { gallery_id }, null);
			if(cur == null){
				return NOTSUCCESS;
			}
			count = cur.getCount();
			cur.close();
			return count;
		}
		
		public static final String getWhere(String gallery_id){
			String where = "";
			
			if(gallery_id != null){
				where = where + FIELD_ID + "=?";
			}

			if (where.equals("")){
				where = null;
			}
			
			return where;
		}
		
		public static final String[] getTerms(String gallery_id){
			String terms[] = new String[1];
			String returnTerms[] = null;
			
			int i = 0;
			
			if(gallery_id != null){
				terms[i] = gallery_id;
				i = i+1;
			}
			
			if(i != 0){
				returnTerms = new String[i];
				for (int j = 0;j < i; j++){
					returnTerms[j] = terms[j];
				}
			}
			
			return returnTerms;
		}
	}
	
}
