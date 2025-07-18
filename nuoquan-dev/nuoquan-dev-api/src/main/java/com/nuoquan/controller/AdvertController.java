package com.nuoquan.controller;

import com.nuoquan.enums.AdPositionEnum;
import com.nuoquan.enums.StreamType;
import com.nuoquan.pojo.Advert;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 广告相关方法
 * @Author: Deyan & Jerrio
 * @Date: 9/30/2020
 */
@RestController
@Api(value = "广告相关接口", tags = {"AdvertController"})
@RequestMapping("/advert")
public class AdvertController extends BasicController {

    @ApiOperation(value = "上传广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "advertiser", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "position", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "streamType", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "content", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "isJump", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "link", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "resourceUrl", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "duration", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "period", required = false, dataType = "String", paramType = "form")
    })
    @PostMapping(value="/uploadAd")
    public JSONResult uploadAd(String advertiser, AdPositionEnum position, StreamType streamType,
                               String content, Integer isJump, String link,
                               String resourceUrl, Integer duration, String period) throws Exception {
        if (StringUtils.isBlank(advertiser) || StringUtils.isEmpty(advertiser)) {
            return JSONResult.errorMsg("Id can't be null");
        }
        advertService.uploadAd(advertiser,
                position,
                streamType,
                content,
                isJump,
                link,
                resourceUrl,
                duration,
                period);
        return JSONResult.ok();
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取全部可查看（激活状态）的广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value="/getAd")
    public JSONResult getAd(Integer page, Integer pageSize, String userId) throws Exception {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult adResult = advertService.getAd(page, pageSize, userId);

        return JSONResult.ok(adResult);
    }

    @ApiOperation(value = "根据广告id查询单个广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "adId", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value="/getAdById")
    public JSONResult getAdById(String adId, String userId) throws Exception {
        return JSONResult.ok(advertService.getAdById(adId, userId));
    }

    @ApiOperation(value = "查询某位置的广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "position", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "num", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value="/getAdByPosition")
    public JSONResult getAdByPosition(AdPositionEnum position, Integer num, String userId) throws Exception {
        List<Advert> list = advertService.getAdByPosition(position, num, userId);
        return JSONResult.ok(list);
    }

    /**
     * 根据广告商id选择他的一组广告
     * @param page
     * @param pageSize
     * @param userId
     * @param advertiser
     * @return
     * @throws Exception
     */
    // TODO: 2020/9/23
    public JSONResult getAdByAdvertiser(Integer page, Integer pageSize, String userId, String advertiser) throws Exception {
        return JSONResult.ok();
    }

}
