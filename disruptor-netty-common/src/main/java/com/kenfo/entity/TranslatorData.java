package com.kenfo.entity;

import java.io.Serializable;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.entity
 * @Description: netty传输的数据包
 * @date 2018/11/27 下午3:28
 */
public class TranslatorData implements Serializable {

    private static final long serialVersionUID = 8763561286199081881L;

    private String id;
    private String name;
    private String message;	//传输消息体内容


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
