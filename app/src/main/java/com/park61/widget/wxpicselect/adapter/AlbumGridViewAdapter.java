package com.park61.widget.wxpicselect.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.widget.wxpicselect.bean.ImageBean;
import com.park61.widget.wxpicselect.utils.BitmapCache;

/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 * @author king
 * @version 2014年10月18日 下午11:49:35
 * @QQ:595163260
 */
public class AlbumGridViewAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<ImageBean> dataList;
    private ArrayList<ImageBean> selectedDataList;
    private DisplayMetrics dm;
    private BitmapCache cache;
    private Bitmap cameraIconBmp;

    public AlbumGridViewAdapter(Context c, ArrayList<ImageBean> dataList, ArrayList<ImageBean> selectedDataList) {
        mContext = c;
        cache = new BitmapCache();
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        cameraIconBmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_im_camera_pressed0);
    }

    public int getCount() {
        return dataList.size();
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
        public Button choosetoggle;
        public TextView textView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_camera_select_imageview, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            viewHolder.choosetoggle = (Button) convertView.findViewById(R.id.choosedbt);
            viewHolder.choosetoggle.setClickable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ImageBean item = dataList.get(position);
        if (position == 0) {
            viewHolder.imageView.setImageBitmap(cameraIconBmp);
            viewHolder.toggleButton.setVisibility(View.GONE);
            viewHolder.choosetoggle.setVisibility(View.GONE);
        } else {
            viewHolder.toggleButton.setVisibility(View.VISIBLE);
            viewHolder.choosetoggle.setVisibility(View.VISIBLE);
            viewHolder.imageView.setTag(item.path);
            cache.displayBmp(viewHolder.imageView, item.thumbnailPath, item.path, callback);
            viewHolder.toggleButton.setTag(position);
            viewHolder.choosetoggle.setTag(position);
            viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.choosetoggle));
            if (selectedDataList.contains(dataList.get(position))) {
                viewHolder.toggleButton.setChecked(true);
                viewHolder.choosetoggle.setVisibility(View.VISIBLE);
            } else {
                viewHolder.toggleButton.setChecked(false);
                viewHolder.choosetoggle.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ToggleClickListener implements OnClickListener {
        Button chooseBt;

        public ToggleClickListener(Button choosebt) {
            this.chooseBt = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(), chooseBt);
                }
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(ToggleButton view, int position, boolean isChecked, Button chooseBt);
    }

}
