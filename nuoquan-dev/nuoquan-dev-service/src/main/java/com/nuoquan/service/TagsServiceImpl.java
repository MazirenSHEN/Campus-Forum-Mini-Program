package com.nuoquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuoquan.mapper.nq1.TagsMapper;
import com.nuoquan.pojo.Tags;

@Service
public class TagsServiceImpl implements TagsService {
	
	@Autowired
	private TagsMapper tagsMapper;
	
	@Transactional(propagation = Propagation.SUPPORTS)	
	public List<Tags> getTagsList(){
		
		return tagsMapper.selectAll();
	}
}
