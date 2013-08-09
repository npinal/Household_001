package com.dmc.grip.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.dmc.grip.R;
import com.dmc.grip.data.OnSensorDataListner;
import com.dmc.grip.data.SensorDataEvent;
import com.dmc.grip.sensor.GripSensorEventManager;
import com.dmc.grip.type.Define;
import com.dmc.grip.utils.FileUtils;

public class GripActivity extends Activity {
	private static final String TAG = "MainActivity";

	ArrayList<ImageView> ivList;

	public static final float total_width = 540; // 1920*1080

	public static final float WIDTH = 540;
	public static final float HEIGHT = 128.135553f;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 2;
	public static final int BOTTOM = 3;
	
	public static final int GRIP_TIMEOUT = 0;
	public static final int GRIP_FAIL = 1;

	AlertDialog mRegistStartDialog;
	AlertDialog mRegistOkDialog;
	AlertDialog mRegistFailDialog;
	Button mRegistOk;

	GripSensorEventManager mGripSensorEventManager;
	Context mContext;

	String mSaveSettingString = "";
	String mSaveSettingPath;
	
	String mPatternSavePath;

	int mQuickRunType;
	
	Boolean mFailFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;

		setContentView(R.layout.activity_main);

		mRegistStartDialog = new AlertDialog.Builder(GripActivity.this)
				.create();
		mRegistStartDialog.setMessage(getBaseContext().getResources()
				.getString(R.string.setting_grip_register_message) + 5);
		mRegistStartDialog.setCancelable(false);
		mRegistStartDialog.show();

		mRegistOkDialog = new AlertDialog.Builder(GripActivity.this).create();
		mRegistOkDialog.setMessage(getBaseContext().getResources().getString(
				R.string.setting_grip_register_ok));
		mRegistOkDialog.setCancelable(false);
		mRegistOkDialog.setButton(AlertDialog.BUTTON_POSITIVE,
				getString(R.string.common_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mRegistOkDialog.dismiss();
						finish();
					}
				});
		
		mRegistFailDialog = new AlertDialog.Builder(GripActivity.this).create();
		mRegistFailDialog.setMessage("");
		mRegistFailDialog.setButton(AlertDialog.BUTTON_POSITIVE,
				getString(R.string.common_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		mRegistOk = (Button) findViewById(R.id.grip_regist_ok);
		mRegistOk.setOnClickListener(mClick);

		ivList = new ArrayList<ImageView>();
		ivList.add((ImageView) findViewById(R.id.iv1));
		ivList.add((ImageView) findViewById(R.id.iv2));
		ivList.add((ImageView) findViewById(R.id.iv3));
		ivList.add((ImageView) findViewById(R.id.iv4));
		ivList.add((ImageView) findViewById(R.id.iv5));
		ivList.add((ImageView) findViewById(R.id.iv6));
		ivList.add((ImageView) findViewById(R.id.iv7));
		ivList.add((ImageView) findViewById(R.id.iv8));
		ivList.add((ImageView) findViewById(R.id.iv9));
		ivList.add((ImageView) findViewById(R.id.iv10));

		ivList.add((ImageView) findViewById(R.id.iv11));
		ivList.add((ImageView) findViewById(R.id.iv12));
		ivList.add((ImageView) findViewById(R.id.iv13));
		ivList.add((ImageView) findViewById(R.id.iv14));

		ivList.add((ImageView) findViewById(R.id.iv15));
		ivList.add((ImageView) findViewById(R.id.iv16));
		ivList.add((ImageView) findViewById(R.id.iv17));
		ivList.add((ImageView) findViewById(R.id.iv18));
		ivList.add((ImageView) findViewById(R.id.iv19));
		ivList.add((ImageView) findViewById(R.id.iv20));
		ivList.add((ImageView) findViewById(R.id.iv21));
		ivList.add((ImageView) findViewById(R.id.iv22));
		ivList.add((ImageView) findViewById(R.id.iv23));
		ivList.add((ImageView) findViewById(R.id.iv24));
		ivList.add((ImageView) findViewById(R.id.iv25));

		ivList.add((ImageView) findViewById(R.id.iv26));
		ivList.add((ImageView) findViewById(R.id.iv27));
		ivList.add((ImageView) findViewById(R.id.iv28));
		ivList.add((ImageView) findViewById(R.id.iv29));
		ivList.add((ImageView) findViewById(R.id.iv30));

		Intent intent = getIntent();

		mQuickRunType = intent.getIntExtra(QuickRunSetting.QUICK_TYPE,
				QuickRunSetting.QUICK_RUN_LOCK);
		if (mQuickRunType == QuickRunSetting.QUICK_RUN_LOCK) {
			mSaveSettingPath = Define.SETTING_QUICK_LOCK;
			mPatternSavePath = Define.PATTERN_QUICK_LOCK;
		} else if (mQuickRunType == QuickRunSetting.QUICK_RUN_CAMERA) {
			mSaveSettingPath = Define.SETTING_QUICK_CAMERA;
			mPatternSavePath = Define.PATTERN_QUICK_CAMERA;
		} else {
			mSaveSettingPath = Define.SETTING_QUICK_EBOOK;
			mPatternSavePath = Define.PATTERN_QUICK_EBOOK;
		}

		// --- Start GripSensorEventManager
		mGripSensorEventManager = new GripSensorEventManager(mContext);

		dialogHandler.sendEmptyMessage(0);

	}

	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");

//		handler.removeMessages(0);

		mGripSensorEventManager.removeCollectSensorDataHandler();
		mGripSensorEventManager.unregisterCAListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");

		if (mRegistStartDialog.isShowing() == false) {
			mGripSensorEventManager.registerCALstner();
			mGripSensorEventManager.setOnSensorDataListner(mSensorDataListener);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		failDialogHandler.removeMessages(GRIP_FAIL);
		failDialogHandler.removeMessages(GRIP_TIMEOUT);
		mFailFlag = false;
	}

	private Boolean SettingFileSave() {
		FileUtils.makeDirectory(Define.SETTING_SAVE_PATH);

		File file = new File(mSaveSettingPath);
		Log.d("Jihye", "mSaveSettingString : " + mSaveSettingString);
		FileUtils.deleteFile(file);

		return FileUtils.writeFile(file, mSaveSettingString.getBytes());
	}
	
	private Boolean PatternFileSave() {
		String accrue = "";
		int token = 1;
		int beforePower = -1;
		int afterPower = -1;
		int time = 0;
		int all_token = 0;
		
		File file = new File(mSaveSettingPath);
		if (file != null && file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis, "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					
					StringBuilder sb = new StringBuilder();
					sb.append(line);
					String[] parse = sb.toString().split(Define.FILE_SEPARATOR);
					
					int power = Integer.parseInt(parse[1]);

					if(accrue.equals("") && power == Define.POWER_FULL){
						accrue = accrue + parse[0];
						accrue = accrue + Define.FILE_SEPARATOR;
						accrue = accrue + parse[2];
					}
					
					if(!accrue.equals("")){
						time = time + Define.GRIP_SETTING_THRESHOLD;
						
						if(beforePower == -1){
							beforePower = power;
							afterPower = power;
							time = Define.GRIP_SETTING_THRESHOLD;
						}
						else{
							afterPower = power;
							
							if(beforePower != afterPower){
								accrue = accrue + Define.FILE_SEPARATOR;
								accrue = accrue + beforePower;
								accrue = accrue + Define.FILE_SEPARATOR;
								accrue = accrue + token;
								
								all_token = all_token + token;
								token = 1;
								time = Define.GRIP_SETTING_THRESHOLD;
							}
							else{
								if(time >= Define.GRIP_THRESHOLD){
									time = 0;
									token = token + 1;
								}
							}
						}
						beforePower = afterPower;
						
					}
				}
				fis.close();
				
				if(time > 0){
					if(token == 0){
						token = token + 1;
					}
					accrue = accrue + Define.FILE_SEPARATOR;
					accrue = accrue + beforePower;
					accrue = accrue + Define.FILE_SEPARATOR;
					accrue = accrue + token;
					
					all_token = all_token + token;
				}
				
				accrue = accrue + Define.FILE_SEPARATOR;
				accrue = accrue + all_token;
				
				Log.d("Jihye", "PatternFileSave : " + accrue);
				
				FileUtils.makeDirectory(Define.SETTING_SAVE_PATH);

				File saveFile = new File(mPatternSavePath);
				FileUtils.deleteFile(saveFile);

				return FileUtils.writeFile(saveFile, accrue.getBytes());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private void accrueSetting(int value[], int power, int hand) {
		String accrueSetting = "";
		for (int i = 0; i < value.length; i++) {
			accrueSetting = accrueSetting + value[i];
			if (i != value.length - 1) {
				accrueSetting = accrueSetting + ",";
			}
		}
		accrueSetting = accrueSetting + Define.FILE_SEPARATOR;
		accrueSetting = accrueSetting + power;
		accrueSetting = accrueSetting + Define.FILE_SEPARATOR;
		accrueSetting = accrueSetting + hand;
		accrueSetting = accrueSetting + "\n";
		mSaveSettingString = mSaveSettingString + accrueSetting;
		
//		Log.d("Jihye", "mPatternString : " + mPatternString);
	}
	
	private void fileParse(String path) {
		File file = new File(path);
		if (file != null && file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis, "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(line);
//					Log.d("Jihye", "fileParse " + sb.toString());
					String[] parse = sb.toString().split(Define.FILE_SEPARATOR);

					String valueString[] = parse[0].split(",");
					int value[] = new int[valueString.length];
					for (int i = 0; i < valueString.length; i++) {
						value[i] = Integer.parseInt(valueString[i]);
						// Log.d("Jihye", "value[i] " + value[i]);
					}

					int power = Integer.parseInt(parse[1]);
					int hand = Integer.parseInt(parse[2]);

					// Log.d("Jihye", "power : " + power + ", hand : " + hand);

					// for(int i=0; i < parse.length; i++){
					// Log.d("Jihye", "fileParse '" + Define.FILE_SEPARATOR +
					// "' " + parse[i]);
					// }
				}
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void drawBitmap(final ImageView iv, int value, int type) {

		int width = 0;
		int height = 0;
		int rect_width = 0;
		int rect_height = 0;
		int left = 0;
		int top = 0;
		int alpha = 50;
		int color = Color.RED;

		if (value == 0) {
			Drawable d = iv.getDrawable();

			iv.setImageBitmap(null);

			if (d != null) {
				if (d instanceof BitmapDrawable) {
					Bitmap b = ((BitmapDrawable) d).getBitmap();
					if (b != null) {
						b.recycle();
					}
				}
			}
			return;
		}

		switch (type) {
		case LEFT:
			width = (int) ((WIDTH / 255) * value);
			height = (int) HEIGHT;
			// rect_width = (int) ((WIDTH / 100) * value);
			// rect_height = (int) HEIGHT;
			left = 0;
			top = 0;
			color = Color.RED;
			break;
		case BOTTOM:
			width = (int) HEIGHT;
			height = (int) WIDTH;
			rect_width = (int) HEIGHT;
			rect_height = (int) ((WIDTH / 255) * value);
			left = 0;
			top = (int) (height - rect_height); //
			color = Color.BLUE;
			break;

		case RIGHT:
			width = (int) WIDTH;
			height = (int) HEIGHT;
			rect_width = (int) ((WIDTH / 255) * value);
			rect_height = (int) HEIGHT;
			left = (int) (width - rect_width); //
			top = 0;
			color = Color.RED;
			break;

		case TOP:
			width = (int) HEIGHT;
			height = (int) ((WIDTH / 255) * value);
			// rect_width = (int) ((WIDTH / 100) * value);
			// rect_height = (int) HEIGHT;
			left = 0;
			top = 0;
			color = Color.BLUE;
			break;

		default:
			break;
		}

		final int bm_width = width;
		final int bm_height = height;
		final int rect_left = left;
		final int rect_top = top;
		final int paint_color = color;
		final int paint_alpha = alpha;

		Bitmap output = Bitmap.createBitmap(bm_width, bm_height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(rect_left, rect_top, bm_width, bm_height);
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(paint_color);
		paint.setAlpha(paint_alpha);
		canvas.drawRect(rectF, paint);

		Drawable d = iv.getDrawable();

		iv.setImageBitmap(output);

		if (d != null) {
			if (d instanceof BitmapDrawable) {
				Bitmap b = ((BitmapDrawable) d).getBitmap();
				if (b != null) {
					b.recycle();
				}
			}
		}

		return;
	}
	
	OnSensorDataListner mSensorDataListener = new OnSensorDataListner() {
		
		@Override
		public void OnSensorDataListner(SensorDataEvent sensorData) {
			Log.d("Jihye", "mSensorDataListener result : " + sensorData.mResult + ", power : " + sensorData.mPower + ", hand : " + sensorData.mHand);
			String log = "mValue : ";
			for(int i=0; i < sensorData.mValue.length; i++){
				log = log + sensorData.mValue[i];
			}
			
			Log.d("Jihye", "mSensorDataListener " + log);
			
			if(sensorData.mResult == true){ // result 가 false 면 입력중에 손이 바뀌거나 뭐 이런거 .. 
				accrueSetting(sensorData.mValue, sensorData.mPower, sensorData.mHand);
				for(int i=0; i < sensorData.mValue.length; i++){
					
					int value = sensorData.mValue[i];
					int type = 0;
					
					if(value == 1 && sensorData.mPower == Define.POWER_FULL){	// && sensorData.mPower == Define.POWER_FULL
						if (i < 11) {
							type = RIGHT;
						} else if (i < 16) {
							type = TOP;
						} else if (i < 26) {
							type = LEFT;
						} else if (i < 30) {
							type = BOTTOM;
						}
						value = 0xFF;
						drawBitmap(ivList.get(i), value, type);
					}
					else{
						Drawable d = ivList.get(i).getDrawable();
						ivList.get(i).setImageBitmap(null);
	
						if (d != null) {
							if (d instanceof BitmapDrawable) {
								Bitmap b = ((BitmapDrawable) d).getBitmap();
								if (b != null) {
									b.recycle();
								}
							}
						}
					}
				}
			}
			else{
				Log.d("Jihye", "mSensorDataListener data fail");
				failDialogHandler.sendEmptyMessage(GRIP_FAIL);
			}
		}
	};

	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.grip_regist_ok) {
				failDialogHandler.removeMessages(GRIP_FAIL);
				failDialogHandler.removeMessages(GRIP_TIMEOUT);
				mFailFlag = false;
				
				SettingFileSave();
				PatternFileSave();
				
				mRegistOkDialog.show();
			}
		}
	};

	private Handler dialogHandler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what < 5) {
				mRegistStartDialog.setMessage(getBaseContext().getResources()
						.getString(R.string.setting_grip_register_message)
						+ (5 - msg.what));
				dialogHandler.sendEmptyMessageDelayed(msg.what + 1, 1000);
			} else {
				mRegistStartDialog.dismiss();

				mGripSensorEventManager.registerCALstner();
				mGripSensorEventManager.setOnSensorDataListner(mSensorDataListener);
				
				failDialogHandler.sendEmptyMessageDelayed(GRIP_TIMEOUT, Define.GRIP_REGIST_TIMEOUT);
			}

		}
	};
	
	private Handler failDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mFailFlag == true){
				if(msg.what == GRIP_TIMEOUT){
					mRegistFailDialog.setMessage(getString(R.string.setting_grip_regist_fail_timeout));
					mRegistFailDialog.show();
				}
				else if(msg.what == GRIP_FAIL){
					mRegistFailDialog.setMessage(getString(R.string.setting_grip_regist_fail_false));
					mRegistFailDialog.show();
				}
			}
		}
	};
	

}
