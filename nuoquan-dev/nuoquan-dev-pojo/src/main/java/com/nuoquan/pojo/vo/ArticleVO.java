package com.nuoquan.pojo.vo;

import java.util.Date;
import java.util.List;

import com.nuoquan.pojo.ArticleImage;

import javax.persistence.Column;

public class ArticleVO {
    private String id;
    private String articleTitle;
    private String userId;
    private String tags;
    private String articleContent;
    private Integer likeNum;
    private Integer dislikeNum;
    private Integer commentNum;
    private Integer collectNum;

    private Integer reportedNum;
    private Integer popularity;
    private Integer status;
    private Date createDate;
    private Byte isAnonymous;
    private Integer viewNum;
    
    private List<ArticleImage> imgList; // 文章图片列表
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
     * @return article_title
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * @param articleTitle
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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
     * @return tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return article_content
     */
    public String getArticleContent() {
        return articleContent;
    }

    /**
     * @param articleContent
     */
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
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

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public List<ArticleImage> getImgList() {
		return imgList;
	}

	public void setImgList(List<ArticleImage> imgList) {
		this.imgList = imgList;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
}