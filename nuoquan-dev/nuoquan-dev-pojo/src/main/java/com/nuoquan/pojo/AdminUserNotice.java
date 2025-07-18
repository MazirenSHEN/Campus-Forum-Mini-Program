package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "admin_user_notice")
public class AdminUserNotice {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 公告id
     */
    @Column(name = "notice_id")
    private String noticeId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 0未阅读 1 阅读
     */
    private Integer state;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取公告id
     *
     * @return notice_id - 公告id
     */
    public String getNoticeId() {
        return noticeId;
    }

    /**
     * 设置公告id
     *
     * @param noticeId 公告id
     */
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取0未阅读 1 阅读
     *
     * @return state - 0未阅读 1 阅读
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置0未阅读 1 阅读
     *
     * @param state 0未阅读 1 阅读
     */
    public void setState(Integer state) {
        this.state = state;
    }
}