package com.park61.widget.wxpicselect.bean;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by Hankkin on 15/8/20.
 */
public class ImageBean {
	public String id;
	public String path;
	public String date;
	private Bitmap bitmap;

	public ImageBean() {
	}

	public ImageBean(String id) {
		this.id = id;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String thumbnailPath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Bitmap getBitmap() {
		if (bitmap == null) {
			try {
				bitmap = Bimp.resizeSmaller(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
