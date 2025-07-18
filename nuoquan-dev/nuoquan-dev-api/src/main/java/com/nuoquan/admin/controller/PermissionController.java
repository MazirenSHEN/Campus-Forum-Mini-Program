package com.nuoquan.admin.controller;

import java.util.List;

import com.nuoquan.controller.BasicController;
import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.service.AdminPermissionService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限Controller
 * 
 * @author fuce
 * @date: 2018年9月2日 下午8:08:21
 */
@Controller
@Api(value = "权限")
@RequestMapping("/PermissionController")
public class PermissionController extends BasicController {

	// 跳转页面参数
	private String prefix = "admin/permission";

	@Autowired
	private AdminPermissionService adminPermissionService;

	/**
	 * 权限列表展示
	 * 
	 * @param model
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午3:43:51
	 */
	@GetMapping("/view")
	@RequiresPermissions("system:permission:view")
	public String view(ModelMap model) {
		String str = "权限";
		setTitle(model, new TitleVO("列表", str + "管理", true, "欢迎进入" + str + "页面", true, false));
		return prefix + "/list";
	}

	/**
	 * 权限列表
	 * 
	 * @param tablepar
	 * @param searchText 搜索字符
	 * @return
	 */
	@PostMapping("/list")
	@RequiresPermissions("system:permission:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult pagedResult = adminPermissionService.list(tablepar, searchText);
		return pagedResult;
	}

	/**
	 * 权限列表
	 * 
	 * @param tablepar
	 * @param searchText 搜索字符
	 * @return
	 */
	@PostMapping("/list2")
	@ResponseBody
	public Object list2(Tablepar tablepar, String searchText) {
		List<AdminPermission> page = adminPermissionService.list2(searchText);
		return page;
	}

	/**
	 * 新增权限
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 权限添加
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping("/add")
	@RequiresPermissions("system:permission:add")
	@ResponseBody
	public JSONResult add(AdminPermission role) {
		int b = adminPermissionService.insertSelective(role);
		if (b > 0) {
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("添加失败");
		}
	}

	/**
	 * 删除权限
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping("/remove")
	@RequiresPermissions("system:permission:remove")
	@ResponseBody
	public JSONResult remove(String ids) {
		int b = adminPermissionService.deleteByPrimaryKey(ids);
		if (b == 1) {
			return JSONResult.ok();
		} else if (b == -1) {
			return JSONResult.errorMsg("该权限有子权限，请先删除子权限");
		} else if (b == -2) {
			return JSONResult.errorMsg("该权限绑定了角色，请解除角色绑定");
		} else {
			return JSONResult.errorMsg("其他原因，删除 权限失败");
		}
	}

	/**
	 * 检查权限
	 * 
	 * @param TsysPermission
	 * @return
	 */
	@PostMapping("/checkNameUnique")
	@ResponseBody
	public int checkNameUnique(AdminPermission TsysPermission) {
		int b = adminPermissionService.checkNameUnique(TsysPermission);
		if (b > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 检查权限URL
	 * 
	 * @param tsysUser
	 * @return
	 */
	@PostMapping("/checkURLUnique")
	@ResponseBody
	public int checkURLUnique(AdminPermission tsysPermission) {
		int b = adminPermissionService.checkURLUnique(tsysPermission);
		if (b > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 检查权限perms字段
	 * 
	 * @param tsysUser
	 * @return
	 */
	@PostMapping("/checkPermsUnique")
	@ResponseBody
	public int checkPermsUnique(AdminPermission tsysPermission) {
		int b = adminPermissionService.checkPermsUnique(tsysPermission);
		if (b > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 修改权限
	 * 
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") String id, ModelMap mmap) {
		// 获取自己的权限信息
		AdminPermission mytsysPermission = adminPermissionService.selectByPrimaryKey(id);
		// 获取父权限信息
		AdminPermission pattsysPermission = adminPermissionService.selectByPrimaryKey(mytsysPermission.getPid());
		mmap.put("TsysPermission", mytsysPermission);
		mmap.put("pattsysPermission", pattsysPermission);
		return prefix + "/edit";
	}

	/**
	 * 修改保存权限
	 */
	@RequiresPermissions("system:permission:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(AdminPermission TsysPermission) {
		return adminPermissionService.updateByPrimaryKey(TsysPermission) > 0 ? JSONResult.ok()
				: JSONResult.errorMsg("修改失败");
	}

	/**
	 * 获取所有的转换成bootstarp的权限数据
	 * 
	 * @return
	 */
	@GetMapping("/getTreePerm")
	@ResponseBody
	public JSONResult getbooBootstrapTreePerm() {

		return JSONResult.ok(adminPermissionService.getbooBootstrapTreePerm(null));
	}

	/**
	 * 根据角色id获取bootstarp 所有打勾权限
	 * 
	 * @param roleId 角色id集合
	 * @return
	 */
	@PostMapping("/getCheckPrem")
	@ResponseBody
	public JSONResult getCheckPrem(String roleId) {

		return JSONResult.ok(adminPermissionService.getCheckPrem(roleId));
	}

	/**
	 * 跳转到菜单树页面
	 * 
	 * @return
	 */
	@GetMapping("/tree")
	public String Tree() {
		return prefix + "/tree";
	}

	/**
	 * 获取菜单树
	 * 
	 * @param pid 父id【没用到】
	 * @return
	 */
	@PostMapping("/tree/{pid}")
	@ResponseBody
	public JSONResult Tree(@PathVariable("pid") String pid) {
		return JSONResult.ok(adminPermissionService.getbooBootstrapTreePerm(null));
	}

}
