package com.dmc.camera.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmc.camera.assist.R;

public final class CustomAlertDialog {
	
	Context mContext;
	AlertDialog mDialog= null;
	
	TextView mTitle;
	TextView mMessage;
	
	Button mPositive;
	Button mNegative;
	
	String mTitleText;
	String mMessageText;
	
	public CustomAlertDialog(Context context, String title, String message){
		mContext = context;
		mTitleText = title;
		mMessageText = message;
	}
	
	public CustomAlertDialog(Context context, String message){
		mContext = context;
		mMessageText = message;
	} 
	
	public void show(){
		LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.custom_alert_dialog, null);
		
		mDialog = new AlertDialog.Builder(mContext).setView(view).create();
		mDialog.show();
		mDialog.setContentView(R.layout.custom_alert_dialog);
		
		mTitle = (TextView) mDialog.findViewById(R.id.alert_dialog_title);
		mMessage = (TextView) mDialog.findViewById(R.id.alert_dialog_message);
		
		if(mTitleText != null && mTitleText.equals("") == false){
			mTitle.setText(mTitleText);
		}
		
		mMessage.setText(mMessageText);
		
		mPositive = (Button) mDialog.findViewById(R.id.alert_dialog_ok);
		mNegative = (Button) mDialog.findViewById(R.id.alert_dialog_cancel);
		
		mPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				if (mOnButtonClickListener != null) {
	            	mOnButtonClickListener.onPositiveButtonClick(v);
	            }
			}
		});
		
		mNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				if (mOnButtonClickListener != null) {
	            	mOnButtonClickListener.onNegativeButtonClick(v);
	            }
			}
		});
	}
	
	
	public interface OnButtonClickListener {
		public abstract void onPositiveButtonClick(View v);
		public abstract void onNegativeButtonClick(View v);
	}
	
	private OnButtonClickListener mOnButtonClickListener= null;
	
	public void setOnButtonClickListener(OnButtonClickListener listener) {
		mOnButtonClickListener= listener;
	}
	
}
