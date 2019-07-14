package com.park61.widget.picselect.utils;

import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class Utils {
	private static Utils utils;

	public static Utils getInstance() {
		if (utils == null) {
			utils = new Utils();
		}
		return utils;
	}

	/***
	 * 选择一张图片 图片类型，这里是image/*，当然也可以设置限制 如：image/jpeg等
	 * 
	 * @param activity
	 *            Activity
	 */
	@SuppressLint("InlinedApi")
	public void selectPicture(Activity activity) {
		if (Build.VERSION.SDK_INT < 19) {
			// Intent intent = new Intent();
			// intent.setType("image/*");
			// intent.setAction(Intent.ACTION_GET_CONTENT);
			Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
			albumIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			// 由于startActivityForResult()的第二个参数"requestCode"为常量，
			// 个人喜好把常量用一个类全部装起来，不知道各位大神对这种做法有异议没？
			activity.startActivityForResult(albumIntent,
					AppConstant.KITKAT_LESS);
		} else {
			// Intent intent = new Intent();
			// intent.setType("image/*");
			// 由于Intent.ACTION_OPEN_DOCUMENT的版本是4.4以上的内容
			// 所以注意这个方法的最上面添加了@SuppressLint("InlinedApi")
			// 如果客户使用的不是4.4以上的版本，因为前面有判断，所以根本不会走else，
			// 也就不会出现任何因为这句代码引发的错误
			// intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
			albumIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			activity.startActivityForResult(albumIntent,
					AppConstant.KITKAT_ABOVE);
		}
	}

	/***
	 * 裁剪图片
	 * 
	 * @param activity
	 *            Activity
	 * @param uri
	 *            图片的Uri
	 */
	public void cropPicture(Activity activity, Uri uri) {
		Intent innerIntent = new Intent("com.android.camera.action.CROP");
		innerIntent.setDataAndType(uri, "image/*");
		innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
		innerIntent.putExtra("aspectX", 1); // 放大缩小比例的X
		innerIntent.putExtra("aspectY", 1);// 放大缩小比例的X 这里的比例为： 1:1
		innerIntent.putExtra("outputX", 320); // 这个是限制输出图片大小
		innerIntent.putExtra("outputY", 320);
		innerIntent.putExtra("return-data", true);
		innerIntent.putExtra("scale", true);
		activity.startActivityForResult(innerIntent, AppConstant.INTENT_CROP);
	}

	public void cropPictureRec(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 15);
		intent.putExtra("aspectY", 8);
		intent.putExtra("outputX", 750);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		String IMAGE_FILE_LOCATION = "file://" + Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
		Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, AppConstant.INTENT_CROP);
	}

	public void cropImageUri(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, AppConstant.INTENT_CROP);
	}

	public void cropImageUriRec(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 15);
		intent.putExtra("aspectY", 8);
		intent.putExtra("outputX", 750);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, AppConstant.INTENT_CROP);
	}

	public Bitmap decodeUriAsBitmap(Context c, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(c.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 下面几个方法来自于stackoverflow,虽然来自大神，但大神的代码也不是就那样？ 看不懂的地方挨个百度。
	 * -----------------------割------------------------- Get a file path from a
	 * Uri. This will get the the path for Storage Access Framework Documents,
	 * as well as the _data field for the MediaStore and other file-based
	 * ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}
