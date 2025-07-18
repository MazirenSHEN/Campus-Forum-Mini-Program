package com.nuoquan.mapper.nq1;

import java.util.List;

import com.nuoquan.pojo.UserFans;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.utils.MyMapper;

public interface UserFansMapperCustom extends MyMapper<UserFans> {
	/**
	 * 查询该用户粉丝的信息
	 * @param userId
	 * @return
	 */
	public List<UserVO> queryFansInfo(String userId);
	
	/**
	 * 查询该用户关注的信息
	 * @param userId
	 * @return
	 */
	public List<UserVO> queryFollowInfo(String userId);
	
}