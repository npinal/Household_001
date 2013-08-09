package com.dmc.grip.utils;

import com.dmc.grip.type.Define;

import android.util.Log;

public class CustomLog {
	
	public final static void d(String LOG_TAG, String text){
		if(Define.DEBUG == true){
			Log.d(LOG_TAG, text);
		}
	}
	
	public final static void e(String LOG_TAG, String text){
		if(Define.DEBUG == true){
			Log.e(LOG_TAG, text);
		}
	}
	
	public final static void v(String LOG_TAG, String text){
		if(Define.DEBUG == true){
			Log.v(LOG_TAG, text);
		}
	}
}
