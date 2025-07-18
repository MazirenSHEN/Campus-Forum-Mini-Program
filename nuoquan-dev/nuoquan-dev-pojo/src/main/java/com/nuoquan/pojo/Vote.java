package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

public class Vote {
    @Id
    private String id;

    @Column(name = "vote_title")
    private String voteTitle;

    /**
     * 0 = single choice, 1 = multiple choice
     */
    @Column(name = "vote_type")
    private Integer voteType;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "comment_num")
    private Integer commentNum;

    @Column(name = "sum_vote")
    private Integer sumVote;

    @Column(name = "sum_user")
    private Integer sumUser;

    /**
     * 0 = unreadable, 1 = readable, 2 = checking
     */
    private Integer status;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "duration_time")
    private Integer durationTime;

    /**
     * 浏览量
     */
    @Column(name = "view_num")
    private Integer viewNum;

    @Column(name = "vote_content")
    private String voteContent;

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
     * @return vote_title
     */
    public String getVoteTitle() {
        return voteTitle;
    }

    /**
     * @param voteTitle
     */
    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    /**
     * 获取0 = single choice, 1 = multiple choice
     *
     * @return vote_type - 0 = single choice, 1 = multiple choice
     */
    public Integer getVoteType() {
        return voteType;
    }

    /**
     * 设置0 = single choice, 1 = multiple choice
     *
     * @param voteType 0 = single choice, 1 = multiple choice
     */
    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
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
     * @return sum_vote
     */
    public Integer getSumVote() {
        return sumVote;
    }

    /**
     * @param sumVote
     */
    public void setSumVote(Integer sumVote) {
        this.sumVote = sumVote;
    }

    /**
     * @return sum_user
     */
    public Integer getSumUser() {
        return sumUser;
    }

    /**
     * @param sumUser
     */
    public void setSumUser(Integer sumUser) {
        this.sumUser = sumUser;
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
     * @return expiry_date
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return duration_time
     */
    public Integer getDurationTime() {
        return durationTime;
    }

    /**
     * @param durationTime
     */
    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
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
     * @return vote_content
     */
    public String getVoteContent() {
        return voteContent;
    }

    /**
     * @param voteContent
     */
    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }
}