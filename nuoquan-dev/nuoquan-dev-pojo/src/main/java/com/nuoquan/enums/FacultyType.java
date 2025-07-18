package com.nuoquan.enums;

/**
 * FacultyType 学院枚举类
 * @author 叶博源
 */

public enum FacultyType {
    FOSE(1, "FoSE"),		//理工学院
    FOB(2, "FoB"),			//商学院
    FHSS(3, "FHSS"),       // 文学院
    ALL(4, "all");        // 全部

    public final Integer type;
    public final String content;

    FacultyType(Integer type, String content){
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
    public String  getContent() {
        return content;
    }
}
