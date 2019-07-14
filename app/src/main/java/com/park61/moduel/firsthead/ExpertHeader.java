package com.park61.moduel.firsthead;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActMapActivity;
import com.park61.moduel.firsthead.bean.ExpertBean;
import com.park61.moduel.shop.ShopQrCodeActivity;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.moduel.shophome.ShowBigPhotoActivity;
import com.park61.moduel.shophome.bean.TempleteData;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的HeaderView(首页专家)
 */
public class ExpertHeader extends RelativeLayout {

    private View area_expert_one, area_expert_two, area_expert_three, area_expert_four;
    private ImageView img_expert_one, img_expert_two, img_expert_three, img_expert_four;
    private TextView tv_expert_name_one, tv_expert_name_two, tv_expert_name_three, tv_expert_name_four;
    private TextView tv_expert_level_one, tv_expert_level_two, tv_expert_level_three, tv_expert_level_four;
    private TextView tv_expert_focus_one, tv_expert_focus_two, tv_expert_focus_three, tv_expert_focus_four;
    private TextView tv_more;
    private ImageView img_right_arrow;

    public ExpertHeader(Context context, List<ExpertBean> list, OnClickListener lsner) {
        super(context);
        init(context, list, lsner);
    }

    public void init(final Context context, List<ExpertBean> list, OnClickListener lsner) {
        View rootView = inflate(context, R.layout.mainfirst_head_expert_layout, this);
        area_expert_one = rootView.findViewById(R.id.area_expert_one);
        area_expert_two = rootView.findViewById(R.id.area_expert_two);
        area_expert_three = rootView.findViewById(R.id.area_expert_three);
        area_expert_four = rootView.findViewById(R.id.area_expert_four);

        img_expert_one = (ImageView) rootView.findViewById(R.id.img_expert_one);
        img_expert_two = (ImageView) rootView.findViewById(R.id.img_expert_two);
        img_expert_three = (ImageView) rootView.findViewById(R.id.img_expert_three);
        img_expert_four = (ImageView) rootView.findViewById(R.id.img_expert_four);

        tv_expert_name_one = (TextView) rootView.findViewById(R.id.tv_expert_name_one);
        tv_expert_name_two = (TextView) rootView.findViewById(R.id.tv_expert_name_two);
        tv_expert_name_three = (TextView) rootView.findViewById(R.id.tv_expert_name_three);
        tv_expert_name_four = (TextView) rootView.findViewById(R.id.tv_expert_name_four);

        tv_expert_level_one = (TextView) rootView.findViewById(R.id.tv_expert_level_one);
        tv_expert_level_two = (TextView) rootView.findViewById(R.id.tv_expert_level_two);
        tv_expert_level_three = (TextView) rootView.findViewById(R.id.tv_expert_level_three);
        tv_expert_level_four = (TextView) rootView.findViewById(R.id.tv_expert_level_four);

        tv_expert_focus_one = (TextView) rootView.findViewById(R.id.tv_expert_focus_one);
        tv_expert_focus_two = (TextView) rootView.findViewById(R.id.tv_expert_focus_two);
        tv_expert_focus_three = (TextView) rootView.findViewById(R.id.tv_expert_focus_three);
        tv_expert_focus_four = (TextView) rootView.findViewById(R.id.tv_expert_focus_four);

        tv_more = (TextView) rootView.findViewById(R.id.tv_more);
        img_right_arrow = (ImageView) rootView.findViewById(R.id.img_right_arrow);
        OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ExpertListActivity.class));
            }
        };
        tv_more.setOnClickListener(mOnClickListener);
        img_right_arrow.setOnClickListener(mOnClickListener);
        fillData(context, list, lsner);
    }

    private void fillData(final Context context, List<ExpertBean> list, OnClickListener lsner) {
        if (list.size() > 0) {
            final ExpertBean expertBeanOne = list.get(0);
            ImageManager.getInstance().displayImg(img_expert_one, expertBeanOne.getPhoto());
            tv_expert_name_one.setText(expertBeanOne.getExpertName());
            tv_expert_level_one.setText(expertBeanOne.getResume());
            if (expertBeanOne.isFocus()) {
                focusView(context, 0);
            }
            tv_expert_focus_one.setOnClickListener(lsner);
            area_expert_one.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, BlogerInfoActivity.class);
                    it.putExtra("userId", expertBeanOne.getUserId());
                    context.startActivity(it);
                }
            });
        }
        if (list.size() > 1) {
            final ExpertBean expertBeanTwo = list.get(1);
            ImageManager.getInstance().displayImg(img_expert_two, expertBeanTwo.getPhoto());
            tv_expert_name_two.setText(expertBeanTwo.getExpertName());
            tv_expert_level_two.setText(expertBeanTwo.getResume());
            if (expertBeanTwo.isFocus()) {
                focusView(context, 1);
            }
            tv_expert_focus_two.setOnClickListener(lsner);
            area_expert_two.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, BlogerInfoActivity.class);
                    it.putExtra("userId", expertBeanTwo.getUserId());
                    context.startActivity(it);
                }
            });
        }
        if (list.size() > 2) {
            final ExpertBean expertBeanThree = list.get(2);
            ImageManager.getInstance().displayImg(img_expert_three, expertBeanThree.getPhoto());
            tv_expert_name_three.setText(expertBeanThree.getExpertName());
            tv_expert_level_three.setText(expertBeanThree.getResume());
            if (expertBeanThree.isFocus()) {
                focusView(context, 2);
            }
            tv_expert_focus_three.setOnClickListener(lsner);
            area_expert_three.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, BlogerInfoActivity.class);
                    it.putExtra("userId", expertBeanThree.getUserId());
                    context.startActivity(it);
                }
            });
        }
        if (list.size() > 3) {
            final ExpertBean expertBeanFour = list.get(3);
            ImageManager.getInstance().displayImg(img_expert_four, expertBeanFour.getPhoto());
            tv_expert_name_four.setText(expertBeanFour.getExpertName());
            tv_expert_level_four.setText(expertBeanFour.getResume());
            if (expertBeanFour.isFocus()) {
                focusView(context, 3);
            }
            tv_expert_focus_four.setOnClickListener(lsner);
            area_expert_four.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, BlogerInfoActivity.class);
                    it.putExtra("userId", expertBeanFour.getUserId());
                    context.startActivity(it);
                }
            });
        }
    }

    public void fillData(final Context context, List<ExpertBean> list) {
        if (list.size() > 0) {
            ImageManager.getInstance().displayImg(img_expert_one, list.get(0).getPhoto());
            tv_expert_name_one.setText(list.get(0).getExpertName());
            tv_expert_level_one.setText(list.get(0).getResume());
            if (list.get(0).isFocus()) {
                focusView(context, 0);
            } else {
                unfocusView(context, 0);
            }
        }
        if (list.size() > 1) {
            ImageManager.getInstance().displayImg(img_expert_two, list.get(1).getPhoto());
            tv_expert_name_two.setText(list.get(1).getExpertName());
            tv_expert_level_two.setText(list.get(1).getResume());
            if (list.get(1).isFocus()) {
                focusView(context, 1);
            } else {
                unfocusView(context, 1);
            }
        }
        if (list.size() > 2) {
            ImageManager.getInstance().displayImg(img_expert_three, list.get(2).getPhoto());
            tv_expert_name_three.setText(list.get(2).getExpertName());
            tv_expert_level_three.setText(list.get(2).getResume());
            if (list.get(2).isFocus()) {
                focusView(context, 2);
            } else {
                unfocusView(context, 2);
            }
        }
        if (list.size() > 3) {
            ImageManager.getInstance().displayImg(img_expert_four, list.get(3).getPhoto());
            tv_expert_name_four.setText(list.get(3).getExpertName());
            tv_expert_level_four.setText(list.get(3).getResume());
            if (list.get(3).isFocus()) {
                focusView(context, 3);
            } else {
                unfocusView(context, 3);
            }
        }
        final ExpertBean expertBeanOne = list.get(0);
        final ExpertBean expertBeanTwo = list.get(1);
        final ExpertBean expertBeanThree = list.get(2);
        final ExpertBean expertBeanFour = list.get(3);
        area_expert_one.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, BlogerInfoActivity.class);
                it.putExtra("userId", expertBeanOne.getUserId());
                context.startActivity(it);
            }
        });
        area_expert_two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, BlogerInfoActivity.class);
                it.putExtra("userId", expertBeanTwo.getUserId());
                context.startActivity(it);
            }
        });
        area_expert_three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, BlogerInfoActivity.class);
                it.putExtra("userId", expertBeanThree.getUserId());
                context.startActivity(it);
            }
        });
        area_expert_four.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, BlogerInfoActivity.class);
                it.putExtra("userId", expertBeanFour.getUserId());
                context.startActivity(it);
            }
        });
    }

    public void focusView(Context context, int index) {
        switch (index) {
            case 0:
                tv_expert_focus_one.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                tv_expert_focus_one.setTextColor(context.getResources().getColor(R.color.g999999));
                tv_expert_focus_one.setText("已关注");
                break;
            case 1:
                tv_expert_focus_two.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                tv_expert_focus_two.setTextColor(context.getResources().getColor(R.color.g999999));
                tv_expert_focus_two.setText("已关注");
                break;
            case 2:
                tv_expert_focus_three.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                tv_expert_focus_three.setTextColor(context.getResources().getColor(R.color.g999999));
                tv_expert_focus_three.setText("已关注");
                break;
            case 3:
                tv_expert_focus_four.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
                tv_expert_focus_four.setTextColor(context.getResources().getColor(R.color.g999999));
                tv_expert_focus_four.setText("已关注");
                break;
        }
    }

    public void unfocusView(Context context, int index) {
        switch (index) {
            case 0:
                tv_expert_focus_one.setBackgroundResource(R.drawable.rec_red_stroke_trans_solid_corner30);
                tv_expert_focus_one.setTextColor(context.getResources().getColor(R.color.com_orange));
                tv_expert_focus_one.setText("+关注");
                break;
            case 1:
                tv_expert_focus_two.setBackgroundResource(R.drawable.rec_red_stroke_trans_solid_corner30);
                tv_expert_focus_two.setTextColor(context.getResources().getColor(R.color.com_orange));
                tv_expert_focus_two.setText("+关注");
                break;
            case 2:
                tv_expert_focus_three.setBackgroundResource(R.drawable.rec_red_stroke_trans_solid_corner30);
                tv_expert_focus_three.setTextColor(context.getResources().getColor(R.color.com_orange));
                tv_expert_focus_three.setText("+关注");
                break;
            case 3:
                tv_expert_focus_four.setBackgroundResource(R.drawable.rec_red_stroke_trans_solid_corner30);
                tv_expert_focus_four.setTextColor(context.getResources().getColor(R.color.com_orange));
                tv_expert_focus_four.setText("+关注");
                break;
        }
    }


}
