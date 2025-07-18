package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "admin_user_role")
public class AdminUserRole {
    @Id
    private String id;

    @Column(name = "admin_user_id")
    private String adminUserId;

    @Column(name = "role_id")
    private String roleId;

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
     * @return admin_user_id
     */
    public String getAdminUserId() {
        return adminUserId;
    }

    /**
     * @param adminUserId
     */
    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}