package com.park61.moduel.child.bean;

import java.util.List;

/**
 * 
 * 功能描述:成才记录评论
 * 
 * @author xiurui
 * @version
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class GrowCommentVO implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -8905505746639771838L;

	// 主键ID
	private Long id;

	// 成才记录ID
	private Long growId;

	// 用户ID
	private Long userId;

	// 评论用户姓名
	private String userName;

	// 用户头像
	private String userHead;

	// 评论父ID
	private Long parentId;

	// 评论父名称
	private String parentName;

	// 评论内容
	private String content;

	// 评论时间
	private String contentTime;

	// 是否删除(0:否;1:不是)
	private Integer isDelete;

	// 根目录ID
	private Long rootId;

	// 评论类型(0:萌照;1:身高体重)
	private int type;

	// 回复列表
	private List<GrowCommentVO> replyList;

	// Constructors

	/** default constructor */
	public GrowCommentVO() {
	}

	public GrowCommentVO(Long id, Long growId, Long userId, String userName,
			String userHead, Long parentId, String parentName, String content,
			String contentTime, Integer isDelete, Long rootId, int type,
			List<GrowCommentVO> replyList) {
		super();
		this.id = id;
		this.growId = growId;
		this.userId = userId;
		this.userName = userName;
		this.userHead = userHead;
		this.parentId = parentId;
		this.parentName = parentName;
		this.content = content;
		this.contentTime = contentTime;
		this.isDelete = isDelete;
		this.rootId = rootId;
		this.type = type;
		this.replyList = replyList;
	}

	/** minimal constructor */
	public GrowCommentVO(Long id) {
		this.id = id;
	}

	// Property accessors
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGrowId() {
		return growId;
	}

	public void setGrowId(Long growId) {
		this.growId = growId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentTime() {
		return this.contentTime;
	}

	public void setContentTime(String contentTime) {
		this.contentTime = contentTime;
	}

	public Integer getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<GrowCommentVO> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<GrowCommentVO> replyList) {
		this.replyList = replyList;
	}

	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

}