package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "admin_notice")
public class AdminNotice {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 创建人id
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建人name
     */
    @Column(name = "create_username")
    private String createUsername;

    /**
     * 发信时间
     */
    @Column(name = "create_date")
    private Date createDate;

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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取创建人id
     *
     * @return create_id - 创建人id
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人id
     *
     * @param createId 创建人id
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取创建人name
     *
     * @return create_username - 创建人name
     */
    public String getCreateUsername() {
        return createUsername;
    }

    /**
     * 设置创建人name
     *
     * @param createUsername 创建人name
     */
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    /**
     * 获取发信时间
     *
     * @return create_date - 发信时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置发信时间
     *
     * @param createDate 发信时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}