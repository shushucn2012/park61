package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.firsthead.bean.ColorSizeBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

public class ColorAndSizeAdapter extends TagAdapter<ColorSizeBean> {

	private Context mContext;

	public ColorAndSizeAdapter(List<ColorSizeBean> datas, Context context){
		super(datas);
		mContext = context;
	}

	@Override
	public View getView(FlowLayout parent, int position, ColorSizeBean colorSizeBean) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.gridview_child_item, null);

		TextView colorSize = (TextView) v.findViewById(R.id.tv_child_name);
		ColorSizeBean b = getItem(position);
		colorSize.setText(TextUtils.isEmpty(b.getProductColor()) ? b.getProductSize():b.getProductColor());

		if (b.isHasChecked()) {
			colorSize.setBackgroundResource(R.drawable.btn_toy_selector);
			colorSize.setTextColor(mContext.getResources().getColor(R.color.gffffff));
		} else {
			colorSize.setBackgroundResource(R.drawable.edit_toy_selector);
			colorSize.setTextColor(mContext.getResources().getColor(R.color.g666666));
		}

		if(!b.isHasShow()){
			//显示灰色不可点击
			colorSize.setBackgroundResource(R.drawable.toy_selector_false);
			colorSize.setTextColor(mContext.getResources().getColor(R.color.gffffff));
		}

		return v;
	}

}
