package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "admin_user")
public class AdminUser {
    @Id
    private String id;

    private String username;

    private String password;

    private String nickname;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * 备注，用于标注账号所属人
     */
    private String description;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
     * 获取备注，用于标注账号所属人
     *
     * @return description - 备注，用于标注账号所属人
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置备注，用于标注账号所属人
     *
     * @param description 备注，用于标注账号所属人
     */
    public void setDescription(String description) {
        this.description = description;
    }
}