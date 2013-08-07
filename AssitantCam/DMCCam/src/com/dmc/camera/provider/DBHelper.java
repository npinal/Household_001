package com.dmc.camera.provider;

import com.dmc.camera.assist.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String LOG_TAG = "DBHelper";
    private static final String DATABASE_NAME = "Assistant.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;
	
	public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
	
	private void createSystemTable(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE system (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE ON CONFLICT REPLACE," +
                "value TEXT" +
                ");");
    }
	
	private void createGalleryTable(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE gallery (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "path TEXT NOT NULL," +
                "filename TEXT NOT NULL," +
                "memo TEXT NOT NULL," +
                "category INTEGER NOT NULL," +
                "sound TEXT," +
                "recording TEXT," +
                "weather TEXT," +
                "gps TEXT" +
                ");");
    }
	
	private void createCategoryTable(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE category (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL" +
                ");");
    }
	
	public void loadSystemSettings(SQLiteDatabase db){
    	SQLiteStatement stmt = null;
    	try {
    		stmt = db.compileStatement("INSERT OR IGNORE INTO system(name,value)"
                    + " VALUES(?,?);");

    		loadStringSetting(stmt, DBApi.TblSystem.SAVE_TYPE, R.string.save_type);
    		loadStringSetting(stmt, DBApi.TblSystem.SHOT_VOICE_GUIDE, R.string.shot_voice_guide);
    		loadStringSetting(stmt, DBApi.TblSystem.SHOT_MODE, R.string.shot_mode);
    		loadStringSetting(stmt, DBApi.TblSystem.PERSON_VOICE_GUIDE, R.string.persion_voice_guide);
    		loadStringSetting(stmt, DBApi.TblSystem.WEATHER_VOICE_GUIDE, R.string.weather_voice_guide);
    		loadStringSetting(stmt, DBApi.TblSystem.VOICE_CONTROL, R.string.voice_control);
    		loadStringSetting(stmt, DBApi.TblSystem.FLASH, R.string.flash_setting);
    		loadStringSetting(stmt, DBApi.TblSystem.RESOLUTION, R.string.resolution);
    		loadStringSetting(stmt, DBApi.TblSystem.GPS_TAG, R.string.gps_tag);
    		loadStringSetting(stmt, DBApi.TblSystem.SOUND_BUTTON, R.string.sound_button);
    		loadStringSetting(stmt, DBApi.TblSystem.VIEW_FINDER, R.string.view_finder);
    		loadStringSetting(stmt, DBApi.TblSystem.SAVE_PLACE, R.string.save_place);
    		
    	} finally {
            if (stmt != null){
            	stmt.close();
            }
        }
    }
	
	private void loadSetting(SQLiteStatement stmt, String key, Object value) {
        stmt.bindString(1, key);
        stmt.bindString(2, value.toString());
        stmt.execute();
    }

    private void loadStringSetting(SQLiteStatement stmt, String key, int resid) {
        loadSetting(stmt, key, mContext.getResources().getString(resid));
    }

    private void loadBooleanSetting(SQLiteStatement stmt, String key, int resid) {
        loadSetting(stmt, key,
                mContext.getResources().getBoolean(resid) ? "1" : "0");
    }

    private void loadIntegerSetting(SQLiteStatement stmt, String key, int resid) {
        loadSetting(stmt, key,
                Integer.toString(mContext.getResources().getInteger(resid)));
    }

    private void loadFractionSetting(SQLiteStatement stmt, String key, int resid, int base) {
        loadSetting(stmt, key,
                Float.toString(mContext.getResources().getFraction(resid, base, base)));
    }
    

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.d(LOG_TAG, "databasehelper onCreate");
		
		createSystemTable(db);
		loadSystemSettings(db);
		createGalleryTable(db);
		createCategoryTable(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
