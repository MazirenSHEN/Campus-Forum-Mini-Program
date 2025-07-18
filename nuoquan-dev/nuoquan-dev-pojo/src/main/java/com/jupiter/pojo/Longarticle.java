package com.jupiter.pojo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Longarticle {
    @Id
    private String id;

    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "cover_image")
    private String coverImage;

    /**
     * 保留字段 为空
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 是否原创，0=原创，1=非原创; 保留字段 为1
     */
    private Integer original;

    /**
     * 保留字段
     */
    private String tags;

    /**
     * 保留字段 0 = unreadable, 1 = readable, 2 = checking
     */
    private Integer status;

    @Column(name = "create_date")
    private String createDate;

    /**
     * 内容来源
     */
    private String source;

    private String link;

    private String content;

    private Integer signFlag;
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
     * 获取保留字段 为空
     *
     * @return user_id - 保留字段 为空
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置保留字段 为空
     *
     * @param userId 保留字段 为空
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取是否原创，0=原创，1=非原创; 保留字段 为1
     *
     * @return original - 是否原创，0=原创，1=非原创; 保留字段 为1
     */
    public Integer getOriginal() {
        return original;
    }

    /**
     * 设置是否原创，0=原创，1=非原创; 保留字段 为1
     *
     * @param original 是否原创，0=原创，1=非原创; 保留字段 为1
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
     * 获取保留字段 0 = unreadable, 1 = readable, 2 = checking
     *
     * @return status - 保留字段 0 = unreadable, 1 = readable, 2 = checking
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置保留字段 0 = unreadable, 1 = readable, 2 = checking
     *
     * @param status 保留字段 0 = unreadable, 1 = readable, 2 = checking
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return create_date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
     * @return link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
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

    public Integer getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}