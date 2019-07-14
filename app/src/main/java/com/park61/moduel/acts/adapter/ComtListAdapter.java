package com.park61.moduel.acts.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.ComtItem;
import com.park61.moduel.acts.bean.ReplyItem;

public class ComtListAdapter extends BaseAdapter {

	private List<ComtItem> mList;
	private Context mContext;
	private LayoutInflater factory;
	private OnReplyClickedLsner mOnReplyClickedLsner;
	private HashMap<Long, View> replysViewCache = new HashMap<Long, View>();

	public ComtListAdapter(Context _context, List<ComtItem> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
		mOnReplyClickedLsner = (ActDetailsActivity) _context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ComtItem getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ComtItem ci = mList.get(position);
		if (replysViewCache.get(ci.getId()) != null)
			return replysViewCache.get(ci.getId());
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.comtlist_item, null);
			holder = new ViewHolder();
			holder.img_user = (ImageView) convertView
					.findViewById(R.id.img_user);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.tv_comt_content = (TextView) convertView
					.findViewById(R.id.tv_comt_content);
			holder.tv_comt_time = (TextView) convertView
					.findViewById(R.id.tv_comt_time);
			holder.lv_reply = (LinearLayout) convertView
					.findViewById(R.id.lv_reply);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(ci.getUserName());
		holder.tv_comt_content.setText(ci.getContent());
		holder.tv_comt_time.setText(DateTool.L2S(ci.getCreateTime()));
		if (!TextUtils.isEmpty(ci.getUserHead()))
			ImageManager.getInstance().displayImg(holder.img_user, ci.getUserHead());
		// 有回复列表就初始化回复列表
		if (ci.getReplyList() != null && ci.getReplyList().size() > 0) {
			Log.e("", "ci.getReplyList().size======" + ci.getReplyList().size());
			holder.lv_reply.setVisibility(View.VISIBLE);
			initReplyList(holder.lv_reply, ci);
		} else {
			holder.lv_reply.setVisibility(View.GONE);
		}
		holder.tv_comt_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnReplyClickedLsner.onComtClicked(ci.getId());
			}
		});
		replysViewCache.put(ci.getId(), convertView);
		return convertView;
	}

	/**
	 * 初始化评论列表
	 */
	private void initReplyList(LinearLayout lv, final ComtItem ci) {
		// lv.setAdapter(new replyListAdapter(ci.getReplyList()));
		// lv.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// mOnReplyClickedLsner.onReplyClicked(ci.getId(), ci
		// .getReplyList().get(position).getId());
		// }
		// });
		lv.removeAllViews();
		List<ReplyItem> replyList = ci.getReplyList();
		for (int i = 0; i < replyList.size(); i++) {
			final ReplyItem ri = ci.getReplyList().get(i);
			View convertView = factory.inflate(R.layout.replylist_item, null);
			TextView tv_reply_username = (TextView) convertView
					.findViewById(R.id.tv_reply_username);
			TextView tv_reply_time = (TextView) convertView
					.findViewById(R.id.tv_reply_time);
			TextView tv_reply_content = (TextView) convertView
					.findViewById(R.id.tv_reply_content);
			tv_reply_username.setText(ri.getUserName());
			tv_reply_content.setText(ri.getContent());
			tv_reply_time.setText(DateTool.L2S(ri.getContentTime()));
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mOnReplyClickedLsner.onReplyClicked(ci.getId(), ri.getId());
				}
			});
			lv.addView(convertView, i);
		}
	}

	class ViewHolder {
		ImageView img_user;
		TextView tv_username;
		TextView tv_comt_content;
		TextView tv_comt_time;
		LinearLayout lv_reply;
	}

	// /**
	// * 回复列表适配器
	// */
	// public class ReplyListAdapter extends BaseAdapter {
	//
	// private List<ReplyItem> replyList;
	//
	// public ReplyListAdapter(List<ReplyItem> _replyList) {
	// this.replyList = _replyList;
	// }
	//
	// @Override
	// public int getCount() {
	// return replyList.size();
	// }
	//
	// @Override
	// public ReplyItem getItem(int position) {
	// return replyList.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// convertView = factory.inflate(R.layout.replylist_item, null);
	// TextView tv_reply_username = (TextView) convertView
	// .findViewById(R.id.tv_reply_username);
	// TextView tv_reply_time = (TextView) convertView
	// .findViewById(R.id.tv_reply_time);
	// TextView tv_reply_content = (TextView) convertView
	// .findViewById(R.id.tv_reply_content);
	// ReplyItem ri = replyList.get(position);
	// tv_reply_username.setText(ri.getUserName());
	// tv_reply_content.setText(ri.getContent());
	// tv_reply_time.setText(DateTool.L2S(ri.getContentTime()));
	// return convertView;
	// }
	// }

	public interface OnReplyClickedLsner {
		public void onComtClicked(Long rootId);

		public void onReplyClicked(Long rootId, Long parentId);
	}
}
