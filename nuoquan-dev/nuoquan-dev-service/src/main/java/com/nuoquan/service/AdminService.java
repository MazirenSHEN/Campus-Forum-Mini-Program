package com.nuoquan.service;

import java.util.List;

import com.nuoquan.enums.PostType;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.pojo.AdminNotice;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.PagedResult;

/**
 * 后台管理业务接口类
 * @author Jerrio
 */
public interface AdminService {
	/**
	 * 查询后台管理员用户信息
	 * @param username
	 * @return 用户对象
	 */
	public AdminUser queryAdminUserName(String username);
		
	/**
	 * 获取用户公告
	 * @param adminUser
	 * @param state 阅读状态  0未阅读 1 阅读  -1全部
	 * @return
	 */
	public List<AdminNotice> getUserNotice(AdminUser adminUser, int state);

	/**
	 * 在后台根据文章Id查看文章（不会增加浏览量）
	 * @param articleId
	 * @param userId
	 * @return
	 */
	ArticleVO getArticleByIdAdmin(String articleId, String userId);

	/**
	 * 在后台查看所有被举报的文章/长文章/评论
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetType
	 * @param queryType
	 * @return
	 */
	PagedResult getReportedPublished(Integer page, Integer pageSize, String userId, PostType targetType, Integer queryType);

}