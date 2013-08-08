package com.dmc.camera.gallery;

import com.dmc.camera.assist.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoViewActivity extends Activity {

	private final static String TAG = "PhotoViewActivity";
	
	public static final String PHOTO_ID = "photo_id";
	public static final String PHOTO_VIEW_MODE = "photo_view_mode";
	
	public static final int PHOTO_VIEW_MODE_VIEW = 0;
	public static final int PHOTO_VIEW_MODE_SHOT = 1;
	
	TextView mTitle;
	TextView mPhotoname;
	
	Button mSave;
	Button mShare;
	Button mDelete;
	
	LinearLayout mPhotoViewDetail;
	LinearLayout mPhotoList;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		
		mTitle = (TextView) findViewById(R.id.photo_view_title);
		
		mSave = (Button) findViewById(R.id.photo_view_save);
		mShare = (Button) findViewById(R.id.photo_view_share);
		mDelete = (Button) findViewById(R.id.photo_view_delete);
		
		mSave.setOnClickListener(mClick);
		mShare.setOnClickListener(mClick);
		mDelete.setOnClickListener(mClick);
		
		mPhotoname = (TextView) findViewById(R.id.photo_view_photoname);
		mPhotoViewDetail = (LinearLayout) findViewById(R.id.photo_view_detail);
		mPhotoList = (LinearLayout) findViewById(R.id.photo_view_photolist);
		mPhotoname.setOnClickListener(mClick);
		mPhotoViewDetail.setOnClickListener(mClick);
		mPhotoList.setOnClickListener(mClick);
		
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
