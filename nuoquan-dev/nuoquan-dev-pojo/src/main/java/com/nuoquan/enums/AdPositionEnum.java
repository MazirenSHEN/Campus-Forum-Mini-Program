package com.nuoquan.enums;

/**
 * 广告位置枚举类
 * @author jerrio
 */
public enum AdPositionEnum {
	OPENING("opening"),	//开屏广告
	MINE("mine");			//我的页面广告

	public final String value;

	AdPositionEnum(String value){
		this.value = value;
	}

	public String getValue() {return value; }

}
