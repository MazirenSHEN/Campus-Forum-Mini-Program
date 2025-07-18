package com.nuoquan.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.enums.StatusEnum;
import com.nuoquan.pojo.Longarticle;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 长文章相关接口
 * Author: Jerrio & wangyu
 * Date: 8/6/2020
 */
@RestController
@Api(value = "长文章相关接口", tags = { "Long-Article-Controller" })
@RequestMapping("/longarticle")
public class LongarticleController extends BasicController{

	/**
	 *
	 * @param page
	 * @param pageSize
	 * @param queryType 0 -- 按"所有"请求, 1 -- 按"关注"请求
	 * @param userId 操作者id
	 * @param selectedTag 选中的标签, 如果为空则正常显示所有文章, 不为空, 则根据传进来的值查询对应文章(拥有该标签的文章)
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查询全部长文章", notes = "查询全部长文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		// dataType 为 String, 应该改为 Integer
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "queryType", value = "排列方式", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "selectedTag", value = "选择的标签", required = false, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "category", value = "分类", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "source", value = "来源", required = false, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryArticles")
	public JSONResult queryArticles(Integer page, Integer pageSize, Integer queryType, String userId, String selectedTag, String category, String source) throws Exception {

		if(page == null) {
			page = 1;
		}

		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = longarticleService.getArticles(page, pageSize, queryType, userId, selectedTag, category, source);

		return JSONResult.ok(result);
	}

	@ApiOperation(value = "查询全部长文章", notes = "查询全部长文章的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping("/getArticlesFromOA")
	public JSONResult getArticlesFromOA(Integer page, Integer pageSize, String userId) throws Exception {

		if(page == null) {
			page = 1;
		}

		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = longarticleService.getArticlesFromOA(page, pageSize, userId);

		return JSONResult.ok(result);
	}

	@ApiOperation(value = "查询全部长文章", notes = "查询全部长文章的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping("/getHeadlines")
	public JSONResult getHeadlines(String userId) throws Exception {

		Object result = longarticleService.getHeadlines(userId);
		return JSONResult.ok(result);
	}


	@ApiOperation(value = "上传长文章", notes = "上传长文章的接口")
	@ApiImplicitParams({
		// uniapp使用formData时，paramType要改成form
		@ApiImplicitParam(name="userId", value="作者id", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTag", value="文章标签", required=false, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTitle", value="文章题目", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleContent", value="文章内容", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="subTitle", value="文章字标题", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="coverImage", value="文章封面", required=false, dataType="String", paramType="form")
	})
	@PostMapping(value="/uploadArticle")
	public JSONResult upload(String userId, String articleTag, String subTitle, String coverImage, String articleTitle, String articleContent) throws Exception {

		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}
		boolean isLegal = false;
		// 保存文章信息到数据库
		Longarticle article = new Longarticle();
		article.setTitle(articleTitle);
		article.setSubTitle(subTitle);
		article.setCoverImage(coverImage);
		article.setContent(articleContent);
		article.setUserId(userId);
		article.setTags(articleTag);
		article.setCreateDate(new Date());
		// 检测内容是否非法
		if (weChatService.msgSecCheck(articleTitle)
			&& weChatService.msgSecCheck(articleTag)
			&& weChatService.msgSecCheck(articleContent)) {
			// 合法
			isLegal = true;
			if (resourceConfig.getAutoCheckArticle()) { //查看是否设置自动过审
				article.setStatus(StatusEnum.READABLE.type);
			}else {
				article.setStatus(StatusEnum.CHECKING.type);
			}
		} else {
			// 非法，尽管非法也保存到数据库
			article.setStatus(StatusEnum.DELETED.type);
		}
		String articleId = longarticleService.saveArticle(article); // 存入数据库

		if (isLegal) {
			return JSONResult.ok(articleId);
		}else {
			return JSONResult.errorMsg("内容违规");
		}
	}

	@ApiOperation(value = "删除长文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/deleteArticle")
	public JSONResult deleteArticle(String articleId, String userId) throws Exception {
		longarticleService.deleteArticle(articleId, userId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "查询我的发布的文章和他人发布的文章", notes = "查看他人时只能查看status为1的, 查询自己时,可显示所有status")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "targetId", value = "目标查询者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryPublishHistory")
	public JSONResult queryPublishHistory(Integer page, Integer pageSize, String userId, String targetId) {

		PagedResult finalResult = new PagedResult();

		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		if(userId.equals(targetId)) {
			// 查询所有状态的文章
			PagedResult result = longarticleService.getAllMyHis(page, pageSize, userId);
			finalResult = result;
		} else if (!userId.equals(targetId)) {
			// 查询他人文章状态为1的文章
			PagedResult result = longarticleService.gerOthersLegalHis(page, pageSize, userId, targetId);
			finalResult = result;
		}

		return JSONResult.ok(finalResult);

	}

	@ApiOperation(value = "按文章 id 查询文章", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getArticleById")
	public JSONResult getArticleById(String articleId, String userId) throws Exception {

		return JSONResult.ok(longarticleService.getArticleById(articleId, userId));
	}

}
