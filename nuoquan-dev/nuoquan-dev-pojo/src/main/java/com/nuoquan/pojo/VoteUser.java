package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "vote_user")
public class VoteUser {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "vote_id")
    private String voteId;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "create_date")
    private Date createDate;

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
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
     * @return option_id
     */
    public String getOptionId() {
        return optionId;
    }

    /**
     * @param optionId
     */
    public void setOptionId(String optionId) {
        this.optionId = optionId;
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
}