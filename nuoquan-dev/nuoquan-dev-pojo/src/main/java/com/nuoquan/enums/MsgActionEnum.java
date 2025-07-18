package com.nuoquan.enums;

/**
 * 
 * @Description: 发送消息的动作 枚举
 */
public enum MsgActionEnum {
	
	CONNECT(1, "第一次(或重连)初始化连接"),
	CHAT(2, "聊天消息"),	
	SIGNED(3, "消息签收"),
	KEEPALIVE(4, "客户端保持心跳"),

	NOTIFY(5, "通知提醒");

//	LIKEARTICLE(5, "点赞文章通知"),
//	LIKECOMMENT(6, "点赞评论通知"),
//	COMMENTARTICLE(7, "评论文章通知"),
//	COMMENTCOMMENT(8, "评论评论通知"),
//	LIKEARTICLE_SIGN(9, "签收点赞文章通知"),
//	LIKECOMMENT_SIGN(10, "签收点赞评论通知"),
//	COMMENT_SIGN(11, "签收评论文章/评论通知"); //在同一个表
	
	public final Integer type;
	public final String content;
	
	MsgActionEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
