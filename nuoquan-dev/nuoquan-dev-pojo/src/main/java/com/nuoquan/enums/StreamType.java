package com.nuoquan.enums;

/**
 * 媒体流类型
 * @author jerrio
 */
public enum StreamType {
	IMAGE("image"),				//图片
	VIDEO("video");				//视频
	
	public final String value;

	StreamType(String value){
		this.value = value;
	}

	public String getValue() {return value; }

}
