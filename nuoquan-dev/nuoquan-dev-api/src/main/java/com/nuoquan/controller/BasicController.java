package com.nuoquan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.nuoquan.mapper.nq1.UserMapper;
import com.nuoquan.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.config.COSProperties;
import com.nuoquan.config.ResourceConfig;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.vo.TitleVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.utils.RedisOperator;

public class BasicController {
	@Autowired
	public RedisOperator redis;
	@Autowired
	public ArticleService articleService;
	@Autowired
	public UserService userService;
	@Autowired
	public VoteService voteService;
	@Autowired
	public WeChatService weChatService;
	@Autowired
	public TagsService tagsService;
	@Value("${fdfs.groupName}")
	public String fdfsGroupName;
	@Autowired
	public ResourceConfig resourceConfig;
	@Autowired
	public LongarticleService longarticleService;
	@Autowired
	public ResourceService resourceService;
	@Autowired
	public COSProperties cosProperties;
	@Autowired
	public SocialService socialService;
	@Autowired
	public NotifyRemindService notifyRemindService;
	@Autowired
	public AdvertService advertService;
	@Autowired
	public OrganizationService organizationService;
	@Autowired
	public EventsCalendarService eventsCalendarService;
	@Autowired
	public FeaturedArticleService featuredArticleService;
	@Autowired
	public UserMapper userMapper;

	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 10;
	
	public static final String USER_EMAIL_CODE = "user-email-code"; //Final 均大写
	

	@Value("${upload.maxImageSize}")
	public long MAX_IMAGE_SIZE;

	@Value("${upload.maxVideoSize}")
	public long MAX_Video_SIZE;

	@Deprecated //该方法移动到 UserService
	public UserVO ConvertUserToUserVO (User user) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		return userVO;
	}

	/**
	 * @param like can be UserLikeArticle or UserLikeComment
	 * @return
	 */
	public UserLikeVO ConvertLikeToLikeVO (Object like) {
		UserLikeVO likeVO = new UserLikeVO();
		BeanUtils.copyProperties(like, likeVO);
		return likeVO;
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @param path
	 * @throws Exception
	 */
	@Deprecated
	public void uploadFile(MultipartFile file, String path) throws Exception {

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (file != null) {

				File outFile = new File(path);
				if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
					// Create the parent directory.
					outFile.getParentFile().mkdirs();
				}

				fileOutputStream = new FileOutputStream(outFile);
				inputStream = file.getInputStream();
				IOUtils.copy(inputStream, fileOutputStream);
			} else {
				System.out.println("The file is null");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
				inputStream.close();
			}
		}
	}
	
	/**
     * 设置标题通用方法
     * @param map
     */
    public void setTitle(ModelMap map, TitleVO titleVo){
    	//标题
    	map.put("title",titleVo.getTitle());
    	map.put("parenttitle",titleVo.getParenttitle());
		//是否打开欢迎语
    	map.put("isMsg",titleVo.isMsg());
		//欢迎语
    	map.put("msgHTML",titleVo.getMsgHtml());
		//小控件
    	map.put("isControl",titleVo.isControl());
		map.put("isribbon", titleVo.isIsribbon());
    }
	
}
