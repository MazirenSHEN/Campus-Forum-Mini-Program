package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user_like")
public class UserLike {
    private String id;

    @Column(name = "user_id")
    private String userId;

    /**
     * 点赞对象类型，如文章，投票，评论
     */
    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "create_date")
    private Date createDate;

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
     * 获取点赞对象类型，如文章，投票，评论
     *
     * @return target_type - 点赞对象类型，如文章，投票，评论
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 设置点赞对象类型，如文章，投票，评论
     *
     * @param targetType 点赞对象类型，如文章，投票，评论
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * @return target_id
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
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
}