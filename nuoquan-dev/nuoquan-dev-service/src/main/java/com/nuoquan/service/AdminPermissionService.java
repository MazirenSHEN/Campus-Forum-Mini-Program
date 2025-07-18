package com.nuoquan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.nq1.AdminPermissionMapper;
import com.nuoquan.mapper.nq1.AdminRolePermissionMapper;
import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.pojo.AdminRolePermission;
import com.nuoquan.pojo.admin.BootstrapTree;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.StringUtils;

import tk.mybatis.mapper.entity.Example;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminPermissionService {

	@Autowired
	private Sid sid;

	// 权限mapper
	@Autowired
	private AdminPermissionMapper adminPermissionMapper;

	// 权限角色关联表
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
		Example example = new Example(AdminPermission.class);
		example.setOrderByClause("order_num  is null  ASC,order_num  ASC");
		if (searchText != null && !"".equals(searchText)) {
			example.createCriteria().andLike("name", "%" + searchText + "%");
		}

		PageHelper.startPage(tablepar.getPageNum(), tablepar.getPageSize());
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		PageInfo<AdminPermission> pageInfo = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfo.getPageNum());
		pagedResult.setTotal(pageInfo.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageInfo.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminPermission> list2(String searchText) {
		Example example = new Example(AdminPermission.class);
		example.setOrderByClause("order_num  is null  ASC,order_num  ASC");
		if (searchText != null && !"".equals(searchText)) {
			example.createCriteria().andLike("name", "%" + searchText + "%");
		}
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据id删除权限
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey(String ids) {
		// 转成集合
		System.out.println("AdminPermissionService.deleteByPrimaryKey:" + ids);
		List<String> lista = Convert.toListStrArray(ids);

		// 判断是否有角色关联
		Example rolePermissionExample = new Example(AdminRolePermission.class);
		rolePermissionExample.createCriteria().andIn("permissionId", lista);
		List<AdminRolePermission> adminRolePermission = adminRolePermissionMapper
				.selectByExample(rolePermissionExample);
		if (adminRolePermission.size() > 0) {// 有角色外键
			return -2;
		}

		// 判断是否有子集
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andIn("id", lista);
		List<AdminPermission> permissions = adminPermissionMapper.selectByExample(example);
		boolean lag = false;
		for (AdminPermission permission : permissions) {
			System.out.println("permission.getChildCount():"+permission.getChildCount());
			if (permission.getChildCount() != null && permission.getChildCount() > 0) {
				lag = true;
			}
		}
		if (lag) {// 有子集 无法删除
			return -1;
		} else {// 删除操作
			int i = adminPermissionMapper.deleteByExample(example);
			if (i > 0) {// 删除成功
				return 1;
			} else {// 删除失败
				return 0;
			}

		}
	}

	/**
	 * 插入权限
	 * 
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertSelective(AdminPermission record) {
		// 添加雪花主键id
		record.setId(sid.nextShort());
		// 判断为目录的时候添加父id为0
		if (record != null && record.getType() == 0) {
			record.setPid("1");
		}
		// 默认设置不跳转
		if (record.getIsBlank() == null) {
			record.setIsBlank(0);
		}
		return adminPermissionMapper.insertSelective(record);
	}

	/**
	 * 根据主键id进行查询
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public AdminPermission selectByPrimaryKey(String id) {

		return adminPermissionMapper.selectByPrimaryKey(id);
	}

	/**
	 * 更新权限, 根据主键更新属性不为null的值
	 * 
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateByPrimaryKeySelective(AdminPermission record) {
		// 默认设置不跳转
		if (record.getIsBlank() == null) {
			record.setIsBlank(0);
		}
		return adminPermissionMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新权限, 根据主键更新实体全部字段，null值会被更新
	 * 
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateByPrimaryKey(AdminPermission record) {
		// 默认设置不跳转
		if (record.getIsBlank() == null) {
			record.setIsBlank(0);
		}
		return adminPermissionMapper.updateByPrimaryKey(record);
	}

	/**
	 * 检查权限名字
	 * 
	 * @param tsysUser
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public int checkNameUnique(AdminPermission adminPermission) {
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andEqualTo("name", adminPermission.getName());
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		return list.size();
	}

	/**
	 * 检查权限URL
	 * 
	 * @param tsysUser
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public int checkURLUnique(AdminPermission AdminPermission) {
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andEqualTo("url", AdminPermission.getUrl());
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		return list.size();
	}

	/**
	 * 检查权限perms字段
	 * 
	 * @param tsysUser
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public int checkPermsUnique(AdminPermission AdminPermission) {
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andEqualTo("perms", AdminPermission.getPerms());
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		return list.size();
	}

	/**
	 * 根据父id 以及类型查询权限子集
	 * 
	 * @param pid
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminPermission> queryPid(String pid, int type) {
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andEqualTo("pid", pid).andEqualTo("type", type);
		return adminPermissionMapper.selectByExample(example);
	}

	/**
	 * 获取转换成bootstarp的权限数据
	 * 
	 * @return
	 */
	public BootstrapTree getbooBootstrapTreePerm(String userid) {
		List<BootstrapTree> treeList = new ArrayList<BootstrapTree>();
		List<AdminPermission> menuList = getRolePermissionByUserId(userid);
		treeList = getbooBootstrapTreePerm(menuList, "0");
		if (treeList != null && treeList.size() == 1) {
			return treeList.get(0);
		}
		return new BootstrapTree("菜单", "fa fa-home", "", "-1", "###", 0, treeList, "", 0);
	}

	/**
	 * 获取树
	 * 
	 * @param menuList
	 * @param parentId
	 * @return
	 */
	private static List<BootstrapTree> getbooBootstrapTreePerm(List<AdminPermission> menuList, String parentId) {
		List<BootstrapTree> treeList = new ArrayList<>();
		List<BootstrapTree> childList = null;
		for (AdminPermission p : menuList) {
			p.setPid(p.getPid() == null || p.getPid().trim().equals("") ? "0" : p.getPid());
			if (p.getPid().trim().equals(parentId)) {
//				System.out.println(p.getChildCount());
				if (p.getChildCount() != null && p.getChildCount() > 0) {
					childList = getbooBootstrapTreePerm(menuList, String.valueOf(p.getId()));
				}
				BootstrapTree bootstrapTree = new BootstrapTree(p.getName(), p.getIcon(), "", String.valueOf(p.getId()),
						p.getUrl(), p.getIsBlank(), childList, p.getPerms(), p.getVisible());
				treeList.add(bootstrapTree);
				childList = null;
			}
		}
		return treeList.size() > 0 ? treeList : null;
	}

	/**
	 * 根据用户id获取用户角色的权限，如果用户为null 获取所有权限
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminPermission> getRolePermissionByUserId(String userid) {
//		System.out.println("getRolePermissionByUserId.userid:"+userid+" isEmpty:"+StringUtils.isEmpty(userid));
		if (StringUtils.isEmpty(userid)) {
			Example example = new Example(AdminPermission.class);
			example.setOrderByClause("order_num asc");
			List<AdminPermission> result = adminPermissionMapper.selectByExample(example);
			return result;
		}
		return adminPermissionMapper.findByAdminUserId(userid);
	}

	/**
	 * 获取角色已有的Bootstarp权限
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public BootstrapTree getCheckPrem(String roleid) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置选中
		map.put("checked", true);
		// 设置展开
		// map.put("expanded", true);
		// 获取角色的权限
		List<AdminPermission> myAdminPermissions = adminPermissionMapper.queryPermissionByRoleId(roleid);
		// 获取所有的权限
		BootstrapTree sysPermissions = getbooBootstrapTreePerm(null);
		iterationCheckPre(sysPermissions, myAdminPermissions, map);
		return sysPermissions;

	}

	/**
	 * 循环迭代获取所有权限
	 * 
	 * @param pboostrapTree
	 * @param myAdminPermissions
	 * @param map
	 */
	public void iterationCheckPre(BootstrapTree pboostrapTree, List<AdminPermission> myAdminPermissions,
			Map<String, Object> map) {
		if (null != pboostrapTree) {
			if (ifpermissions(myAdminPermissions, pboostrapTree)) {
				pboostrapTree.setState(map);
			}
			List<BootstrapTree> bootstrapTreeList = pboostrapTree.getNodes();
			if (null != bootstrapTreeList && !bootstrapTreeList.isEmpty()) {
				for (BootstrapTree bootstrapTree : bootstrapTreeList) {
					if (ifpermissions(myAdminPermissions, bootstrapTree)) {// 菜单栏设置
						bootstrapTree.setState(map);
					}
					// 检查子节点
					iterationCheckPre(bootstrapTree, myAdminPermissions, map);
				}
			}
		}
	}

	/**
	 * 判断我是否有权限
	 * 
	 * @param myAdminPermissions
	 * @param sysPermission
	 */
	public Boolean ifpermissions(List<AdminPermission> myAdminPermissions, BootstrapTree sysPermission) {
		for (AdminPermission myAdminPermission : myAdminPermissions) {
			if (sysPermission.getId().equals(myAdminPermission.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据权限字段查询是否存在
	 * 
	 * @param perms
	 * @return
	 * @author fuce
	 * @Date 2019年9月1日 上午2:06:31
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public Boolean queryLikePerms(String perms) {
		Example example = new Example(AdminPermission.class);
		example.createCriteria().andLike("perms", "%gen:" + perms + "%");
		List<AdminPermission> list = adminPermissionMapper.selectByExample(example);
		return list.size() > 0;
	}

	/**
	 * 查询某角色的权限列表
	 *
	 * @param roleId
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminPermission> queryPermissionByRoleId(String roleId) {
		return adminPermissionMapper.queryPermissionByRoleId(roleId);
	}
}
