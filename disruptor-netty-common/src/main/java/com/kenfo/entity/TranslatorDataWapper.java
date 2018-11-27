package com.kenfo.entity;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.entity
 * @Description: disruptor传输包
 * @date 2018/11/27 下午3:54
 */
public class TranslatorDataWapper implements Serializable {

    private TranslatorData data;
    private ChannelHandlerContext ctx;

    public TranslatorData getData() {
        return data;
    }

    public void setData(TranslatorData data) {
        this.data = data;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
