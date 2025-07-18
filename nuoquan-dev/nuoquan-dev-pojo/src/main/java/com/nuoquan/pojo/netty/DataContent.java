package com.nuoquan.pojo.netty;

import java.io.Serializable;

public class DataContent implements Serializable {

	private static final long serialVersionUID = 2530446680896211117L;
	
	private Integer action = -1;							// 动作类型(枚举在MsgActionEnum)
	private Object data;
//	private ChatMessage chatMessage = new ChatMessage();	// 用户的聊天内容 entity
	private String extend = "";								// 扩展字段
	
	public Integer getAction() {
		return action;
	}
	
	public void setAction(Integer action) {
		this.action = action;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getExtend() {
		return extend;
	}
	
	public void setExtend(String extend) {
		this.extend = extend;
	}

}
