package com.dmc.camera.gallery;

import java.io.File;

import com.dmc.camera.Global;
import com.dmc.camera.assist.R;
import com.dmc.camera.cache.ImageCache;
import com.dmc.camera.cache.ImageFetcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoViewActivity extends Activity {

	private final static String TAG = "PhotoViewActivity";
	
	public static final String VIEW_MODE = "view_mode";
	
	public static final int PHOTO_VIEW_MODE_VIEW = 0;
	public static final int PHOTO_VIEW_MODE_SHOT = 1;
	
	public static final String PHOTO_PATH = "photo_path";
	public static final String PHOTO_ID = "photo_id";
	
	TextView mTitle;
	TextView mPhotoname;
	
	Button mSave;
	Button mShare;
	Button mDelete;
	
	LinearLayout mPhotoViewDetail;
	TextView mPhotoList;
	
	ImageFetcher mImageFetcher;
	ImageView mPhotoView;
	
	int mViewMode;
	String mPath;
	
	Global mGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		
		mGlobal = (Global) getBaseContext().getApplicationContext();
		
		ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getBaseContext(), "images");
//        cacheParams.setMemCacheSizePercent(getBaseContext(), 0.15f); 
		cacheParams.setMemCacheSizePercent(0.15f);
		
        int height = 1024;
        int width = 768;
		
		int longest = (height > width ? height : width) / 2;
		
		mImageFetcher = new ImageFetcher(getBaseContext(), longest);
		mImageFetcher.addImageCache(null, cacheParams);
        mImageFetcher.setImageFadeIn(false);
		
		mTitle = (TextView) findViewById(R.id.photo_view_title);
		
		mSave = (Button) findViewById(R.id.photo_view_save);
		mShare = (Button) findViewById(R.id.photo_view_share);
		mDelete = (Button) findViewById(R.id.photo_view_delete);
		
		mSave.setOnClickListener(mClick);
		mShare.setOnClickListener(mClick);
		mDelete.setOnClickListener(mClick);
		
		mPhotoView = (ImageView) findViewById(R.id.photo_view_display);
		
		mPhotoname = (TextView) findViewById(R.id.photo_view_photoname);
		mPhotoViewDetail = (LinearLayout) findViewById(R.id.photo_view_detail);
		mPhotoList = (TextView) findViewById(R.id.photo_view_photolist);
		mPhotoname.setOnClickListener(mClick);
		mPhotoViewDetail.setOnClickListener(mClick);
		mPhotoList.setOnClickListener(mClick);
		
		Intent intent = getIntent();
		
		mViewMode = intent.getExtras().getInt(VIEW_MODE);
		
		if(mViewMode == PHOTO_VIEW_MODE_SHOT){
			mPhotoList.setVisibility(View.GONE);
			mSave.setVisibility(View.VISIBLE);
			
			mPath = createPhotoPath(intent.getExtras().getString(PHOTO_PATH));
			mImageFetcher.loadImage(mPath, mPhotoView);
			mPhotoname.setText(mPath);
		}
		else{
			mPhotoList.setVisibility(View.VISIBLE);
			mSave.setVisibility(View.GONE);
		}
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		//		super.onBackPressed();
	}
	
	public void shareImage(String filepath) { //공유 이미지 함수
	    File file = new File(filepath); //image 파일의 경로를 설정합니다.
	    Uri mSaveImageUri = Uri.fromFile(file); //file의 경로를 uri로 변경합니다. 
	    Intent intent = new Intent(Intent.ACTION_SEND); //전송 메소드를 호출합니다. Intent.ACTION_SEND
	    intent.setType("image/jpg"); //jpg 이미지를 공유 하기 위해 Type을 정의합니다.
	    intent.putExtra(Intent.EXTRA_STREAM, mSaveImageUri); //사진의 Uri를 가지고 옵니다.
	    startActivity(Intent.createChooser(intent, getString(R.string.gallery_photoview_share))); //Activity를 이용하여 호출 합니다.
	}
	
	private String createPhotoPath(String filename){
		return mGlobal.getPhotoPath() + filename + ".jpg";
	}
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.photo_view_save){
				finish();
			}
			else if(v.getId() == R.id.photo_view_share){
				if(mViewMode == PHOTO_VIEW_MODE_SHOT){
					shareImage(mPath);
				}
				else{
					
				}
			}
			else if(v.getId() == R.id.photo_view_delete){
				finish();
			}
			else if(v.getId() == R.id.photo_view_photoname){
				
			}
			else if(v.getId() == R.id.photo_view_detail){
				
			}
			else if(v.getId() == R.id.photo_view_photolist){
				
			}
		}
	};


}
