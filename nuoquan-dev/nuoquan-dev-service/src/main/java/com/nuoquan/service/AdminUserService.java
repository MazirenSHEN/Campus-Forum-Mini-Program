package com.nuoquan.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.nq1.AdminRoleMapper;
import com.nuoquan.mapper.nq1.AdminUserMapper;
import com.nuoquan.mapper.nq1.AdminUserRoleMapper;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.pojo.AdminUserRole;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.AdminRoleVO;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.StringUtils;

import tk.mybatis.mapper.entity.Example;

/**
 * 系统用户服务类
 * 
 * @ClassName: SysUserService
 * @author fuce
 * @date 2018年8月26日
 *
 */
@Service
public class AdminUserService {
	@Autowired
	private Sid sid;
	// 生成的用户dao
	@Autowired
	private AdminUserMapper adminUserMapper;

	// 生成的角色用户dao
	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;

	// 自动生成的角色dao
	@Autowired
	private AdminRoleMapper adminRoleMapper;

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public PagedResult list(Tablepar tablepar, String username) {
		Example testExample = new Example(AdminUser.class);
		testExample.setOrderByClause("id+0 DESC");
		if (username != null && !"".equals(username)) {
			testExample.createCriteria().andLike("username", "%" + username + "%");
		}

		PageHelper.startPage(tablepar.getPageNum(), tablepar.getPageSize());
		List<AdminUser> list = adminUserMapper.selectByExample(testExample);
		PageInfo<AdminUser> pageInfo = new PageInfo<AdminUser>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfo.getPageNum());
		pagedResult.setTotal(pageInfo.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageInfo.getTotal());
		
		return pagedResult;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey(String ids) {
		List<String> lista = Convert.toListStrArray(ids);
		Example example = new Example(AdminUser.class);
		example.createCriteria().andIn("id", lista);
		return adminUserMapper.deleteByExample(example);
	}

	/**
	 * 添加用户
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertSelective(AdminUser record) {
		return adminUserMapper.insertSelective(record);
	}

	/**
	 * 添加用户跟角色信息
	 * 
	 * @param record
	 * @param roles
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertUserRoles(AdminUser record, List<String> roles) {
		String userId = sid.nextShort();
		record.setId(userId);
		if (StringUtils.isNotEmpty(roles)) {
			for (String roleId : roles) {
				AdminUserRole userRole = new AdminUserRole();
				userRole.setId(sid.nextShort());
				userRole.setAdminUserId(userId);
				userRole.setRoleId(roleId);
				adminUserRoleMapper.insertSelective(userRole);
			}
		}

		// 密码加密
		record.setPassword(md5EncodePassword(record.getPassword()));
		return adminUserMapper.insertSelective(record);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateByPrimaryKeySelective(AdminUser record) {
		record.setPassword(md5EncodePassword(record.getPassword()));
		return adminUserMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 设置用户密码
	 * @param password
	 * @return
	 */
	public String md5EncodePassword(String password) {
    	Md5Hash md5Hash = new Md5Hash(password, "jumbox", 1); //加盐
    	return md5Hash.toString();
    }
	
	/**
	 * 检查用户name
	 * 
	 * @p aram tsysUser
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public int checkLoginNameUnique(AdminUser tsysUser) {
		Example example = new Example(AdminUser.class);
		example.createCriteria().andEqualTo("username", tsysUser.getUsername());
		List<AdminUser> list = adminUserMapper.selectByExample(example);

		return list.size();
	}

	/**
	 * 获取所有权限 并且增加是否有权限字段
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminRoleVO> getUserIsRole(String userid) {
		List<AdminRoleVO> list = new ArrayList<AdminRoleVO>();
		// 查询出我的权限
		List<AdminRole> myRoles = adminRoleMapper.queryAdminUserRoles(userid);
		Example tsysRoleExample = new Example(AdminRole.class);
		// 查询系统所有的角色
		List<AdminRole> tsysRoles = adminRoleMapper.selectByExample(tsysRoleExample);
		if (StringUtils.isNotEmpty(tsysRoles)) {
			for (AdminRole tsysRole : tsysRoles) {
				Boolean isflag = false;
				AdminRoleVO roleVo = new AdminRoleVO(tsysRole.getId(), tsysRole.getName(), isflag);
				for (AdminRole myRole : myRoles) {
					if (tsysRole.getId().equals(myRole.getId())) {
						isflag = true;
						break;
					}
				}
				if (isflag) {
					roleVo.setIscheck(true);
					list.add(roleVo);
				} else {
					list.add(roleVo);
				}
			}
		}
		return list;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserPassword(AdminUser record) {
		record.setPassword(md5EncodePassword(record.getPassword()));
		// 修改用户信息
		return adminUserMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 修改用户信息以及角色信息
	 * 
	 * @param record
	 * @param roles
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserRoles(AdminUser record, List<String> roles) {
		// 先删除这个用户的所有角色
		Example tSysRoleUserExample = new Example(AdminUserRole.class);
		tSysRoleUserExample.createCriteria().andEqualTo("adminUserId", record.getId());
		adminUserRoleMapper.deleteByExample(tSysRoleUserExample);
		if (roles != null) {
			// 添加新的角色信息
			for (String role : roles) {
				AdminUserRole userRole = new AdminUserRole();
				userRole.setId(sid.nextShort());
				userRole.setAdminUserId(record.getId());
				userRole.setRoleId(role);
				adminUserRoleMapper.insertSelective(userRole);
			}
		}
		// 修改用户信息
		return 1;
	}
	
	/**
	 * 根据userId查询用户
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public AdminUser selectByPrimaryKey(String id) {
		return adminUserMapper.selectByPrimaryKey(id);
	}

}
