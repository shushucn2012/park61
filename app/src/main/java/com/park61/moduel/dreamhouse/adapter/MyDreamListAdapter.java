package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;

import java.util.ArrayList;

/**
 * 我的梦想列表adapter
 */
public class MyDreamListAdapter extends BaseAdapter {
    private ArrayList<DreamItemInfo> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnClickLsner mOnClickLsner;

    public MyDreamListAdapter(Context _context, ArrayList<DreamItemInfo> _list) {
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.mydream_list_item, null);
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_statue_name = (TextView) convertView.findViewById(R.id.tv_statue_name);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_people_num = (TextView) convertView.findViewById(R.id.tv_people_num);
            holder.img_area = convertView.findViewById(R.id.img_area);
            holder.btn_area = convertView.findViewById(R.id.btn_area);
            holder.line = convertView.findViewById(R.id.line);
            holder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            holder.btn2 = (Button) convertView.findViewById(R.id.btn2);

            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.img3);
            holder.img4 = (ImageView) convertView.findViewById(R.id.img4);
            holder.img5 = (ImageView) convertView.findViewById(R.id.img5);
            holder.img_quesheng = (ImageView) convertView.findViewById(R.id.img_quesheng);

            holder.img_realized = (ImageView) convertView.findViewById(R.id.img_realized);
            holder.fl_area = (FrameLayout) convertView.findViewById(R.id.fl_area);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        DreamItemInfo item = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_game, item.getCoverPic());
        ImageManager.getInstance().displayImg(holder.img_user, GlobalParam.currentUser.getPictureUrl(),
                R.drawable.headimg_default_img);
        holder.tv_user_name.setText(GlobalParam.currentUser.getPetName());
//        holder.tv_statue_name.setText(item.getStatusName());
        holder.tv_title.setText(item.getTitle());
        holder.tv_lable.setText(item.getRequirementLabel());
        if(item.getOfficeName()!=null){
            holder.tv_shop_name.setText(item.getOfficeName());
        }else{
            holder.tv_shop_name.setText("");
        }
        if(item.getExpectDate()!=null){
            holder.tv_date.setText(DateTool.L2SEndDay(item.getExpectDate()));
        }else{
            holder.tv_date.setText("");
        }
        if(item.getPredictionNum()!=0){
            holder.tv_people_num.setVisibility(View.VISIBLE);
            holder.tv_people_num.setText(item.getPredictionNum() + "同梦人");
        }else{
            holder.tv_people_num.setVisibility(View.GONE);
        }
        if (!CommonMethod.isListEmpty(item.getPicList()) && item.getPicList().size() > 0) {
            holder.img_area.setVisibility(View.VISIBLE);
            setImg(item, holder);
        } else {
            holder.img_area.setVisibility(View.GONE);
        }
        String status = item.getStatus();//梦想状态：0，未审核 1审核中 2审核未通过 10 造梦中（游戏未发布）
        //11梦想成真 12遗憾错过 13 造梦成功（游戏已发布）
        if(status.equals("0")){
            holder.tv_statue_name.setText("未审核");
        }else if(status.equals("1")){
            holder.tv_statue_name.setText("审核通过");
        }else if(status.equals("2")){
            holder.tv_statue_name.setText("审核未通过");
        }else if(status.equals("10")){
            holder.tv_statue_name.setText("造梦中");
        }else if(status.equals("11")){
            holder.tv_statue_name.setText("梦想成真");
        }else if(status.equals("12")){
            holder.tv_statue_name.setText("遗憾错过");
        }else if(status.equals("13")){
            holder.tv_statue_name.setText("造梦成功");
        }
        if (status.equals("0") || status.equals("1")|| status.equals("10")) {
            holder.img_realized.setVisibility(View.GONE);
            holder.btn1.setVisibility(View.VISIBLE);
            holder.btn2.setVisibility(View.GONE);
            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLsner.giveUp(position);
                }
            });
        } else if (status.equals("2")) {
            holder.img_realized.setVisibility(View.GONE);
            holder.btn1.setVisibility(View.VISIBLE);
            holder.btn2.setVisibility(View.GONE);
//            holder.btn2.setText("修改/编辑");
            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLsner.giveUp(position);
                }
            });
//            holder.btn2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mOnClickLsner.update(position);
//                }
//            });
        } else if (status.equals("13")) {
            holder.img_realized.setVisibility(View.GONE);
            holder.btn1.setVisibility(View.VISIBLE);
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn1.setText("查看详情");
            holder.btn2.setText("梦想买单");
            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLsner.detail(position);
                }
            });
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLsner.dreamPay(position);
                }
            });
        } else if (status.equals("12")) {
            holder.img_realized.setVisibility(View.GONE);
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn2.setText("重新加入");
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLsner.againJoin(position);
                }
            });
        } else if (status.equals("11")) {
            holder.img_realized.setVisibility(View.VISIBLE);
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.GONE);
        }
        holder.fl_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickLsner.detail(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_user, img1, img2, img3, img4, img5, img_quesheng;
        TextView tv_user_name, tv_statue_name, tv_title, tv_lable, tv_people_num,
                tv_shop_name,tv_date;
        ImageView img_game, img_realized;
        View line, img_area, btn_area;
        Button btn1, btn2;
        FrameLayout fl_area;
    }

    private void setImg(DreamItemInfo item, ViewHolder holder) {
        if (item.getPicList().size() == 0) {
            holder.img1.setVisibility(View.GONE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (item.getPicList().size() == 1) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (item.getPicList().size() == 2) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, item.getPicList().get(1));
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (item.getPicList().size() == 3) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, item.getPicList().get(1));
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, item.getPicList().get(2));
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (item.getPicList().size() == 4) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, item.getPicList().get(1));
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, item.getPicList().get(2));
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, item.getPicList().get(3));
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (item.getPicList().size() == 5) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, item.getPicList().get(1));
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, item.getPicList().get(2));
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, item.getPicList().get(3));
            holder.img5.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img5, item.getPicList().get(4));
            holder.img_quesheng.setVisibility(View.GONE);
        } else {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, item.getPicList().get(0));
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, item.getPicList().get(1));
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, item.getPicList().get(2));
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, item.getPicList().get(3));
            holder.img5.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img5, item.getPicList().get(4));
            holder.img_quesheng.setVisibility(View.VISIBLE);
        }
    }

    public interface OnClickLsner {
        public void giveUp(int position);

        //        public void update(int position);
        public void detail(int position);

        public void dreamPay(int position);

        public void againJoin(int position);
    }

    public void setOnClickLsner(OnClickLsner lsner) {
        mOnClickLsner = lsner;
    }
}
