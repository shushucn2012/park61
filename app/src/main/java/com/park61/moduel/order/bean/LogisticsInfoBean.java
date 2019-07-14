package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 物流信息
 *
 */
public class LogisticsInfoBean implements Serializable{
	private EmsMessage basicMessage;
	private List<OrderTrack> track;

	public EmsMessage getBasicMessage() {
		return basicMessage;
	}

	public void setBasicMessage(EmsMessage basicMessage) {
		this.basicMessage = basicMessage;
	}

	public List<OrderTrack> getTrack() {
		return track;
	}

	public void setTrack(List<OrderTrack> track) {
		this.track = track;
	}
}
