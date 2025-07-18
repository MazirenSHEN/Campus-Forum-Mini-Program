package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "featured_article")
public class FeaturedArticle {
    @Id
    private String id;

    // 对应文章id
    @Column
    private String articleId;

    // 精选文章封面图
    @Column
    private String coverPath;

    /**
     * 0 = 取消精选, 1 = 精选文章
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 加精时间，不是文章发布时间
     */
    @Column(name = "create_date")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
