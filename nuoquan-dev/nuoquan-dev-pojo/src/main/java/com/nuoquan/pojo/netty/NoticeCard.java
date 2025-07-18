package com.nuoquan.pojo.netty;

/**
 * 通知卡片内容
 * @author jerrio
 */
@Deprecated
public class NoticeCard {
	
	private Object source; // 来自的点赞或评论
	private Object target; // 文章或评论
	
	public NoticeCard(Object source, Object target) {
		this.source = source;
		this.target = target;
	}
	
	public Object getSource() {
		return source;
	}
	
	public void setSource(Object source) {
		this.source = source;
	}
	
	public Object getTarget() {
		return target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}
}
