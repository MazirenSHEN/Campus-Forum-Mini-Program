package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "organization")
public class Organization {
    @Id
    private String id;

    // 组织名称
    @Column(name = "name")
    private String name;

    // 组织简介
    @Column(name = "intro")
    private String intro;

    // 组织部门组成
    @Column(name = "division")
    private String division;

    // 组织活动介绍
    @Column(name = "activity_intro")
    private String activityIntro;

    // 组织logo
    @Column(name = "logo_path")
    private String logoPath;

    // 组织公众号或推文链接
    @Column(name = "official_account_Link")
    private String officialAccountLink;

    // 后台创建时间
    @Column(name = "create_date")
    private Date createDate;

    // 0:unreadable/1:readable/2:checking
    @Column(name = "status")
    private Integer status;

    public String getOfficialAccountLink() {
        return officialAccountLink;
    }

    public void setOfficialAccountLink(String officialAccountLink) {
        this.officialAccountLink = officialAccountLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getActivityIntro() {
        return activityIntro;
    }

    public void setActivityIntro(String activityIntro) {
        this.activityIntro = activityIntro;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
