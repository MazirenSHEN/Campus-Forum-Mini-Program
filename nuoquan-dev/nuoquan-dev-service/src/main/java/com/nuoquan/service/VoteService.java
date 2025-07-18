package com.nuoquan.service;

import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.utils.PagedResult;

public interface VoteService {

	/**
	 * 保存投票
	 * @param vote
	 * @return voteId
	 */
	public String saveVote(Vote vote);

	/**
	 * 保存投票图片
	 * @param voteImage
	 */
	public void saveVoteImages(VoteImage voteImage);

	/**
	 * 保存投票的选项
	 * @param voteOption
	 */
	public void saveVoteOption(VoteOption voteOption);

	/**
	 * 分页查询全部投票
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllVotes(Integer page, Integer pageSize, String userId);

	/**
	 * 按 voteId 获取投票
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public VoteVO getVoteById(String voteId, String userId);

	@Deprecated
	public VoteVO getSingleVote(String userId, String voteId);

	public void passVote(String voteId);

	public void fDeleteVote(String voteId);

	public void banVote(String voteId);

	public void selectOption(String userId, String optionId, String voteId);

	public boolean isUserVoted(String userId, String voteId);
	
	/**
	 * 分页查询所有状态的投票
	 */
	public PagedResult list(Integer page, Integer pageSize);
	
	/**
	 * 列出所有等待审核的投票
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 修改投票状态(可批量)
	 */
	public int updateVoteStatus(String voteIds, int status);

	public PagedResult getAllMyHisVote(Integer page, Integer pageSize, String userId);

	public PagedResult getOtherslegalHisVote(Integer page, Integer pageSize, String userId, String targetId);

	/**
	 * 每8分钟自动跟新vote状态
	 */
	public void autoUpdateVoteStatus();

	/**
	 * 判断用户可否投票（红墙投票业务）
	 * @param userId
	 * @return 用户投票剩余票数
	 */
	public int userLeftVote(String voteId, String optionId, String userId);
}
