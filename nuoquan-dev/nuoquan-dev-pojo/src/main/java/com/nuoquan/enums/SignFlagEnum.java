package com.nuoquan.enums;

/**
 * 
 * @Description: 消息签收状态 枚举
 */
public enum SignFlagEnum {
	
	UNSIGN(0, "未签收"),
	SIGNED(1, "已签收");	
	
	public final Integer type;
	public final String content;
	
	SignFlagEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}

	public Integer getType() {
		return type;
	}  
}
