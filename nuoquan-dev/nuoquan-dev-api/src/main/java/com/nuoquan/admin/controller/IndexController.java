package com.nuoquan.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.nuoquan.controller.BasicController;
import com.nuoquan.pojo.vo.TitleVO;

import io.swagger.annotations.Api;

@Controller
@Api(value = "后台管理", tags = { "Admin Stage" })
public class IndexController extends BasicController {
	@GetMapping("/")
	public String index(ModelMap map) {
		String str="前台";
		setTitle(map, new TitleVO("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
//		System.out.print(resourceConfig.getVersion());
    	return "index";
	}
	
//	@GetMapping("/admin/login")
//	public String login(ModelMap map) {
//		String str="前台";
//		setTitle(map, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
////		System.out.print(map);
//    	return "login";
//	}
}
