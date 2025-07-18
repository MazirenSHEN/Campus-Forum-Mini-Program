package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.User;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends MyMapper<User> {


	/**
	 * @Des: 用户受喜欢数累加
	 * @param userId
	 */
	public void addReceiveLikeCount(String userId);

	/**
	 * @Des: 用户受喜欢数累减
	 * @param userId
	 */
	public void reduceReceiveLikeCount(String userId);

	/**
	 * @Description: 增加粉丝数
	 */
	public void addFansCount(String userId);

	/**
	 * @Description: 增加粉丝数
	 */
	public void reduceFansCount(String userId);

	/**
	 * @Description: 增加关注数
	 */
	public void addFollowCount(String userId);

	/**
	 * @Description: 减少关注数
	 */
	public void reduceFollowCount(String userId);

	/**
	 * 将用户状态改为正常（1）
	 * @param userId
	 */
	public void changeStateToNormal(String userId);

	/**
	 * 将用户状态改为禁言（0）
	 * @param userId
	 */
	public void changeStateToBanned(String userId);
}
