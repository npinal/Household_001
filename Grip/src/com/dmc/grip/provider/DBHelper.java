package com.dmc.grip.provider;

import com.dmc.grip.R;

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
	
	public void loadSystemSettings(SQLiteDatabase db){
    	SQLiteStatement stmt = null;
    	try {
    		stmt = db.compileStatement("INSERT OR IGNORE INTO system(name,value)"
                    + " VALUES(?,?);");
    		
    		loadBooleanSetting(stmt, DBApi.TblSystem.GRIP_TOUCH, R.bool.grip_touch);
    		loadBooleanSetting(stmt, DBApi.TblSystem.QUICK_RUN, R.bool.quick_run);
    		loadBooleanSetting(stmt, DBApi.TblSystem.QUICK_RUN_LOCK, R.bool.quick_run_lock);
    		loadBooleanSetting(stmt, DBApi.TblSystem.QUICK_RUN_CAMERA, R.bool.quick_run_camera);
    		loadBooleanSetting(stmt, DBApi.TblSystem.QUICK_RUN_EBOOK, R.bool.quick_run_ebook);
    		
    		loadBooleanSetting(stmt, DBApi.TblSystem.ONE_HAND_NAVI, R.bool.one_hand_navi);
    		loadBooleanSetting(stmt, DBApi.TblSystem.MEDIA_EDIT, R.bool.media_edit);
    		
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
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
