package com.nuoquan.controller;

import java.util.*;

import com.nuoquan.mapper.nq1.AuthenticatedUserMapper;
import com.nuoquan.pojo.*;
import com.nuoquan.pojo.vo.*;
import com.nuoquan.service.AuthenticatedUserService;
import com.nuoquan.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.email.EmailTool;
import com.nuoquan.enums.ReputeWeight;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;

@RestController
@Api(value = "User workflow logic")
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private Sid sid;

	@Autowired
	private EmailTool emailTool;

	@Autowired
	private FastDFSClient fastDFSClient;

	@Autowired
	private AuthenticatedUserMapper authenticatedUserMapper;

	@Autowired 
	private AuthenticatedUserService authenticatedUserService;
	// @ApiOperation(value = "获取某用户feed流", notes = "")
	// @ApiImplicitParams({
	// @ApiImplicitParam(name = "page", value = "页数", required = true, dataType =
	// "String", paramType = "form"),
	// @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false,
	// dataType = "String", paramType = "form"),
	// @ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType
	// = "String", paramType = "form"),
	// @ApiImplicitParam(name = "extend", value = "扩展字段", required = false, dataType
	// = "String", paramType = "form"),
	// })
	// @PostMapping("/getFeed")
	// public JSONResult getFeed(Integer page, Integer pageSize, String userId,
	// String extend) throws Exception {
	//
	// if(page == null) {
	// page = 1;
	// }
	//
	// if(pageSize == null) {
	// pageSize = PAGE_SIZE;
	// }
	//
	// return JSONResult.ok();
	// }

	/**
	 * Description 上传文件到 fastdfs 文件服务器 的实例方法。
	 * PS: fastdfs 为非结构化储存，暂不使用，本方法仅供参考。
	 *
	 * @author jerrio
	 */
	@Deprecated
	@ApiOperation(value = "An example for user uploading face image")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/uploadFaceFastdfs")
	public JSONResult uploadFaceFastdfs(String userId,
			@ApiParam(value = "face image", required = true) MultipartFile file)
			throws Exception {

		// 删除用户旧头像
		deleteOldFace(userId);

		String url = fastDFSClient.uploadBase64(file);
		System.out.println("/uploadFace: url=" + url);

		// 分割url
		// url example: "hiao30j02joajd156.png" -> "hiao30j02joajd156_80x80.png"
		String thumb = "_80x80.";
		String[] arr = url.split("\\.");
		String thumbImgUrl = arr[0] + thumb + arr[1];

		// 更新用户头像
		User user = new User();
		user.setId(userId);
		user.setFaceImg(url);
		user.setFaceImgThumb(thumbImgUrl);

		UserVO userVO = userService.updateUserInfo(user);
		return JSONResult.ok(userVO);
	}

	@Deprecated // fastdfs删除文件示例
	public void deleteOldFace(String userId) {
		try {
			UserVO user = userService.getUserById(userId);
			String url = user.getFaceImg();
			String thumbUrl = user.getFaceImgThumb();
			fastDFSClient.deleteFile(fdfsGroupName, url);
			fastDFSClient.deleteFile(fdfsGroupName, thumbUrl);
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
			System.out.println("Delete failed");
		}
	}

	// 老版本更新用户头像功能，已停用
	// @Deprecated
	// @ApiOperation(value = "User uploads face image")
	// @ApiImplicitParams({
	// @ApiImplicitParam(name = "userId", required = true, dataType = "String",
	// paramType = "form"), })
	// @PostMapping("/uploadFaceOld")
	// public JSONResult uploadFaceOld(String userId, @ApiParam(value = "face
	// image", required = true) MultipartFile file)
	// throws Exception {
	//
	// if (StringUtils.isNoneBlank(userId) && file != null) {
	// // 判断是否超出大小限制
	// if (file.getSize() > MAX_IMAGE_SIZE) {
	// return JSONResult.errorException("Uploaded file size exceed server's limit
	// (10MB)");
	// }
	// // 暂时不用删除用户旧头像 @Jerrio
	//
	// // 上传文件
	// String fileName = file.getOriginalFilename();
	// if (StringUtils.isNotBlank(fileName)) {
	// // 保存到数据库中的相对路径
	// String uploadPathDB = "/" + userId + "/face" + "/" + fileName;
	// String fileSpace = resourceConfig.getFileSpace(); // 文件保存空间地址
	// // 文件上传的最终保存路径
	// String finalVideoPath = fileSpace + uploadPathDB;
	// // 保存图片
	// uploadFile(file, finalVideoPath); // 调用 BasicController 里的方法
	//
	// User user = new User();
	// user.setId(userId);
	// user.setFaceImg(uploadPathDB);
	// userService.updateUserInfo(user);
	// return JSONResult.ok(uploadPathDB);
	// }else {
	// return JSONResult.errorMsg("File name is blank");
	// }
	// }else {
	// return JSONResult.errorMsg("Upload error");
	// }
	// }

	@ApiOperation(value = "User uploads face image to COS")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId, @ApiParam(value = "face image", required = true) MultipartFile file)
			throws Exception {

		if (StringUtils.isNoneBlank(userId) && file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 暂时不用删除用户旧头像 @Jerrio
			// 上传文件
			String uploadPathDB = resourceService.uploadImg(file);

			User user = new User();
			user.setId(userId);
			user.setFaceImg(uploadPathDB);
			user.setFaceImgThumb(uploadPathDB);
			userService.updateUserInfo(user);
			return JSONResult.ok(uploadPathDB);
		} else {
			return JSONResult.errorMsg("Upload error");
		}
	}

	@ApiOperation(value = "Be the fans")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/follow")
	public JSONResult follow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(fanId) || StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		userService.saveUserFanRelation(userId, fanId);

		// 被关注者影响力++
		userService.updateReputation(userId, ReputeWeight.FOLLOW.weight, 1);

		return JSONResult.ok("Follow success");
	}

	@ApiOperation(value = "Don't be the fans")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/dontFollow")
	public JSONResult dontFollow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(fanId) || StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		userService.deleteUserFanRelation(userId, fanId);

		// 被关注者影响力--
		userService.updateReputation(userId, ReputeWeight.FOLLOW.weight, -1);

		return JSONResult.ok("Cancel follow success");
	}

	@ApiOperation(value = "Query a user's fans and follow lists")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/queryFansAndFollow")
	public JSONResult queryFansAndFollow(String userId, String myId) {

		List<UserVO> fansList = userService.queryUserFans(userId, myId);
		List<UserVO> followList = userService.queryUserFollow(userId, myId);
		FansFollow fansAndFollow = new FansFollow(fansList, followList);

		return JSONResult.ok(fansAndFollow);
	}

	@ApiOperation(value = "Wechat first login or change profile")
	@PostMapping("/updateUser")
	public JSONResult updateUser(@RequestBody User userData) throws Exception {
		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userData.getId())) {
			return JSONResult.errorMsg("该账号为空");
		}
		User user = new User();
		UserVO userVO; // 返回前端对象
		// 2. 判断用户名是否存在以及昵称是否合法
		boolean isNicknameValid = userService.CheckNicknameIsLegal(userData.getNickname());
		if (!isNicknameValid) {
			return JSONResult.errorException("用户名不合法，请换一个试试");
		}
		boolean isIdExist = userService.checkIdIsExist(userData.getId());
		// 3. 注册信息
		if (!isIdExist) {
			// 新用户，只添加用户id（openId）头像和昵称
			user.setId(userData.getId());
			user.setNickname("用户" + sid.userInitNickname());
			user.setFaceImg(RandomInitFaceImg.getRandomPath(userData.getFaceImg()));
			user.setFaceImgThumb(RandomInitFaceImg.getRandomPath(userData.getFaceImg()));
			user.setPassword("ChangeMe");
			user.setFollowNum(0);
			user.setFansNum(0);
			user.setCreateDate(new Date());
			userVO = userService.saveUserDirectly(user);
		} else {
			// 3.1 修改信息
			// boolean isNicknameExist =
			// userService.checkNicknameIsExist(userData.getNickname());
			// if (isNicknameExist){
			// return JSONResult.errorMsg("用户名已存在，请换一个试试");
			// }
			user.setId(userData.getId()); // 用作索引
			user.setNickname(userData.getNickname());
			user.setFaceImg(userData.getFaceImg());
			user.setFaceImgThumb(userData.getFaceImgThumb());
			user.setEmail(EncryptUtils.base64Decode(userData.getEmail()));
			user.setDegree(userData.getDegree());
			user.setGraduationYear(userData.getGraduationYear());
			user.setGender(userData.getGender());
			user.setMajor(userData.getMajor());
			user.setSignature(userData.getSignature());
			userVO = userService.updateUserInfo(user);
		}
		// user.setPassword(null);
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		return JSONResult.ok(userVO);
	}

	@ApiOperation(value = "query user's info")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/queryUser")
	public JSONResult queryUser(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		UserVO userVO = userService.getUserById(userId);
		return JSONResult.ok(userVO);
	}

	@ApiOperation(value = "update the latest login time of user.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/updateLatestLoginTime")
	public JSONResult updateLatestLoginTime(String userId) throws Exception {

		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("该账号为空");
		}
		// 2. 判断用户名是否存在
		boolean isIdExist = userService.checkIdIsExist(userId);
		if (isIdExist) {
			userService.updateLatestLoginTime(userId);
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("用户不存在");
		}

	}

	@ApiOperation(value = "query the user's info and whether I followed him")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/queryUserWithFollow")
	public JSONResult queryUserWithFollow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return JSONResult.errorMsg("Id can not be null.");
		}
		UserVO userVO = userService.getUserById(userId);
		userVO.setFollow(userService.queryIfFollow(userId, fanId));
		return JSONResult.ok(userVO);
	}

	@ApiOperation(value = "Get the user's unread chat msg")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedMsg")
	public JSONResult getUnsignedMsg(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}

		// 查询列表
		List<ChatMsg> unreadMsgList = userService.getUnsignedChat(userId);

		return JSONResult.ok(unreadMsgList);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedNotif")
	public JSONResult getUnsignedNotif(String userId) {
		List<NotifyRemindVO> notifyVOs = notifyRemindService.getUnsignedNotif(1, 1, userId);
		return JSONResult.ok(notifyVOs);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/batchSignNotif")
	public JSONResult batchSignNotif(String notifIds) {
		String[] list = notifIds.split(",");
		List<String> notifList = new ArrayList<>();
		for (String id : list) {
			if (StringUtils.isNotBlank(id)) {
				notifList.add(id);
			}
		}
		notifyRemindService.batchSign(notifList);
		return JSONResult.ok();
	}

	/**
	 * 微信登陆获取openId
	 *
	 * @param code
	 * @param iv            加密算法的初始向量
	 * @param encryptedData 加密数据，好像可以根据官方提供的方式自行解密，还没试验
	 * @return
	 */
	@Value("${WXConst.appId}")
	public String appId;
	@Value("${WXConst.appSecret}")
	public String appSecret;
	@Value("${WXConst.wxGetOpenIdUrl}")
	public String wxGetOpenIdUrl;

	@ApiOperation(value = "get Wechat UserInfo")
	@PostMapping("/getWxUserInfo")
	public JSONResult getWxUserInfo(String code, String iv, String encryptedData, String nickname, String faceImg)
			throws Exception {

		// 获取openid
		Map<String, String> requestUrlParam = new HashMap<String, String>();
		requestUrlParam.put("appid", appId); // 开发者设置中的appId  
		requestUrlParam.put("secret", appSecret); // 开发者设置中的appSecret  
		requestUrlParam.put("js_code", code); // 小程序调用wx.login返回的code 

		// ObjectMapper objectMapper = new ObjectMapper();
		// objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);

		// 发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session
		// 接口获取openid用户唯一标识  
		String res = UrlUtil.sendPost(wxGetOpenIdUrl, requestUrlParam);
		WxRes wxRes = JsonUtils.jsonToPojo(res, WxRes.class);
		// System.out.println("res:" + res);
		// System.out.println(wxRes.getOpenid());

		User user = new User();
		String id = wxRes.getOpenid();
		if (id == null) {
			return JSONResult.errorMsg("无法获取微信openId");
		} else {
			id = EncryptUtils.md5Encode(id);
		}
		user.setId(id);
		user.setNickname(nickname);
		user.setFaceImg(faceImg);
		UserVO userVO = wxLogin(user);
		if (userVO == null) {
			return JSONResult.errorMsg("空用户");
		}
		return JSONResult.ok(userVO);
	}

	/**
	 * 把微信 login 业务从 updateUser 里剥离出来
	 *
	 * @param userData
	 * @return
	 * @throws Exception
	 */
	public UserVO wxLogin(@RequestBody User userData) throws Exception {
		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userData.getId())) {
			return null;
		}
		User user = new User();
		UserVO userVO;
		// 2. 判断用户id是否存在
		boolean isIdExist = userService.checkIdIsExist(userData.getId());
		// 3. 注册信息
		if (!isIdExist) {
			// 3.1 用户不存在，只添加用户id（openId）头像和昵称
			user.setId(userData.getId());
			user.setNickname("用户" + sid.userInitNickname());
			user.setFaceImg(RandomInitFaceImg.getRandomPath(userData.getFaceImg()));
			user.setFaceImgThumb(RandomInitFaceImg.getRandomPath(userData.getFaceImg()));
			user.setPassword("ChangeMe");
			user.setFollowNum(0);
			user.setFansNum(0);
			user.setCreateDate(new Date());
			userVO = userService.saveUserDirectly(user);
		} else {
			// 3.2 查询信息
			// userVO = userService.getUserById(userData.getId());
			userVO = userService.getUserById(userData.getId());
			//判断该用户是否被禁言，如果被禁言则判断该用户的禁言日期是否到达，如果到达则使该用户状态变为1（正常）
			userService.judgeUserState(userData.getId());
		}
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		return userVO;
	}

	@ApiOperation(value = "Get the identifying code by email")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "email", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getCode")
	public JSONResult getCode(String userId, String email) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		if (userService.checkEmailIsExist(email)){
			return JSONResult.errorException("Email already binding!");
		}
		// 生成验证码
		int length = 6; // 位数
		String code = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			code += random.nextInt(10);
		}
		// System.out.println(code);
		// 存入 redis
		redis.set(USER_EMAIL_CODE + ":" + userId, email + code, 60 * 10); // 过期时间单位为秒 10分钟过期
		// 发送验证码邮件模板
		emailTool.sendCodeToMail(email, code);

		return JSONResult.ok();
	}

	@ApiOperation(value = "Confirm identifying code")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "code", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/confirmCode")
	public JSONResult confirmCode(String userId, String email, String code) throws Exception {

		String rightCode = redis.get(USER_EMAIL_CODE + ":" + userId);
		if (StringUtils.isBlank(rightCode)) {
			return JSONResult.errorMsg("The code for this user is blank.");
		} else {
			String finalCode = email + code;
			if (finalCode.equals(rightCode)) {
				// 认证成功，修改用户邮箱
				User user = new User();
				user.setId(userId);
				user.setEmail(email);
				UserVO userVO = userService.updateUserInfo(user);
				return JSONResult.ok(userVO);
			}
		}
		return JSONResult.errorMsg("Wrong code.");
		// if (StringUtils.isBlank(userId) || StringUtils.isBlank(email)){
		// return JSONResult.errorMsg("Wrong");
		// }
		// User user = new User();
		// user.setId(userId);
		// user.setEmail(email);
		// UserVO userVO = userService.updateUserInfo(user);
		// return JSONResult.ok(userVO);
	}

	@ApiOperation(value = "获取所有认证用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form") })
	@PostMapping("/listAllAuthUsers")
	public JSONResult listAllAuthUsers(Integer page, Integer pageSize) throws Exception {

		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = authenticatedUserService.listAllAuthUsers(page, pageSize);

		return JSONResult.ok(result);
	}

	@ApiOperation(value = "获取指定类别所有认证用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "type", value = "类别", required = true, dataType = "Integer", paramType = "form") })
	@PostMapping("/listAuthUserByType")
	public JSONResult listAuthUserByType(Integer page, Integer pageSize, Integer type) throws Exception {

		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		if (type != 1 && type != 2) {
			return JSONResult.errorException("wrong authenticate type!");
		}

		PagedResult result = authenticatedUserService.listByType(page, pageSize, type);

		return JSONResult.ok(result);
	}

	@ApiOperation(value = "通过email认证")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "email", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "type", required = true, dataType = "Integer", paramType = "form") })
	@PostMapping("/authenticateUserByEmail")
	public JSONResult authenticateUserByEmail(String email, Integer type) throws Exception {

		if (StringUtils.isBlank(email)) {
			return JSONResult.errorMsg("email can not be null.");
		}
		if (type != 1 && type != 2) {
			return JSONResult.errorException("wrong authenticate type!");
		}
		String userId = userService.getUserByEmail(email);
		if (userId == null) {
			return JSONResult.errorException("email not exists!");
		}
		if (authenticatedUserService.checkUserIsAuth(userId)) {
			return JSONResult.errorException("User Already be authenticated");
		}
		String authId = authenticatedUserService.saveAuth(email, type, userId);
		return JSONResult.ok(authId);
	}

	@ApiOperation(value = "撤销认证(真删除)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "认证id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value = "/authenticationCancel")
	public JSONResult authenticationCancel(String id) throws Exception {
		Example example = new Example(AuthenticatedUser.class);
		// 创造条件
		Example.Criteria criteria = example.createCriteria();
		// 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
		criteria.andEqualTo("id", id);

		authenticatedUserMapper.deleteByExample(example);
		return JSONResult.ok();
	}

	@ApiOperation(value = "获取所有认证用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form") })
	@PostMapping("/queryAllUsers")
	public JSONResult queryAllUsers(Integer page, Integer pageSize) throws Exception {

		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = userService.listAllUsers(page, pageSize);

		return JSONResult.ok(result);
	}

	/**
	 * 分页和搜索用户
	 *
	 * @param searchText
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "综合搜索")
	@PostMapping(value = "/searchUserYang")
	public JSONResult searchUserYang(String searchText, Integer page, String userId)
			throws Exception {

		if (page == null) {
			page = 1;
		}

		PagedResult result = userService.searchUserYang(page, PAGE_SIZE, searchText, userId);
		return JSONResult.ok(result);
	}

	/**
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "上传虚拟用户", notes = "上传虚拟用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="form"),
			@ApiImplicitParam(name="nickname", value="昵称", required=true, dataType="String", paramType="form"),
			@ApiImplicitParam(name="signature", value="个性签名", required=true, dataType="String", paramType="form")
	})
	@PostMapping(value="/addUser")
	public JSONResult addUser(@ApiParam(value="file", required=true) MultipartFile faceImg,
							  String userId, String nickname, String signature) throws Exception {

		User user = new User();
		UserVO userVO;
		// 2. 判断用户id是否存在
		boolean isIdExist = userService.checkIdIsExist(userId);
		// 3. 注册信息
		if (!isIdExist) {
			// 3.1 用户不存在，只添加用户id（openId）头像和昵称
			user.setId(userId);
			user.setNickname(nickname);
			user.setSignature(signature);
			// 上传组织logo
			if (faceImg != null) {
				// 判断是否超出大小限制
				if (faceImg.getSize() > MAX_IMAGE_SIZE) {
					return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
				}
				String faceImgName = faceImg.getOriginalFilename();
				if (StringUtils.isNotBlank(faceImgName)) {
					// 保存到数据库中的相对路径
					String uploadPathDB = resourceService.uploadImg(faceImg);
					user.setFaceImgThumb(uploadPathDB);
					user.setFaceImg(uploadPathDB);
				}else {
					return JSONResult.errorMsg("File name is blank");
				}
			}else {
				return JSONResult.errorMsg("Upload error");
			}
			user.setPassword("ChangeMe");
			user.setFollowNum(0);
			user.setFansNum(0);
			user.setCreateDate(new Date());
			userVO = userService.saveUserDirectly(user);
		} else {
			return JSONResult.errorMsg("id already exists");
		}
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		return JSONResult.ok(userVO);
	}

}
