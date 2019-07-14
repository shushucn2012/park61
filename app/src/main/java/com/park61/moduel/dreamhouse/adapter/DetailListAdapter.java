package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.DreamDetailBean;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;
import com.park61.moduel.dreamhouse.bean.SameDreamInfo;

import java.util.ArrayList;

/**
 * 梦想详情adapter
 */
public class DetailListAdapter extends BaseAdapter {
    private ArrayList<EvaluateItemInfo> mList;
    private Context mContext;
    private LayoutInflater factory;
    private ToSameDreamPeople mToSameDreamPeople;
    private OnReplyClickedLsner mOnReplyClickedLsner;
    private final int TYPE_HAS_HEAD = 1;
    private final int TYPE_NO_HEAD = 0;
    private int totalSize;
    private DreamDetailBean detailBean;

    public DetailListAdapter(Context _context, ArrayList<EvaluateItemInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size() + 2;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 0) {
            return TYPE_NO_HEAD;
        }
        return TYPE_HAS_HEAD;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Log.out("1111111111111==============================5555555555555555555555555555555");
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = factory.inflate(R.layout.dream_detail_list_item, null);
            holder = new ViewHolder();
            holder.user_area = convertView.findViewById(R.id.user_area);
            holder.content_area = convertView.findViewById(R.id.content_area);
            holder.same_dream_area = convertView.findViewById(R.id.same_dream_area);
            holder.area = convertView.findViewById(R.id.area);
            holder.line = convertView.findViewById(R.id.line);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.webview = (WebView) convertView.findViewById(R.id.webview);
            holder.img_next = (ImageView) convertView.findViewById(R.id.img_next);
            holder.tv_samedream_num = (TextView) convertView.findViewById(R.id.tv_samedream_num);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_label = (TextView) convertView.findViewById(R.id.tv_label);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_evaluate_mark = (TextView) convertView.findViewById(R.id.tv_evaluate_mark);
            holder.img_area = convertView.findViewById(R.id.img_area);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.img3);
            holder.img4 = (ImageView) convertView.findViewById(R.id.img4);
            holder.img5 = (ImageView) convertView.findViewById(R.id.img5);
            holder.img_quesheng = (ImageView) convertView.findViewById(R.id.img_quesheng);

            holder.item_area = convertView.findViewById(R.id.item_area);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_evaluate_date = (TextView) convertView.findViewById(R.id.tv_evaluate_date);
            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_replay);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            switch (type) {
                case TYPE_HAS_HEAD:
                    holder.user_area.setVisibility(View.VISIBLE);
                    holder.content_area.setVisibility(View.VISIBLE);
                    holder.same_dream_area.setVisibility(View.VISIBLE);
                    holder.webview.setVisibility(View.VISIBLE);
                    holder.area.setVisibility(View.VISIBLE);
                    convertView.setTag(holder);
                    break;
                case TYPE_NO_HEAD:
                    holder.user_area.setVisibility(View.GONE);
                    holder.content_area.setVisibility(View.GONE);
                    holder.same_dream_area.setVisibility(View.GONE);
                    holder.webview.setVisibility(View.GONE);
                    holder.area.setVisibility(View.GONE);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (totalSize > 0) {
            holder.tv_evaluate_mark.setText(totalSize + "条评论");
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.tv_evaluate_mark.setText("暂无评论");
            holder.line.setVisibility(View.GONE);
        }
        if (detailBean != null) {
            holder.tv_user_name.setText(detailBean.getUserName());
            holder.tv_samedream_num.setText("同梦人（" + detailBean.getPredictionList().size() + "）");
            holder.tv_title.setText(detailBean.getTitle());
            holder.tv_label.setText(detailBean.getTypeName());
            holder.tv_date.setText(DateTool.L2SEndDay(detailBean.getCreateDate()));

            if (detailBean.getPredictionList().size() > 0) {
                holder.img_area.setVisibility(View.VISIBLE);
                setImg(detailBean, holder);
            } else {
                holder.img_area.setVisibility(View.GONE);
            }
            /*holder.webview.getSettings().setDefaultTextEncodingName("utf-8");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                holder.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } else {
                holder.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            }*/
            holder.webview.loadData(HtmlParserTool.getHtmlData(detailBean.getContent()), "text/html; charset=utf-8", "utf-8");
        }

        if (!CommonMethod.isListEmpty(mList) && position < mList.size()) {
            holder.item_area.setVisibility(View.VISIBLE);
            EvaluateItemInfo itemInfo = mList.get(position);
            ImageManager.getInstance().displayImg(holder.img, itemInfo.getUserUrl(),R.drawable.headimg_default_img);
            holder.tv_name.setText(itemInfo.getUserName());
            holder.tv_evaluate_date.setText(toPDateStr(System.currentTimeMillis(), itemInfo.getCreateDate()));

            if ((itemInfo.isReply() == true)) {
                holder.tv_replay.setVisibility(View.VISIBLE);
                holder.tv_replay.setText("回复  " + itemInfo.getParentUserName() + ":");
            } else {
                holder.tv_replay.setVisibility(View.GONE);
            }
            holder.tv_comt_content.setText(itemInfo.getContent());
        } else {
            holder.item_area.setVisibility(View.GONE);
        }

        holder.same_dream_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToSameDreamPeople.toSameDreamPeople();
            }
        });
        holder.item_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnReplyClickedLsner.onComtClicked(mList.get(position).getId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        View same_dream_area, user_area, content_area, img_area, area, line;
        TextView tv_user_name, tv_samedream_num, tv_title, tv_label, tv_date, tv_evaluate_mark;
        ImageView img_next;
        ImageView img, img1, img2, img3, img4, img5, img_quesheng;
        WebView webview;

        TextView tv_name, tv_evaluate_date, tv_replay, tv_comt_content;
        View item_area;
    }

    public interface ToSameDreamPeople {
        public void toSameDreamPeople();
    }

    public void setToSameDreamPeople(ToSameDreamPeople lsner) {
        this.mToSameDreamPeople = lsner;
    }

    public interface OnReplyClickedLsner {
        public void onComtClicked(Long parentId);
    }

    public void setOnReplyClickedLsner(OnReplyClickedLsner lsner) {
        this.mOnReplyClickedLsner = lsner;
    }

    public void setTotalSize(int size) {
        totalSize = size;
    }

    public void detailInfo(DreamDetailBean item) {
        detailBean = item;
    }

    /**
     * 时间个性化转换
     *
     * @param currDate   当前时间
     * @param covertDate 发布时间
     * @return 转化后的字符串
     */
    @SuppressWarnings("deprecation")
    public String toPDateStr(long currDate, String covertDate) {
        int d_minutes, d_hours, d_days;
        long currcovertDate = Long.parseLong(covertDate);
        long d;
        String result;
        d = (currDate - currcovertDate) / 1000;
        d_days = (int) (d / 86400);
        d_hours = (int) (d / 3600);
        d_minutes = (int) (d / 60);
        if (d_days <= 0 && d_hours > 0) {
            result = d_hours + "小时前";
        } else if (d_hours <= 0 && d_minutes > 0) {
            result = d_minutes + "分钟前";
        } else if (d_minutes <= 0 && d >= 0) {
            return Math.round(d) + "秒前";
        } else {
            result = DateTool.L2S(covertDate);
        }
        return result;
    }

    private void setImg(DreamDetailBean detailBean, ViewHolder holder) {
        ArrayList<SameDreamInfo> dreamList = (ArrayList<SameDreamInfo>) detailBean.getPredictionList();
        if (detailBean.getPredictionList().size() == 0) {
            holder.img1.setVisibility(View.GONE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() == 1) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() == 2) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, dreamList.get(1).getUserUrl());
            holder.img3.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() == 3) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, dreamList.get(1).getUserUrl());
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, dreamList.get(2).getUserUrl());
            holder.img4.setVisibility(View.GONE);
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() == 4) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, dreamList.get(1).getUserUrl());
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, dreamList.get(2).getUserUrl());
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, dreamList.get(3).getUserUrl());
            holder.img5.setVisibility(View.GONE);
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() == 5) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, dreamList.get(1).getUserUrl());
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, dreamList.get(2).getUserUrl());
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, dreamList.get(3).getUserUrl());
            holder.img5.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img5, dreamList.get(4).getUserUrl());
            holder.img_quesheng.setVisibility(View.GONE);
        } else if (detailBean.getPredictionList().size() > 5) {
            holder.img1.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img1, dreamList.get(0).getUserUrl());
            holder.img2.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img2, dreamList.get(1).getUserUrl());
            holder.img3.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img3, dreamList.get(2).getUserUrl());
            holder.img4.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img4, dreamList.get(3).getUserUrl());
            holder.img5.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayCircleImg(holder.img5, dreamList.get(4).getUserUrl());
            holder.img_quesheng.setVisibility(View.VISIBLE);
        }
    }
}
