package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.utils.MyMapper;

public interface ArticleImageMapper extends MyMapper<ArticleImage> {
	/**
	 * 获取文章图片列表
	 * @param articleId
	 * @return
	 */
	public List<ArticleImage> getArticleImgs(String articleId);
}