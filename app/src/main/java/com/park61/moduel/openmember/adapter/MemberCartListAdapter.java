package com.park61.moduel.openmember.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.park61.R;
import com.park61.moduel.pay.bean.MemberCardLengthVO;
import java.util.List;

/**
 * 会员卡列表adapter
 */
public class MemberCartListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
//    private ArrayList<MemberCartBean> mList;
    private List<MemberCardLengthVO> mList;// 卡长度类型列表
    private OpenMemberListener mOpenMemberListener;
    public MemberCartListAdapter(Context _context,List<MemberCardLengthVO> _list){
        this.mContext = _context;
        this.mList = _list;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MemberCardLengthVO getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.member_cart_list_item,null);
            holder = new ViewHolder();
            holder.btn_open = (Button) convertView.findViewById(R.id.btn_open);
            holder.tv_current_price = (TextView) convertView.findViewById(R.id.tv_current_price);
            holder.tv_src_price = (TextView) convertView.findViewById(R.id.tv_src_price);
            holder.tv_cart_title = (TextView) convertView.findViewById(R.id.tv_cart_title);
            holder.tv_sales_text = (TextView) convertView.findViewById(R.id.tv_sales_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        MemberCardLengthVO item = mList.get(position);
        holder.tv_current_price.setText(item.getActualPrice()+"");
        holder.tv_src_price.setText(item.getCardLengthPrice()+"");
        holder.tv_src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_cart_title.setText(item.getCardLengthShowName());
        if(TextUtils.isEmpty(item.getSalesText())){
            holder.tv_sales_text.setVisibility(View.GONE);
        }else{
            holder.tv_sales_text.setVisibility(View.VISIBLE);
            holder.tv_sales_text.setText(item.getSalesText());
        }
        holder.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenMemberListener.openMember(position);
            }
        });
        return convertView;
    }
    class ViewHolder{
        Button btn_open;
        TextView tv_current_price,tv_src_price,tv_cart_title,tv_sales_text;
    }
    public interface OpenMemberListener{
        public void openMember(int pos);
    }
    public void setListener(OpenMemberListener mOpenMemberListener){
        this.mOpenMemberListener = mOpenMemberListener;
    }
}
