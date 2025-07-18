package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_report")
public class UserReport {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "create_date")
    private Date createDate;

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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}