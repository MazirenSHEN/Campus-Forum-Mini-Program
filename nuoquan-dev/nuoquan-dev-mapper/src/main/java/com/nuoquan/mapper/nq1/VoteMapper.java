package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.Vote;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteMapper extends MyMapper<Vote> {

	public void addTotalVoteNum(String voteId);

	public Integer getTotalVoteNum(String voteId);
}