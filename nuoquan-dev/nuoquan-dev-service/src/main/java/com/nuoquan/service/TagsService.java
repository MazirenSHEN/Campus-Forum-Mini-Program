package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.Tags;

public interface TagsService {
	
	/**
	 * 获取标签列表
	 * @return
	 */
	public List<Tags> getTagsList();
}
