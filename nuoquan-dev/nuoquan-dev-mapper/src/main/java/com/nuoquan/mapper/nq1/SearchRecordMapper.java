package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.utils.MyMapper;

public interface SearchRecordMapper extends MyMapper<SearchRecord> {
	
	public List<String> getHotWords();
}