package com.dmc.grip.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public final class DBProvider extends ContentProvider {
	private static SQLiteDatabase     sqlDB;
	protected static DBHelper mOpenHelper;
	private static final String LOG_TAG = "DBProvider";

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		String Tablename = null;
		Tablename = uri.getPathSegments().get(0);
		int count = sqlDB.delete(Tablename, selection, selectionArgs);
        
        if (count > 0) {
        	getContext().getContentResolver().notifyChange(uri, null);
        }
        
        Log.v(LOG_TAG, Tablename + ": " + count + " row(s) deleted");
        return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		sqlDB = mOpenHelper.getWritableDatabase();
		
		String Tablename = null;
		Tablename = uri.getPathSegments().get(0);
		long rowId = sqlDB.insert(Tablename, "", values);
		
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(
					uri.buildUpon(), rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		else{
			return null;
		}
	}

	@Override
	public boolean onCreate() {
		
		mOpenHelper = new DBHelper(getContext());
		sqlDB = mOpenHelper.getWritableDatabase();
		
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		sqlDB = mOpenHelper.getReadableDatabase();
        
        String Tablename = null;
		Tablename = uri.getPathSegments().get(0);
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Tablename);

        Cursor c = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String Tablename = null;
		Tablename = uri.getPathSegments().get(0);
        
        int count = sqlDB.update(Tablename, values, selection, selectionArgs);

        if (count > 0) {
        	getContext().getContentResolver().notifyChange(uri, null);
        }
        
        Log.v(LOG_TAG, Tablename + ": " + count + " row(s) <- " + values + " update.");
        return count;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		
		sqlDB = mOpenHelper.getWritableDatabase();
		
		String Tablename = null;
		Tablename = uri.getPathSegments().get(0);
		try{
			sqlDB.beginTransaction();
			for(ContentValues cv : values){
				long rowId = sqlDB.insert(Tablename, "", cv);
				
				if (rowId > 0) {
					Uri rowUri = ContentUris.appendId(
							uri.buildUpon(), rowId).build();
					getContext().getContentResolver().notifyChange(rowUri, null);
					
				}
				else{
					Log.v(LOG_TAG, Tablename + ": " + "Failed to insert row into ..");
					return -1;
				}
			}
			sqlDB.setTransactionSuccessful();
			
		}
		catch (SQLException e){
			Log.v(LOG_TAG, Tablename + ": " + "Failed to insert row into .. " + e);
		}
		finally{
			sqlDB.endTransaction();
		}
		return values.length;
	}
}
