package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.ShowClassNoticeActivity;
import com.park61.moduel.firsthead.ShowClassPhotoActivity;
import com.park61.moduel.firsthead.bean.TeachClassNotice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017/3/7.
 */
public class FirstClassListAdapter extends BaseAdapter {

    private final int TYPE_0PICS = 0;
    private final int TYPE_1PICS = 1;
    private final int TYPE_2PICS = 2;
    private final int TYPE_3PICS = 3;
    private final int TYPE_4PICS = 4;
    private final int TYPE_5PICS = 5;
    private final int TYPE_6PICS = 6;
    private final int TYPE_7PICS = 7;
    private final int TYPE_8PICS = 8;
    private final int TYPE_9PICS = 9;

    private Context mContext;
    private LayoutInflater factory;
    private List<TeachClassNotice> mList;

    public FirstClassListAdapter(Context _context, List<TeachClassNotice> _list) {
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
    public int getItemViewType(int position) {
        if (mList.get(position).getNoticeType() == 1) {
            return TYPE_0PICS;
        } else {
            if (CommonMethod.isListEmpty(mList.get(position).getImgList()))
                return TYPE_0PICS;
            int num = mList.get(position).getImgList().size();
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
            } else if (num == 6) {
                return TYPE_6PICS;
            } else if (num == 7) {
                return TYPE_7PICS;
            } else if (num == 8) {
                return TYPE_8PICS;
            } else if (num >= 9) {
                return TYPE_9PICS;
            }
        }
        return TYPE_0PICS;
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_0PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item0, null);
                    break;
                case TYPE_1PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item1, null);
                    break;
                case TYPE_2PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item2, null);
                    break;
                case TYPE_3PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item3, null);
                    break;
                case TYPE_4PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item4, null);
                    break;
                case TYPE_5PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item5, null);
                    break;
                case TYPE_6PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item6, null);
                    break;
                case TYPE_7PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item7, null);
                    break;
                case TYPE_8PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item8, null);
                    break;
                case TYPE_9PICS:
                    convertView = factory.inflate(R.layout.class_sample_record_item9, null);
                    break;
            }
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_view_num = (TextView) convertView.findViewById(R.id.tv_view_num);
            holder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
            holder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_praise_num);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.img_share = (ImageView) convertView.findViewById(R.id.img_share);
            holder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
            holder.img_praise = (ImageView) convertView.findViewById(R.id.img_praise);
            holder.more_area = convertView.findViewById(R.id.more_area);
            holder.img_teacher = (ImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
            initImageItems(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TeachClassNotice item = mList.get(position);
        /*if (type == TYPE_0PICS) {
            String titleWhole = "# 通知 # " + item.getTitle();
            SpannableString sp = new SpannableString(titleWhole);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff7ca1")), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_title.setText(sp);
        } else {
            holder.tv_title.setText(item.getTitle());
        }*/
        if (TextUtils.isEmpty(item.getTitle())) {
            holder.tv_title.setVisibility(View.GONE);
        } else {
            holder.tv_title.setText(item.getTitle());
            holder.tv_title.setVisibility(View.VISIBLE);
        }

        ImageManager.getInstance().displayImg(holder.img_teacher, item.getHeadPic());
        holder.tv_name.setText(item.getUserName());
        holder.tv_class.setText(item.getClassName());

        if (TextUtils.isEmpty(item.getContent())) {
            holder.tv_content.setVisibility(View.GONE);
        } else {
            holder.tv_content.setText(item.getContent());
            holder.tv_content.setVisibility(View.VISIBLE);
        }

        holder.tv_date.setText(formatTimeAndDate(item.getCreateDate()));
        holder.tv_view_num.setText(item.getViewNum() + "");
        holder.tv_comment_num.setText(item.getCommentNum() + "");
        holder.tv_praise_num.setText(item.getPraiseNum() + "");
        if (!CommonMethod.isListEmpty(item.getImgList())) {
            fillPicToLinear(holder, item.getImgList(), item.getNoticeId(), item.getClassId());
        } else {
            List<TeachClassNotice.ImgListBean> imgList = new ArrayList<>();
            fillPicToLinear(holder, imgList, item.getNoticeId(), item.getClassId());
        }
        if (holder.more_area != null) {
            if (type == TYPE_9PICS && item.getImgList().size() > 9) {
                holder.more_area.setVisibility(View.VISIBLE);
            }
            holder.more_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ArrayList<String> urlList = new ArrayList<String>();
                    for (int j = 0; j < item.getImgList().size(); j++) {
                        urlList.add(item.getImgList().get(j).getImgUrl());
                    }
                    Intent bIt = new Intent(mContext, ShowClassPhotoActivity.class);
                    bIt.putExtra("albumId", item.getNoticeId());
                    bIt.putExtra("itemId", item.getImgList().get(0).getId());
                    bIt.putExtra("classId", item.getClassId());
                    bIt.putParcelableArrayListExtra("picList", (ArrayList) item.getImgList());
                    bIt.putExtra("position", 0);
                    bIt.putExtra("picUrl", item.getImgList().get(0).getImgUrl());
                    if (urlList.size() >= 1) {// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    }
                    mContext.startActivity(bIt);
                }
            });
        }
        if (holder.img_praise != null) {
            if (item.isPraiseStatus()) {
                holder.img_praise.setImageResource(R.drawable.kg_praise_num_red);
            } else {
                holder.img_praise.setImageResource(R.drawable.kg_praise_num);
            }
        }
        /*View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bIt = new Intent(mContext, ShowClassNoticeActivity.class);
                bIt.putExtra("albumId", item.getNoticeId());
                bIt.putExtra("classId", item.getClassId());
                mContext.startActivity(bIt);
            }
        };
        holder.tv_content.setOnClickListener(onClickListener);
        if (holder.tv_title != null)
            holder.tv_title.setOnClickListener(onClickListener);*/
       /* holder.act_area.setOnClickListener(new View.OnClickListener() {
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
        });*/
        /*holder.img_share.setOnClickListener(new View.OnClickListener() {
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
        });*/
        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        LinearLayout pic_area;
        TextView tv_content, tv_view_num, tv_comment_num, tv_praise_num, tv_name, tv_class;
        ImageView img_teacher, act_img, img_share, img_comment, img_praise, img_sample_item0, img_sample_item1, img_sample_item2,
                img_sample_item3, img_sample_item4, img_sample_item5, img_sample_item6, img_sample_item7, img_sample_item8;
        View more_area;
        TextView tv_title;
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

    private void fillPicToLinear(FirstClassListAdapter.ViewHolder holder, final List<TeachClassNotice.ImgListBean> picList, final Long albumId, final long classId) {
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
            urlList.add(picList.get(j).getImgUrl());
        }
        int size = Math.min(picList.size(), 9);
        for (int i = 0; i < size; i++) {
            final String url = picList.get(i).getImgUrl();
            final Long itemsId = picList.get(i).getId();
            final int position = i;
            photoViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent bIt = new Intent(mContext, ShowClassPhotoActivity.class);
                    bIt.putExtra("albumId", albumId);
                    bIt.putExtra("itemId", itemsId);
                    bIt.putExtra("classId", classId);
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
            ImageManager.getInstance().displayImg(photoViewList.get(i), picList.get(i).getImgUrl());
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
        }
        /*else if (betweenDays == 1) {
            return new SpannableString("昨天");
        }*/
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateStr = sdf.format(new Date(tsp));
        SpannableString sp = new SpannableString(dateStr);
        //sp.setSpan(new AbsoluteSizeSpan(28, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

}
