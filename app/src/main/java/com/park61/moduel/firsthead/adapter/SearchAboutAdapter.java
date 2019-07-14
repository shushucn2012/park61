package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.firsthead.bean.AutoWordAboutBean;

import java.util.List;

/**
 * Created by nieyu on 2017/11/14.
 */

public class SearchAboutAdapter extends BaseAdapter {

    private Context ctx;
    private  List<AutoWordAboutBean>  str;
    private LayoutInflater factory;
    private String key;
    public  SearchAboutAdapter(Context ctx,List<AutoWordAboutBean> str,String key){
        this.str=str;
        this.ctx=ctx;
        factory = LayoutInflater.from(ctx);
        this.key=key;
    }
    @Override
    public int getCount() {
        return str.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SearchAboutAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.adapter_searchabout, null);
            holder = new SearchAboutAdapter.ViewHolder();
            holder.tv_expert_name = (TextView) convertView.findViewById(R.id.tv_expert_name);
            convertView.setTag(holder);
        } else {
            holder = (SearchAboutAdapter.ViewHolder) convertView.getTag();
        }
//        holder.tv_expert_name.setText(str.get(i).getKeyword());
        setTextviewColorAndBold(holder.tv_expert_name,key,str.get(i).getKeyword(),R.color.com_red);
        return convertView;
    }

    class ViewHolder {
        TextView tv_expert_name;
    }



    public  void setTextviewColorAndBold(TextView textView, String key, String value, int color) {
        if (isEmpty(value)) {
            return;
        }
        if (!isEmpty(key)) {
            SpannableStringBuilder style = new SpannableStringBuilder(value);
            int index = value.indexOf(key);
            if (index >= 0) {
                while (index < value.length() && index >= 0) {

                    style.setSpan(new ForegroundColorSpan(color), index, index + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new StyleSpan(Typeface.BOLD), index, index + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(style);
                    index = value.indexOf(key, index + key.length());
                }
            } else {
                textView.setText(value);
            }

        } else {
            textView.setText(value);
        }
    }
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || "null".equals(str.toLowerCase().trim());
    }
}
