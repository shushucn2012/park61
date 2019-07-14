package com.park61.moduel.growing.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.park61.CanBackWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.child.bean.GrowCommentVO;
import com.park61.moduel.child.bean.ShowItem;
import com.park61.moduel.child.bean.ShowPhotosDetailsVO;
import com.park61.moduel.childtest.TestResultWebViewActivity;
import com.park61.moduel.growing.GrowingActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ShowListAdapter extends BaseAdapter {

    private List<ShowItem> mList;
    private Context mContext;
    private LayoutInflater factory;
    private GmShowItemCallBack mGmShowItemCallBack;

    private final int TYPE_HAS_DATE_TITLE = 0;
    private final int TYPE_NO_DATE_TITLE = 1;

    public ShowListAdapter(Context _context, List<ShowItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        mGmShowItemCallBack = (GrowingActivity) _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ShowItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 0) {
            ShowItem siBefore = mList.get(position - 1);
            ShowItem si = mList.get(position);
            String theDateStr = DateTool.L2SEndDay(si.getOrderTime());
            String lastDateStr = DateTool.L2SEndDay(siBefore.getOrderTime());
            if (theDateStr.equals(lastDateStr)) {
                return TYPE_NO_DATE_TITLE;
            } else {
                return TYPE_HAS_DATE_TITLE;
            }
        }
        return TYPE_HAS_DATE_TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = factory.inflate(R.layout.grow_show_list_item, null);
            holder = new ViewHolder();
            holder.img_user_head = (ImageView) convertView.findViewById(R.id.img_user_head);
            holder.photos_area = convertView.findViewById(R.id.photos_area);
            holder.build_area = convertView.findViewById(R.id.build_area);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.area_age_title = convertView.findViewById(R.id.area_age_title);
            holder.tv_photo_age = (TextView) convertView.findViewById(R.id.tv_photo_age);
            holder.tv_photo_content = (TextView) convertView.findViewById(R.id.tv_photo_content);
            holder.tv_photo_commit_useratime = (TextView) convertView.findViewById(R.id.tv_photo_commit_useratime);
            holder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
            holder.linear_to_grid = (LinearLayout) convertView.findViewById(R.id.linear_to_grid);
            holder.row1 = (LinearLayout) convertView.findViewById(R.id.row1);
            holder.row2 = (LinearLayout) convertView.findViewById(R.id.row2);
            holder.row3 = (LinearLayout) convertView.findViewById(R.id.row3);
            initPhotoViews(holder, convertView);
            holder.tv_height_data = (TextView) convertView.findViewById(R.id.tv_height_data);
            holder.tv_weight_data = (TextView) convertView.findViewById(R.id.tv_weight_data);
            holder.tv_state_data = (TextView) convertView.findViewById(R.id.tv_state_data);
            holder.img_grow_huifu = (ImageView) convertView.findViewById(R.id.img_grow_huifu);
            holder.img_grow_pinlun = (ImageView) convertView.findViewById(R.id.img_grow_pinlun);
            holder.img_grow_delete = (ImageView) convertView.findViewById(R.id.img_grow_delete);
            holder.lv_reply = (LinearLayout) convertView.findViewById(R.id.lv_reply);
            holder.line = convertView.findViewById(R.id.line);
            holder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);

            holder.comment_area = convertView.findViewById(R.id.comment_area);
            holder.tv_appraisal_stage = (TextView) convertView.findViewById(R.id.tv_appraisal_stage);
            holder.tv_appraisal_type = (TextView) convertView.findViewById(R.id.tv_appraisal_type);
            holder.tv_example_name = (TextView) convertView.findViewById(R.id.tv_example_name);
            holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);

            holder.vote_area = convertView.findViewById(R.id.vote_area);
            holder.img_vote_left = (ImageView) convertView.findViewById(R.id.img_vote_left);
            holder.tv_vote_title = (TextView) convertView.findViewById(R.id.tv_vote_title);
            holder.tv_vote_details = (TextView) convertView.findViewById(R.id.tv_vote_details);
            holder.tv_vote_intro = (TextView) convertView.findViewById(R.id.tv_vote_intro);
            holder.img_grow_vote = (ImageView) convertView.findViewById(R.id.img_grow_vote);

            switch (type) {
                case TYPE_HAS_DATE_TITLE:
                    holder.area_age_title.setVisibility(View.VISIBLE);
                    convertView.setTag(holder);
                    break;
                case TYPE_NO_DATE_TITLE:
                    holder.area_age_title.setVisibility(View.GONE);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShowItem si = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_user_head, si.getUserHead());
        holder.tv_user_name.setText(si.getUserName());
        if (si.getListGrowComment().size() > 0) {
            holder.tv_mark.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.tv_mark.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }
        switch (type) {
            case TYPE_HAS_DATE_TITLE:
                holder.tv_photo_age.setText(si.getAgeYear() + "岁" + si.getAgeMonth() + "月" + si.getAgeDay() + "天");
                break;
        }

        holder.tv_photo_commit_useratime.setText(si.getRelationName() + "，" + toPDateStr(si.getCurrDate(), si.getCreateTime()));
        if (si.getType() == 0) {
            if (si.getResultId() == null && si.getVoteApplyId() == null) {// type =0,resultId为空，voteApplyId萌照数据
                holder.photos_area.setVisibility(View.VISIBLE);
                holder.build_area.setVisibility(View.GONE);
                holder.comment_area.setVisibility(View.GONE);
                holder.vote_area.setVisibility(View.GONE);
                holder.img_grow_vote.setVisibility(View.GONE);
                if (TextUtils.isEmpty(si.getComment())) {
                    holder.tv_photo_content.setVisibility(View.GONE);
                } else {
                    holder.tv_photo_content.setVisibility(View.VISIBLE);
                    holder.tv_photo_content.setText(si.getComment());
                }
                if (si.getListDetails() != null && si.getListDetails().size() > 0) {
                    if (si.getListDetails().size() == 1) {
                        holder.img_photo.setVisibility(View.VISIBLE);
                        holder.linear_to_grid.setVisibility(View.GONE);
                        final String url = si.getListDetails().get(0).getMediaUrl();
                        ImageManager.getInstance().displayImg(holder.img_photo, url);
                        holder.img_photo.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                                bIt.putExtra("picUrl", url);
                                mContext.startActivity(bIt);
                            }
                        });
                    } else {
                        holder.img_photo.setVisibility(View.GONE);
                        holder.linear_to_grid.setVisibility(View.VISIBLE);
                        fillPicToLinear(holder, si.getListDetails());
                    }
                } else {
                    holder.img_photo.setVisibility(View.GONE);
                    holder.linear_to_grid.setVisibility(View.GONE);
                }
            } else if (si.getResultId() != null) {// type =0,resultId不为空，测评数据
                holder.comment_area.setVisibility(View.VISIBLE);
                holder.photos_area.setVisibility(View.GONE);
                holder.build_area.setVisibility(View.GONE);
                holder.vote_area.setVisibility(View.GONE);
                holder.img_grow_vote.setVisibility(View.GONE);
//				holder.tv_appraisal_stage.setText(si.getComment());
                holder.tv_appraisal_stage.setText(si.getEaSysName() + "--" + si.getEaItemName());
                if (si.getRepresent() != null) {
                    holder.tv_appraisal_type.setText(si.getRepresent().getProperty());
                    holder.tv_example_name.setText("榜样人物：" + si.getRepresent().getName());
                    holder.tv_comment_content.setText(si.getRepresent().getContent());
                } else {
                    holder.tv_appraisal_type.setText("");
                    holder.tv_example_name.setText("");
                    holder.tv_comment_content.setText("");
                }
            } else if (si.getVoteApplyId() != null) {// type =0,voteApplyId不为空，投票数据
                holder.vote_area.setVisibility(View.VISIBLE);
                holder.comment_area.setVisibility(View.GONE);
                holder.photos_area.setVisibility(View.GONE);
                holder.build_area.setVisibility(View.GONE);
                holder.img_grow_vote.setVisibility(View.VISIBLE);
                ImageManager.getInstance().displayImg(holder.img_vote_left, si.getVoteApply().getImgUrl());
                holder.tv_vote_title.setText(si.getVoteApply().getTitle());
                String voteDetails = "我是" + si.getVoteApply().getVoteCode() + "号选手" + si.getVoteApply().getChildName() + "，快快给我投一票吧，谢谢啦~";
                SpannableString sp = new SpannableString(voteDetails);
                sp.setSpan(new ForegroundColorSpan(Color.RED), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_vote_details.setText(sp);
                holder.tv_vote_intro.setText(si.getVoteApply().getResume());
            }
        } else {// 体格数据
            holder.build_area.setVisibility(View.VISIBLE);
            holder.photos_area.setVisibility(View.GONE);
            holder.comment_area.setVisibility(View.GONE);
            holder.vote_area.setVisibility(View.GONE);
            holder.img_grow_vote.setVisibility(View.GONE);
            holder.tv_height_data.setText(si.getHeight() == null ? "--" : FU.strDbFmt(si.getHeight()) + "厘米");
            holder.tv_weight_data.setText(si.getWeight() == null ? "--" : FU.strDbFmt(si.getWeight()) + "公斤");
            holder.tv_state_data.setText(si.getState());
        }
        holder.comment_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Long resultId = si.getResultId();
                Intent it = new Intent(mContext, TestResultWebViewActivity.class);
                it.putExtra("PAGE_TITLE", "测评结果");
                //String url = AppUrl.demoHost_head + "/ea/toGrowEaResult.do" + "?eaKey=31c7f6b070ebfa101660a4bd4df75063&resultId=" + resultId;
                String url = AppUrl.demoHost_head + "/html-sui/dapTest/test-result.html?resultid=" + resultId;
                it.putExtra("url", url);
                String title = si.getEaSysName() + "-" + si.getEaItemName();
                it.putExtra("title", title);
                if (si.getRepresent() != null) {
                    String picUrl = si.getRepresent().getPicUrl();
                    String content = si.getRepresent().getProperty() + si.getRepresent().getName();
                    it.putExtra("picUrl", picUrl);
                    it.putExtra("content", content);
                }
                mContext.startActivity(it);
            }
        });
        holder.vote_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                it.putExtra("PAGE_TITLE", "投票");
                String url = "http://m.61park.cn/vote/toVoteDetail.do?applyId=" + si.getVoteApplyId() + "&voteId=" + si.getVoteApply().getVoteId();
                it.putExtra("url", url);
                mContext.startActivity(it);
            }
        });
        holder.img_grow_huifu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mGmShowItemCallBack.onShareClicked(position);
            }
        });
        holder.img_grow_pinlun.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mGmShowItemCallBack.onComtClicked(position);
            }
        });
        holder.img_grow_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mGmShowItemCallBack.onDeleteClicked(position);
            }
        });
        holder.tv_mark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mGmShowItemCallBack.onComtClicked(position);
            }
        });
        holder.img_grow_vote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGmShowItemCallBack.onShareVoteClicked(position);
            }
        });
        if (si.getListGrowComment() != null && si.getListGrowComment().size() > 0) {
            holder.lv_reply.setVisibility(View.VISIBLE);
            buildReplyList(holder.lv_reply, position);
        } else {
            holder.lv_reply.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 在线性布局中构建回复列表
     */
    private void buildReplyList(LinearLayout lv, final int pos) {
        lv.removeAllViews();
        List<GrowCommentVO> replyList = mList.get(pos).getListGrowComment();
        for (int i = 0; i < replyList.size(); i++) {
            if (replyList.get(i).getReplyList() != null && replyList.get(i).getReplyList().size() > 0) {
                replyList.removeAll(replyList.get(i).getReplyList());
                replyList.addAll(replyList.get(i).getReplyList());
            }
        }
        Collections.sort(replyList, new Comparator<GrowCommentVO>() {

            @Override
            public int compare(GrowCommentVO lhs, GrowCommentVO rhs) {
                return lhs.getId().compareTo(rhs.getId());
            }
        });
        for (int i = 0; i < replyList.size(); i++) {
            final GrowCommentVO ri = replyList.get(i);
            View convertView = LayoutInflater.from(mContext).inflate(R.layout.reply_list_item, null);
            TextView tv_reply_username = (TextView) convertView.findViewById(R.id.tv_reply_username);
            TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            TextView tv_reply_content = (TextView) convertView.findViewById(R.id.tv_reply_content);
            if (ri.getUserName().equals(ri.getParentName())) {
                tv_reply_username.setText(ri.getUserName() + "：");
                tv_content.setText(ri.getContent());
//				tv_reply_username.setText(ri.getUserName() + "："+ ri.getContent());
            } else {
                tv_reply_username.setText(ri.getUserName() + " 回复 " + ri.getParentName());
                tv_content.setText(ri.getContent());
//				tv_reply_username.setText(ri.getUserName() + " 回复 "+ ri.getParentName() + "：" + ri.getContent());
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mGmShowItemCallBack.onReplyClicked(pos, ri.getId());
                }
            });
            lv.addView(convertView, i);
        }
    }

    private void initPhotoViews(ViewHolder holder, View convertView) {
        holder.photo0 = (ImageView) convertView.findViewById(R.id.photo0);
        holder.photo1 = (ImageView) convertView.findViewById(R.id.photo1);
        holder.photo2 = (ImageView) convertView.findViewById(R.id.photo2);
        holder.photo3 = (ImageView) convertView.findViewById(R.id.photo3);
        holder.photo4 = (ImageView) convertView.findViewById(R.id.photo4);
        holder.photo5 = (ImageView) convertView.findViewById(R.id.photo5);
        holder.photo6 = (ImageView) convertView.findViewById(R.id.photo6);
        holder.photo7 = (ImageView) convertView.findViewById(R.id.photo7);
        holder.photo8 = (ImageView) convertView.findViewById(R.id.photo8);
    }

    private void fillPicToLinear(ViewHolder holder, List<ShowPhotosDetailsVO> picList) {
        ArrayList<ImageView> photoViewList = new ArrayList<ImageView>();
        photoViewList.add(holder.photo0);
        photoViewList.add(holder.photo1);
        photoViewList.add(holder.photo2);
        photoViewList.add(holder.photo3);
        photoViewList.add(holder.photo4);
        photoViewList.add(holder.photo5);
        photoViewList.add(holder.photo6);
        photoViewList.add(holder.photo7);
        photoViewList.add(holder.photo8);

        if (picList.size() < 4) {
            holder.row2.setVisibility(View.GONE);
            holder.row3.setVisibility(View.GONE);
        } else if (picList.size() < 7) {
            holder.row2.setVisibility(View.VISIBLE);
            holder.row3.setVisibility(View.GONE);
        } else {
            holder.row2.setVisibility(View.VISIBLE);
            holder.row3.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < Math.min(picList.size(), 9); i++) {
            photoViewList.get(i).setVisibility(View.VISIBLE);
        }

        for (int i = picList.size(); i < 9; i++) {
            photoViewList.get(i).setVisibility(View.GONE);
        }

        // url集合
        final ArrayList<String> urlList = new ArrayList<String>();
        for (int j = 0; j < picList.size(); j++) {
            urlList.add(picList.get(j).getMediaUrl());
        }

        for (int i = 0; i < Math.min(picList.size(), 9); i++) {
            final String url = picList.get(i).getMediaUrl();

            photoViewList.get(i).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                    bIt.putExtra("picUrl", url);
                    if (urlList.size() > 1)// url集合
                        bIt.putStringArrayListExtra("urlList", urlList);
                    mContext.startActivity(bIt);
                }
            });
        }

        for (int i = 0; i < Math.min(picList.size(), 9); i++) {
            ImageManager.getInstance().displayImg(photoViewList.get(i), picList.get(i).getMediaUrl(), R.drawable.img_default_v);
        }
    }

    class ViewHolder {
        ImageView img_photo, photo0, photo1, photo2, photo3, photo4, photo5,
                photo6, photo7, photo8, img_grow_pinlun, img_grow_huifu, img_grow_vote, img_vote_left,
                img_grow_delete, img_user_head;
        View photos_area, area_age_title, vote_area, comment_area;
        View build_area, line;
        TextView tv_photo_age, tv_user_name;
        TextView tv_photo_content;
        TextView tv_photo_commit_useratime;
        TextView tv_height_data;
        TextView tv_weight_data;
        TextView tv_state_data;
        TextView tv_mark;
        LinearLayout linear_to_grid, row1, row2, row3, lv_reply;
        TextView tv_appraisal_stage, tv_appraisal_type, tv_example_name, tv_comment_content,
                tv_vote_title, tv_vote_details, tv_vote_intro;
    }

    public interface GmShowItemCallBack {
        public void onComtClicked(int pos);

        public void onReplyClicked(int pos, Long replyId);

        public void onShareClicked(int pos);

        public void onDeleteClicked(int pos);

        public void onShareVoteClicked(int pos);
    }

    /**
     * 时间个性化转换
     *
     * @param currDate   当前时间
     * @param covertDate 发布时间
     * @return 转化后的字符串
     */
    @SuppressWarnings("deprecation")
    public String toPDateStr(String currDate, String covertDate) {
        int d_minutes, d_hours, d_days;
        long timeNow = Long.parseLong(currDate); // 转换为服务器当前时间戳
        long currcovertDate = Long.parseLong(covertDate);
        long d;
        String result;
        d = (timeNow - currcovertDate) / 1000;
        d_days = (int) (d / 86400);
        d_hours = (int) (d / 3600);
        d_minutes = (int) (d / 60);
        if (d_days > 0 && d_days < 4) {
            result = d_days + "天前";
        } else if (d_days <= 0 && d_hours > 0) {
            result = d_hours + "小时前";
        } else if (d_hours <= 0 && d_minutes > 0) {
            result = d_minutes + "分钟前";
        } else if (d_minutes <= 0 && d >= 0) {
            return Math.round(d) + "秒前";
        } else {
            Date s = new Date(currcovertDate);
            result = (s.getMonth() + 1) + "月" + s.getDate() + "日";
        }
        return result;
    }

}
