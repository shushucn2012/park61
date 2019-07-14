package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.dreamhouse.DreamDetailActivity;
import com.park61.moduel.shophome.ShopCommentActivity;
import com.park61.moduel.shophome.ShopSampleActivity;
import com.park61.moduel.shophome.ShowBigPhotoActivity;
import com.park61.moduel.shophome.bean.OfficeContentAlbums;
import com.park61.moduel.shophome.bean.OfficeContentItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017/3/7.
 */
public class SampleRecordListAdapter extends BaseAdapter {

    private final int TYPE_0PICS = 0;
    private final int TYPE_1PICS = 1;
    private final int TYPE_2PICS = 2;
    private final int TYPE_3PICS = 3;
    private final int TYPE_4PICS = 4;
    private final int TYPE_5PICS = 5;
    private final int TYPE_6PICS = 6;
    private final int TYPE_9PICS = 7;

    private Context mContext;
    private LayoutInflater factory;
    private List<OfficeContentAlbums> mList;
    private RecordOnCallBack mRecordOnCallBack;

    public SampleRecordListAdapter(Context _context, List<OfficeContentAlbums> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        mRecordOnCallBack = (ShopSampleActivity) _context;
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
    public int getItemViewType(int position) {
        int num = mList.get(position).getOfficeContentItemsList().size();
        if (num == 0) {
            return TYPE_0PICS;
        } else if (num == 1) {
            return TYPE_1PICS;
        } else if (num == 2) {
            return TYPE_2PICS;
        } else if (num == 3) {
            return TYPE_3PICS;
        } else if (num == 4) {
            return TYPE_4PICS;
        } else if (num == 5) {
            return TYPE_5PICS;
        } else if (num >= 6 && num < 9) {
            return TYPE_6PICS;
        } else if (num >= 9) {
            return TYPE_9PICS;
        }
        return TYPE_0PICS;
    }

    @Override
    public int getViewTypeCount() {
        return 8;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_0PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item0, null);
                    break;
                case TYPE_1PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item1, null);
                    break;
                case TYPE_2PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item2, null);
                    break;
                case TYPE_3PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item3, null);
                    break;
                case TYPE_4PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item4, null);
                    break;
                case TYPE_5PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item5, null);
                    break;
                case TYPE_6PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item6, null);
                    break;
                case TYPE_9PICS:
                    convertView = factory.inflate(R.layout.shop_sample_record_item9, null);
                    break;
            }
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_src_price = (TextView) convertView.findViewById(R.id.tv_src_price);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.act_area = convertView.findViewById(R.id.act_area);
            holder.act_img = (ImageView) convertView.findViewById(R.id.act_img);
            holder.img_share = (ImageView) convertView.findViewById(R.id.img_share);
            holder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
            holder.img_praise = (ImageView) convertView.findViewById(R.id.img_praise);
            holder.more_area = convertView.findViewById(R.id.more_area);
            initImageItems(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OfficeContentAlbums item = mList.get(position);
        holder.tv_content.setText(item.getDesc());
        holder.tv_date.setText(formatTimeAndDate(item.getCreateDate()));
        holder.tv_num.setText(item.getPraiseNum() + "个赞" + "   " + item.getCommentNum() + "条评论" + "   " + item.getViewNum() + "次浏览");
        holder.tv_src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (item.getActivityVO() != null && item.getRequirement() == null) {//游戏
            ImageManager.getInstance().displayImg(holder.act_img, item.getActivityVO().getActCover(), R.drawable.img_default_h);
            holder.tv_act_price.setTextColor(mContext.getResources().getColor(R.color.com_orange));
            holder.tv_act_price.setText(FU.formatPrice(item.getActivityVO().getActPrice()));
            holder.tv_act_title.setText(item.getActivityVO().getActTitle());
        } else if (item.getActivityVO() == null && item.getRequirement() != null) {//梦想
            ImageManager.getInstance().displayImg(holder.act_img, item.getRequirement().getCoverPic(), R.drawable.img_default_h);
            holder.tv_act_title.setText(item.getRequirement().getTitle());
            holder.tv_act_price.setTextColor(mContext.getResources().getColor(R.color.g999999));
            holder.tv_act_price.setText("走起");
        } else {
            holder.act_area.setVisibility(View.GONE);
        }
        fillPicToLinear(holder, item.getOfficeContentItemsList(), item.getId());
        if (holder.more_area != null) {
            if (type == TYPE_6PICS && item.getOfficeContentItemsList().size() == 6) {
                holder.more_area.setVisibility(View.GONE);
            }
            if (type == TYPE_9PICS && item.getOfficeContentItemsList().size() == 9) {
                holder.more_area.setVisibility(View.GONE);
            }
            holder.more_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < item.getOfficeContentItemsList().size(); j++) {
                        urlList.add(item.getOfficeContentItemsList().get(j).getResourceUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", item.getId());
                    bIt.putExtra("itemsId", item.getOfficeContentItemsList().get(0).getId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) item.getOfficeContentItemsList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", item.getOfficeContentItemsList().get(0).getResourceUrl());
                    if (urlList.size() >= 1) {// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    }
                    mContext.startActivity(bIt);
                }
            });
        }
        holder.act_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getActivityVO() != null && item.getRequirement() == null) {
                    Intent actIntent = new Intent(mContext, ActDetailsActivity.class);
                    actIntent.putExtra("id", item.getActivityVO().getId());
                    actIntent.putExtra("actTempId", item.getActTemplateId());
                    mContext.startActivity(actIntent);
                } else if (item.getActivityVO() == null && item.getRequirement() != null) {
                    Intent actIntent = new Intent(mContext, DreamDetailActivity.class);
                    actIntent.putExtra("requirementId", item.getRequirement().getId());
                    mContext.startActivity(actIntent);
                }
            }
        });
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordOnCallBack.onShareClicked(position);
            }
        });
        holder.img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(mContext, ShopCommentActivity.class);
                commentIntent.putExtra("albumId", item.getId());
                mContext.startActivity(commentIntent);
            }
        });
        holder.img_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> urlList = new ArrayList<String>();
                for (int j = 0; j < item.getOfficeContentItemsList().size(); j++) {
                    urlList.add(item.getOfficeContentItemsList().get(j).getResourceUrl());
                }
                Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                bIt.putExtra("albumId", item.getId());
                bIt.putExtra("itemsId", item.getOfficeContentItemsList().get(0).getId());
                bIt.putParcelableArrayListExtra("picList", (ArrayList) item.getOfficeContentItemsList());
                bIt.putExtra("position", 0);
                bIt.putExtra("picUrl", item.getOfficeContentItemsList().get(0).getResourceUrl());
                if (urlList.size() >= 1) {// url集合
                    bIt.putStringArrayListExtra("urlList", urlList);
                }
                mContext.startActivity(bIt);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        LinearLayout pic_area;
        TextView tv_content, tv_act_title, tv_act_price, tv_src_price, tv_num;
        ImageView act_img, img_share, img_comment, img_praise, img_sample_item0, img_sample_item1, img_sample_item2,
                img_sample_item3, img_sample_item4, img_sample_item5, img_sample_item6, img_sample_item7, img_sample_item8;
        View act_area, more_area;
    }

    private void initImageItems(ViewHolder holder, View convertView) {
        holder.img_sample_item0 = (ImageView) convertView.findViewById(R.id.img_sample_item0);
        holder.img_sample_item1 = (ImageView) convertView.findViewById(R.id.img_sample_item1);
        holder.img_sample_item2 = (ImageView) convertView.findViewById(R.id.img_sample_item2);
        holder.img_sample_item3 = (ImageView) convertView.findViewById(R.id.img_sample_item3);
        holder.img_sample_item4 = (ImageView) convertView.findViewById(R.id.img_sample_item4);
        holder.img_sample_item5 = (ImageView) convertView.findViewById(R.id.img_sample_item5);
        holder.img_sample_item6 = (ImageView) convertView.findViewById(R.id.img_sample_item6);
        holder.img_sample_item7 = (ImageView) convertView.findViewById(R.id.img_sample_item7);
        holder.img_sample_item8 = (ImageView) convertView.findViewById(R.id.img_sample_item8);
    }

    private void fillPicToLinear(SampleRecordListAdapter.ViewHolder holder, final ArrayList<OfficeContentItem> picList, final Long albumId) {
        ArrayList<ImageView> photoViewList = new ArrayList<ImageView>();
        photoViewList.add(holder.img_sample_item0);
        photoViewList.add(holder.img_sample_item1);
        photoViewList.add(holder.img_sample_item2);
        photoViewList.add(holder.img_sample_item3);
        photoViewList.add(holder.img_sample_item4);
        photoViewList.add(holder.img_sample_item5);
        photoViewList.add(holder.img_sample_item6);
        photoViewList.add(holder.img_sample_item7);
        photoViewList.add(holder.img_sample_item8);
        // url集合
        final ArrayList<String> urlList = new ArrayList<String>();
        for (int j = 0; j < picList.size(); j++) {
            urlList.add(picList.get(j).getResourceUrl());
        }
        int size;
        if (picList.size() > 0 && picList.size() < 6) {
            size = picList.size();
        } else if (picList.size() >= 6 && picList.size() < 9) {
            size = Math.min(picList.size(), 6);
        } else {
            size = Math.min(picList.size(), 9);
        }
        for (int i = 0; i < size; i++) {
            final String url = picList.get(i).getResourceUrl();
            final Long itemsId = picList.get(i).getId();
            final int position = i;
            photoViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent bIt = new Intent(mContext, ShowBigPhotoActivity.class);
                    bIt.putExtra("albumId", albumId);
                    bIt.putExtra("itemsId", itemsId);
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) picList);
                    bIt.putExtra("position", position);
                    bIt.putExtra("picUrl", url);
                    if (urlList.size() >= 1) {// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    }
                    mContext.startActivity(bIt);
                }
            });
        }
        for (int i = 0; i < size; i++) {
            ImageManager.getInstance().displayImg(photoViewList.get(i), picList.get(i).getResourceUrl());
        }
    }

    public interface RecordOnCallBack {
        //void onPraiseClicked(int position);
        void onShareClicked(int position);
    }

    public SpannableString formatTimeAndDate(String timestamp) {
        if (timestamp == null || timestamp.equals(""))
            return new SpannableString("");
        Long tsp = Long.parseLong(timestamp);
        int betweenDays = 0;
        try {
            betweenDays = DateTool.daysBetween(new Date(tsp), new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (betweenDays == 0) {
            return new SpannableString("今天");
        } else if (betweenDays == 1) {
            return new SpannableString("昨天");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateStr = sdf.format(new Date(tsp));
        SpannableString sp = new SpannableString(dateStr);
        sp.setSpan(new AbsoluteSizeSpan(28, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

}
