package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

public class Users {
    @Column(name = "unionId")
    private String unionid;

    @Column(name = "openId")
    private String openid;

    private String name;

    @Column(name = "studentId")
    private String studentid;

    private Boolean gender;

    private String phone;

    private Boolean year;

    private String major;

    private String motto;

    @Column(name = "cur_state")
    private Boolean curState;

    @Column(name = "cur_seat")
    private String curSeat;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_visit_time")
    private Date lastVisitTime;

    @Column(name = "usage_count")
    private Integer usageCount;

    private Integer studytime;

    /**
     * @return unionId
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * @param unionid
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * @return openId
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return studentId
     */
    public String getStudentid() {
        return studentid;
    }

    /**
     * @param studentid
     */
    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    /**
     * @return gender
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return year
     */
    public Boolean getYear() {
        return year;
    }

    /**
     * @param year
     */
    public void setYear(Boolean year) {
        this.year = year;
    }

    /**
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @param major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * @return motto
     */
    public String getMotto() {
        return motto;
    }

    /**
     * @param motto
     */
    public void setMotto(String motto) {
        this.motto = motto;
    }

    /**
     * @return cur_state
     */
    public Boolean getCurState() {
        return curState;
    }

    /**
     * @param curState
     */
    public void setCurState(Boolean curState) {
        this.curState = curState;
    }

    /**
     * @return cur_seat
     */
    public String getCurSeat() {
        return curSeat;
    }

    /**
     * @param curSeat
     */
    public void setCurSeat(String curSeat) {
        this.curSeat = curSeat;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return last_visit_time
     */
    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    /**
     * @param lastVisitTime
     */
    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    /**
     * @return usage_count
     */
    public Integer getUsageCount() {
        return usageCount;
    }

    /**
     * @param usageCount
     */
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    /**
     * @return studytime
     */
    public Integer getStudytime() {
        return studytime;
    }

    /**
     * @param studytime
     */
    public void setStudytime(Integer studytime) {
        this.studytime = studytime;
    }
}