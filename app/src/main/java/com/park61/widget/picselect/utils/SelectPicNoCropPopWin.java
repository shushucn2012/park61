package com.park61.widget.picselect.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.park61.BaseActivity;
import com.park61.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectPicNoCropPopWin extends BaseActivity implements
		OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	// 临时文件用来存照片
	private static final String IMAGE_FILE_LOCATION = "file://"
			+ Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";

	private static final int TAKE_PHOTO_PICTURE = 10;

	// 用来存照片的临时文件URI
	Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			// 使用MediaStore.ACTION_IMAGE_CAPTURE可以轻松调用Camera程序进行拍照：
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 不使用默认位置，使用uri存图片
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TAKE_PHOTO_PICTURE);
			break;
		case R.id.btn_pick_photo:
			Utils.getInstance().selectPicture(this);
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.select_pic_dialog);
	}

	@Override
	public void initView() {
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initListener() {
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (null == data && requestCode != TAKE_PHOTO_PICTURE) {
			return;
		}
		Uri uri = null;
		String thePath = null;
		if (requestCode == AppConstant.KITKAT_LESS) {
			uri = data.getData();
			thePath = Utils.getInstance().getPath(this, uri);
		} else if (requestCode == AppConstant.KITKAT_ABOVE) {
			uri = data.getData();
			thePath = Utils.getInstance().getPath(this, uri);
		} else if (requestCode == TAKE_PHOTO_PICTURE) {
			thePath = Utils.getInstance().getPath(this, imageUri);
		}
		File temp = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/GHPCacheFolder/Format");// 自已缓存文件夹
		if (!temp.exists()) {
			temp.mkdirs();
		}
		String filePath = temp.getAbsolutePath() + "/"
				+ Calendar.getInstance().getTimeInMillis() + ".jpg";
		File tempFile = new File(filePath);
		// 图像保存到文件中
		compressBmpToFile(tempFile, thePath);
		Intent picData = new Intent();
		picData.putExtra("path", filePath);
		setResult(RESULT_OK, picData);
		finish();
	}

	/**
	 * 把bitmap压缩到文件中
	 * 
	 * @param tempFile
	 */
	public void compressBmpToFile(File tempFile, String thePath) {
		FileOutputStream foutput = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			foutput = new FileOutputStream(tempFile);
			bmp = revitionImageSize(thePath);
			baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if (baos.toByteArray().length / 1024 > 200) {// 判断如果图片大于200k
				baos.reset();// 重置baos即清空baos
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, foutput);// 这里压缩50%，把压缩后的数据存放到foutput中
			} else {
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			showShortToast("图片压缩失败！请重试！");
			finish();
		} finally {
			try {
				baos.close();
				foutput.close();
				bmp.recycle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 图片压缩
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
