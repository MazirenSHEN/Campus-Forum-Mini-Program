package com.nuoquan.service;


import com.nuoquan.enums.PostType;
import com.nuoquan.pojo.NotifyRemind;
import com.nuoquan.pojo.vo.NotifyRemindVO;
import com.nuoquan.utils.PagedResult;

import java.util.List;

/**
 * @Description: 通知提醒服务类
 * @Author:  Jerrio
 * @Date: 2020.8.28
 */
public interface NotifyRemindService {

	public enum SenderAction{
		LIKE("like"),			//点赞
		COMMENT("comment"), 	//评论
		FOLLOW("follow");		//关注

		public final String value;

		SenderAction(String value){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 插入一条通知
	 * @param senderId		//操作者Id
	 * @param senderAction	//操作者行为
	 * @param sourceId		//动作源对象，如评论内容
	 * @param targetType	//被操作对象类型，如文章，评论，投票
	 * @param targetId		//被操作对象Id
	 * @param recipientId	//消息接收者，可能是对象的所有者或订阅者；
	 * @return
	 */
	public void insert(String senderId,
							   SenderAction senderAction,
							   String sourceId,
							   PostType targetType,
							   String targetId,
							   String recipientId);

	/**
	 * 批量签收通知
     * @param notifyIds
     * @return
     */
	public int batchSign(List<String> notifyIds);

	/**
	 * 获取用户未签收通知
	 * @param userId
	 * @return
	 */
	public List<NotifyRemindVO> getUnsignedNotif(Integer page, Integer pageSize, String recipientId);
}
