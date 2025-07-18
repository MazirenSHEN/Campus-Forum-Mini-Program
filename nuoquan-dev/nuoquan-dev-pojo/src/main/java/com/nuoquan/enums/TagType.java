package com.nuoquan.enums;

/**
 * TagType 事件标签类型
 */
public enum TagType {
    ACADEMIC(1, "Academic"),    //学术信息 黄色
    ACTIVITY(2, "Activity"),    //校园活动 橙色
    RECREATION(3, "Recreation");//吃喝玩乐 红色

    public final Integer type;
    public final String content;

    TagType(Integer type, String content){
        this.type = type;
        this.content = content;
    }

    public Integer getType() { return this.type; }
    public String getContent() { return this.content; }
}
