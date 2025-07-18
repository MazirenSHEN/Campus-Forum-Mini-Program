package com.nuoquan.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.utils.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@Api(value = "微信相关接口", tags = { "WeChat-Controller" })
@RequestMapping("/wechat")
public class WeChatController extends BasicController {
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", required = false, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "scene", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "width", required = false, dataType = "Number", paramType = "form"),
		@ApiImplicitParam(name = "isHyaline", required = false, dataType = "boolean", paramType = "form") })
	@PostMapping("/getQrcodeUnlimit")
	public JSONResult getQrcodeUnlimit(String page, String scene, Number width, boolean isHyaline) {
		String imgBuffer = weChatService.getQrcodeUnlimit(page, scene, width, isHyaline);
		return JSONResult.ok(imgBuffer);
	}
}
