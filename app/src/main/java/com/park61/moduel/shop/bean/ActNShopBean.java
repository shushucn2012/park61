package com.park61.moduel.shop.bean;

import java.io.Serializable;

public class ActNShopBean implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -42654374320996671L;

	private Long id;
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
