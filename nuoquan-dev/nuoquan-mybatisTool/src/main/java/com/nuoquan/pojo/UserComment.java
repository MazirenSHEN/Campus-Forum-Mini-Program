package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_comment")
public class UserComment {
    private String id;

    /**
     * 评论人
     */
    @Column(name = "from_user_id")
    private String fromUserId;

    @Column(name = "to_user_id")
    private String toUserId;

    /**
     * 评论对象类型，如评论，文章，投票
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 评论对象Id
     */
    @Column(name = "target_id")
    private String targetId;

    private String comment;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "like_num")
    private Integer likeNum;

    @Column(name = "dislike_num")
    private Integer dislikeNum;

    @Column(name = "comment_num")
    private Integer commentNum;

    @Column(name = "under_comment_id")
    private String underCommentId;

    /**
     * 评论消息是否被签收 0: 未签收 1：签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    private Integer status;

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
     * 获取评论人
     *
     * @return from_user_id - 评论人
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     * 设置评论人
     *
     * @param fromUserId 评论人
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * @return to_user_id
     */
    public String getToUserId() {
        return toUserId;
    }

    /**
     * @param toUserId
     */
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    /**
     * 获取评论对象类型，如评论，文章，投票
     *
     * @return target_type - 评论对象类型，如评论，文章，投票
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 设置评论对象类型，如评论，文章，投票
     *
     * @param targetType 评论对象类型，如评论，文章，投票
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * 获取评论对象Id
     *
     * @return target_id - 评论对象Id
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * 设置评论对象Id
     *
     * @param targetId 评论对象Id
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
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
     * @return under_comment_id
     */
    public String getUnderCommentId() {
        return underCommentId;
    }

    /**
     * @param underCommentId
     */
    public void setUnderCommentId(String underCommentId) {
        this.underCommentId = underCommentId;
    }

    /**
     * 获取评论消息是否被签收 0: 未签收 1：签收
     *
     * @return sign_flag - 评论消息是否被签收 0: 未签收 1：签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置评论消息是否被签收 0: 未签收 1：签收
     *
     * @param signFlag 评论消息是否被签收 0: 未签收 1：签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}