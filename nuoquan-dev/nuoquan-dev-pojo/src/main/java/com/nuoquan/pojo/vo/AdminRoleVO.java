package com.nuoquan.pojo.vo;

import com.nuoquan.pojo.AdminRole;

/**
 * 角色自定义数据
 * @author fuce 
 * @date: 2018年9月8日 上午12:18:59
 */
public class AdminRoleVO extends AdminRole{
	private boolean ischeck;//判断是否又这个权限
	
	public boolean isIscheck() {
		return ischeck;
	}
	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
	
	public AdminRoleVO() {
		super();
	}
	public AdminRoleVO(String id, String name,Boolean ischeck) {
		super.setId(id);
		super.setName(name);
		this.ischeck=ischeck;
	}
	
}
