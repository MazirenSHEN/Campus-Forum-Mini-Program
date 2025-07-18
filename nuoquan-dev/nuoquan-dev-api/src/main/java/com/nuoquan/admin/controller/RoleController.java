package com.nuoquan.admin.controller;

import javax.servlet.http.HttpServletRequest;

import com.nuoquan.controller.BasicController;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.admin.BootstrapTree;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.service.AdminPermissionService;
import com.nuoquan.service.AdminRoleService;
import com.nuoquan.shiro.util.ShiroUtils;
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
 * 角色Controller
 */
@Api(value = "用户角色")
@Controller
@RequestMapping("/RoleController")
public class RoleController extends BasicController {

	// 跳转页面参数
	private String prefix = "admin/role";
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	@Autowired
	private AdminPermissionService adminPermissionService;
	/**
	 * 展示页面
	 * 
	 * @param model
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:01:58
	 */
	@GetMapping("/view")
	@RequiresPermissions("system:role:view")
	public String view(ModelMap model) {
		String str = "角色";
		setTitle(model, new TitleVO("列表", str + "管理", true, "欢迎进入" + str + "页面", true, false));
		return prefix + "/list";
	}

	/**
	 * 角色列表
	 * 
	 * @param tablepar
	 * @param searchText 搜索字符
	 * @return
	 */
	@PostMapping("/list")
	@RequiresPermissions("system:user:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult result = adminRoleService.list(tablepar, searchText);
		return result;
	}

	/**
	 * 新增角色
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 角色添加
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping("/add")
	@RequiresPermissions("system:user:add")
	@ResponseBody
	public JSONResult add(AdminRole role, String prem) {
		int b = adminRoleService.insertRoleAndPrem(role, prem);
		if (b > 0) {
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("添加失败");
		}
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping("/remove")
	@RequiresPermissions("system:user:remove")
	@ResponseBody
	public JSONResult remove(String ids) {
		int b = adminRoleService.deleteByPrimaryKey(ids);
		if (b > 0) {
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("删除失败");
		}
	}

	/**
	 * 检查角色
	 * 
	 * @param AdminRole
	 * @return
	 */
	@PostMapping("/checkNameUnique")
	@ResponseBody
	public int checkNameUnique(AdminRole tsysRole) {
		int b = adminRoleService.checkNameUnique(tsysRole);
		if (b > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 修改角色
	 * 
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") String id, ModelMap mmap) {
		mmap.put("adminRole", adminRoleService.selectByPrimaryKey(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存角色
	 */
	@RequiresPermissions("system:user:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(AdminRole tsysRole, String prem, HttpServletRequest request) {
		int i = adminRoleService.updateRoleAndPrem(tsysRole, prem);
		if (i > 0) {
			// 大于0刷新权限
			ShiroUtils.clearCachedAuthorizationInfo();
			// 获取菜单栏
			BootstrapTree bootstrapTree = adminPermissionService.getbooBootstrapTreePerm(ShiroUtils.getUserId());
			request.getSession().setAttribute("bootstrapTree", bootstrapTree);
		}

		return i > 0 ? JSONResult.ok()
				: JSONResult.errorMsg("修改失败");
	}

	@RequiresPermissions("system:user:edit")
	@PostMapping("/editUserState")
	@ResponseBody
	public JSONResult editUserState(AdminRole tsysRole, String prem, HttpServletRequest request) {
		int i = adminRoleService.updateRoleAndPrem(tsysRole, prem);
		if (i > 0) {
			// 大于0刷新权限
			ShiroUtils.clearCachedAuthorizationInfo();
			// 获取菜单栏
			BootstrapTree bootstrapTree = adminPermissionService.getbooBootstrapTreePerm(ShiroUtils.getUserId());
			request.getSession().setAttribute("bootstrapTree", bootstrapTree);
		}

		return i > 0 ? JSONResult.ok()
				: JSONResult.errorMsg("修改失败");
	}
}
