package com.nuoquan.service;

import java.util.List;

import com.nuoquan.enums.StatusEnum;
import com.nuoquan.pojo.Longarticle;
import com.nuoquan.pojo.vo.LongarticleVO;
import com.nuoquan.utils.PagedResult;

/**
 * @Description: 长文章服务类
 * @Author: jerrio
 * @Date: 2020.8.13
 */
public interface LongarticleService {

	/**
	 * 分页查询全部长文章
	 */
	public PagedResult list(Integer page, Integer pageSize);
	
	/**
	 * 列出所有等待审核的长文章
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 按 articleId 获取长文章
	 * @param articleId
	 * @param userId
	 * @return
	 */
	public LongarticleVO getArticleById(String articleId, String userId);

	/**
	 * 保存文章
	 */
	public String saveArticle(Longarticle article);

	/**
	 * 保存文章
	 */
	public String saveArticle(String title,
							  String subTitle,
							  String coverImage,
							  String content,
							  String userId,
							  String tags,
							  StatusEnum status);
	/**
	 * 删除文章
	 * @param articleId
	 */

	public void deleteArticle(String articleId, String userId);

	/**
	 * 根据公式更新文章热度
	 */
	public void updatePopByFunction();
	
	/**
	 * 获取热度值前三的文章
	 * @param pageSize 
	 * @param page 
	 * @return
	 */
	public PagedResult getArticleByPopularity(Integer page, Integer pageSize, String userId);
	
	/**
	 * 操作者本人查看自己发布过的所有文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllMyHis(Integer page, Integer pageSize, String userId);

	/**
	 * 操作者查看他人发布过的文章(状态为1的文章)
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult gerOthersLegalHis(Integer page, Integer pageSize, String userId, String targetId);
	
	/**
	 * 浏览量+1
	 * @param articleId
	 */
	public void addViewCount(String articleId);

	/**
	 * 修改文章状态(可批量)
	 */
	public int updateArticleStatus(String articleIds, int status);

	/**
	 * 查询文章
	 * @param page
	 * @param pageSize
	 * @param queryType
	 * @param userId
	 * @param selectedTag
	 * @param category
	 * @param source
	 * @return
	 */
	public PagedResult getArticles(Integer page,
								   Integer pageSize,
								   Integer queryType,
								   String userId,
								   String selectedTag,
								   String category,
								   String source);

	/**
	 * 查询来自公众号（Official Account）的文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getArticlesFromOA(Integer page,
								   Integer pageSize,
								   String userId);


	/**
	 * 是否为关联的来源（公众号）
	 * @param source
	 * @return
	 */
	public boolean isRelatedOA(String source);
	/**
	 * 从头条数据库获取随机一条记录
	 * @param userId
	 * @return
	 */
	public Object getHeadlines(String userId);
}
