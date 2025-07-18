package com.nuoquan.service;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.nq1.AdminRoleMapper;
import com.nuoquan.mapper.nq1.AdminRolePermissionMapper;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.AdminRolePermission;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;

@Service
public class AdminRoleService {

	@Autowired
	private Sid sid;

	// 角色mapper
	@Autowired
	private AdminRoleMapper adminRoleMapper;

	// 自动生成的权限角色映射mapper
	@Autowired
	private AdminRolePermissionMapper adminRolePermissionMapper;

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public PagedResult list(Tablepar tablepar, String searchText) {
		Example example = new Example(AdminRole.class);
		example.setOrderByClause("id+0 DESC");
		if (searchText != null && !"".equals(searchText)) {
			example.createCriteria().andLike("name", "%" + searchText + "%");
		}

		PageHelper.startPage(tablepar.getPageNum(), tablepar.getPageSize());
		List<AdminRole> list = adminRoleMapper.selectByExample(example);
		PageInfo<AdminRole> pageInfo = new PageInfo<AdminRole>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfo.getPageNum());
		pagedResult.setTotal(pageInfo.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageInfo.getTotal());
		
		return pagedResult;
	}

	/**
	 * 查询全部角色集合
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminRole> queryList() {
		Example example = new Example(AdminRole.class);
		return adminRoleMapper.selectByExample(example);
	}

	/**
	 * 删除角色和角色所有权限
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey(String ids) {
//		System.out.println("adminRoleService.deleteByPrimaryKey.ids:"+ids);
	
		List<String> lista = Convert.toListStrArray(ids);
		// 先删除角色下面的所有权限
		Example rolePermissionExample = new Example(AdminRolePermission.class);
		rolePermissionExample.createCriteria().andIn("roleId", lista);
		adminRolePermissionMapper.deleteByExample(rolePermissionExample);
		// 在删除角色
		Example example = new Example(AdminRole.class);
		example.createCriteria().andIn("id", lista);
		return adminRoleMapper.deleteByExample(example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertSelective(AdminRole record) {
		// 添加雪花主键id
		record.setId(sid.nextShort());
		return adminRoleMapper.insertSelective(record);
	}

	/**
	 * 添加角色绑定权限
	 * 
	 * @param record 角色信息
	 * @param prem   权限id集合
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertRoleAndPrem(AdminRole record, String prem) {
		// 添加雪花主键id
		String roleId = sid.nextShort();
		record.setId(roleId);
		// 添加权限
		List<String> prems = Convert.toListStrArray(prem);
		for (String premId : prems) {
			AdminRolePermission rolePermission = new AdminRolePermission();
			rolePermission.setId(sid.nextShort());
			rolePermission.setRoleId(roleId);
			rolePermission.setPermissionId(premId);
			adminRolePermissionMapper.insertSelective(rolePermission);
		}
		return adminRoleMapper.insertSelective(record);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public AdminRole selectByPrimaryKey(String id) {
		return adminRoleMapper.selectByPrimaryKey(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateByPrimaryKeySelective(AdminRole record) {
		return adminRoleMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 修改用户角色 以及下面绑定的权限
	 * 
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoleAndPrem(AdminRole record, String prem) {
		// 先删除角色下面的所有权限
		Example rolePermissionExample = new Example(AdminRolePermission.class);
		rolePermissionExample.createCriteria().andEqualTo("roleId", record.getId());
		adminRolePermissionMapper.deleteByExample(rolePermissionExample);
		// 添加权限关联
		List<String> prems = Convert.toListStrArray(prem);
		for (String pre : prems) {
			AdminRolePermission rolePermission = new AdminRolePermission();
			rolePermission.setId(sid.nextShort());
			rolePermission.setRoleId(record.getId());
			rolePermission.setPermissionId(pre);
			adminRolePermissionMapper.insertSelective(rolePermission);
		}

		return adminRoleMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 检查角色name
	 * 
	 * @param tsysUser
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public int checkNameUnique(AdminRole adminRole) {
		Example example = new Example(AdminRole.class);
		example.createCriteria().andEqualTo("name", adminRole.getName());
		List<AdminRole> list = adminRoleMapper.selectByExample(example);
		return list.size();
	}

	/**
	 * 根据用户id查询角色, 一个用户可能多个角色
	 * 
	 * @param userid
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminRole> queryAdminUserRoles(String userId) {
		return adminRoleMapper.queryAdminUserRoles(userId);
	}

}
