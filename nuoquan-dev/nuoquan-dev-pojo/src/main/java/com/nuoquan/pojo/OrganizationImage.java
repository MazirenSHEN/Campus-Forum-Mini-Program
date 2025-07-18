package com.nuoquan.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "organization_image")
public class OrganizationImage {
    @Id
    private String id;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_order")
    private Integer imageOrder;

    @Column(name = "status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(Integer imageOrder) {
        this.imageOrder = imageOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
