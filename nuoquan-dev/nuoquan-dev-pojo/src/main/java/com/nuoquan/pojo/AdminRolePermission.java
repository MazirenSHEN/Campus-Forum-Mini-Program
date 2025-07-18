package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "admin_role_permission")
public class AdminRolePermission {
    @Id
    private String id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private String permissionId;

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
     * 获取角色id
     *
     * @return role_id - 角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取权限id
     *
     * @return permission_id - 权限id
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * 设置权限id
     *
     * @param permissionId 权限id
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}