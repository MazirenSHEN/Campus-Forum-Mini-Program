package com.nuoquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.nuoquan.enums.VoteStatusEnum;
import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.service.UserService;
import com.nuoquan.service.VoteService;
import com.nuoquan.service.WeChatService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "投票相关接口", tags = {"Vote-Controller"})
@RequestMapping("/vote")
public class VoteController extends BasicController{
	
	@Autowired
	private WeChatService weChatService;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "查询全部投票", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryAllVotes")
	public JSONResult queryAllVotes(Integer page, Integer pageSize, String userId) throws Exception {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		PagedResult result = voteService.getAllVotes(page, pageSize, userId);
		
		return JSONResult.ok(result);
	}
	
	@ApiOperation(value = "上传投票", notes = "上传投票的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="作者id", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="voteTitle", value="投票题目", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="voteContent", value="投票内容", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="duration", value="持续时间", required=true, dataType="Integer", paramType="form"),
		@ApiImplicitParam(name="optionContent", value="投票选项", required=true, dataType="String", paramType="form")
	})
	@PostMapping(value="uploadVote")
	public JSONResult uploadVote(String userId, String voteTitle, String voteContent, String optionContent, Integer duration) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("UserId can't be null");
		}
		if (StringUtils.isEmpty(optionContent) || StringUtils.isBlank(optionContent)) {
			return JSONResult.errorMsg("voteContent can't be null");
		}
		boolean isLegal = false;
		// 保存投票信息到数据库
		Vote vote = new Vote();
		vote.setVoteTitle(voteTitle);
		vote.setVoteContent(voteContent);
		vote.setUserId(userId);
		vote.setDurationTime(duration);
		Date date = new Date();
		vote.setCreateDate(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, duration);
		Date expiryDate = ca.getTime();
		vote.setExpiryDate(expiryDate);
		
		// 检测内容是否合法
		if (weChatService.msgSecCheck(voteTitle)
				&& weChatService.msgSecCheck(voteContent)
				&& weChatService.msgSecCheck(optionContent)) {
			// 合法
			isLegal = true;
			vote.setStatus(VoteStatusEnum.CHECKING.type);
		} else {
			// 非法, 存入数据库, 状态为不可见
			vote.setStatus(VoteStatusEnum.UNQUALIFIED.type);
		}
		
		// save to database
		String voteId = voteService.saveVote(vote);


		Integer optionIndex = 0;
		String[] optionList = optionContent.split("#");
		for (String opt : optionList){
			VoteOption voteOption = new VoteOption();
			voteOption.setVoteId(voteId);
			voteOption.setContent(opt);
			voteOption.setImg("");
			voteOption.setIndex(optionIndex);
			optionIndex++;
			voteService.saveVoteOption(voteOption);
		}
		
		if (isLegal) {
			return JSONResult.ok(voteId);
		} else {
			return JSONResult.errorMap(voteId);
		}
	}
	

	@PostMapping(value = "/uploadVoteImg")
	public JSONResult uploadVoteImg(String userId, String voteId, String order, @ApiParam(value="file", required=true) MultipartFile file) throws Exception {
		VoteImage voteImage = new VoteImage();
		
		if (file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 保存图片
			String fileSpace = resourceConfig.getFileSpace();
			// 获取文件后缀
			String fileName = file.getOriginalFilename();
			String[] strList = fileName.split("\\.");
			
			// 把顺序 order.原后缀 作为文件名
			String newFileName = order + "." + strList[strList.length-1];
			// 保存到数据库中的相对路径
			String uploadPathDB = "/" + userId + "/vote" + "/" + voteId + "/" + newFileName;
			// 文件上传的最终保存路径
			String finalVideoPath = "";
			
			if (StringUtils.isNotBlank(newFileName)) {
				finalVideoPath = fileSpace + uploadPathDB;
				uploadFile(file, finalVideoPath);
				Integer imageOrder = Integer.valueOf(order);
				voteImage.setImagePath(uploadPathDB);
				voteImage.setVoteId(voteId);
				voteImage.setImageOrder(imageOrder);
			}
			voteService.saveVoteImages(voteImage);
		}
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为unvoteable")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/endVote")
	public JSONResult endVote(String voteId) throws Exception {
		voteService.fDeleteVote(voteId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为votable")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/passVote")
	public JSONResult passVote(String voteId) throws Exception {
		voteService.passVote(voteId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为unqualified")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/banVote")
	public JSONResult banVote(String voteId) throws Exception {
		voteService.banVote(voteId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "选择某个选项")
	@PostMapping(value="/selectOption")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "optionId", value = "选项id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "投票者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	public JSONResult selectOption(String userId, String optionId, String voteId) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("UserId can't be null!");
		} else if (StringUtils.isBlank(optionId)) {
			return JSONResult.errorMsg("OptionId can't be null!");
		} else if (StringUtils.isBlank(voteId)) {
			return JSONResult.errorMsg("VoteId can't be null!");
		}

		//红墙投票业务，临时规则
		int leftVoteNum = voteService.userLeftVote(voteId, optionId, userId);
		if (leftVoteNum<=0){
			return JSONResult.errorMsg("今日票数已用完，剩余票数："+leftVoteNum);
		}

		voteService.selectOption(userId, optionId, voteId);
		return JSONResult.ok(voteService.getVoteById(voteId, userId));
	}

	@ApiOperation(value = "用户剩余票数")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping(value="/userLeftVote")
	public JSONResult userLeftVote(String voteId, String optionId, String userId) throws Exception {
		//红墙投票业务，临时规则
		int leftVoteNum = voteService.userLeftVote(voteId, optionId, userId);
		return JSONResult.ok(leftVoteNum);
	}
	
	@ApiOperation(value = "查询单个投票", notes = "在确认选择后刷新单个投票")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),

	})
	@PostMapping("/getVoteById")
	public JSONResult getVoteById(String voteId, String userId) throws Exception {
		
		VoteVO result = voteService.getVoteById(voteId, userId);
		
		return JSONResult.ok(result);
	}
	
	@ApiOperation(value = "查询我的发布的投票和他人发布的投票", notes = "查看他人时只能查看status为1的, 查询自己时,可显示所有status")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "targetId", value = "目标查询者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryPublishedVoteHistory")
	public JSONResult queryPublishedVoteHistory(Integer page, Integer pageSize, String userId, String targetId) {
		
		PagedResult finalResult = new PagedResult();
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		if(userId.equals(targetId)) {
			// 查询自己的投票历史
			PagedResult result = voteService.getAllMyHisVote(page, pageSize, userId);
			finalResult = result;
		} else if (!userId.equals(targetId)) {
			// 查询他人的投票历史
			PagedResult result = voteService.getOtherslegalHisVote(page, pageSize, userId, targetId);
			finalResult = result;
		}
		
		
		return JSONResult.ok(finalResult);
	}
}










