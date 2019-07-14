package com.park61.moduel.acts.adapter;

import java.util.List;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.ShopAlbumItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ShopAlbumAdapter extends BaseAdapter {

	private List<ShopAlbumItem> mList;
	private Context mContext;
	private LayoutInflater factory;

	public ShopAlbumAdapter(Context _context, List<ShopAlbumItem> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ShopAlbumItem getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.item_shop_album, null);
			holder = new ViewHolder();
			holder.act_img = (ImageView) convertView.findViewById(R.id.ablum_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShopAlbumItem item = mList.get(position);
		ImageManager.getInstance().displayImg(holder.act_img, item.getThumbNail());
		return convertView;
	}

	class ViewHolder {
		ImageView act_img;
	}
}
