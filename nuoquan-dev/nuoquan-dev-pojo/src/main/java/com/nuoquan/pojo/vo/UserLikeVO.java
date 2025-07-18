/**
 * 消息提醒-用户点赞消息的VO
 */
package com.nuoquan.pojo.vo;

import java.util.Date;

public class UserLikeVO {

	private String id;
	private String userId;
	private String articleId;
	private String commentId;
	private Date createDate;
	private Integer signFlag;
	private String authorId;
	
	// 点赞人信息（source）
	private String nickname;
    private String faceImg;
    private String faceImgThumb;

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	private Integer authType;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(Integer signFlag) {
		this.signFlag = signFlag;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getFaceImg() {
		return faceImg;
	}

	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}

	public String getFaceImgThumb() {
		return faceImgThumb;
	}

	public void setFaceImgThumb(String faceImgThumb) {
		this.faceImgThumb = faceImgThumb;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
