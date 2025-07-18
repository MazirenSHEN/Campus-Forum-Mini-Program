package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "source_map")
public class SourceMap {
    /**
     * 分配的作者，多个用“，”隔开
     */
    @Column(name = "author_id")
    private String authorId;

    /**
     * 来源
     */
    private String source;

    /**
     * 获取分配的作者，多个用“，”隔开
     *
     * @return author_id - 分配的作者，多个用“，”隔开
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * 设置分配的作者，多个用“，”隔开
     *
     * @param authorId 分配的作者，多个用“，”隔开
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取来源
     *
     * @return source - 来源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源
     *
     * @param source 来源
     */
    public void setSource(String source) {
        this.source = source;
    }
}