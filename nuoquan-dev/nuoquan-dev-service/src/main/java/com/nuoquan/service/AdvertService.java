package com.nuoquan.service;

import com.nuoquan.enums.AdPositionEnum;
import com.nuoquan.enums.StreamType;
import com.nuoquan.pojo.Advert;
import com.nuoquan.utils.PagedResult;

import java.util.Date;
import java.util.List;

/**
 * 广告业务相关服务类
 */
public interface AdvertService {

    /**
     * 上传广告
     * @param advertiser
     * @param position
     * @param streamType
     * @param content
     * @param isJump
     * @param link
     * @param resourceUrl
     * @param duration
     * @param period
     */
    void uploadAd(String advertiser, AdPositionEnum position, StreamType streamType, String content, Integer isJump, String link, String resourceUrl, Integer duration, String period);

    /**
     * 查询全部可视状态/激活状态的广告
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    PagedResult getAd(Integer page, Integer pageSize, String userId);

    /**
     * 根据广告id查询单条广告
     * @param userId
     * @param adId
     * @return
     */
    Object getAdById(String adId, String userId);

    /**
     * 查询某位置的广告
     * @param position 位置
     * @param num 数量 不能小于0，如小于0自动设成1
     * @param userId 用户id
     * @return
     */
    List<Advert> getAdByPosition(AdPositionEnum position, Integer num, String userId);
}
