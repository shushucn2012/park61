package com.park61.net.request;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.park61.common.tool.FilesManager;
import com.park61.common.tool.ImageManager;
import com.park61.net.toolbox.ImageLoader.ImageCache;

/**
 * @Description:图片缓存
 * @author:Cai
 * @time:2014-7-1 上午11:17:33
 */
public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mCache;
	private Context mContext;

	public BitmapCache(Context context) {
		mContext = context;
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int maxSize = maxMemory / 8;
		mCache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = mCache.get(url);
		if (bitmap == null) {
			File file = new File(FilesManager.getDownLoadImageDir(mContext, url));
			if (file.exists()) {
				bitmap = ImageManager.getInstance().readFileBitMap(
						FilesManager.getDownLoadImageDir(mContext, url));
				if (bitmap != null) {
					putBitmap(url, bitmap);
				}
			}
		}
		return bitmap;

	}

	@Override
	public void putBitmap(String url, final Bitmap bitmap) {
		mCache.put(url, bitmap);
		final File file = new File(FilesManager.getDownLoadImageDir(mContext, url));

		if (!file.exists()) {
			new Thread(new Runnable() {
				public void run() {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					BufferedOutputStream bos = null;
					try {
						bos = new BufferedOutputStream(new FileOutputStream(file));

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					try {
						bos.flush();
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

}
