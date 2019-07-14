package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.acts.TravellerEditActivity;
import com.park61.moduel.acts.bean.TravellerBean;

import java.util.List;

public class TravellerListAdapter extends BaseAdapter {

    private List<TravellerBean> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnTravellerChosenLsner mOnTravellerChosenLsner;
    private boolean fromChosen;//是否从输入过来

    public TravellerListAdapter(Context _context, List<TravellerBean> _list, boolean fromChosen) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        this.fromChosen = fromChosen;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TravellerBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.travellerlist_item, null);
            holder = new ViewHolder();
            holder.img_edit = (ImageView) convertView.findViewById(R.id.img_edit);
            holder.tv_traveller_name = (TextView) convertView.findViewById(R.id.tv_traveller_name);
            holder.tv_paper_no = (TextView) convertView.findViewById(R.id.tv_paper_no);
            holder.tv_paper_type = (TextView) convertView.findViewById(R.id.tv_paper_type);
            holder.chk = (CheckBox) convertView.findViewById(R.id.chk);
            if(fromChosen){
                holder.img_edit.setVisibility(View.GONE);
                holder.chk.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TravellerBean tb = mList.get(position);
        holder.tv_traveller_name.setText(tb.getContactsName());
        if (tb.getCredentialsType() == 0) {
            holder.tv_paper_type.setText("身份证：");
        } else {
            holder.tv_paper_type.setText("护照：");
        }
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, TravellerEditActivity.class);
                it.putExtra("TRAVELLER_INFO", tb);
                mContext.startActivity(it);
            }
        });
        holder.tv_paper_no.setText(tb.getCredentialsNo());
        holder.chk.setChecked(tb.getChecked());
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tb.setChecked(b);
                mOnTravellerChosenLsner.onChosen();
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_edit;
        TextView tv_paper_type;
        TextView tv_traveller_name;
        TextView tv_paper_no;
        CheckBox chk;
    }

    public interface OnTravellerChosenLsner{
        public void onChosen();
    }

    public void setOnOnTravellerChosenLsner(OnTravellerChosenLsner lsner){
        mOnTravellerChosenLsner = lsner;
    }

}
