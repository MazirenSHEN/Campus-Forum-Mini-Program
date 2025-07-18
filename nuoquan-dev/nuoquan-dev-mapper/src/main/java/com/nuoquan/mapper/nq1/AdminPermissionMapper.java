package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.utils.MyMapper;

public interface AdminPermissionMapper extends MyMapper<AdminPermission> {

	/**
	 * 根据用户id获取用户角色的权限
	 * 
	 * @return
	 */
	public List<AdminPermission> findByAdminUserId(String userId);
	
	/**
	 * 查询某角色的权限列表
	 *
	 * @param roleId
	 * @return
	 */
	public List<AdminPermission> queryPermissionByRoleId(String roleId);
	
	@Override
	public AdminPermission selectByPrimaryKey(Object id);

	@Override
	public List<AdminPermission> selectByExample(Object example);
	
	@Override
	public int updateByPrimaryKey(AdminPermission record);
}