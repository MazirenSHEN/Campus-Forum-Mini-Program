package com.nuoquan.admin.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nuoquan.controller.BasicController;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.AdminRoleVO;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.service.AdminRoleService;
import com.nuoquan.service.AdminUserService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;

/**
 * 用户Controller
 * 
 * @ClassName: UserController
 * @author fuce
 * @date 2019-11-20 22:35
 */
@Api(value = "用户数据")
@Controller
@RequestMapping("/UserController")
public class AdminUserController extends BasicController {

	private String prefix = "admin/user";

	@Autowired
	private AdminRoleService adminRoleService;

	@Autowired
	private AdminUserService adminUserService;

	/**
	 * 展示跳转页面
	 * 
	 * @param model
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:14:34
	 */
	@GetMapping("/view")
	@RequiresPermissions("system:user:view")
	public String view(ModelMap model) {
		String str = "用户";
		setTitle(model, new TitleVO("列表", str + "管理", true, "欢迎进入" + str + "页面", true, false));
		return prefix + "/list";
	}

	/**
	 * list集合
	 * 
	 * @param tablepar
	 * @param searchText
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:14:40
	 */
	@PostMapping("/list")
	@RequiresPermissions("system:user:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult result = adminUserService.list(tablepar, searchText);
		return result;
	}

	/**
	 * 新增跳转
	 * 
	 * @param modelMap
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:14:51
	 */
	@GetMapping("/add")
	public String add(ModelMap modelMap) {
		// 添加角色列表
		List<AdminRole> roleList = adminRoleService.queryList();
		modelMap.put("tsysRoleList", roleList);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * 
	 * @param user
	 * @param model
	 * @param roles
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:14:57
	 */
//    @Log(title = "用户新增", action = "111")
	@PostMapping("/add")
	@RequiresPermissions("system:user:add")
	@ResponseBody
	public JSONResult add(AdminUser user, Model model,
			@RequestParam(value = "roles", required = false) List<String> roles) {
//		System.out.println("AdminUserController.add.roles:"+roles);
		int b = adminUserService.insertUserRoles(user, roles);
		if (b > 0) {
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("添加失败");
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping("/remove")
	@RequiresPermissions("system:user:remove")
	@ResponseBody
	public JSONResult remove(String ids) {
		int b = adminUserService.deleteByPrimaryKey(ids);
		if (b > 0) {
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("删除失败");
		}
	}

	/**
	 * 检查用户
	 * 
	 * @param tsysUser
	 * @return
	 */
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public int checkLoginNameUnique(AdminUser tsysUser) {
		int b = adminUserService.checkLoginNameUnique(tsysUser);
		if (b > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 修改用户
	 * 
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		// 查询所有角色
		List<AdminRoleVO> roleVos = adminUserService.getUserIsRole(id);
		mmap.put("roleVos", roleVos);
		mmap.put("TsysUser", adminUserService.selectByPrimaryKey(id));

		return prefix + "/edit";
	}

	/**
	 * 修改保存用户
	 */
	@RequiresPermissions("system:user:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(AdminUser tsysUser,
			@RequestParam(value = "roles", required = false) List<String> roles) {
		return adminUserService.updateUserRoles(tsysUser, roles) > 0 ? JSONResult.ok() : JSONResult.errorMsg("保存失败");
	}

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/editPwd/{id}")
	public String editPwd(@PathVariable("id") String id, ModelMap mmap) {
		mmap.put("TsysUser", adminUserService.selectByPrimaryKey(id));
		return prefix + "/editPwd";
	}

	/**
	 * 修改保存用户
	 */
	@RequiresPermissions("system:user:editPwd")
	@PostMapping("/editPwd")
	@ResponseBody
	public JSONResult editPwdSave(AdminUser adminUser) {
		return adminUserService.updateUserPassword(adminUser) > 0 ? JSONResult.ok() : JSONResult.errorMsg("修改密码失败");
	}

}
