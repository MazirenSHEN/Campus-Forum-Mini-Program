package com.nuoquan.pojo.vo;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nuoquan.utils.EncryptUtils;

public class UserVO {

    private String id;
    private String email;
    @JsonIgnore //不返回密码
    private String password;
    private String nickname;
    private String signature;
    private Date createDate;
    private String faceImg;
    private String faceImgThumb;
    private Integer followNum;
    private Integer fansNum;
    /**
     * 0 = female, 1 = male, 2 = others
     */
    private Byte gender;
    private String major;
    /**
     * Example: 2022
     */
    private Integer graduationYear;
    /**
     * 0 = high school, 1 = undergraduate, 2 = graduate
     */
    private Byte degree;
    private Integer receiveLikeCounts;
    private Integer state;
    /**
     * Client-id 设备id，用于消息推送
     */
    private String cid;
    private Integer reputation;

    /**
     * 用户最近登录时间
     */
    private Date latestLogin;

    private boolean isFollow; // 是否关注该用户

    private Integer authType; // 认证类型
    
    
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
//        return email;
        if (email!=null && !email.endsWith("@nottingham.edu.cn")){
            email = EncryptUtils.base64Decode(email);
        }
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
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

	public boolean isFollow() {
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
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

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }
}
