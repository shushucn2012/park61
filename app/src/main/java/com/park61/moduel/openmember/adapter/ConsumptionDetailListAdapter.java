package com.park61.moduel.openmember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.park61.R;
import com.park61.moduel.openmember.bean.ConsumptionDetail;
import java.util.ArrayList;

/**
 * 消费明细adapter
 */
public class ConsumptionDetailListAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<ConsumptionDetail> mList;

    public ConsumptionDetailListAdapter(Context _context,ArrayList<ConsumptionDetail> _list){
        this.mContext = _context;
        this.mList = _list;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.consumption_detail_list_item,null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ConsumptionDetail item = mList.get(position);
        holder.tv_title.setText(item.getCardName());
        holder.tv_date.setText(item.getTime());
        holder.tv_count.setText(item.getCardLengthName());
        if("1".equals(item.getConsumerType())){
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        }else{
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_title,tv_date,tv_count,tv_unit;
    }
}
