package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "vote_option")
public class VoteOption {
    private String id;

    @Column(name = "vote_id")
    private String voteId;

    /**
     * 投票选项的图片
     */
    private String img;

    @Column(name = "`index`")
    private Integer index;

    @Column(name = "create_date")
    private Date createDate;

    private Integer count;

    private Double percent;

    private String content;

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
     * @return vote_id
     */
    public String getVoteId() {
        return voteId;
    }

    /**
     * @param voteId
     */
    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    /**
     * 获取投票选项的图片
     *
     * @return img - 投票选项的图片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置投票选项的图片
     *
     * @param img 投票选项的图片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index
     */
    public void setIndex(Integer index) {
        this.index = index;
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
     * @return count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return percent
     */
    public Double getPercent() {
        return percent;
    }

    /**
     * @param percent
     */
    public void setPercent(Double percent) {
        this.percent = percent;
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
}