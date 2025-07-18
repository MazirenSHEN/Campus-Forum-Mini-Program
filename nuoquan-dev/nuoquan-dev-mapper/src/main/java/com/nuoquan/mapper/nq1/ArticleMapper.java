package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.Article;
import com.nuoquan.utils.MyMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends MyMapper<Article> {
    /**
     * @Description: 对文章喜欢的数量进行累加
     * @param articleId
     */
    public void addLikeCount(String articleId);

    /**
     * @Description: 对文章喜欢的数量进行累减
     * @param articleId
     */
    public void reduceLikeCount(String articleId);

    /**
     * @Description: 对文章收藏的数量进行累加
     * @param articleId
     */
    public void addCollectCount(String articleId);
    
    /**
     * @Description: 对文章收藏的数量进行累减
     * @param articleId
     */
    public void reduceCollectCount(String articleId);

    /**
     * @Description: 文章评论数量累加
     * @param articleId
     */
    public void addCommentCount(String articleId);

    /**
     * @Description: 对文章评论的数量进行累减
     * @param articleId
     */
    public void reduceCommentCount(String articleId);

    /**
     * @Description: 当删除文章某个评论时文章评论数变为减去此评论的子评论数
     * @param articleId
     */
    public void reduceCommentCountSpecific(String articleId, Integer newCommentNum);

    /**
     * 浏览量+1
     * @param articleId
     */
    public void addViewCount(String articleId);

    /**
     * @Description: 文章举报数量累加
     * @param articleId
     */
    public void addReportedCount(String articleId);

    /**
     * 查询文章状态
     * @param articleId
     * @return
     */
    public int selectArticleStatus(String articleId);

    /**
     * 根据创建时间降序查找所有被举报的文章
     * @return
     */
    public List<Article> queryReportedArticleByCreateTime();

    /**
     * 根据被举报数量降序查找所有被举报的文章
     * @return
     */
    public List<Article> queryReportedArticleByReportedNum();

    /**
     * 查询此文章的评论数
     * @param articleId
     * @return
     */
    public Integer selectArticleCommentNum(String articleId);
}