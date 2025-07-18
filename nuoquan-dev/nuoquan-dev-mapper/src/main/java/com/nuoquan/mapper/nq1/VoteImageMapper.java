package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.VoteImage;
import com.nuoquan.utils.MyMapper;

public interface VoteImageMapper extends MyMapper<VoteImage> {
	
	public List<VoteImage> getVoteImgs(String voteId);
	
}