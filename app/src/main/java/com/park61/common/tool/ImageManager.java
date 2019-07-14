package com.park61.common.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.widget.circleimageview.GlideCircleTransform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片管理工具类
 *
 * @author super
 */
public class ImageManager {
    /**
     * 资源图片缓存软引用
     */
    private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    private static ImageManager imageManage;

    private ImageManager() {

    }

    public static ImageManager getInstance() {
        if (imageManage == null) {
            imageManage = new ImageManager();
        }
        return imageManage;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId
     * @return BitMap
     */
    public Bitmap readResBitMap(int resId, Context mContext) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softBitmap = null;
        softBitmap = imageCache.get(resId + "");
        if (softBitmap != null) {
            bitmap = softBitmap.get();
            if (bitmap == null) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                InputStream is = mContext.getResources().openRawResource(resId);
                bitmap = BitmapFactory.decodeStream(is, null, opt);
                softBitmap = new SoftReference<Bitmap>(bitmap);
                if (bitmap != null) {
                    imageCache.put(resId + "", softBitmap);

                }
            }
            return bitmap;
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = mContext.getResources().openRawResource(resId);
        bitmap = BitmapFactory.decodeStream(is, null, opt);
        softBitmap = new SoftReference<Bitmap>(bitmap);
        if (bitmap != null) {
            imageCache.put(resId + "", softBitmap);

        }
        return bitmap;
    }

    /**
     * 以最省内存的方式读取文件资源的图片
     *
     * @param imageFile
     * @return
     */
    public Bitmap readFileBitMap(String imageFile) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softBitmap = null;
        softBitmap = imageCache.get(imageFile);
        if (softBitmap != null) {// 缓存存在
            bitmap = softBitmap.get();
            if (bitmap == null) {
                try {
                    // BitmapFactory.Options opts = new BitmapFactory.Options();
                    // opts.inJustDecodeBounds = true;
                    // BitmapFactory.decodeFile(imageFile, opts);
                    // opts.inSampleSize = computeSampleSize(opts, -1, 128 *
                    // 128);
                    // opts.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(imageFile);
                    softBitmap = new SoftReference<Bitmap>(bitmap);
                    if (bitmap != null) {
                        imageCache.put(imageFile, softBitmap);
                    }
                } catch (OutOfMemoryError e) {
                    Log.e("OutOfMemoryError----->", "OutOfMemoryError");
                }
            }
        } else {// 缓存不存在
            // BitmapFactory.Options opts = new BitmapFactory.Options();
            // opts.inJustDecodeBounds = true;
            // BitmapFactory.decodeFile(imageFile, opts);
            // opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
            // opts.inJustDecodeBounds = false;
            try {
                bitmap = BitmapFactory.decodeFile(imageFile);
                softBitmap = new SoftReference<Bitmap>(bitmap);
                if (bitmap != null) {
                    imageCache.put(imageFile, softBitmap);
                }
            } catch (OutOfMemoryError e) {
                Log.e("OutOfMemoryError----->", "OutOfMemoryError");
            }
        }
        return bitmap;
    }

    /**
     * 读取图片（普通读取方式）
     */
    public Bitmap readFileBitMapNomal(String imageFile) {
        Bitmap bitmap = null;
        // BitmapFactory.Options opts = new BitmapFactory.Options();
        // opts.inJustDecodeBounds = true;
        // BitmapFactory.decodeFile(imageFile, opts);
        // opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        // opts.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(imageFile);
        } catch (OutOfMemoryError e) {
            Log.e("OutOfMemoryError----->", "OutOfMemoryError");
        }
        return bitmap;
    }

    /**
     * 回收图片内存
     */
    public void releaseImage(String path) {
        if (imageCache.containsKey(path)) {
            SoftReference<Bitmap> reference = imageCache.get(path);
            Bitmap bitmap = reference.get();
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    Log.e("bitmap recycle", "now bitmap recycle " + path);
                    bitmap.recycle();
                    bitmap = null;
                    System.gc();
                }
                imageCache.remove(path);
            }
        }
    }

    /**
     * 获取最优缩放比
     */
    public int computeSampleSize(BitmapFactory.Options options,
                                 int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 按宽高压缩图片
     *
     * @param image
     * @return
     */
    public Bitmap reduceBitmap(Bitmap image) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 400f;//
        float ww = 300f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bmp = BitmapFactory.decodeStream(isBm, null, newOpts);
        try {
            baos.close();
            isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;// 压缩好比例大小后再进行质量压缩
    }

    public Bitmap reduceBitmap(String  path, float pixelW, float pixelH) {
        Bitmap image = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * 按宽高压缩图片
     *
     * @param image
     * @return
     */
    public Bitmap reduceBitmapForWx(Bitmap image) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 200f;//
        float ww = 150f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        } else if (w == h) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bmp = BitmapFactory.decodeStream(isBm, null, newOpts);
        try {
            baos.close();
            isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 设置缩略图
     *
     * @param bitMap
     * @param needRecycle
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 80;
        int newHeight = 80;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        if (width > height) {//宽大于高 按宽压缩
            scaleHeight = scaleWidth;
        } else if (width < height) {//宽小于高 按高压缩，否则同时压缩
            scaleWidth = scaleHeight;
        }

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        if (needRecycle) bitMap.recycle();
        return newBitMap;
    }

    public void displayCircleImg(ImageView img, String picUrl){
        Glide.with(img.getContext()).load(picUrl).placeholder(R.drawable.headimg_default_img).
                transform(new GlideCircleTransform(img.getContext())).dontAnimate().into(img);
    }

    /**
     * 调用UIL显示图片
     *
     * @param imageView 图片控件
     * @param picUrl    图片url
     */
    public void displayImg(ImageView imageView, String picUrl) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.img_default_v)
//                .showImageOnLoading(R.drawable.img_default_v)
//                .showImageOnFail(R.drawable.img_default_v)
//                .cacheInMemory(true).cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .delayBeforeLoading(100)
//                .build();
//        ImageAware imageAware = new ImageViewAware(imageView, false);
//        ImageLoader.getInstance().displayImage(picUrl, imageAware, options);

        Glide.with(imageView.getContext()).load(picUrl).diskCacheStrategy(DiskCacheStrategy.ALL).
                placeholder(R.drawable.img_default_v).error(R.drawable.img_default_v).dontAnimate().into(imageView);
    }

    /**
     * 调用UIL显示图片
     *
     * @param imageView 图片控件
     * @param picUrl    图片url
     */
    public void displayImg(ImageView imageView, String picUrl, int defaultResId) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(defaultResId)
//                .showImageOnLoading(defaultResId)
//                .showImageOnFail(defaultResId)
//                .cacheInMemory(true).cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .delayBeforeLoading(100)
//                .build();
//        ImageAware imageAware = new ImageViewAware(imageView, false);
//        ImageLoader.getInstance().displayImage(picUrl, imageAware, options);

        Glide.with(imageView.getContext()).load(picUrl).diskCacheStrategy(DiskCacheStrategy.ALL).
                placeholder(defaultResId).error(defaultResId).dontAnimate().into(imageView);
    }

    /**
     * 按像素图片压缩，然后覆盖
     *
     * @param strImgPath2
     */
    // public void myImageCompress(String strImgPath2) {
    // Bitmap bmp = myReduceImage(strImgPath2);
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // int options = 100;//
    // bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
    // while (baos.toByteArray().length / 1024 > 300 && options > 10) {
    // baos.reset();
    // options -= 10;
    // bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
    // }
    // if(bmp != null && !bmp.isRecycled()){
    // bmp.recycle();
    // bmp = null;
    // }
    // try {
    // FileOutputStream fos = new FileOutputStream(strImgPath2);
    // fos.write(baos.toByteArray());
    // fos.flush();
    // fos.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 回收ImageView占用的图像内存;
     *
     * @param view
     */
    public static void recycleImageView(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }

    /**
     * 回收View背景占用的图像内存;
     *
     * @param view
     */
    public static void recycleViewBg(View view) {
        if (view == null) return;
        Drawable drawable = view.getBackground();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            if (bmp != null && !bmp.isRecycled()) {
                view.setBackgroundResource(0);
                drawable.setCallback(null);
                bmp.recycle();
                bmp = null;
            }
        }
    }

    /**
     * 按宽/高缩放图片到指定大小并进行裁剪得到中间部分图片 <br>
     * 方 法 名：zoomBitmap <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-7 下午12:02:52 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param bitmap 源bitmap
     * @param w      缩放后指定的宽度
     * @param h      缩放后指定的高度
     * @return 缩放后的中间部分图片 Bitmap
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.i("TAG", "zoomBitmap---" + "width:" + width + "---" + "height:" + height);
        float scaleWidht, scaleHeight, x, y;
        Bitmap newbmp;
        Matrix matrix = new Matrix();
        if (width > height) {
            scaleWidht = ((float) h / height);
            scaleHeight = ((float) h / height);
            x = Math.abs(width - w * height / h) / 2;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        } else if (width < height) {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = height - width / 2;// 获取bitmap源文件中y做表需要偏移的像数大小
        } else {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = width / 2;
        }
        matrix.postScale(scaleWidht, scaleHeight);
        try {
            // createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
            Log.out("x=======" + x);
            Log.out("y=======" + y);
            Log.out("(int) (width - x)=======" + (int) (width - x));
            Log.out("(int) (height - y)=======" + (int) (height - y));
            newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x), (int) Math.abs(y), (int) (width - x), (int) (height - y), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

}
