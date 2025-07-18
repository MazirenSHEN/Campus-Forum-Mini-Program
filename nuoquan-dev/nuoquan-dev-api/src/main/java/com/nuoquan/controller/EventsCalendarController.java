package com.nuoquan.controller;

import com.nuoquan.enums.DegreeType;
import com.nuoquan.enums.FacultyType;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.enums.TagType;
import com.nuoquan.pojo.EventsCalendar;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 迎新周日程相关接口
 *
 * @author BoyuanYE
 * @Date: 2022.07.05
 **/

@RestController
@Api(value = "迎新周日程相关接口", tags = { "EventsCalendar-Controller" })
@RequestMapping("/eventsCalendar")
public class EventsCalendarController extends BasicController {
    /**
     *
     * @param page       页面数
     * @param pageSize   页面大小
     * @param userId     操作者id
     * @param targetDate 查询目标日期
     * @param faculty    学院
     * @param degree     学历
     * @return JSONResult
     */
    @ApiOperation(value = "按日期查询日程", notes = "查询全部可读日程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "targetDate", value = "查询目标日期", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "faculty", value = "学院", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "degree", value = "学历", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/queryEventsCalendarByDate")
    public JSONResult queryEventsCalendarByDate(String userId, Integer page, Integer pageSize, Integer targetDate, Integer faculty, Integer degree) {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = eventsCalendarService.queryEventsCalendarByDate(userId, page, pageSize, targetDate,
                faculty, degree);

        return JSONResult.ok(result);
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param userId   操作者id
     * @return JSONResult
     * @throws Exception
     */
    @ApiOperation(value = "查询全部日程", notes = "查询全部可读日程的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/listAllEvents")
    public JSONResult listAllEvents(String userId, Integer page, Integer pageSize) throws Exception {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = eventsCalendarService.queryAllEventsCalendar(userId, page, pageSize);

        return JSONResult.ok(result);
    }

    @ApiOperation(value = "上传日程", notes = "上传日程的接口")
    @ApiImplicitParams({
            // uniapp使用formData时，paramType要改成form
            @ApiImplicitParam(name="userId", value="操作者id", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="title", value="事件名称", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="venue", value="事件地点", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="date", value="事件日期", required=true, dataType="Integer", paramType="form"),
            @ApiImplicitParam(name="time", value="事件时间", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="faculty", value="学院", required=true, dataType="Integer", paramType="form"),
            @ApiImplicitParam(name="degree", value="学历", required=true, dataType="Integer", paramType="form"),
            @ApiImplicitParam(name="tag", value="标签", required=true, dataType="Integer", paramType="form"),
            @ApiImplicitParam(name="description", value="描述", required=false, dataType="String", paramType="form")
    })
    @PostMapping(value="/uploadEvent")
    public JSONResult uploadEvent(String userId, String title, String venue, Integer date, String time, FacultyType faculty, DegreeType degree, String description, TagType tag) throws Exception {

        //TODO: 是否增加ALL选项，简化录入
        if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
            return JSONResult.errorMsg("Id can't be null");
        }
        int isLegal;
        // 检测内容是否非法
        if (weChatService.msgSecCheck(title)
                && weChatService.msgSecCheck(venue)
                && weChatService.msgSecCheck(time)
                && weChatService.msgSecCheck(description)) {
            // 合法
            isLegal = StatusEnum.READABLE.type;
//            if (resourceConfig.getAutoCheckArticle()) { // 查看是否设置自动过审
//                isLegal = StatusEnum.READABLE.type;
//            } else {
//                isLegal = StatusEnum.CHECKING.type;
//            }
        } else {
            // 非法，尽管非法也保存到数据库
            isLegal = StatusEnum.DELETED.type;
        }

        EventsCalendar eventsCalendar = new EventsCalendar();
        String eventId = "";

        // 如果学院选择为全部
        if (faculty.getContent().equals("all")){
            // 遍历所有学院种类
            for (int f = 1; f < faculty.getType(); f++){
                // 如果学历选择为全部
                if (degree.getContent().equals("all")){
                    // 遍历所有学历种类
                    for (int d = 1; d < degree.getType(); d++){
                        eventsCalendar = eventsCalendarService.insert(eventsCalendar, title, venue, date, time, f, d, isLegal, tag.getType(), description);
                        eventId = eventsCalendarService.saveEvent(eventsCalendar);
                    }
                } else {
                    // 如果学历选择不为全部
                    eventsCalendar = eventsCalendarService.insert(eventsCalendar, title, venue, date, time, f, degree.getType(), isLegal, tag.getType(), description);
                    eventId = eventsCalendarService.saveEvent(eventsCalendar);
                }
            }
        } else {
            // 学院选择不为全部
            // 如果学历选择为全部
            if (degree.getContent().equals("all")){
                // 遍历所有学历种类
                for (int d = 1; d < degree.getType(); d++){
                    eventsCalendar = eventsCalendarService.insert(eventsCalendar, title, venue, date, time, faculty.getType(), d, isLegal, tag.getType(), description);
                    eventId = eventsCalendarService.saveEvent(eventsCalendar);                }
            } else {
                // 学历选择不为全部
                eventsCalendar = eventsCalendarService.insert(eventsCalendar, title, venue, date, time, faculty.getType(), degree.getType(), isLegal, tag.getType(), description);
                eventId = eventsCalendarService.saveEvent(eventsCalendar);                }
        }

        // TODO: 这里只返回了最后一次的id，可能在未来业务中出bug
        if (isLegal != 0) {
            return JSONResult.ok(eventId);
        } else {
            return JSONResult.errorMsg("内容违规");
        }
    }

    @ApiOperation(value = "移除日程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eventId", value = "事件id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/removeEvent")
    public JSONResult removeEvent(String eventId) throws Exception {
        eventsCalendarService.removeEvent(eventId);
        return JSONResult.ok();
    }
}
