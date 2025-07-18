package com.nuoquan.pojo.vo;

import java.util.Date;

public class AuthenticatedUserVO {
    private String id;
    private String userId;
    /**
     * 0 = 已取消认证, 1 = 官方号, 2 = 普通用户认证
     */
    private Integer type;
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
