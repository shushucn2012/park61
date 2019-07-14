package com.park61.moduel.sales.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.ComtItem;
import com.park61.moduel.acts.bean.ReplyItem;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.sales.bean.ProductEvaluate;
import com.park61.moduel.sales.bean.ProductEvaluatePicture;
import com.park61.widget.ProductRatingBar;

public class GoodsDetailsComtFiller {

	// 图片间隔距离
	public static final int SPACE_VALUE = 5;
	// 图片长宽
	public static final int IMAGE_WIDTH = 75;

	/**
	 * 在线性布局中构建评论列表
	 */
	public static void buildListInLinear(LinearLayout linear_comment,
			List<ProductEvaluate> comtList, Context mContext) {
		linear_comment.removeAllViews();
		for (int i = 0; i < comtList.size(); i++) {
			final ProductEvaluate pe = comtList.get(i);
			View convertView = LayoutInflater.from(mContext).inflate(
					R.layout.goods_comtlist_item, null);
			TextView tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			TextView tv_comt_content = (TextView) convertView
					.findViewById(R.id.tv_comt_content);
			TextView tv_comt_time = (TextView) convertView
					.findViewById(R.id.tv_comt_time);
			ProductRatingBar ratingbar_score = (ProductRatingBar) convertView
					.findViewById(R.id.ratingbar_score);
			LinearLayout linear_reply = (LinearLayout) convertView
					.findViewById(R.id.lv_reply);
			View list_line = convertView.findViewById(R.id.list_line);
			LinearLayout lv_eva_imgs = (LinearLayout) convertView
					.findViewById(R.id.lv_eva_imgs);
			fillEvaImages(lv_eva_imgs, pe.getPicList(), mContext);
			tv_username.setText(pe.getUserName());
			tv_comt_content.setText(pe.getContent());
			tv_comt_time.setText(DateTool.L2S(pe.getCreateTime()));
			linear_reply.setVisibility(View.GONE);
			if (i == comtList.size() - 1)
				list_line.setVisibility(View.GONE);
			ratingbar_score.setVisibility(View.VISIBLE);
			ratingbar_score.setPoint(pe.getCompositeScore().intValue());
			linear_comment.addView(convertView, i);
		}
	}

	/**
	 * 填充评价的图片列表
	 */
	public static void fillEvaImages(LinearLayout lv_eva_imgs,
			List<ProductEvaluatePicture> listPictrue, final Context mContext) {
		if (listPictrue == null || listPictrue.size() <= 0)
			return;
		List<ImageView> imgViewList = new ArrayList<ImageView>();
		if (listPictrue.size() <= 3) {// 小于等于3张图时
			LinearLayout linear1 = new LinearLayout(mContext);
			linear1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 0; i < listPictrue.size(); i++) {
				fillImageToLinear(imgViewList, linear1, mContext);
			}
			lv_eva_imgs.addView(linear1);
		} else if (listPictrue.size() <= 6) {// 小于等于6张图时
			LinearLayout linear1 = new LinearLayout(mContext);
			linear1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 0; i < 3; i++) {
				fillImageToLinear(imgViewList, linear1, mContext);
			}
			lv_eva_imgs.addView(linear1);

			View spaceView = new View(mContext);
			spaceView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, DevAttr.dip2px(mContext,
							SPACE_VALUE)));
			lv_eva_imgs.addView(spaceView);

			LinearLayout linear2 = new LinearLayout(mContext);
			linear2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 3; i < listPictrue.size(); i++) {
				fillImageToLinear(imgViewList, linear2, mContext);
			}
			lv_eva_imgs.addView(linear2);
		} else {// 小于等于9张图时
			LinearLayout linear1 = new LinearLayout(mContext);
			linear1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 0; i < 3; i++) {
				fillImageToLinear(imgViewList, linear1, mContext);
			}
			lv_eva_imgs.addView(linear1);
			View spaceView1 = new View(mContext);
			spaceView1.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, DevAttr.dip2px(mContext,
							SPACE_VALUE)));
			lv_eva_imgs.addView(spaceView1);

			LinearLayout linear2 = new LinearLayout(mContext);
			linear2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 3; i < 6; i++) {
				fillImageToLinear(imgViewList, linear2, mContext);
			}
			lv_eva_imgs.addView(linear2);
			View spaceView2 = new View(mContext);
			spaceView2.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, DevAttr.dip2px(mContext,
							SPACE_VALUE)));
			lv_eva_imgs.addView(spaceView2);

			LinearLayout linear3 = new LinearLayout(mContext);
			linear3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int i = 6; i < listPictrue.size(); i++) {
				fillImageToLinear(imgViewList, linear3, mContext);
			}
			lv_eva_imgs.addView(linear3);
		}
		// 把图片填充到ImageView上
		for (int i = 0; i < imgViewList.size(); i++) {
			ImageManager.getInstance().displayImg(imgViewList.get(i),
					listPictrue.get(i).getPictureUrl(), R.drawable.img_default_v);
		}

		// url集合
		final ArrayList<String> urlList = new ArrayList<String>();
		for (int j = 0; j < listPictrue.size(); j++) {
			urlList.add(listPictrue.get(j).getPictureUrl());
		}

		for (int i = 0; i < imgViewList.size(); i++) {
			final String url = listPictrue.get(i).getPictureUrl();
			imgViewList.get(i).setOnClickListener(new OnClickListener() {

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
	}

	/**
	 * 把图片填充到线性布局一行
	 * 
	 * @param imgViewList
	 * @param linear
	 */
	public static void fillImageToLinear(List<ImageView> imgViewList,
			LinearLayout linear, Context mContext) {
		ImageView img = new ImageView(mContext);
		img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext,
				IMAGE_WIDTH), DevAttr.dip2px(mContext, IMAGE_WIDTH)));
		img.setScaleType(ScaleType.FIT_XY);
		imgViewList.add(img);
		linear.addView(img);
		// 两个图片之间加空格
		View spaceView = new View(mContext);
		spaceView.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext,
				SPACE_VALUE), LayoutParams.MATCH_PARENT));
		linear.addView(spaceView);
	}

	/**
	 * 在线性布局中构建回复列表
	 */
	public static void buildReplyList(LinearLayout lv, final ComtItem ci,
			Context mContext) {
		lv.removeAllViews();
		List<ReplyItem> replyList = ci.getReplyList();
		for (int i = 0; i < replyList.size(); i++) {
			final ReplyItem ri = ci.getReplyList().get(i);
			View convertView = LayoutInflater.from(mContext).inflate(
					R.layout.replylist_item, null);
			TextView tv_reply_username = (TextView) convertView
					.findViewById(R.id.tv_reply_username);
			TextView tv_reply_time = (TextView) convertView
					.findViewById(R.id.tv_reply_time);
			TextView tv_reply_content = (TextView) convertView
					.findViewById(R.id.tv_reply_content);
			if (ri.getUserName().equals(ri.getParentName())) {
				tv_reply_username.setText(ri.getUserName());
			} else {
				tv_reply_username.setText(ri.getUserName() + " 回复 "
						+ ri.getParentName());
			}
			tv_reply_content.setText(ri.getContent());
			tv_reply_time.setText(DateTool.L2S(ri.getContentTime()));
			lv.addView(convertView, i);
		}
	}

}
