package com.nuoquan.enums;


/**
 * DegreeType 学历枚举类
 * @author 叶博源
 */
public enum DegreeType {
    UGDOMESTIC(1, "UG-Domestic"),		//国内本科
    UGSPP(2, "UG-SPP"),			        // spp本科
    PGT(3, "PGT"),                      // 授课型硕士
    PGR(4, "PGR"),                      // 研究型硕士
    ALL(5, "all");// 全部

    public final Integer type;
    public final String content;

    DegreeType(Integer type, String content){
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
