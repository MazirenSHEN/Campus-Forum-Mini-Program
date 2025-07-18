package com.nuoquan.service;

/**
 * 微信业务 业务接口类
 * @author cy
 */
public interface WeChatService {
 
    /**
     * 获取微信access_token
     * @return
     */
    public String getWeChatAccessToken();
    
    /**
     * 对文本进行安全检测
     * @param content
     * @return true: 合法 false: 非法
     */
    public boolean msgSecCheck(String content);
    
    /**
     * 生成小程序页面二维码，对应微信官方文档二维码接口场景B，生成总数无限制
     * @param page 必填
     * @param scene
     * @param width
     * @param isHyaline
     * @return 二维码图片 buffer
     */
    public String getQrcodeUnlimit(String page, String scene, Number width, boolean isHyaline);
}