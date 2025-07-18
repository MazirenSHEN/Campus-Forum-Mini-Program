package com.nuoquan.enums;

/**
 * Post 状态枚举类
 * @author jerrio
 */
public enum PostType {
	COMMENT("comment"),			//评论
	ARTICLE("article"),			//文章
	LONGARTICLE("longarticle"),	//长文章
	VOTE("vote");					//投票
	
	public final String value;

	PostType(String value){
		this.value = value;
	}

	public String getValue() {return value; }

}
