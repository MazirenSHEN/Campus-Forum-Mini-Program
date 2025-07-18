package com.nuoquan.pojo;

import javax.persistence.*;

public class Headlines {
    /**
     * 对象id
     */
    @Column(name = "target_id")
    private String targetId;

    @Column(name = "target_type")
    private String targetType;

    /**
     * 获取对象id
     *
     * @return target_id - 对象id
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * 设置对象id
     *
     * @param targetId 对象id
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
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
}