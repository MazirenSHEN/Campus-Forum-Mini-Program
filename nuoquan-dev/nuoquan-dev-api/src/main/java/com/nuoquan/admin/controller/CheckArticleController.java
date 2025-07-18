package com.nuoquan.admin.controller;

import com.nuoquan.enums.PostType;
import com.nuoquan.pojo.vo.UserCommentVO;
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
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.service.ArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;

import java.util.List;

/**
 * 文章人工审核
 *
 * @ClassName: UserController
 * @author fuce
 * @date 2019-11-20 22:35
 */
@Api(value = "文章人工审核")
@Controller
@RequestMapping("/CheckArticle")
public class CheckArticleController extends BasicController {

	private String prefix = "admin/article";

	@Autowired
	private ArticleService articleService;

	/**
	 * 展示跳转页面
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/view")
	@RequiresPermissions("system:article:view")
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
	@RequiresPermissions("system:article:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult result = articleService.list(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}

	@PostMapping("/listCheckOnly")
	@RequiresPermissions("system:article:list")
	@ResponseBody
	public Object listCheckOnly(Tablepar tablepar, String searchText) {
		PagedResult result = articleService.listCheckOnly(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}

	/**
	 * 修改文章状态
	 *
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		ArticleVO article = articleService.getArticleById(id, null);
		mmap.put("Article", article);
		return prefix + "/edit";
	}

	/**
	 * 修改文章状态
	 *
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */

	@GetMapping("/check/{id}")
	public String check(@PathVariable("id") String id, ModelMap mmap) {
		ArticleVO article = articleService.getArticleById(id, null);
		mmap.put("Article", article);
		return prefix + "/check";
	}
	/**
	 * 修改文章状态
	 *
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */

//	@GetMapping("/comment/{id}")
//	public Object comment(Tablepar tablepar, @PathVariable("id") String id, ModelMap mmap) {
//		Integer page = tablepar.getPageNum();
//		Integer pageSize = tablepar.getPageSize();
//		PagedResult commentResult = socialService.getCommentsByTargetId(page, pageSize, PostType.ARTICLE, id);
//		mmap.put("Comment", commentResult);
//		return prefix + "/comment";
//	}

	/**
	 * 删除评论
	 */
	@PostMapping("/delete")
	@ResponseBody
	public JSONResult deleteComment(String id, String targetId) {
		return socialService.fDeleteComment(id, "AdminUser", targetId, PostType.ARTICLE) > 0 ? JSONResult.ok() : JSONResult.errorMsg("删除失败");
	}

	/**
	 * 修改文章状态
	 *
	 * @param ids 文章id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/batchEdit/{ids}")
	public String batchEdit(@PathVariable("ids") String ids, ModelMap mmap) {
		return prefix + "/batchEdit";
	}

	/**
	 * 修改保存文章状态
	 */
	@RequiresPermissions("system:article:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(String id, Integer status) {
		return articleService.updateArticleStatus(id, status) > 0 ? JSONResult.ok() : JSONResult.errorMsg("保存失败");
	}

	/**
	 * 查询自动审核
	 */
	@RequiresPermissions("system:article:list")
	@PostMapping("/getAutoCheck")
	@ResponseBody
	public Boolean getAutoCheck() {
		return resourceConfig.getAutoCheckArticle();
	}

	/**
	 * 开启/关闭自动审核
	 */
	@RequiresPermissions("system:article:edit")
	@PostMapping("/toggleAutoCheck")
	@ResponseBody
	public JSONResult toggleAutoCheck(Boolean status) {
		resourceConfig.setAutoCheckArticle(status);
		return JSONResult.ok();
	}

}
