package com.nuoquan.pojo.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import com.nuoquan.pojo.ArticleImage;

public class LongarticleVO {
    @Id
	private String id;
    private String title;
    @Column(name = "sub_title")
    private String subTitle;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "user_id")
    private String userId;
    /**
     * 是否原创，0=原创，1=非原创
     */
    private Integer original;
    /**
     * 保留字段
     */
    private String tags;
    @Column(name = "like_num")
    private Integer likeNum;
    @Column(name = "dislike_num")
    private Integer dislikeNum;
    @Column(name = "comment_num")
    private Integer commentNum;
    @Column(name = "collect_num")
    private Integer collectNum;

    @Column(name = "reported_num")
    private Integer reportedNum;
    private Integer popularity;
    /**
     * 0 = unreadable, 1 = readable, 2 = checking
     */
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    /**
     * 0 = not anonymous, 1 = anonymous
     */
    @Column(name = "is_anonymous")
    private Byte isAnonymous;
    /**
     * 浏览量
     */
    @Column(name = "view_num")
    private Integer viewNum;
    /**
     * 内容来源
     */
    private String source;
    private String content;
    /**
     * 跳转超链接
     */
    private String link;

    /**
     * 是否跳转超链接 0：否 1：是
     */
    @Column(name = "is_jump")
    private Integer isJump;
    
    private Boolean isLike; // 用户是否喜欢该文章
    private Boolean isCollect; // 用户是否收藏该文章
    
    private String nickname;
    private String faceImg;
    private String faceImgThumb;
    private List<String> tagList;

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    private Integer authType;
    
    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return sub_title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取是否原创，0=原创，1=非原创
     *
     * @return original - 是否原创，0=原创，1=非原创
     */
    public Integer getOriginal() {
        return original;
    }

    /**
     * 设置是否原创，0=原创，1=非原创
     *
     * @param original 是否原创，0=原创，1=非原创
     */
    public void setOriginal(Integer original) {
        this.original = original;
    }

    /**
     * 获取保留字段
     *
     * @return tags - 保留字段
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置保留字段
     *
     * @param tags 保留字段
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return like_num
     */
    public Integer getLikeNum() {
        return likeNum;
    }

    /**
     * @param likeNum
     */
    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    /**
     * @return dislike_num
     */
    public Integer getDislikeNum() {
        return dislikeNum;
    }

    /**
     * @param dislikeNum
     */
    public void setDislikeNum(Integer dislikeNum) {
        this.dislikeNum = dislikeNum;
    }

    /**
     * @return comment_num
     */
    public Integer getCommentNum() {
        return commentNum;
    }

    /**
     * @param commentNum
     */
    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    /**
     * @return collect_num
     */
    public Integer getCollectNum() {
        return collectNum;
    }

    /**
     * @param collectNum
     */
    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getReportedNum() {
        return reportedNum;
    }

    public void setReportedNum(Integer reportedNum) {
        this.reportedNum = reportedNum;
    }

    /**
     * @return popularity
     */
    public Integer getPopularity() {
        return popularity;
    }

    /**
     * @param popularity
     */
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    /**
     * 获取0 = unreadable, 1 = readable, 2 = checking
     *
     * @return status - 0 = unreadable, 1 = readable, 2 = checking
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0 = unreadable, 1 = readable, 2 = checking
     *
     * @param status 0 = unreadable, 1 = readable, 2 = checking
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取0 = not anonymous, 1 = anonymous
     *
     * @return is_anonymous - 0 = not anonymous, 1 = anonymous
     */
    public Byte getIsAnonymous() {
        return isAnonymous;
    }

    /**
     * 设置0 = not anonymous, 1 = anonymous
     *
     * @param isAnonymous 0 = not anonymous, 1 = anonymous
     */
    public void setIsAnonymous(Byte isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    /**
     * 获取浏览量
     *
     * @return view_num - 浏览量
     */
    public Integer getViewNum() {
        return viewNum;
    }

    /**
     * 设置浏览量
     *
     * @param viewNum 浏览量
     */
    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    /**
     * 获取内容来源
     *
     * @return source - 内容来源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置内容来源
     *
     * @param source 内容来源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getIsJump() {
        return isJump;
    }

    public void setIsJump(Integer isJump) {
        this.isJump = isJump;
    }
}
