package com.nuoquan.controller;

import com.nuoquan.enums.PostType;
import com.nuoquan.service.ReportService;
import com.nuoquan.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "举报功能相关接口", tags = { "ReportController" })
@RequestMapping("/Report")
public class ReportController extends BasicController {
    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "举报发布的文章/长文章/评论", notes = "举报相关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "操作者ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "targetType", value = "被举报对象的类型", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "targetId", value = "被举报对象的Id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/reportPublished")
    public JSONResult reportPublished(String userId, PostType targetType, String targetId){
        reportService.reportPublished(userId, targetType, targetId);
        return JSONResult.ok();
    }
}
