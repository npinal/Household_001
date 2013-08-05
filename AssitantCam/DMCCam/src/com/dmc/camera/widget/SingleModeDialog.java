package com.dmc.camera.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dmc.camera.assist.R;

public final class SingleModeDialog {
	
	Context mContext;
	AlertDialog mDialog= null;
	
	TextView mTitle;
	ListView mListView;
	
	String mTitleText;
	
	SingleModeDialogAdapter mAdapter;
	
	int mSelectPosition;
	
	public SingleModeDialog(Context context, String title, String[] menuArray, String[] menuValue, int select_postion){
		
		mContext = context;
		mTitleText = title;
		mSelectPosition = select_postion;
		
		mAdapter = new SingleModeDialogAdapter(mContext, R.layout.single_mode_dialog_list_row, menuArray, menuValue);
	}
	
	public void show(){
		LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.single_mode_dialog, null);
		
		mDialog = new AlertDialog.Builder(mContext).setView(view).create();
		mDialog.show();
		mDialog.setContentView(R.layout.single_mode_dialog);
		
		
		mTitle = (TextView) mDialog.findViewById(R.id.single_mode_dialog_title);
		mListView = (ListView) mDialog.findViewById(R.id.single_mode_dialog_list);
		
		mTitle.setText(mTitleText);
		mListView.setAdapter(mAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setItemChecked(mSelectPosition, true);
		mListView.setOnItemClickListener(mItemClick);
		
	}
	
	OnItemClickListener mItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if(mDialog != null){
				mSelectPosition = position;
				mDialog.dismiss();
				if (mOnDismissListener != null) {
					mOnDismissListener.onDismissed(position);
				}
			}
		}
	};
	
	private OnDismissListener mOnDismissListener= null;
	
	public interface OnDismissListener{
		public abstract void onDismissed(int position);
	}

	public void setOnDismissListener(OnDismissListener listener) {
		mOnDismissListener= listener;
	}
	
	
}
