package com.nuoquan.pojo.vo;

/**
 * 获取微信 openid 请求的返回对象
 * @author jerrio
 */
public class WxRes {
	private String session_key;
	private String openid;
	private String unionid;

	public String getUnionid(){
		return unionid;
	}
	public void setUnionid(String unionid){
		this.unionid = unionid;
	}
	
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
