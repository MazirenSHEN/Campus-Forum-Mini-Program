package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.EventsCalendarMapper;
import com.nuoquan.pojo.EventsCalendar;
import com.nuoquan.pojo.vo.EventsCalendarVO;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 迎新周日程业务模型
 * @author BoyuanYE
 * @Date: 2022.07.05
 */
@Service
public class EventsCalendarServiceImpl implements EventsCalendarService{

    @Autowired
    private Sid sid;
    @Autowired
    public RedisOperator redis;
    @Autowired
    private EventsCalendarMapper eventsCalendarMapper;
    /**
     * 把 EventsCalendar 转换为 EventsCalendarVO, 组装日程VO对象
     * @param eventsCalendar
     * @param userId 操作者（我）
     * @return eventsCalendarVO
     */
    public EventsCalendarVO composeEventsCalendarVO(EventsCalendar eventsCalendar, String userId) {
        EventsCalendarVO eventsCalendarVO = new EventsCalendarVO();
        BeanUtils.copyProperties(eventsCalendar, eventsCalendarVO);
        return composeEventsCalendarVO(eventsCalendarVO, userId);
    }

    public EventsCalendarVO composeEventsCalendarVO(EventsCalendarVO eventsCalendarVO, String userId) {
        int date = eventsCalendarVO.getEventDate();
        if (date>=10){
            eventsCalendarVO.setStrEventDate("Sept. "+eventsCalendarVO.getEventDate().toString());
        } else {
            eventsCalendarVO.setStrEventDate("Sept. 0"+eventsCalendarVO.getEventDate().toString());
        }
        switch (eventsCalendarVO.getFaculty()){
            case 1:
                eventsCalendarVO.setStrFaculty("FoSE");
                break;
            case 2:
                eventsCalendarVO.setStrFaculty("FoB");
                break;
            case 3:
                eventsCalendarVO.setStrFaculty("FHSS");
                break;
        }
        switch (eventsCalendarVO.getDegree()){
            case 1:
                eventsCalendarVO.setStrDegree("UG-Domestic");
                break;
            case 2:
                eventsCalendarVO.setStrDegree("UG-SPP");
                break;
            case 3:
                eventsCalendarVO.setStrDegree("PGT");
                break;
            case 4:
                eventsCalendarVO.setStrDegree("PGR");
                break;
        }
        switch (eventsCalendarVO.getTags()){
            case 1:
                eventsCalendarVO.setStrTag("ACADEMIC");
                break;
            case 2:
                eventsCalendarVO.setStrTag("ACTIVITY");
                break;
            case 3:
                eventsCalendarVO.setStrTag("RECREATION");
                break;
        }
        return eventsCalendarVO;
    }

    @Override
    public PagedResult queryEventsCalendarByDate(String userId, Integer page, Integer pageSize, Integer targetDate, Integer faculty, Integer degree){

        // 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
        PageHelper.startPage(page, pageSize);
        List<EventsCalendarVO> list = eventsCalendarMapper.queryEventsCalendarByDate(faculty, degree, targetDate);

        List<EventsCalendarVO> newList = new ArrayList<>();
        for (EventsCalendarVO ec : list) {
            EventsCalendarVO eventsCalendarVO = composeEventsCalendarVO(ec, "");
            newList.add(eventsCalendarVO);
        }
        PageInfo<EventsCalendarVO> pageList = new PageInfo<>(newList);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(newList);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }
    /**
     * queryAllEventsCalender接口的方法
     * @param userId 操作者id
     * @param page 前端页数
     * @param pageSize 前端页面大小
     * @return JSONResult
     */
    public PagedResult queryAllEventsCalendar(String userId, Integer page, Integer pageSize){

        // 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
        PageHelper.startPage(page, pageSize);
        List<EventsCalendarVO> list = eventsCalendarMapper.queryAllEventsCalendar();

        List<EventsCalendarVO> newList = new ArrayList<>();
        for (EventsCalendarVO ec : list) {
            EventsCalendarVO eventsCalendarVO = composeEventsCalendarVO(ec, "");
            newList.add(eventsCalendarVO);
        }
        PageInfo<EventsCalendarVO> pageList = new PageInfo<>(newList);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(newList);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }


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
    @Override
    public EventsCalendar insert(EventsCalendar eventsCalendar, String title, String venue, Integer date, String time, Integer faculty, Integer degree, Integer status, Integer tags, String description) {
        // 保存事件信息到数据库
        eventsCalendar.setEventTitle(title);
        eventsCalendar.setEventVenue(venue);
        eventsCalendar.setEventDate(date);
        eventsCalendar.setEventTime(time);
        eventsCalendar.setFaculty(faculty);
        eventsCalendar.setDegree(degree);
        eventsCalendar.setStatus(status);
        eventsCalendar.setTags(tags);
        eventsCalendar.setDescription(description);
        eventsCalendar.setCreateDate(new Date());

        return eventsCalendar;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveEvent(EventsCalendar eventsCalendar) {
        String id = sid.nextShort();
        eventsCalendar.setId(id);
        eventsCalendarMapper.insertSelective(eventsCalendar);
        return id;
    }

    /**
     * 移除日程
     *
     * @param eventId 文章加精id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void removeEvent(String eventId) {
        Example example = new Example(EventsCalendar.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", eventId);
        EventsCalendar ec = new EventsCalendar();
        ec.setStatus(StatusEnum.DELETED.type);
        eventsCalendarMapper.updateByExampleSelective(ec, example);
    }
}
