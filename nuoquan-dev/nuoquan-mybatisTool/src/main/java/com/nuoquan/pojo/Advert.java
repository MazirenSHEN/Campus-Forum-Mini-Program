package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

public class Advert {
    private String id;

    private String advertiser;

    private String position;

    @Column(name = "stream_type")
    private String streamType;

    private String content;

    @Column(name = "resource_url")
    private String resourceUrl;

    private String link;

    @Column(name = "is_jump")
    private Integer isJump;

    @Column(name = "view_num")
    private Integer viewNum;

    @Column(name = "click_num")
    private Integer clickNum;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "expire_date")
    private Date expireDate;

    private String period;

    private Integer status;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return advertiser
     */
    public String getAdvertiser() {
        return advertiser;
    }

    /**
     * @param advertiser
     */
    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    /**
     * @return position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return stream_type
     */
    public String getStreamType() {
        return streamType;
    }

    /**
     * @param streamType
     */
    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return resource_url
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    /**
     * @param resourceUrl
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    /**
     * @return link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return is_jump
     */
    public Integer getIsJump() {
        return isJump;
    }

    /**
     * @param isJump
     */
    public void setIsJump(Integer isJump) {
        this.isJump = isJump;
    }

    /**
     * @return view_num
     */
    public Integer getViewNum() {
        return viewNum;
    }

    /**
     * @param viewNum
     */
    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    /**
     * @return click_num
     */
    public Integer getClickNum() {
        return clickNum;
    }

    /**
     * @param clickNum
     */
    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return expire_date
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * @return period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}