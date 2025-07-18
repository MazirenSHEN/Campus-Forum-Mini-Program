package com.nuoquan.pojo;

import javax.persistence.Table;

@Table(name = "official_account")
public class OfficialAccount {
    private String name;

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
}