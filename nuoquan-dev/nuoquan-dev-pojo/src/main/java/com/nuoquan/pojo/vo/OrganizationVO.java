package com.nuoquan.pojo.vo;

import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.OrganizationImage;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

public class OrganizationVO {
    private String id;

    // 组织名称
    private String name;

    // 组织简介
    private String intro;

    // 组织部门组成
    private String division;

    // 组织活动介绍
    private String activityIntro;

    // 组织logo
    private String logoPath;

    // 组织公众号或推文链接
    private String officialAccountLink;

    // 后台创建时间
    private Date createDate;

    // 0:unreadable/1:readable/2:checking
    private Integer status;

    // 组织图片列表
    private List<OrganizationImage> imgList;

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

    public String getOfficialAccountLink() {
        return officialAccountLink;
    }

    public void setOfficialAccountLink(String officialAccountLink) {
        this.officialAccountLink = officialAccountLink;
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

    public List<OrganizationImage> getImgList() {
        return imgList;
    }

    public void setImgList(List<OrganizationImage> imgList) {
        this.imgList = imgList;
    }
}
