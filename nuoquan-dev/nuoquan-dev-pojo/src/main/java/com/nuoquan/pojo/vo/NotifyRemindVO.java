package com.nuoquan.pojo.vo;

import io.swagger.models.auth.In;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "notify_remind")
public class NotifyRemindVO {
    private String id;

    /**
     * 操作者的ID，三个0代表是系统发送的；
     */
    @Column(name = "sender_id")
    private String senderId;

    /**
     * 操作者的动作，如：like, comment、follow、donate、collect;
     */
    @Column(name = "sender_action")
    private String senderAction;

    /**
     * 动作源对象ID，如评论内容
     */
    @Column(name = "source_id")
    private String sourceId;

    /**
     * 目标对象ID；
     */
    @Column(name = "target_id")
    private String targetId;

    /**
     * 被操作对象类型，如：user, article, vote
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 消息接收者；可能是对象的所有者或订阅者；
     */
    @Column(name = "recipient_id")
    private String recipientId;

    /**
     * 0: 未签收\n1：签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 阅读时间
     */
    @Column(name = "read_date")
    private Date readDate;

    /**
     * Sender头像昵称
     */
    private String nickname;
    private String faceImg;
    private String faceImgThumb;

    /**
     * 源对象和目标对象
     */
    private Object source;
    private Object target;

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    private Integer authType;

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
     * 获取操作者的ID，三个0代表是系统发送的；
     *
     * @return sender_id - 操作者的ID，三个0代表是系统发送的；
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * 设置操作者的ID，三个0代表是系统发送的；
     *
     * @param senderId 操作者的ID，三个0代表是系统发送的；
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取操作者的动作，如：like, comment、follow、donate、collect;
     *
     * @return sender_action - 操作者的动作，如：like, comment、follow、donate、collect;
     */
    public String getSenderAction() {
        return senderAction;
    }

    /**
     * 设置操作者的动作，如：like, comment、follow、donate、collect;
     *
     * @param senderAction 操作者的动作，如：like, comment、follow、donate、collect;
     */
    public void setSenderAction(String senderAction) {
        this.senderAction = senderAction;
    }

    /**
     * 获取动作源对象，如评论内容
     *
     * @return source_id - 动作源对象，如评论内容
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 设置动作源对象，如评论内容
     *
     * @param sourceId 动作源对象，如评论内容
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 获取目标对象ID；
     *
     * @return target_id - 目标对象ID；
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * 设置目标对象ID；
     *
     * @param targetId 目标对象ID；
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * 获取被操作对象类型，如：user, article, vote
     *
     * @return target_type - 被操作对象类型，如：user, article, vote
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 设置被操作对象类型，如：user, article, vote
     *
     * @param targetType 被操作对象类型，如：user, article, vote
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * 获取消息接收者；可能是对象的所有者或订阅者；
     *
     * @return recipient_id - 消息接收者；可能是对象的所有者或订阅者；
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * 设置消息接收者；可能是对象的所有者或订阅者；
     *
     * @param recipientId 消息接收者；可能是对象的所有者或订阅者；
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * 获取0: 未签收\n1：签收
     *
     * @return sign_flag - 0: 未签收\n1：签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置0: 未签收\n1：签收
     *
     * @param signFlag 0: 未签收\n1：签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取阅读时间
     *
     * @return read_date - 阅读时间
     */
    public Date getReadDate() {
        return readDate;
    }

    /**
     * 设置阅读时间
     *
     * @param readDate 阅读时间
     */
    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}