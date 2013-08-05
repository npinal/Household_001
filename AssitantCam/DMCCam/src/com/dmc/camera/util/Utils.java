package com.dmc.camera.util;

public class Utils {
	
	private static final String LOG_TAG = "Utils";
	
	public static final long TimeStamp() {
		return System.currentTimeMillis();
	}

	public static final String intToString(int value){
		String result = null;
		result = Integer.toString(value);
		return result;
	}
	
	public static final int stringToInt(String value){
		int result = Integer.parseInt(value);
		return result;
	}
	
	public static final String longToString(long value) {
		String result = Long.toString(value);
		return result;
	}
	
	public static final int findPosition(String[] menuValue, String value){
		
		int result = -1;
		
		for(int i=0; i < menuValue.length; i++){
			if(menuValue[i].equals(value)){
				result = i;
				break;
			}
		}
		
		return result;
	}
	
}
