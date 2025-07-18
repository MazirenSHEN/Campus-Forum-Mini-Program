package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
@Deprecated
//TODO: 不需要这个Custom，使用ArticleMapper，VO属性添加写在Service层
public interface ArticleMapperCustom extends MyMapper<Article> {
	
	/**
	 * @Description: 直接列出所有文章
	 * @return
	 */
	public List<ArticleVO> list();
	
	/**
	 * @Description: 直接列出所有等待审核的文章
	 * @return
	 */
	public List<ArticleVO> listCheckOnly();
	
	/**
	 * @Description: 获取所有状态为发布中的文章(status==1)
	 * @return
	 */
	public List<ArticleVO> queryAllArticles();
	
	/**
	 * @Description: 对文章喜欢的数量进行累加
	 * @param articleId
	 */
	public void addArticleLikeCount(String articleId);
	
	
	/**
	 * @Description: 对文章喜欢的数量进行累减
	 * @param articleId
	 */
	public void reduceArticleLikeCount(String articleId);
	
	/**
	 * @Description: 对文章收藏的数量进行累加
	 * @param articleId
	 */
	public void addArticleCollectCount(String articleId);
	
	
	/**
	 * @Description: 对文章收藏的数量进行累减
	 * @param articleId
	 */
	public void reduceArticleCollectCount(String articleId);
	
	/**
	 * @Description: 文章评论数量累加
	 * @param articleId
	 */
	public void addArticleCommentCount(String articleId);
	
	/**
	 * @deprecated
	 * 搜索文章内容
	 * @param searchKeywords
	 * @return
	 */
	public List<ArticleVO> searchArticleContentYang(@Param("searchKeywords") String searchKeywords);
	
	/**
	 * 按公式更新热度
	 * (24小时内的点赞数+24小时内的评论数)
	 */
	public void updatePopByFunction();
	
	/**
	 * 根据热度获取文章
	 * @return
	 */
	public List<ArticleVO> getArticleByPopularity();
	
	/**
	 * 按 articleId 获取文章
	 * @param articleId
	 * @return
	 */
	@Deprecated
	public ArticleVO getArticleById(String articleId);

	/**
	 * 按userId获取本人发布的所有文章
	 * @return
	 */
	public List<ArticleVO> queryAllMyHisArticle(String userId);

	/**
	 * 按targetId获取他人的, status为1的所有文章
	 * @param targetId
	 * @return
	 */
	public List<ArticleVO> queryOthersLegalHisArticle(String targetId);
	
	/**
	 * 查询目标用户收藏，我或者他人
	 * @param targetId
	 * @return
	 */
	public List<ArticleVO> queryCollectArticle(String targetId);
	
	/**
	 * 浏览量+1
	 * @param articleId
	 */
	public void addViewCount(String articleId);
	
	
}