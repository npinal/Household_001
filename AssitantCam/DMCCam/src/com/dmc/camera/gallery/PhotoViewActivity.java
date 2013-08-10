package com.dmc.camera.gallery;

import java.io.IOException;

import com.dmc.camera.assist.R;
import com.dmc.camera.cache.ImageCache;
import com.dmc.camera.cache.ImageFetcher;
import com.dmc.camera.cache.ImageResizer;
import com.dmc.camera.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		
		ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getBaseContext(), "images");
//        cacheParams.setMemCacheSizePercent(getBaseContext(), 0.15f); 
		
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
		
		int view_mode = intent.getExtras().getInt(VIEW_MODE);
		
		if(view_mode == PHOTO_VIEW_MODE_SHOT){
			mPhotoList.setVisibility(View.GONE);
			String path = intent.getExtras().getString(PHOTO_PATH);
			mImageFetcher.loadImage(path, mPhotoView);
			
//			final BitmapFactory.Options options = new BitmapFactory.Options();
//	        options.inJustDecodeBounds = true;
//	        options.inSampleSize = 4;
//			Bitmap bm = BitmapFactory.decodeFile(path);
//			mPhotoView.setImageBitmap(bm);
			
			try {
				ExifInterface exif = new ExifInterface(path);
				int orentation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				switch (orentation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					Log.d("Jihye", "image orientation 90 ");
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					Log.d("Jihye", "image orientation 180 ");
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					Log.d("Jihye", "image orientation 270 ");
					break;

				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			mPhotoList.setVisibility(View.VISIBLE);
		}
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.photo_view_save){
				
			}
			else if(v.getId() == R.id.photo_view_share){
				
			}
			else if(v.getId() == R.id.photo_view_delete){
				
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
