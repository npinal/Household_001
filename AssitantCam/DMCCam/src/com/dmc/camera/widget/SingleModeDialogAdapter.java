package com.dmc.camera.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dmc.camera.assist.R;

public final class SingleModeDialogAdapter extends BaseAdapter {
	
	Context mContext;
	int mLowLayout;
	String[] mMenuTitle;
	String[] mMenuValue;
	LayoutInflater mLinflater;
	
	public SingleModeDialogAdapter(Context context, int row_layout, String[] menuTitle, String[] menuValue){
		mContext = context;
		
		mLowLayout = row_layout;
		mMenuTitle = menuTitle;
		mLinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {
		if(mMenuTitle != null){
			return mMenuTitle.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mMenuTitle != null){
			return mMenuTitle[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ListViewHolder{
		TextView menuTitle;
		RadioButton radio;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mLinflater.inflate(mLowLayout, parent, false);
			ListViewHolder holder = new ListViewHolder();
			
			holder.menuTitle = (TextView) convertView.findViewById(R.id.single_list_title);
			holder.radio = (RadioButton) convertView.findViewById(R.id.Radiobutton);
			holder.radio.setSelected(false);
			
			convertView.setTag(holder);
			
		}
		
		final ListViewHolder holder = (ListViewHolder) convertView.getTag();
		
		holder.menuTitle.setText(mMenuTitle[position]);
		
//		holder.radio = (RadioButton) convertView.findViewById(R.id.Radiobutton);
		
		return convertView;
		
//		return null;
	}
	
	
}
