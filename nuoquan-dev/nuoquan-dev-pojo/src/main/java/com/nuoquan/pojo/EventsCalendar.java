package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class EventsCalendar {
    @Id
    private String id;

    @Column(name = "title")
    private String eventTitle;

    @Column(name = "faculty")
    private Integer faculty;

    @Column(name = "degree")
    private Integer degree;

    @Column(name = "venue")
    private String eventVenue;

    @Column(name = "date")
    private Integer eventDate;

    @Column(name = "time")
    private String eventTime;

    @Column(name = "status")
    private Integer status;

    public Integer getTags() { return tags; }

    public void setTags(Integer tags) { this.tags = tags; }

    @Column(name = "tags")
    private Integer tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setStatus(Integer status) {this.status = status;}

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
