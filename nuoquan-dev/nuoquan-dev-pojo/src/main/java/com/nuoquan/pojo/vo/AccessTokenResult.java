package com.nuoquan.pojo.vo;

/**
 * 获取微信 access_token 的返回数据对象
 * @author jerrio
 */
public class AccessTokenResult {
	private String access_token;
	private Number expires_in;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Number getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Number expires_in) {
		this.expires_in = expires_in;
	}
}
