package com.nuoquan.admin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nuoquan.controller.BasicController;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.service.VoteService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;

/**
 * 文章人工审核
 * 
 * @ClassName: UserController
 * @author fuce
 * @date 2019-11-20 22:35
 */
@Api(value = "文章人工审核")
@Controller
@RequestMapping("/CheckVote")
public class CheckVoteController extends BasicController {

	private String prefix = "admin/vote";
	
	@Autowired
	private VoteService voteService;

	/**
	 * 展示跳转页面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/view")
	@RequiresPermissions("system:vote:view")
	public String view(ModelMap model) {
		String str = "审核";
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
	@RequiresPermissions("system:vote:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult result = voteService.list(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}
	
	@PostMapping("/listCheckOnly")
	@RequiresPermissions("system:vote:list")
	@ResponseBody
	public Object listCheckOnly(Tablepar tablepar, String searchText) {	
		PagedResult result = voteService.listCheckOnly(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}

	/**
	 * 修改投票状态
	 * 
	 * @param id 投票id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		VoteVO vote = voteService.getVoteById(id, null);
		mmap.put("Vote", vote);
		return prefix + "/edit";
	}
	
	/**
	 * 修改投票状态
	 * 
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/batchEdit/{ids}")
	public String batchEdit(@PathVariable("ids") String ids, ModelMap mmap) {
		return prefix + "/batchEdit";
	}
	
	/**
	 * 修改保存投票状态
	 */
	@RequiresPermissions("system:vote:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(String id, Integer status) {
		return voteService.updateVoteStatus(id, status) > 0 ? JSONResult.ok() : JSONResult.errorMsg("保存失败");
	}

}
