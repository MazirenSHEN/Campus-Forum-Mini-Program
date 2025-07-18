package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "admin_permission")
public class AdminPermission {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 授权链接
     */
    private String url;

    /**
     * 是否跳转 0 不跳转 1跳转
     */
    @Column(name = "is_blank")
    private Integer isBlank;

    /**
     * 父节点id
     */
    private String pid;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 是否可见
     */
    private Integer visible;
    
    @Column(name = "child_count")
    private Integer childCount;
    
    /**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取权限名称
     *
     * @return name - 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置权限名称
     *
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取权限描述
     *
     * @return description - 权限描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置权限描述
     *
     * @param description 权限描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取授权链接
     *
     * @return url - 授权链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置授权链接
     *
     * @param url 授权链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取是否跳转 0 不跳转 1跳转
     *
     * @return is_blank - 是否跳转 0 不跳转 1跳转
     */
    public Integer getIsBlank() {
        return isBlank;
    }

    /**
     * 设置是否跳转 0 不跳转 1跳转
     *
     * @param isBlank 是否跳转 0 不跳转 1跳转
     */
    public void setIsBlank(Integer isBlank) {
        this.isBlank = isBlank;
    }

    /**
     * 获取父节点id
     *
     * @return pid - 父节点id
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置父节点id
     *
     * @param pid 父节点id
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * 获取权限标识
     *
     * @return perms - 权限标识
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 设置权限标识
     *
     * @param perms 权限标识
     */
    public void setPerms(String perms) {
        this.perms = perms;
    }

    /**
     * 获取类型   0：目录   1：菜单   2：按钮
     *
     * @return type - 类型   0：目录   1：菜单   2：按钮
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型   0：目录   1：菜单   2：按钮
     *
     * @param type 类型   0：目录   1：菜单   2：按钮
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取菜单图标
     *
     * @return icon - 菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置菜单图标
     *
     * @param icon 菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取排序
     *
     * @return order_num - 排序
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序
     *
     * @param orderNum 排序
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取是否可见
     *
     * @return visible - 是否可见
     */
    public Integer getVisible() {
        return visible;
    }

    /**
     * 设置是否可见
     *
     * @param visible 是否可见
     */
    public void setVisible(Integer visible) {
        this.visible = visible;
    }
    
    public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
}