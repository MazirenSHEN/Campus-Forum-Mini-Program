package com.nuoquan.enums;

/**
 * Redis 缓存键值枚举类
 * @Author: jerrio
 * @Date: 2020.9.13
 */
public enum RedisKeys {

    USER_FETCH("USER_FETCH:userId-targetType-targetId"),	//用户 fetch 对象
    USER_READ("USER_READ:userId-targetType-targetId");	//用户 阅读 对象

    public final String value;

    RedisKeys(String value){
        this.value = value;
    }

    public String getValue() {return value; }
}
