package com.park61.moduel.salesafter.adapter;

import java.util.ArrayList;
import com.park61.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * 图片适配器
 * 
 */
public class InputPictureAdapter extends BaseAdapter {
	private ArrayList<Bitmap> inputPicList;
	private LayoutInflater factory;
	
	public InputPictureAdapter(ArrayList<Bitmap> inputPicList, Context mContext) {
		super();
		this.inputPicList = inputPicList;
		factory = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return inputPicList.size();
	}

	@Override
	public Object getItem(int position) {
		return inputPicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = factory.inflate(R.layout.gridview_inputpic_item, null);
		}
		ImageView img_input = (ImageView) convertView
				.findViewById(R.id.img_input);
		img_input.setImageBitmap(inputPicList.get(position));
		if (position == 3) {
			img_input.setVisibility(View.GONE);
		}
		return convertView;
	}

}
