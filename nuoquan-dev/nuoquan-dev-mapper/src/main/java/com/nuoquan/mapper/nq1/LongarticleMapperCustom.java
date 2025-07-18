package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.Longarticle;
import com.nuoquan.pojo.vo.LongarticleVO;
import com.nuoquan.utils.MyMapper;

public interface LongarticleMapperCustom extends MyMapper<Longarticle>{

	/*
	 * 按 articleId 获取文章
	 * @param articleId
	 * @return
	 */
	@Deprecated
	public LongarticleVO getArticleById(String articleId);
}
