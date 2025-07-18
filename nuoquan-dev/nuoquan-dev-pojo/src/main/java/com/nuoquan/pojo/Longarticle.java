package com.nuoquan.pojo;

import javax.persistence.*;
import java.util.Date;

public class Longarticle {
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
     * 分类
     */
    private String category;

    /**
     * 内容来源
     */
    private String source;

    /**
     * 跳转超链接
     */
    private String link;

    /**
     * 是否跳转超链接 0：否 1：是
     */
    @Column(name = "is_jump")
    private Integer isJump;

    /**
     * 富文本格式，包含文字和图片/视频链接
     */
    private String content;

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
     * @return cover_image
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     * @param coverImage
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
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
     * 获取分类
     *
     * @return category - 分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置分类
     *
     * @param category 分类
     */
    public void setCategory(String category) {
        this.category = category;
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
     * 获取跳转超链接
     *
     * @return link - 跳转超链接
     */
    public String getLink() {
        return link;
    }

    /**
     * 设置跳转超链接
     *
     * @param link 跳转超链接
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 获取是否跳转超链接 0：否 1：是
     *
     * @return is_jump - 是否跳转超链接 0：否 1：是
     */
    public Integer getIsJump() {
        return isJump;
    }

    /**
     * 设置是否跳转超链接 0：否 1：是
     *
     * @param isJump 是否跳转超链接 0：否 1：是
     */
    public void setIsJump(Integer isJump) {
        this.isJump = isJump;
    }

    /**
     * 获取富文本格式，包含文字和图片/视频链接
     *
     * @return content - 富文本格式，包含文字和图片/视频链接
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置富文本格式，包含文字和图片/视频链接
     *
     * @param content 富文本格式，包含文字和图片/视频链接
     */
    public void setContent(String content) {
        this.content = content;
    }
}