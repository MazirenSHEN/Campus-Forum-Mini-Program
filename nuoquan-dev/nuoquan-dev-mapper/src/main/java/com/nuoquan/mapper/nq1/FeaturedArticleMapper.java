package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.FeaturedArticle;
import com.nuoquan.pojo.vo.FeaturedArticleVO;
import com.nuoquan.utils.MyMapper;

import java.util.List;

public interface FeaturedArticleMapper extends MyMapper<FeaturedArticle> {

    List<FeaturedArticleVO> queryAllFeaturedArticles();
}
