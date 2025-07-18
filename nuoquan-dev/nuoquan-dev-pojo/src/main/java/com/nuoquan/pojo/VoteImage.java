package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "vote_image")
public class VoteImage {
    @Id
    private String id;

    @Column(name = "vote_id")
    private String voteId;

    @Column(name = "image_path")
    private String imagePath;
    
    @Column(name = "image_order")
    private Integer imageOrder;

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
     * @return image_path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

	public Integer getImageOrder() {
		return imageOrder;
	}

	public void setImageOrder(Integer imageOrder) {
		this.imageOrder = imageOrder;
	}
}