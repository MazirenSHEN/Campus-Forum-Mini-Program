package com.nuoquan.pojo.vo;

import java.util.List;

/**
 * 查询用户粉丝和关注列表的返回对象
 * @author jerrio
 */
public class FansFollow {

	private List<UserVO> fansList;
	private List<UserVO> followList;
	
	public FansFollow(List<UserVO> fansList2, List<UserVO> followList2) {
		this.setFansList(fansList2);
		this.setFollowList(followList2);
	}

	public List<UserVO> getFansList() {
		return fansList;
	}

	public void setFansList(List<UserVO> fansList) {
		this.fansList = fansList;
	}

	public List<UserVO> getFollowList() {
		return followList;
	}

	public void setFollowList(List<UserVO> followList) {
		this.followList = followList;
	}
	
}
