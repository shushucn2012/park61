package com.park61.moduel.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.moduel.me.bean.ActEvaluateDimension;
import com.park61.widget.ActsRatingBar;

import java.util.List;

/**
 * 评论维度adapter
 */
public class EvaluateDimensionListAdapter extends BaseAdapter {
    private List<ActEvaluateDimension> mList;
    private Context mContext;
    private LayoutInflater factory;
    public EvaluateDimensionListAdapter(Context _context, List<ActEvaluateDimension> _list){
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
        if (convertView == null) {
            convertView = factory.inflate(R.layout.evaluate_dimension_item, null);
            holder = new ViewHolder();
            holder.tv_dimension_name = (TextView) convertView.findViewById(R.id.tv_dimension_name);
            holder.ratingbar_score = (ActsRatingBar) convertView.findViewById(R.id.ratingbar_score);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ActEvaluateDimension item = mList.get(position);
        holder.tv_dimension_name.setText(item.getName());
        final ActsRatingBar score = holder.ratingbar_score;
        holder.ratingbar_score.setOnItemClickedListener(new ActsRatingBar.OnItemClickedListener() {
            @Override
            public void OnClicked() {
                item.setPoint(score.getPoint()*20);
                Log.out("===============point="+item.getPoint());
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView tv_dimension_name;
        ActsRatingBar ratingbar_score;
    }
}
