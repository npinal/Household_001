package com.dmc.camera.assist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.conn.ssl.AbstractVerifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dmc.camera.Global;
import com.dmc.camera.gallery.PhotoViewActivity;
import com.dmc.camera.lib.FaceDetector;
import com.dmc.camera.lib.FaceROI;
import com.dmc.camera.provider.DBApi;
import com.dmc.camera.provider.SettingDefine;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private final static String TAG = "Preview";
//	private final String savePath = "/sdcard/DCIM/Assistant_Camera";
	private String savePath;
	private int mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

	private CameraSettings mCameraSettings = null;
	private SurfaceHolder mHolder = null;
	private Camera mCamera = null;
	private Context mContext = null;
	
	private Global mGlobal;

	// --- -- After Edittest : 아래의 값들은 추후 DB에서 읽어서 setPreviewSize에 넣어야 한다. 현재는
	// DB에 읽는 부분이 업
	private int TEMP_GALAXY_S4_PREVIEW_WIDTH = 1920;
	private int TEMP_GALAXY_S4_PREVIEW_HEIGHT = 1080;
	private int TEMP_GALAXY_NOTE_2_PREVIEW_WIDTH = 3264;
	private int TEMP_GALAXY_NOTE_2_PREVIEW_HEIGHT = 2448;

	private int MAX_BEST_PHOTO_COUNT = 8;

	private FaceDetector mFaceDetector;
	private FocusManager mFocusManager;
	private ZoomControl mZoomControl;

	// --- Focus Resources
	static float oldFixScale = 1f;

	// --- Touch Event Resouces
	private static final int NONE = 0;
	private static final int TOUCH = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float oldDist = 1f;
	private float MAXSCLAE = 7f;
	private float MINSCAALE = 0f;

	// --- DB
	private String mShotMode;

	// --- 입시파일이름 (차후 삭제바람)
	private long mCurrentTime;
	private int mBestShotSavedCount = 0;

	public Preview(Context context) {
		super(context);

		init(context);
	}

	public Preview(Context context, CameraSettings cameraSettings) {
		super(context);

		mCameraSettings = cameraSettings;
		mShotMode = DBApi.TblSystem.getString(context.getContentResolver(),
				DBApi.TblSystem.SHOT_MODE);
		Log.e(TAG, "mShotMode = " + mShotMode);

		if (mShotMode.matches(SettingDefine.SHOT_MODE_SELF_SHOT)) {
			mCameraFacing = CameraInfo.CAMERA_FACING_FRONT;
		} else {
			mCameraFacing = CameraInfo.CAMERA_FACING_BACK;
		}

		init(context);
	}

	public Preview(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	public Preview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context);
	}

	private void init(Context context) {
		Log.e(TAG, "init!!");
		this.setWillNotDraw(false);
		
		mGlobal = (Global) context.getApplicationContext();
		savePath = mGlobal.getPhotoPath();
		
		int myDevice = Util.getMyDevice();

		if (myDevice == Util.DEVICE_GALAXY_S4)
			Util.setPreviewSize(TEMP_GALAXY_S4_PREVIEW_WIDTH,
					TEMP_GALAXY_S4_PREVIEW_HEIGHT);
		else if (myDevice == Util.DEVICE_GALAXY_NOTE2)
			Util.setPreviewSize(TEMP_GALAXY_NOTE_2_PREVIEW_WIDTH,
					TEMP_GALAXY_NOTE_2_PREVIEW_HEIGHT);
		else
			Util.setPreviewSize(TEMP_GALAXY_S4_PREVIEW_WIDTH,
					TEMP_GALAXY_S4_PREVIEW_HEIGHT);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// --- 투명하게
		mHolder.setFormat(PixelFormat.TRANSLUCENT);

		mContext = context;

		mFaceDetector = new FaceDetector(context);
		mFocusManager = new FocusManager();
		mZoomControl = new ZoomControl();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "surfaceCreated!!");

		mCamera = Camera.open(mCameraFacing);

		// --- Camera Object Setting
		Util.setCamera(mCamera);

		mCameraSettings.setCamera(mCamera, mHolder);
		mFocusManager.setCamera(mCamera);
		mZoomControl.setCamera(mCamera);

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.e(TAG, "surfaceChanged!! width = " + width + "height" + height);

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(width, height);
		mCamera.setParameters(parameters);
		mCamera.setPreviewCallback(new PreviewCallback() {

			public void onPreviewFrame(byte[] data, Camera camera) {

				int faceNum = 0;
				boolean faceDetectionLibraryState = false;
				int rotation = Util.getDisplayRotation(mContext);

				faceDetectionLibraryState = mFaceDetector.getLibraryState();

				if (faceDetectionLibraryState == false) {
					Log.d(TAG, "face detector library is not initialized");
					return;
				}

				faceNum = mFaceDetector.faceDetect(data, rotation);

				if (faceNum == -1) {
					Log.d(TAG, "face detector library is not initialize");
					return;
				} else if (faceNum == -2) {
					Log.d(TAG, "file to convert yuv420 image to gray image");
					return;
				} else if (faceNum == 0) {
					// Log.d(TAG, "fail to detect face");
					return;
				}

				Log.d(TAG, "preview. found faces num : " + faceNum);

				for (int i = 0; i < faceNum; i++) {
					FaceROI tmpFace = mFaceDetector.getFaceROI(i);

					int left = tmpFace.Left;
					int top = tmpFace.Top;
					int right = tmpFace.Right;
					int bottom = tmpFace.Bottom;

					Log.e(TAG, "" + i + "th Face : L[" + left + "] T[" + top
							+ "] R[" + right + "] B[" + bottom + "]");
				}
			}
		});

		mFaceDetector.init(width, height, mCameraFacing);

		mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "surfaceDestroyed!!");
		mCamera.stopPreview();
		mCamera.setPreviewCallback(null);
		mCamera.release();
		mCamera = null;

		mFaceDetector.release();
	}

	public void capture() {
		int orientation = 0;
		int shotCount = 1;

		mShotMode = DBApi.TblSystem.getString(mContext.getContentResolver(),
				DBApi.TblSystem.SHOT_MODE);
		Log.e(TAG, "mShotMode = " + mShotMode);

		if (mShotMode.matches(SettingDefine.SHOT_MODE_BEST_PHOTO)) {
			shotCount = MAX_BEST_PHOTO_COUNT;
		}

		if (mCamera != null)
			orientation = Util.getOrientationCompensation();

		Camera.Parameters parameters = mCamera.getParameters();
		if (mShotMode.matches(SettingDefine.SHOT_MODE_SELF_SHOT)) {
			switch (orientation) {
			case Util.ORIENTATION_COMPENSATAION_VERTICAL:
				parameters.setRotation(270);
				break;
			case Util.ORIENTATION_COMPENSATAION_HORIZONTAL:
				parameters.setRotation(180);
				break;
			case Util.ORIENTATION_COMPENSATAION_L_VERTICAL:
				parameters.setRotation(90);
				break;
			case Util.ORIENTATION_COMPENSATAION_L_HORIZONTAL:
				parameters.setRotation(0);
				break;
			default:
				break;
			}
		} else {
			switch (orientation) {
			case Util.ORIENTATION_COMPENSATAION_VERTICAL:
				parameters.setRotation(90);
				break;
			case Util.ORIENTATION_COMPENSATAION_HORIZONTAL:
				parameters.setRotation(180);
				break;
			case Util.ORIENTATION_COMPENSATAION_L_VERTICAL:
				parameters.setRotation(270);
				break;
			case Util.ORIENTATION_COMPENSATAION_L_HORIZONTAL:
				parameters.setRotation(0);
				break;
			default:
				break;
			}
		}
		mCamera.setParameters(parameters);

		int totalDelay = 0;
		for (; shotCount > 0; shotCount--) {
			//mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);

			if (mShotMode.matches(SettingDefine.SHOT_MODE_BEST_PHOTO)) {
				shutterHandler.sendEmptyMessageDelayed(0, totalDelay);
				totalDelay = totalDelay + Util.BEST_PHOTO_SHOT_DELAY;
			} else {
				shutterHandler.sendEmptyMessage(0);
			}
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
		}
	};

	@SuppressLint("DefaultLocale")
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(final byte[] data, Camera camera) {
			Log.e(TAG, "onPictureTaken");
			
			mCamera.stopPreview();
			setPreviewRestart();

			/*
			 * //--- 세로사진에 대한 데이터 회전 처리 int orientation =
			 * Util.getOrientationCompensation();
			 * 
			 * if (orientation == Util.ORIENTATION_COMPENSATAION_VERTICAL){
			 * Bitmap srcBitmap = Util.byteArrayToBitmap(data); Bitmap
			 * destBitmap = Util.bitmapRotate(srcBitmap, 90); destData =
			 * Util.bitmapToByteArray(destBitmap); } else
			 */;

			new Thread(new Runnable() {
				@Override
				public void run() {
					FileOutputStream outStream = null;
					try {
						File file = new File(savePath);
						if (!file.isDirectory()) {
							file.mkdirs();
						}

						// Write to SD Card
						mCurrentTime = System.currentTimeMillis();
						
//						String fileName = String.format(savePath + "/%d.jpg", mCurrentTime);
						String fileName = Long.toString(mCurrentTime);
						String filePath = String.format(savePath + "%s.jpg", mCurrentTime);
						
						outStream = new FileOutputStream(filePath);
						outStream.write(data);
						outStream.close();
						Log.d(TAG, "onPictureTaken - wrote bytes: "
								+ data.length);
						if (mShotMode.matches(SettingDefine.SHOT_MODE_SOUND_SHOT)) {
							ACVoicePlayer.GetInstance().StopRec();
							startRecoderHandler.sendEmptyMessage(0);
						} else if(mShotMode.matches(SettingDefine.SHOT_MODE_SELF_SHOT) || mShotMode.matches(SettingDefine.SHOT_MODE_NORMAL)){
							startShotView(SettingDefine.SHOT_MODE_NORMAL, fileName);
						} else if (mShotMode
								.matches(SettingDefine.SHOT_MODE_BEST_PHOTO)) {
							mBestShotSavedCount++;
							Log.e(TAG, "mBestShotSavedCount = " + mBestShotSavedCount);
							if (mBestShotSavedCount >= MAX_BEST_PHOTO_COUNT) {
								mBestShotSavedCount = 0;
								// mShotViewHandler.handleMessage.......
							}
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
					}
					Log.d(TAG, "onPictureTaken - jpeg");
				}
			}).start();
			// setPreviewRestart();
		}
	};

	private void setPreviewRestart() {
		mCamera.stopPreview();
		mCamera.startPreview();
	}

	public void cameraSwitch(int cameraInfo) {
		int width = Util.getPreviewSizeWidth();
		int height = Util.getPreviewSizeHeigth();

		if (cameraInfo == mCameraFacing) {
			Log.e(TAG, "CameraInfo is same direction!");
			return;
		}

		mCameraFacing = cameraInfo;
		surfaceDestroyed(mHolder);
		surfaceCreated(mHolder);
		surfaceChanged(mHolder, PixelFormat.TRANSLUCENT, width, height);
	}

	private Handler startRecoderHandler = new Handler() {
		public void handleMessage(Message msg) {
			String fileName = String.format(savePath + "/%d.mp4", mCurrentTime);
			ACVoicePlayer.GetInstance().Record(fileName);
			stopRecoderHandler.sendEmptyMessageDelayed(0, 5000);
		}
	};

	private Handler stopRecoderHandler = new Handler() {
		public void handleMessage(Message msg) {
			String fileName = String.format(savePath + "/%d.mp4", mCurrentTime);
			ACVoicePlayer.GetInstance().StopRec();

			// mShotViewHandler.handleMessage.......
		}
	};
	
	private Handler shutterHandler = new Handler(){
		public void handleMessage(Message msg) {
			mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
	};
	
	Handler mShotViewHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(mContext, PhotoViewActivity.class);
			intent.putExtras(msg.getData());
			mContext.startActivity(intent);
		};
	};
	
	public void startShotView(String shot_mode, String photo_name){
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString(PhotoViewActivity.PHOTO_PATH, photo_name);
		b.putInt(PhotoViewActivity.VIEW_MODE,
				PhotoViewActivity.PHOTO_VIEW_MODE_SHOT);
		msg.setData(b);
		mShotViewHandler.handleMessage(msg);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We purposely disregard child measurements because act as a
		// wrapper to a SurfaceView that centers the camera preview instead
		// of stretching it.
		/*
		 * final int width = resolveSize(getSuggestedMinimumWidth(),
		 * widthMeasureSpec); final int height =
		 * resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		 * 
		 * setMeasuredDimension(width, height); Log.e(TAG, "width = " + width +
		 * "height = " + height);
		 */
		int width = Util.getPreviewSizeWidth();
		int height = Util.getPreviewSizeHeigth();

		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "ACTION_DOWN");
			mode = TOUCH;
			break;

		case MotionEvent.ACTION_UP:
			Log.e(TAG, "ACTION_UP");
			if (mode == TOUCH) {
				float x = event.getX();
				float y = event.getY();

				mFocusManager.touchFocus((int) x, (int) y);
			}

			mZoomControl.initOldFixScale();
			mode = NONE;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			Log.e(TAG, "ACTION_POINTER_DOWN");
			oldDist = Util.spacing(event);
			if (oldDist > 10f) {
				mode = ZOOM;
			}
			break;

		case MotionEvent.ACTION_POINTER_UP:
			Log.e(TAG, "ACTION_POINTER_UP");
			mode = NONE;

			break;

		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "ACTION_MOVE");
			if (mode == ZOOM) {
				float newDist = Util.spacing(event);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					if (scale >= MAXSCLAE)
						scale = MAXSCLAE;
					else if (scale <= MINSCAALE)
						scale = MINSCAALE;

					Log.e(TAG, "scale = " + scale);
					mZoomControl.setZoomControl(scale, MAXSCLAE);
				}
			}
			break;

		default:
			break;
		}

		return true;
	}

	/*
	 * public void onDraw(Canvas canvas){ super.onDraw(canvas); Paint paint =
	 * new Paint(Paint.ANTI_ALIAS_FLAG);; paint.setColor(Color.GREEN);
	 * paint.setStrokeWidth(3); canvas.drawLine(0, 110, 200, 110,paint); }
	 */
}
