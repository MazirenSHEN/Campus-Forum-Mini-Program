package com.nuoquan.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.service.UserService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.MD5Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 当前业务暂时不用这个 controller
 * @author jerrio
 * @date 2/8/20
 */
@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegistLoginController extends BasicController{
	
	@ApiOperation(value="用户注册", notes="用户注册的接口")
	@PostMapping("/regist")
	// 因为出传进来是一个json对象，需要RequestBody
	public JSONResult regist(@RequestBody User user) throws Exception  {

		//1. 判断用户名和密码不为空
		if (StringUtils.isEmpty(user.getNickname()) || StringUtils.isEmpty(user.getPassword())) {
			return JSONResult.errorMsg("用户名和密码不能为空");
		}
		user.setNickname(user.getNickname());
		// 会提示 unhandled exception type Exception， 在regist处throw
		user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
		user.setFollowNum(0);
		user.setEmail("x.nottingham.edu.cn");
		user.setFaceImg(null);
		user.setCreateDate(new Date());
		userService.saveUser(user);
//		//2. 判断用户名是否存在
//		boolean nicknameIsExist = userService.checkNicknameIsExist(user.getNickname());
//		//3. 保存用户，注册信息
//		if (!nicknameIsExist) {
//			user.setNickname(user.getNickname());
//			// 会提示 unhandled exception type Exception， 在regist处throw
//			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
//			user.setFollowNum(0);
//			user.setEmail("x.nottingham.edu.cn");
//			user.setFaceImg(null);
//			user.setCreateDate(new Date());
//			userService.saveUser(user);
//		} else {
//			return JSONResult.errorMsg("用户名已存在，请换一个试试");
//		}
		// user.setPassword(null);
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		UserVO userVO = ConvertUserToUserVO(user);
		return JSONResult.ok(userVO);
	}
	
	@ApiOperation(value="用户登录", notes="用户登录的接口")
	@PostMapping("/login")
	public JSONResult login(@RequestBody User user) throws Exception {
		
		String nickname = user.getNickname();
		String password = user.getPassword();
		
		//1. 判断用户名和密码不为空
		if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
			return JSONResult.ok("用户名和密码不能为空");
		}
		//2. 判断用户是否存在
		User userResult = userService.checkUserForLogin(nickname, MD5Utils.getMD5Str(user.getPassword()));
		//3. 返回信息
		if (userResult != null) {
			UserVO userVO = ConvertUserToUserVO(userResult);
			return JSONResult.ok(userVO);
		} else {
			return JSONResult.errorMsg("用户名或密码错误，请重试...");
		}
	}
}
