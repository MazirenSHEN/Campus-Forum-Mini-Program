package com.nuoquan.pojo.vo;

import java.util.Date;

/**
 * 迎新周日程VO
 * @author BoyuanYE
 * @date: 2022年7月5日 16:50
 */
public class EventsCalendarVO {
    private String id;
    private String eventTitle;
    private Integer faculty;
    private Integer degree;
    private String eventVenue;
    private Integer eventDate;
    private String eventTime;
    private Integer status;
    private Integer tags;
    private Date createDate;
    private String description;

    private String strEventDate;
    private String strFaculty;
    private String strDegree;

    public String getStrTag() { return strTag;}

    public void setStrTag(String strTag) { this.strTag = strTag;}

    private String strTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getFaculty() {
        return faculty;
    }

    public void setFaculty(Integer faculty) {
        this.faculty = faculty;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }


    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public Integer getEventDate() {
        return eventDate;
    }

    public void setEventDate(Integer eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTags() { return tags; }

    public void setTags(Integer tags) { this.tags = tags; }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStrEventDate() {
        return strEventDate;
    }

    public void setStrEventDate(String strEventDate) {
        this.strEventDate = strEventDate;
    }

    public String getStrFaculty() {
        return strFaculty;
    }

    public void setStrFaculty(String strFaculty) {
        this.strFaculty = strFaculty;
    }

    public String getStrDegree() {
        return strDegree;
    }

    public void setStrDegree(String strDegree) {
        this.strDegree = strDegree;
    }
}
