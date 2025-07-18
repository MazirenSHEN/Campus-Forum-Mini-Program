package com.nuoquan.pojo;

import com.nuoquan.utils.EncryptUtils;
import java.util.Date;
import javax.persistence.*;

public class User {
    @Id
    private String id;

    private String email;

    private String password;

    private String nickname;

    private String signature;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "face_img")
    private String faceImg;

    /**
     * 小头像
     */
    @Column(name = "face_img_thumb")
    private String faceImgThumb;

    /**
     * 关注数
     */
    @Column(name = "follow_num")
    private Integer followNum;

    /**
     * 粉丝数
     */
    @Column(name = "fans_num")
    private Integer fansNum;

    /**
     * 0 = female, 1 = male, 2 = others
     */
    private Byte gender;

    private String major;

    /**
     * Example: 2022
     */
    @Column(name = "graduation_year")
    private Integer graduationYear;

    /**
     * 0 = high school, 1 = undergraduate, 2 = graduate
     */
    private Byte degree;

    @Column(name = "receive_like_counts")
    private Integer receiveLikeCounts;

    /**
     * 0 = 封号, 1 = 正常
     */
    @Column(name = "state")
    private Integer state;

    /**
     * Client-id 设备id，用于消息推送
     */
    private String cid;

    private Integer reputation;

    /**
     * 用户最近登录的时间
     */
    @Column(name = "latest_login")
    private Date latestLogin;


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
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        if (email!=null && email.endsWith("@nottingham.edu.cn")){
            email = EncryptUtils.base64Encode(email);
        }
        this.email = email;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
     * @return face_img
     */
    public String getFaceImg() {
        return faceImg;
    }

    /**
     * @param faceImg
     */
    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    /**
     * 获取小头像
     *
     * @return face_img_thumb - 小头像
     */
    public String getFaceImgThumb() {
        return faceImgThumb;
    }

    /**
     * 设置小头像
     *
     * @param faceImgThumb 小头像
     */
    public void setFaceImgThumb(String faceImgThumb) {
        this.faceImgThumb = faceImgThumb;
    }

    /**
     * 获取关注数
     *
     * @return follow_num - 关注数
     */
    public Integer getFollowNum() {
        return followNum;
    }

    /**
     * 设置关注数
     *
     * @param followNum 关注数
     */
    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    /**
     * 获取粉丝数
     *
     * @return fans_num - 粉丝数
     */
    public Integer getFansNum() {
        return fansNum;
    }

    /**
     * 设置粉丝数
     *
     * @param fansNum 粉丝数
     */
    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    /**
     * 获取0 = female, 1 = male, 2 = others
     *
     * @return gender - 0 = female, 1 = male, 2 = others
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * 设置0 = female, 1 = male, 2 = others
     *
     * @param gender 0 = female, 1 = male, 2 = others
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @param major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 获取Example: 2022
     *
     * @return graduation_year - Example: 2022
     */
    public Integer getGraduationYear() {
        return graduationYear;
    }

    /**
     * 设置Example: 2022
     *
     * @param graduationYear Example: 2022
     */
    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    /**
     * 获取0 = high school, 1 = undergraduate, 2 = graduate
     *
     * @return degree - 0 = high school, 1 = undergraduate, 2 = graduate
     */
    public Byte getDegree() {
        return degree;
    }

    /**
     * 设置0 = high school, 1 = undergraduate, 2 = graduate
     *
     * @param degree 0 = high school, 1 = undergraduate, 2 = graduate
     */
    public void setDegree(Byte degree) {
        this.degree = degree;
    }

    /**
     * @return receive_like_counts
     */
    public Integer getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    /**
     * @param receiveLikeCounts
     */
    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取Client-id 设备id，用于消息推送
     *
     * @return cid - Client-id 设备id，用于消息推送
     */
    public String getCid() {
        return cid;
    }

    /**
     * 设置Client-id 设备id，用于消息推送
     *
     * @param cid Client-id 设备id，用于消息推送
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

	public Integer getReputation() {
		return reputation;
	}

	public void setReputation(Integer reputation) {
		this.reputation = reputation;
	}

	public Date getLatestLogin() {
		return latestLogin;
	}

	public void setLatestLogin(Date latestLogin) {
		this.latestLogin = latestLogin;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
