package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品种类
 * 
 * @author shushucn2012
 * 
 */
public class CateBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5274646351261240631L;
	private Long id;
	private String imgUrl;
	private String name;
	private List<CateBean> cateList;

	public CateBean() {
		super();
	}

	public CateBean(Long id, String imgUrl, String name) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CateBean> getCateList() {
		return cateList;
	}

	public void setCateList(List<CateBean> cateList) {
		this.cateList = cateList;
	}

	public static ArrayList<CateBean> genDatas() {
		ArrayList<CateBean> bigcateList = new ArrayList<CateBean>();

		// 添加品牌类别
		CateBean bigCateBean1 = new CateBean(1l, "", "品牌");
		List<CateBean> childCateList = new ArrayList<CateBean>();
		childCateList
				.add(new CateBean(
						11l,
						"http://b1.hucdn.com/upload/brand/1406/30/94223219010032_200x100.png",
						"好孩子"));
		childCateList
				.add(new CateBean(
						12l,
						"http://b1.hucdn.com/upload/brand/1505/20/18201098854685_200x100.jpg",
						"BBG"));
		childCateList
				.add(new CateBean(
						13l,
						"http://b1.hucdn.com/upload/brand/1603/03/99616527229794_200x100.png",
						"巴喜尼儿"));
		childCateList
				.add(new CateBean(
						14l,
						"http://b1.hucdn.com/upload/brand/1408/12/25822586350973_200x100.png",
						"奈足"));
		childCateList
				.add(new CateBean(
						15l,
						"http://b1.hucdn.com/upload/brand/1409/01/49879909550973_200x100.jpg",
						"CE"));
		childCateList
				.add(new CateBean(
						16l,
						"http://b1.hucdn.com/upload/brand/1503/11/71884834049794_200x100.jpg",
						"笨笨鼠"));
		bigCateBean1.setCateList(childCateList);
		bigcateList.add(bigCateBean1);

		// 添加年龄类别
		CateBean bigCateBean2 = new CateBean(2l, "", "年龄");
		List<CateBean> childCateList2 = new ArrayList<CateBean>();
		childCateList2
				.add(new CateBean(
						21l,
						"http://b1.hucdn.com/upload/item/1511/02/64626434628359_500x500.jpg!320x320.jpg",
						"孕期/产后"));
		childCateList2
				.add(new CateBean(
						22l,
						"http://b1.hucdn.com/upload/show/1603/07/49856138814091_750x350.jpg!hphb.jpg",
						"0-1岁"));
		childCateList2
				.add(new CateBean(
						23l,
						"http://b1.hucdn.com/upload/show/1603/07/16851611936750_750x350.jpg!hphb.jpg",
						"1-3岁"));
		childCateList2
				.add(new CateBean(
						24l,
						"http://b1.hucdn.com/upload/show/1603/03/02879847025791_750x350.jpg!hphb.jpg",
						"3-6岁"));
		childCateList2
				.add(new CateBean(
						25l,
						"http://b1.hucdn.com/upload/show/1603/07/17420829864213_750x350.jpg!hphb.jpg",
						"6-9岁"));
		childCateList2
				.add(new CateBean(
						26l,
						"http://b1.hucdn.com/upload/show/1603/07/50172460274213_750x350.jpg!hphb.jpg",
						"9岁以上"));
		bigCateBean2.setCateList(childCateList2);
		bigcateList.add(bigCateBean2);

		// 添加玩具类别
		CateBean bigCateBean3 = new CateBean(3l, "", "玩具");
		List<CateBean> childCateList3 = new ArrayList<CateBean>();
		childCateList3
				.add(new CateBean(
						31l,
						"http://b1.hucdn.com/upload/category/1512/01/39198710456826_120x120.jpg!60x60.jpg",
						"益智玩具"));
		childCateList3
				.add(new CateBean(
						32l,
						"http://b1.hucdn.com/upload/category/1511/04/20838147526826_120x120.jpg!60x60.jpg",
						"电动遥控"));
		childCateList3
				.add(new CateBean(
						33l,
						"http://b1.hucdn.com/upload/category/1511/04/20779815336826_120x120.jpg!60x60.jpg",
						"积木拼插"));
		childCateList3
				.add(new CateBean(
						34l,
						"http://b1.hucdn.com/upload/category/1511/16/60543628256826_120x120.jpg!60x60.jpg",
						"毛绒布艺"));
		childCateList3
				.add(new CateBean(
						35l,
						"http://b1.hucdn.com/upload/category/1512/01/39261734626826_120x120.jpg!60x60.jpg",
						"健身玩具"));
		childCateList3
				.add(new CateBean(
						36l,
						"http://b1.hucdn.com/upload/category/1601/13/71900331086826_120x120.jpg!60x60.jpg",
						"模型玩具"));
		childCateList3
				.add(new CateBean(
						37l,
						"http://b1.hucdn.com/upload/category/1601/13/71934632486826_120x120.jpg!60x60.jpg",
						"动漫玩具"));
		childCateList3
				.add(new CateBean(
						38l,
						"http://b1.hucdn.com/upload/category/1601/13/71981722926826_120x120.jpg!60x60.jpg",
						"DIY玩具"));
		bigCateBean3.setCateList(childCateList3);
		bigcateList.add(bigCateBean3);

		return bigcateList;
	}

}
