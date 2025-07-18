package com.nuoquan.controller;

import com.nuoquan.enums.*;
import com.nuoquan.pojo.*;
import com.nuoquan.service.NotifyRemindService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nuoquan.enums.PostType.ARTICLE;

/**
 * 社交相关 Controller，如点赞，评论，收藏
 *
 * @author jerrio
 * @date 2020.9.1
 */
@RestController
@Api(value = "社交相关接口", tags = { "SocialController" })
@RequestMapping("/social")
public class SocialController extends BasicController {

	@ApiOperation(value = "用户点赞")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "点赞对象类型", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "点赞对象Id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userLike")
	public JSONResult userLike(String userId, PostType targetType, String targetId) throws Exception {
		String sourceId = socialService.userLike(userId, targetType, targetId);
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (StringUtils.isEmpty(sourceId)) { // 判空，防止用户短时间内多次请求
			return JSONResult.errorMsg("用户已点过赞");
		}
		// 如果不是给自己点赞，插入通知发推送
		String authorId = socialService.getPostAuthor(targetType, targetId).getId();
		if (!userId.equals(authorId)) {
			notifyRemindService.insert(userId,
					NotifyRemindService.SenderAction.LIKE,
					sourceId,
					targetType,
					targetId,
					authorId);
		}
		return JSONResult.ok();
	}

	@ApiOperation(value = "取消点赞对象")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "对象类型", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "对象id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUnLike")
	public JSONResult userUnLike(String userId, PostType targetType, String targetId) throws Exception {
		socialService.userUnLike(userId, targetType, targetId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "保存评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fromUserId", value = "评论人", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "toUserId", value = "被评论人", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "评论对象类型", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "评论对象ID", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "comment", value = "评论内容", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "underCommentId", value = "主评论id", required = false, dataType = "String", paramType = "form")
	})
	@PostMapping("/saveComment")
	public JSONResult saveComment(String fromUserId,
								  String toUserId, // 更方便判断是否为自己点赞，以及查询对方昵称
								  PostType targetType,
								  String targetId,
								  String comment,
								  String underCommentId) throws Exception {
		// 当用户状态为1时才能发表文章
		if (userMapper.selectByPrimaryKey(fromUserId).getState() == 1){
			if (targetType.equals(PostType.COMMENT)) {
				return JSONResult.errorMsg("targetType不能为comment");
			}
			// 内容安全检测（测试时总是出现内容不合法，先暂时注释掉内容安全检测）
			// if (weChatService.msgSecCheck(comment) ) {
			// 插入评论
			String sourceId = socialService.insertComment(fromUserId,
					toUserId,
					targetType,
					targetId,
					comment,
					underCommentId);

			// 如果不是给自己评论，插入通知发推送
			if (!fromUserId.equals(toUserId)) {
				if (!StringUtils.isEmpty(underCommentId)) {
					// 是子评论
					targetType = PostType.COMMENT;
					targetId = underCommentId;
				}
				notifyRemindService.insert(fromUserId,
						NotifyRemindService.SenderAction.COMMENT,
						sourceId,
						targetType,
						targetId,
						toUserId);
			}
			return JSONResult.ok();
		}
		else {
			return JSONResult.errorMsg("您已被禁言");
		}

	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "type", value = "0=按时间查询, 1=按热度查询", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "targetType", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form") })
	@PostMapping("/getMainComments")
	public JSONResult getMainComments(Integer page,
			Integer pageSize,
			Integer type,
			PostType targetType,
			String targetId,
			String userId) throws Exception {

		if (StringUtils.isBlank(targetId)) {
			return JSONResult.errorMsg("targetId can't be null");
		}
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		// type: 0 -- 按时间查询, 1 -- 按热度查询
		PagedResult list = socialService.getMainComments(page,
				pageSize,
				type,
				targetType,
				targetId,
				userId);

		return JSONResult.ok(list);
	}

	/**
	 * @param page
	 * @param pageSize
	 * @param type           0 -- 按时间查询, 1 -- 按热度查询
	 * @param underCommentId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "type", value = "0=按时间查询, 1=按热度查询", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "underCommentId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form") })
	@PostMapping("/getSubComments")
	public JSONResult getSubComments(Integer page, Integer pageSize, Integer type, String underCommentId, String userId)
			throws Exception {

		if (StringUtils.isBlank(underCommentId)) {
			return JSONResult.errorMsg("underCommentId can't be null");
		}
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		// type: 0 -- 按时间查询, 1 -- 按热度查询
		PagedResult reCommentList = socialService.getSubComments(page, pageSize, type, underCommentId, userId);

		return JSONResult.ok(reCommentList);
	}

	@ApiOperation(value = "更改评论状态为unreadable")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "评论对象ID", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "评论对象类型", required = true, dataType = "String", paramType = "form")

	})
	@PostMapping(value = "/fDeleteComment")
	// 特别注意：返回值为1时代表执行此操作的人是文章发布者或评论发布者（此时可以删除评论），返回值为0时无法删除评论
	public JSONResult fDeleteComment(String commentId, String userId, String targetId, PostType targetType)
			throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId) || StringUtils.isBlank(commentId) || StringUtils.isEmpty(commentId)) {
			return JSONResult.errorMsg("Id can't be null");
		}
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (socialService.fDeleteComment(commentId, userId, targetId, targetType) == 1){
			return JSONResult.ok();
		}
		else{
			return JSONResult.errorMsg("你不是文章发布者或评论发布者，无法删除");
		}
	}

	@ApiOperation(value = "收藏文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "收藏对象类型", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "收藏对象Id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userCollect")
	public JSONResult userCollect(String userId, PostType targetType, String targetId) throws Exception {
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		socialService.userCollect(userId, targetType, targetId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "取消收藏文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUncollect")
	public JSONResult userUncollect(String userId, PostType targetType, String targetId) throws Exception {
		String id = socialService.userUncollect(userId, targetType, targetId);
		return JSONResult.ok(id);
	}

	@ApiOperation(value = "查询自己收藏的文章")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping("/queryCollect")
	public JSONResult queryCollect(Integer page, Integer pageSize, String userId) {
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}


		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		// 查询targetId收藏状态为1的文章
		PagedResult result = socialService.queryCollect(page, pageSize, userId);

		return JSONResult.ok(result);

	}

	@ApiOperation(value = "用户阅读文章")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetType", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "targetId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/userRead")
	public JSONResult userRead(String userId, PostType targetType, String targetId) {
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		socialService.userRead(userId, targetType, targetId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "获取所有给我的评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getAllCommentToMe")
	public JSONResult getAllCommentToMe(Integer page, Integer pageSize, String userId) {
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult list = socialService.getAllCommentToMe(page, pageSize, userId);

		return JSONResult.ok(list);

	}

	@ApiOperation(value = "获取所有给我的点赞")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getAllLikeToMe")
	public JSONResult getAllLikeToMe(Integer page, Integer pageSize, String userId) {
		if (!userService.checkIdIsExist(userId)){
			return JSONResult.errorMsg("userId not exists!");
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult list = socialService.getAllLikeToMe(page, pageSize, userId);

		return JSONResult.ok(list);

	}

}
