package com.nuoquan.pojo;

import javax.persistence.*;

public class Tags {
    @Id
    private String id;

    private String tag;

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
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}