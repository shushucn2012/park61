package com.park61.moduel.firsthead;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.FirstHeadChildBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.fragment.SpecialTypeActivity;
import com.park61.moduel.firsthead.fragment.TextDetailActivity;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.moduel.okdownload.db.DownloadTask;
import com.park61.moduel.sales.bean.CmsItem;
import com.park61.moduel.sales.bean.GoldTeacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/11/16.
 */

/**
 * 首页视图初始化帮助类
 */
public class MainHomeViewHelper {

    /**
     * 首页快捷入口初始化
     *
     * @param headview  recycleview 头部试图
     * @param mContext  首页上下文
     * @param quickList 数据列表
     */
    public static void initFastGo(View headview, final Context mContext, final List<CmsItem> quickList) {
        if (quickList.size() == 1) {
            headview.findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick1).setVisibility(View.INVISIBLE);
            headview.findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            headview.findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());//
            //定义一个csmitem对象
            headview.findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            headview.findViewById(R.id.area_quick1).setOnClickListener(null);
            headview.findViewById(R.id.area_quick2).setOnClickListener(null);
            headview.findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 2) {
            headview.findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            headview.findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            headview.findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            headview.findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            headview.findViewById(R.id.area_quick2).setOnClickListener(null);
            headview.findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 3) {
            headview.findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            headview.findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            headview.findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            headview.findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            headview.findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() >= 4) {
            headview.findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            headview.findViewById(R.id.area_quick3).setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_quick3), quickList.get(3).getLinkPic());
            ((TextView) headview.findViewById(R.id.tv_quick3)).setText(quickList.get(3).getTitle());
            headview.findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            headview.findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            headview.findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            headview.findViewById(R.id.area_quick3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(quickList.get(3), mContext);
                }
            });
        }
    }

    /**
     * 首页无规则广告页初始化
     *
     * @param headview       recycleview 头部试图
     * @param mContext       首页上下文
     * @param midBanner2List 数据列表
     */
    public static void initMiddleBanner2(final Context mContext, View headview, final List<CmsItem> midBanner2List) {
        if (midBanner2List.size() == 1) {
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ((ImageView) headview.findViewById(R.id.img_mid1)).setImageResource(R.drawable.img_default_h);
            ((ImageView) headview.findViewById(R.id.img_mid2)).setImageResource(R.drawable.img_default_h);
            headview.findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            headview.findViewById(R.id.img_mid1).setOnClickListener(null);
            headview.findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 2) {
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            ((ImageView) headview.findViewById(R.id.img_mid2)).setImageResource(R.drawable.img_default_h);
            headview.findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            headview.findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            headview.findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 3) {
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) headview.findViewById(R.id.img_mid2), midBanner2List.get(2).getLinkPic());
            headview.findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            headview.findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            headview.findViewById(R.id.img_mid2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.judgeGoWhere(midBanner2List.get(2), mContext);
                }
            });
        }
    }


    /**
     * 构建专家列表数据
     *
     * @param linear_gold_teacher 专家列表父容器
     * @param mContext            首页上下文
     * @param gList               数据列表
     */
    public static void buildLinearGoldTeacher(LinearLayout linear_gold_teacher, final Context mContext, final List<GoldTeacher> gList) {
        linear_gold_teacher.removeAllViews();

        for (int i = 0; i < gList.size(); i++) {
            View view_gold_teacher_item = LayoutInflater.from(mContext).inflate(R.layout.mexpertlist_item, null);
            ImageView img_teacher = (ImageView) view_gold_teacher_item.findViewById(R.id.img_teacher);
            TextView tv_title = (TextView) view_gold_teacher_item.findViewById(R.id.tv_title);
            TextView tv_num = (TextView) view_gold_teacher_item.findViewById(R.id.tv_num);
            TextView tv_lable = (TextView) view_gold_teacher_item.findViewById(R.id.tv_lable);
            View bottom_line = view_gold_teacher_item.findViewById(R.id.bottom_line);

            GoldTeacher teacher = gList.get(i);
            ImageManager.getInstance().displayImg(img_teacher, teacher.getHeadPictureUrl());
            tv_title.setText(teacher.getName() + " · " + teacher.getSubhead());
            tv_lable.setText(teacher.getDescription());
            tv_num.setText(teacher.getContentTitle());

            final int position = i;
            if (position == gList.size() - 1) {
                bottom_line.setVisibility(View.GONE);
            } else {
                bottom_line.setVisibility(View.VISIBLE);
            }
            view_gold_teacher_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, BlogerInfoActivity.class);
                    it.putExtra("teacherId", FU.paseInt(gList.get(position).getUserId()));
                    mContext.startActivity(it);
                }
            });
            linear_gold_teacher.addView(view_gold_teacher_item);
        }
    }

    /**
     * 点击标签，点亮当前标签，更换其他标签视图
     *
     * @param hscrollview_linear 父容器试图
     * @param mContext           上下文
     * @param position           当前点击位置
     */
    public static void hScrollviewChangeOthers(LinearLayout hscrollview_linear, Context mContext, int position) {
        ((TextView) hscrollview_linear.getChildAt(position).findViewById(R.id.tv_tab_name)).setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        hscrollview_linear.getChildAt(position).findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
        for (int i = 0; i < hscrollview_linear.getChildCount(); i++) {
            if (i != position) {
                ((TextView) hscrollview_linear.getChildAt(i).findViewById(R.id.tv_tab_name)).setTextColor(mContext.getResources().getColor(R.color.g333333));
                hscrollview_linear.getChildAt(i).findViewById(R.id.bottom_line).setVisibility(View.INVISIBLE);
            }
        }
    }

    public static FirstHeadBean transNewBeanToOld(FirstHeadChildBean p){
        FirstHeadBean fb = new FirstHeadBean();
        fb.setClassifyType(p.getContentType());
        fb.setComposeType(p.getCoverType());
        fb.setIssuerName(p.getAuthorName());
        fb.setItemCommentNum(p.getCommentNum());
        fb.setItemTitle(p.getTitle());
        fb.setItemReadNum(p.getViewNum());
        fb.setItemId(p.getId());

        List<MediaBean> mediaList = new ArrayList<>();
        if (!TextUtils.isEmpty(p.getCoverImg1()))
            mediaList.add(new MediaBean("", p.getCoverImg1()));

        if (!TextUtils.isEmpty(p.getCoverImg2()))
            mediaList.add(new MediaBean("", p.getCoverImg2()));

        if (!TextUtils.isEmpty(p.getCoverImg3()))
            mediaList.add(new MediaBean("", p.getCoverImg3()));

        if (0 == p.getCoverType()) {
            fb.setComposeType(2);
        } else if (1 == p.getCoverType()) {
            fb.setComposeType(1);
        } else if (2 == p.getCoverType()) {
            fb.setComposeType(3);
        }

        fb.setItemMediaList(mediaList);
        return fb;
    }

    /**
     * 获取当前资源的下载状态
     *
     * @param sourceId 资源id
     * @return 下载状态
     */
    public static int getSourceItemDownLoadStatus(int sourceId, String itemId) {
        List<DownloadTask> downloadTaskList = DownloadDAO.getInstance().selectContentList(itemId);
        for (int i = 0; i < downloadTaskList.size(); i++) {
            if (FU.paseInt(downloadTaskList.get(i).getSourceId()) == sourceId) {
                return downloadTaskList.get(i).getTask_status();//0未完成, 1完成, 2下载中
            }
        }
        return -1;//-1未下载
    }

    public static String getSourceItemDownLoadPath(int sourceId, String itemId) {
        List<DownloadTask> downloadTaskList = DownloadDAO.getInstance().selectContentList(itemId);
        for (int i = 0; i < downloadTaskList.size(); i++) {
            if (FU.paseInt(downloadTaskList.get(i).getSourceId()) == sourceId) {
                return downloadTaskList.get(i).getTask_filePath();
            }
        }
        return "";
    }

    /**
     * 根据内容类型跳转对应详情页
     * // 资源类型;0:图文;1:视频;2:专栏;3:音频
     * @param fhb
     * @param mContext
     */
    public static void judgeGoWhereByFirstHeadBean(FirstHeadBean fhb, Context mContext){
        Intent intent = null;
        if (fhb.getClassifyType() == 0) {
            intent = new Intent(mContext, TextDetailActivity.class);
        } else if (fhb.getClassifyType() == 1) {
            intent = new Intent(mContext, VideoListDetailsActivity.class);
        } else if (fhb.getClassifyType() == 2) {
            intent = new Intent(mContext, SpecialTypeActivity.class);
        } else if (fhb.getClassifyType() == 3) {
            intent = new Intent(mContext, AudioListDetailsActivity.class);
        }
        if (intent == null)
            return;
        intent.putExtra("itemId", fhb.getItemId());
        mContext.startActivity(intent);
    }
}
