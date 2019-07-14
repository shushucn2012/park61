package com.park61.widget.wxpicselect.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * bitmap 操作类
 * 
 * @author super
 */
public class Bimp {

	/**
	 * 选择的图片的临时列表
	 */
	public static ArrayList<ImageBean> tempSelectBitmap = new ArrayList<ImageBean>();

	public static Bitmap revitionImageSize(String path) throws IOException {
		if (path == null) {
			return null;
		}
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

	public static Bitmap resizeSmaller(String path) throws IOException {
		if (path == null) {
			return null;
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 200)
					&& (options.outHeight >> i <= 200)) {
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

	/**
	 * 把bitmap压缩到文件中
	 * 
	 * @param tempFile
	 */
	public static void compressBmpToFile(File tempFile, String thePath)
			throws Exception {
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
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
}
