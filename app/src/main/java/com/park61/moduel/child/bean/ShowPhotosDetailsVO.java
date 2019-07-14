package com.park61.moduel.child.bean;

import java.io.Serializable;

public class ShowPhotosDetailsVO implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1055958961777862114L;
	private Long id;// bigint(20)主键id
	private Long showId;// bigint(20)记录的孩子的id
	private String mediaUrl;// 多媒体地址
	private Integer mediaType;// 多媒体类型 0图片

	public ShowPhotosDetailsVO() {
		super();
	}

	public ShowPhotosDetailsVO(Long id, Long showId, String mediaUrl,
			Integer mediaType) {
		super();
		this.id = id;
		this.showId = showId;
		this.mediaUrl = mediaUrl;
		this.mediaType = mediaType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

}
