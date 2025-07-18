package com.nuoquan.pojo.vo;

public class FeaturedArticleVO {
    private String id;

    // 对应文章id
    private String articleId;

    // 加精文章封面图
    private String coverPath;

    // 加精文章标题
    private String articleTitle;

    // 加精文章用户头像
    private String faceImg;

    // 加精文章用户小头像
    private String faceImgThumb;

    // 加精文章用户名字
    private String nickname;

    // 加精文章点赞数量
    private Integer likeNum;

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    private Integer authType;

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

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getFaceImgThumb() {
        return faceImgThumb;
    }

    public void setFaceImgThumb(String faceImgThumb) {
        this.faceImgThumb = faceImgThumb;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }
}
