package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.PagedResult;

public interface ArticleService {
	
	/**
	 * 分页查询全部文章
	 */
	public PagedResult list(Integer page, Integer pageSize);
	

	/**
	 * 列出所有等待审核的文章
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 分页查询全部发布的文章
	 */
	@Deprecated
	public PagedResult getAllArticles(Integer page, Integer pageSize, String userId);
	
	/**
	 * 按 articleId 获取文章
	 * @param articleId
	 * @param userId
	 * @return
	 */
	public ArticleVO getArticleById(String articleId, String userId);

	/**
	 * 查询目标用户收藏的文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult queryCollectArticle(Integer page, Integer pageSize, String userId, String targetId);

	/**
	 *  按关键词搜索阳面文章，支持多关键词以空格分割
	 *  TODO: 可以增加多种排序模式 @author jerrio
	 */
	public PagedResult searchArticleYang(Integer isSaveRecord, Integer page, Integer pageSize, String searchText, String userId);
	
	/**
	 * 获取热搜词
	 * @return
	 */
	public List<String> getHotWords();
	
	/**
	 * 保存文章
	 */
	public String saveArticle(Article article);
	
	/**
	 * 删除文章
	 * @param articleId
	 */
//	@Deprecated
//	public void deleteArticle(String articleId, String userId);

	/**
	 * 保存文章图片
	 */
	public void saveArticleImages(ArticleImage articleImage);
	
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
	 * Set the status to unreadable
	 * @param articleId
	 */
	public void fDeleteArticle(String articleId);
	
	/**
	 * 操作者本人查看自己发布过的所有文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllMyHisArticle(Integer page, Integer pageSize, String userId);

	/**
	 * 操作者查看他人发布过的文章(状态为1的文章)
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult getOthersLegalHisArticle(Integer page, Integer pageSize, String userId, String targetId);
	
	/**
	 * 浏览量+1
	 * @param articleId
	 */
	@Deprecated
	public void addViewCount(String articleId, String userId);

	/**
	 * 修改文章状态(可批量)
	 * 0 = unreadable, 1 = readable, 2 = checking
	 */
	public int updateArticleStatus(String articleIds, int status);

	/**
	 * QueryArticle接口的方法
	 * @param page
	 * @param pageSize
	 * @param queryType
	 * @param userId
	 * @param selectedTag
	 * @return
	 */
	public PagedResult queryArticle(Integer page, Integer pageSize, Integer queryType, String userId, String selectedTag);
	
//	/**
//	 * 通过热度查询评论主
//	 * @param page
//	 * @param pageSize
//	 * @param articleId
//	 * @param userId
//	 * @return
//	 */
//	public PagedResult getMainCommentsByPopularity(Integer page, Integer pageSize, String articleId, String userId);
//
//	/**
//	 * 通过热度查询次ping
//	 * @param page
//	 * @param pageSize
//	 * @param underCommentId
//	 * @param userId
//	 * @return
//	 */
//	public PagedResult getSonCommentsByPopularity(Integer page, Integer pageSize, String underCommentId, String userId);
	
}
