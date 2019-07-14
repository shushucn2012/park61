package com.park61.widget.imageview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.park61.common.set.GlobalParam;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * @类 说 明: 图片扫描工具类
 */
public class ScannerUtils {


	// 首先保存图片
	public static void saveImageToGallery(Context context, String url) {
		File appDir = new File(GlobalParam.IMAGE_FILE_PATH);
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
		if (bitmap != null) {
			if (!appDir.exists()) {
				// 目录不存在 则创建
				appDir.mkdirs();
			}
			String fileName = System.currentTimeMillis() + ".jpg";
			File fileNew = new File(appDir, fileName);
			try {
				FileOutputStream fos = new FileOutputStream(fileNew);
				bitmap.compress(CompressFormat.JPEG, 100, fos); // 保存bitmap至本地
				fos.flush();
				fos.close();

				// 其次把文件插入到系统图库
				try {
					MediaStore.Images.Media.insertImage(context.getContentResolver(), appDir.getAbsolutePath(),
							fileName, null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// 最后通知图库更新
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileNew)));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (!bitmap.isRecycled()) {
					bitmap.recycle(); // 当存储大图片时，为避免出现OOM ，及时回收Bitmap
					System.gc(); // 通知系统回收
				}
				Toast.makeText(context, "图片保存成功，存储路径:为" + fileNew.getAbsolutePath(), Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(context, "图片正在下载中", Toast.LENGTH_SHORT).show();
		}

	}

}
