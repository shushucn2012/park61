package com.park61.moduel.openmember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.openmember.bean.MemberCartBean;
import java.util.ArrayList;

/**
 *
 */
public class CardTypeListAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<MemberCartBean> mList;

    public CardTypeListAdapter(Context _context, ArrayList<MemberCartBean> _list){
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
        return 6;
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
            convertView = layoutInflater.inflate(R.layout.cardtype_list_item,null);
            holder.img_card = (ImageView) convertView.findViewById(R.id.img_card);
            holder.tv_cardtype_name = (TextView) convertView.findViewById(R.id.tv_cardtype_name);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        MemberCartBean cardItem = mList.get(position);
        if(position==mList.size()-1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }
        ImageManager.getInstance().displayImg(holder.img_card,cardItem.getPic());
        holder.tv_cardtype_name.setText(cardItem.getName());
        holder.tv_description.setText(cardItem.getTimes());

        return convertView;
    }
    class ViewHolder{
        ImageView img_card;
        TextView tv_cardtype_name,tv_description;
        View line;
    }
}
