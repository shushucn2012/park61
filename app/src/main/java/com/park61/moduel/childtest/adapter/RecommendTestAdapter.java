package com.park61.moduel.childtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.childtest.TestSysDetailsWvActivity;
import com.park61.moduel.childtest.bean.TestRecommedBean;
import com.park61.widget.textview.CirButton;

import java.util.List;

/**
 * Created by nieyu on 2017/12/5.
 */

public class RecommendTestAdapter extends BaseAdapter {

    private List<TestRecommedBean> testlistbean;
    private Context ctx;
    private OnBtnClickedLsner mOnBtnClickedLsner;

    public RecommendTestAdapter(List<TestRecommedBean> testlistbean, Context ctx) {
        this.testlistbean = testlistbean;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return testlistbean.size();
    }

    @Override
    public Object getItem(int i) {
        return testlistbean.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final TestRecommedBean testRecommedBean = testlistbean.get(i);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.adapter_testrecommend, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageManager.getInstance().displayImg(holder.iv_righticon, testRecommedBean.getImgUrl());
        if (testRecommedBean.getSalePrice() == 0) {
            holder.iv_rightup.setImageDrawable(ctx.getResources().getDrawable(R.drawable.comment_free));
        } else {
            holder.iv_rightup.setImageDrawable(ctx.getResources().getDrawable(R.drawable.commendiconz_all));
        }

        holder.tv_agearount.setText("适合年龄：" + testRecommedBean.getEaLowAgeLimit() + "-" + testRecommedBean.getEaUpAgeLimit() + "岁");
        holder.tv_title.setText(testRecommedBean.getTitle());
        holder.tv_orignalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_orignalprice.setText("¥" + testRecommedBean.getOriginalPrice());
        if (testRecommedBean.getSalePrice() == 0) {
            holder.tv_realprice.setText("免费");
        } else {
            holder.tv_realprice.setText("¥" + testRecommedBean.getSalePrice());
        }

        holder.tv_viewnum.setText(testRecommedBean.getCountFinishedNum() + "位小朋友完成测评");
        holder.tv_expert_focus_btn.setText(testRecommedBean.getBtnStatusName());
        if (testRecommedBean.getBtnStatus() == 0 || testRecommedBean.getBtnStatus() == 1) {
            holder.tv_expert_focus_btn.setColor(ctx, R.color.gree);
        } else {
            holder.tv_expert_focus_btn.setColor(ctx, R.color.color_text_red_deep);
        }
        if (testRecommedBean.getCanUseCoupon() == 0) {
            holder.img_test_coupon.setVisibility(View.GONE);
        } else {
            holder.img_test_coupon.setVisibility(View.VISIBLE);
        }
        holder.tv_expert_focus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBtnClickedLsner.onClick(i);
            }
        });
        holder.iv_righticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ctx, TestSysDetailsWvActivity.class);
                it.putExtra("PAGE_TITLE", testRecommedBean.getTitle());
                it.putExtra("url", "http://m.61park.cn/#/dap/detail/" + testRecommedBean.getId());
                ctx.startActivity(it);
            }
        });
        convertView.findViewById(R.id.area_sys_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ctx, TestSysDetailsWvActivity.class);
                it.putExtra("PAGE_TITLE", testRecommedBean.getTitle());
                it.putExtra("url", "http://m.61park.cn/#/dap/detail/" + testRecommedBean.getId());
                ctx.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_righticon;
        TextView tv_agearount;
        ImageView iv_rightup;
        TextView tv_orignalprice, tv_realprice;
        TextView tv_title;
        TextView tv_viewnum;
        CirButton tv_expert_focus_btn;
        ImageView img_test_coupon;

        public ViewHolder(View view) {
            tv_realprice = (TextView) view.findViewById(R.id.tv_realprice);
            tv_viewnum = (TextView) view.findViewById(R.id.tv_viewnum);
            iv_rightup = (ImageView) view.findViewById(R.id.iv_rightup);
            iv_righticon = (ImageView) view.findViewById(R.id.iv_righticon);
            tv_agearount = (TextView) view.findViewById(R.id.tv_agearount);
            tv_orignalprice = (TextView) view.findViewById(R.id.tv_orignalprice);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_expert_focus_btn = (CirButton) view.findViewById(R.id.tv_expert_focus_btn);
            img_test_coupon = (ImageView) view.findViewById(R.id.img_test_coupon);
        }
    }

    public void setBtnClickedLsner(OnBtnClickedLsner lsner) {
        this.mOnBtnClickedLsner = lsner;
    }

    public interface OnBtnClickedLsner {
        void onClick(int position);
    }

}
