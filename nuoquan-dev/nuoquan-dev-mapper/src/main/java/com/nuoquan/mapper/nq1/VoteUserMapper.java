package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.VoteUser;
import com.nuoquan.utils.MyMapper;

public interface VoteUserMapper extends MyMapper<VoteUser> {

	String getSelectedOptionId(String userId, String id);
	
}