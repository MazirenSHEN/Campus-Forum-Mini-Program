package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class AuthenticatedUser {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    /**
     * 0 = 已取消认证, 1 = 官方号, 2 = 普通用户认证
     */
    @Column(name = "type")
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
