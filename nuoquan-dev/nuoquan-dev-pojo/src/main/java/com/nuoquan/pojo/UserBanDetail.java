package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

public class UserBanDetail {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Date endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
