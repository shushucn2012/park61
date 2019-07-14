package com.park61.moduel.sales.bean;

/**
 * 将两个种类合成一个bean，方便在一行两条的list中显示
 */
public class CateCombine {

	private ProductCategory cateLeft;
	private ProductCategory cateRight;

	public CateCombine() {

	}

	public CateCombine(ProductCategory cateLeft, ProductCategory cateRight) {
		super();
		this.cateLeft = cateLeft;
		this.cateRight = cateRight;
	}

	public ProductCategory getCateLeft() {
		return cateLeft;
	}

	public void setCateLeft(ProductCategory cateLeft) {
		this.cateLeft = cateLeft;
	}

	public ProductCategory getCateRight() {
		return cateRight;
	}

	public void setCateRight(ProductCategory cateRight) {
		this.cateRight = cateRight;
	}

}
