package com.nuoquan.service;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.FeaturedArticle;
import com.nuoquan.pojo.vo.FeaturedArticleVO;
import com.nuoquan.utils.PagedResult;

public interface FeaturedArticleService {
    /**
     * 获取全部加精文章
     * @param page 前端页数
     * @param pageSize 前端页面大小
     * @param userId 操作者id
     * @return 页面结果
     */
    PagedResult queryFeaturedArticle(Integer page, Integer pageSize, String userId);

    /**
     * 使用文章加精id获取文章
     * @param featuredArticleId 文章加精id
     * @param userId 操作者id
     * @return 页面结果
     */
    FeaturedArticleVO getFeaturedArticleById(String featuredArticleId, String userId);

    /**
     * 查询文章是否已经加精
     * @param articleId 文章id
     * @return Boolean false-当前未加精；true-当前已加精；
     */
    Boolean isArticleFeatured(String articleId);


    /**
     * 文章加精
     * @param featuredArticle 精选文章类的实例
     * @return 文章加精id
     */
    public String setToFeaturedArticle(FeaturedArticle featuredArticle);

    /**
     * 取消文章加精
     * @param featuredArticleId 文章加精id
     */
    public void removeFeaturedArticle(String featuredArticleId);
}
