package com.park61.moduel.me;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.moduel.me.pw.SelectPopWin;
import com.park61.moduel.me.pw.SelectPopWin.OnFolderSelectedLsner;
import com.park61.widget.wxpicselect.adapter.AlbumGridViewAdapter;
import com.park61.widget.wxpicselect.bean.Bimp;
import com.park61.widget.wxpicselect.bean.ImageBean;
import com.park61.widget.wxpicselect.bean.ImageBucket;
import com.park61.widget.wxpicselect.pw.SelectFolderPopWin;
import com.park61.widget.wxpicselect.utils.AlbumHelper;

/**
 * 这个是进入相册显示所有图片的界面
 * 
 */
public class ActsEvaluatePhotoAlbumActivity extends BaseActivity implements
		OnFolderSelectedLsner {

	protected static final int TAKE_PHOTO_PICTURE = 10;

	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private Button okButton;

	private Button btn_choose_folder;// 选择文件夹按钮
	private ArrayList<ImageBean> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	private SelectPopWin pw;
	public static List<ImageBean> allImages;
	private View cover;// 阴影遮罩
	// 用来存照片的临时文件URI
	private Uri imageUri;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.plugin_camera_album);
	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			setResult(RESULT_OK, new Intent());
			finish();
		}
	}

	// 初始化，给一些对象赋值
	public void initView() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(GlobalParam.PhotosNeedRefresh);
		GlobalParam.PhotosNeedRefresh = false;
		dataList = new ArrayList<ImageBean>();

		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		Collections.sort(dataList, new Comparator<ImageBean>() {

			@Override
			public int compare(ImageBean lhs, ImageBean rhs) {
				Long leftSize = Long.parseLong(lhs.date);
				Long rightSize = Long.parseLong(rhs.date);
				return rightSize.compareTo(leftSize);
			}
		});

		allImages = new ArrayList<ImageBean>();
		allImages.addAll(dataList);

		// 添加一个照相图标
		dataList.add(0, new ImageBean());

		gridView = (GridView) findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(mContext, dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(R.id.ok_button);
		okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + 4
				+ ")");

		btn_choose_folder = (Button) findViewById(R.id.btn_choose_folder);
		cover = findViewById(R.id.cover);
		pw = new SelectPopWin(ActsEvaluatePhotoAlbumActivity.this, contentList, cover);
		pw.setOnFolderSelectedLsner(ActsEvaluatePhotoAlbumActivity.this);
	}

	public void initListener() {
		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						if (Bimp.tempSelectBitmap.size() >= 4) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(ActsEvaluatePhotoAlbumActivity.this,
										"超出可选图片张数", Toast.LENGTH_SHORT).show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.tempSelectBitmap.add(dataList.get(position));
							okButton.setText("完成" + "("
									+ Bimp.tempSelectBitmap.size() + "/" + 4
									+ ")");
						} else {
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText("完成" + "("
									+ Bimp.tempSelectBitmap.size() + "/" + 4
									+ ")");
						}
						isShowOkBt();
					}
				});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					if (Bimp.tempSelectBitmap.size() >= 4) {
						return;
					}
					// 临时文件用来存照片
					File temp = new File(Environment
							.getExternalStorageDirectory().getPath()
							+ "/GHPCacheFolder/photo");// 自已缓存文件夹
					if (!temp.exists()) {
						temp.mkdirs();
					}
					String filePath = temp.getAbsolutePath() + "/"
							+ Calendar.getInstance().getTimeInMillis() + ".jpg";
					File tempFile = new File(filePath);
					imageUri = Uri.fromFile(tempFile);
					// 使用MediaStore.ACTION_IMAGE_CAPTURE可以轻松调用Camera程序进行拍照：
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 不使用默认位置，使用uri存图片
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, TAKE_PHOTO_PICTURE);
				}
			}
		});
		okButton.setOnClickListener(new AlbumSendListener());
		btn_choose_folder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置弹窗位置
				pw.showAtLocation(
						ActsEvaluatePhotoAlbumActivity.this.findViewById(R.id.root),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
						SelectFolderPopWin.dip2px(mContext, 50));
				cover.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void initData() {
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，
		// 防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.img_default_v);
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	private boolean removeOneData(ImageBean ImageBean) {
		if (Bimp.tempSelectBitmap.contains(ImageBean)) {
			Bimp.tempSelectBitmap.remove(ImageBean);
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/"
					+ 4 + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/"
					+ 4 + ")");
			okButton.setPressed(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			// preview.setVisibility(View.VISIBLE);
		} else {
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/"
					+ 4 + ")");
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			// preview.setVisibility(View.GONE);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;

	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	public void OnSelected(ImageBucket ib) {
		dataList.clear();
		dataList.add(new ImageBean());
		dataList.addAll(ib.imageList);
		gridImageAdapter.notifyDataSetChanged();
		pw.dismiss();
		btn_choose_folder.setText(ib.bucketName + " >>");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(imageUri);
		GlobalParam.PhotosNeedRefresh = true;
		mContext.sendBroadcast(intent);// 这个广播的目的就是更新图库，发了这个广播进入相册就
		showDialog();
		new Handler().postDelayed((new Runnable() {

			@Override
			public void run() {
				List<ImageBucket> mFolderList = helper
						.getImagesBucketList(true);
				List<ImageBean> imgList = new ArrayList<ImageBean>();
				for (int i = 0; i < mFolderList.size(); i++) {
					imgList.addAll(mFolderList.get(i).imageList);
				}
				Collections.sort(imgList, new Comparator<ImageBean>() {

					@Override
					public int compare(ImageBean lhs, ImageBean rhs) {
						Long leftSize = Long.parseLong(lhs.date);
						Long rightSize = Long.parseLong(rhs.date);
						return rightSize.compareTo(leftSize);
					}
				});
				Bimp.tempSelectBitmap.add(imgList.get(0));
				setResult(RESULT_OK, new Intent());
				dismissDialog();
				finish();
			}
		}), 1000);
	}
}
