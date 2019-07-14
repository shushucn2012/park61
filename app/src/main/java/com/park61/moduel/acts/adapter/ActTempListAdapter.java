package com.park61.moduel.acts.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.ActItem;

public class ActTempListAdapter extends BaseAdapter {

    private List<ActItem> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ActTempListAdapter(Context _context, List<ActItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.acttemplist_item, null);
            holder = new ViewHolder();
            holder.act_img = (ImageView) convertView.findViewById(R.id.act_img);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_leiji = (TextView) convertView.findViewById(R.id.tv_leiji);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            holder.tv_act_price_old = (TextView) convertView.findViewById(R.id.tv_act_price_old);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ActItem item = mList.get(position);
        //标题
        holder.tv_act_title.setText(item.getActTitle());
        //价格
        ViewInitTool.fmActPrice(item, holder.tv_act_price, holder.tv_act_price_old);
        //举办方
        if (TextUtils.isEmpty(item.getActReleaseAuthor())) {
            holder.tv_author.setVisibility(View.GONE);
        } else {
            holder.tv_author.setVisibility(View.VISIBLE);
            holder.tv_author.setText(item.getActReleaseAuthor());
        }
        //累计
        holder.tv_leiji.setText("累计" + (item.getGrandTotal() == null ? 0 : item.getGrandTotal()) + "人报名");
        //封面图
        ImageManager.getInstance().displayImg(holder.act_img, item.getActCover());
        return convertView;
    }

    class ViewHolder {
        ImageView act_img;
        TextView tv_act_title;
        TextView tv_act_price;
        TextView tv_act_addr;
        TextView tv_author;
        TextView tv_leiji;
        TextView tv_act_price_old;
    }
}
