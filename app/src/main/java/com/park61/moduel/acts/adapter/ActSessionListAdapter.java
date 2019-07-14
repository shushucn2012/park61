package com.park61.moduel.acts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.bean.ActItem;

public class ActSessionListAdapter extends BaseAdapter {

    private List<ActItem> mList;
    private Context mContext;
    private OnChooseListener mOnChooseListener;

    public ActSessionListAdapter(Context _context, List<ActItem> _list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.actsession_list_item, null);
            holder = new ViewHolder();
            holder.tv_act_date = (TextView) convertView.findViewById(R.id.tv_act_date);
            holder.btn_choose_session = (Button) convertView.findViewById(R.id.btn_choose_session);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ActItem ai = mList.get(position);
        String dateStr = DateTool.L2SByDay2(ai.getActStartTime()) + " - "
                + DateTool.L2SByDay2(ai.getActEndTime());
        holder.tv_act_date.setText(dateStr);
        String price = "";
        if (ai.getIsProm() == 0) {//非促销
            price = ai.getActPrice();
        } else if (ai.getIsProm() == 1) {//促销
            price = ai.getChildPromPrice();
        }
        if (ai.isApply()) {
            holder.btn_choose_session.setText(FU.zeroToMF(price) + "\n选择场次");
            holder.btn_choose_session.setBackgroundResource(R.drawable.rec_orange_stroke_trans_solid);
            holder.btn_choose_session.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        } else {
            holder.btn_choose_session.setText(FU.zeroToMF(price) + "\n报名已满");
            holder.btn_choose_session.setBackgroundResource(R.drawable.rec_lightgray_stroke_trans_solid);
            holder.btn_choose_session.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
        holder.btn_choose_session.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ai.isApply()) {
                    mOnChooseListener.onChoose(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_act_date;
        Button btn_choose_session;
    }

    public interface OnChooseListener {
        public void onChoose(int pos);
    }

    public void setOnChooseListener(OnChooseListener lsner) {
        this.mOnChooseListener = lsner;
    }

}
