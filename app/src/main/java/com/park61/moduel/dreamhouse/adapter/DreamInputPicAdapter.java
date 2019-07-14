package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.park61.R;

import java.util.List;

/**
 * Created by shushucn2012 on 2017/1/19.
 */
public class DreamInputPicAdapter extends BaseAdapter {

    private List<Bitmap> inputPicList;
    private Context mContext;
    private OnDelClickListener mOnDelClickListener;

    public DreamInputPicAdapter(Context context, List<Bitmap> list) {
        mContext = context;
        inputPicList = list;
    }

    @Override
    public int getCount() {
        return inputPicList.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return inputPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dream_input_piclist_item, null);
        }
        ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
        ImageView img_del = (ImageView) convertView.findViewById(R.id.img_del);
        img_input.setImageBitmap(inputPicList.get(position));
        if (position == inputPicList.size() - 1) {
            img_del.setVisibility(View.INVISIBLE);
        } else {
            img_del.setVisibility(View.VISIBLE);
        }
        if (position == 4) {
            img_input.setVisibility(View.GONE);
        }
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDelClickListener.onDel(position);
            }
        });
        return convertView;
    }

    public void setOnDelClickListener(OnDelClickListener lsner) {
        this.mOnDelClickListener = lsner;
    }

    public interface OnDelClickListener {
        public void onDel(int pos);
    }
}
