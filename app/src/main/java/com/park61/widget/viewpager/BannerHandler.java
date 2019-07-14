package com.park61.widget.viewpager;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import com.park61.common.set.Log;

public class BannerHandler extends Handler {

	/**
	 * 请求更新显示的View。
	 */
	protected static final int MSG_UPDATE_IMAGE = 1;
	/**
	 * 请求暂停轮播。
	 */
	protected static final int MSG_KEEP_SILENT = 2;
	/**
	 * 请求恢复轮播。
	 */
	protected static final int MSG_BREAK_SILENT = 3;
	/**
	 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
	 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
	 */
	protected static final int MSG_PAGE_CHANGED = 4;

	// 轮播间隔时间
	protected static final long MSG_DELAY = 3000;

	private int currentItem = 0;
	private ViewPager viewPager;

	protected BannerHandler(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.out("receive message " + msg.what);
		Log.out("receive arg1 " + msg.arg1);
		switch (msg.what) {
		case MSG_UPDATE_IMAGE:
			currentItem++;
			viewPager.setCurrentItem(currentItem);
			// 准备下次播放
			this.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
			break;
		case MSG_KEEP_SILENT:
			// 只要不发送消息就暂停了
			break;
		case MSG_BREAK_SILENT:
			this.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
			break;
		case MSG_PAGE_CHANGED:
			// 记录当前的页号，避免播放的时候页面显示不正确。
			currentItem = msg.arg1;
			break;
		default:
			break;
		}
	}
}
