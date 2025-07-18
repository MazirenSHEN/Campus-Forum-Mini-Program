package com.nuoquan.service;

import com.nuoquan.pojo.EventsCalendar;
import com.nuoquan.utils.PagedResult;

public interface EventsCalendarService {

    /**
     * queryAllEventsCalender接口的方法
     * @param userId 操作者id
     * @param page 前端页数
     * @param pageSize 前端页面大小
     * @param currentDate 查询的目标日期(此参数为null时默认查询所有)
     * @param faculty 学院
     * @param degree 学历
     * @return JSONResult
     */
    public PagedResult queryEventsCalendarByDate(String userId, Integer page, Integer pageSize, Integer currentDate, Integer faculty, Integer degree);

    /**
     * queryAllEventsCalender接口的方法
     * @param userId 操作者id
     * @param page 前端页数
     * @param pageSize 前端页面大小
     * @return JSONResult
     */
    public PagedResult queryAllEventsCalendar(String userId, Integer page, Integer pageSize);

    /**
     * 插入一条通知
     *
     * @param eventsCalendar
     * @param title
     * @param venue
     * @param date
     * @param time
     * @param faculty
     * @param degree
     * @param status
     * @param description
     * @return
     */
    public EventsCalendar insert(EventsCalendar eventsCalendar, String title,
                                 String venue,
                                 Integer date,
                                 String time,
                                 Integer faculty,
                                 Integer degree,
                                 Integer status,
                                 Integer tags, String description);


    /**
     * queryAllEventsCalender接口的方法
     * @param eventsCalendar EventsCalendar
     * @return JSONResult
     */
    public String saveEvent(EventsCalendar eventsCalendar);

    /**
     * 取消文章加精
     * @param eventId 文章加精id
     */
    public void removeEvent(String eventId);
}
