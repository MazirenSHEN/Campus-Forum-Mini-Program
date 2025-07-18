package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.AdminRole;
import com.nuoquan.utils.MyMapper;

public interface AdminRoleMapper extends MyMapper<AdminRole> {
	
	/**
	 * 根据 userId 查询所属角色
	 * @param uid
	 * @return
	 */
	public List<AdminRole> queryAdminUserRoles(String uid);
}