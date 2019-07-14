package com.park61.moduel.shop;

public interface IShopMainView {
	/**
	 * 停止列表进度条
	 */
	public void listStopLoadView();

	/**
	 * 将上下拉控件设为到底
	 */
	public void setPullToRefreshViewEnd();
	
	public void setPullToRefreshViewBoth();
}
