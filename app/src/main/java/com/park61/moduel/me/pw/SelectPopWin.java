package com.park61.moduel.me.pw;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.me.ActsEvaluatePhotoAlbumActivity;
import com.park61.widget.wxpicselect.bean.ImageBucket;

public class SelectPopWin extends PopupWindow {

	private ListView lv_folder;
	private View menuview;

	private Context mContext;
	private List<ImageBucket> folders;
	private OnFolderSelectedLsner mOnFolderSelectedLsner;

	public SelectPopWin(Activity context, List<ImageBucket> list,
			final View cover) {
		super(context);
		mContext = context;
		folders = list;
		ImageBucket allBucket = new ImageBucket();
		allBucket.bucketName = "所有图片";
		allBucket.imageList = ActsEvaluatePhotoAlbumActivity.allImages;
		allBucket.count = ActsEvaluatePhotoAlbumActivity.allImages.size();
		folders.add(0, allBucket);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menuview = inflater.inflate(R.layout.pw_select_folder, null);
		lv_folder = (ListView) menuview.findViewById(R.id.lv_folder);

		// 设置SelectPicPopupWindow的View
		this.setContentView(menuview);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		// 修改高度显示，解决被手机底部虚拟键挡住的问题 by黄海杰 at:2015-4-30
		this.setHeight(dip2px(context, 400));
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// menuview添加ontouchlistener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		menuview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionEvent) {
				int height = menuview.findViewById(R.id.parent).getTop();
				int y = (int) motionEvent.getY();
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
						cover.setVisibility(View.GONE);
					}
				}
				return true;
			}
		});

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				cover.setVisibility(View.GONE);
			}
		});
		

		lv_folder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mOnFolderSelectedLsner.OnSelected(folders.get(position));
			}
		});
		lv_folder.setAdapter(new MyAdapter());
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return folders.size();
		}

		@Override
		public Object getItem(int position) {
			return folders.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.folderlist_item, null);
				holder = new ViewHolder();
				holder.img_folder_logo = (ImageView) convertView
						.findViewById(R.id.img_folder_logo);
				holder.tv_folder_name = (TextView) convertView
						.findViewById(R.id.tv_folder_name);
				holder.tv_folder_count = (TextView) convertView
						.findViewById(R.id.tv_folder_count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageBucket item = folders.get(position);
			holder.img_folder_logo.setImageBitmap(item.imageList.get(0)
					.getBitmap());
			holder.tv_folder_name.setText(item.bucketName);
			holder.tv_folder_count.setText("(" + item.count + "张)");
			return convertView;
		}

		class ViewHolder {
			ImageView img_folder_logo;
			TextView tv_folder_name;
			TextView tv_folder_count;
		}
	}

	/**
	 * 把dp转换为像素
	 */
	public static int dip2px(Context context, float px) {
		float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

	/**
	 * 得到设备的密度
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public interface OnFolderSelectedLsner {
		public void OnSelected(ImageBucket ib);
	}

	public void setOnFolderSelectedLsner(OnFolderSelectedLsner lsner) {
		this.mOnFolderSelectedLsner = lsner;
	}

}
