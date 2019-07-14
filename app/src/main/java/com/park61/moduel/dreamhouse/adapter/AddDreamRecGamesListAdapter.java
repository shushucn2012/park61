package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.AddDreamRecommendGames;

import java.util.ArrayList;

/**
 * 推荐游戏列表adapter
 */
public class AddDreamRecGamesListAdapter extends BaseAdapter {
    private ArrayList<AddDreamRecommendGames> mList;
    private Context mContext;
    private LayoutInflater factory;

    public AddDreamRecGamesListAdapter(Context _context, ArrayList<AddDreamRecommendGames> _list) {
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
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.dream_recommend_game_list_item, null);
            holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddDreamRecommendGames item = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_game,item.getActCover());
        holder.tv_title.setText(item.getGrandTotal()+"");
        holder.tv_price.setText(FU.formatPrice(item.getActPrice()));
        holder.tv_num.setText("已售"+item.getGrandTotal());
        return convertView;
    }

    class ViewHolder {
        ImageView img_game;
        TextView tv_title, tv_lable, tv_price, tv_src_price, tv_num;
    }
}
