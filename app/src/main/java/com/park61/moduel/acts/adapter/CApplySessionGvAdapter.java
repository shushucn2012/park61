package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.ActItem;

import java.util.List;

public class CApplySessionGvAdapter extends BaseAdapter {

    private List<ActItem> mList;
    private Context mContext;
    private int selectedPos = 0;

    public void selectItem(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public CApplySessionGvAdapter(List<ActItem> _list, Context _context) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ActItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_applysession_item, null);
        }
        TextView tv_first_line = (TextView) convertView.findViewById(R.id.tv_first_line);
        TextView tv_second_line = (TextView) convertView.findViewById(R.id.tv_second_line);
        ImageView img_chosen = (ImageView) convertView.findViewById(R.id.img_chosen);
        ActItem ai = mList.get(position);
        String price = ViewInitTool.getActCurPrice(ai);
        tv_first_line.setText(FU.zeroToMF(price));
        int leftNum = ai.getActLowQuotaLimit().intValue() - ai.getActNumberVisitor();
        tv_second_line.setText("还剩"+leftNum+"名额");
        if (position == selectedPos) {
            convertView.setBackgroundResource(R.drawable.rec_orange_stroke_trans_solid);
            img_chosen.setVisibility(View.VISIBLE);
            tv_first_line.setTextColor(mContext.getResources().getColor(R.color.com_orange));
            tv_second_line.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        } else {
            convertView.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid);
            img_chosen.setVisibility(View.GONE);
            if (ai.isApply()) {
                tv_first_line.setTextColor(mContext.getResources().getColor(R.color.g333333));
                tv_second_line.setTextColor(mContext.getResources().getColor(R.color.g333333));
            } else {
                tv_first_line.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
                tv_second_line.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
            }
        }
        return convertView;
    }

}
