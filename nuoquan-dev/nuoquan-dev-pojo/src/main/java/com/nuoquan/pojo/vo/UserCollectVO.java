package com.nuoquan.pojo.vo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user_collect")
public class UserCollectVO {
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "create_date")
    private Date createDate;

    private Object target;

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
     * @return target_type
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * @param targetType
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

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}