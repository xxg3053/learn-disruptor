package com.kenfo.disruptor.high.chain;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high
 * @Description: 模拟交易 event
 * @date 2018/11/25 下午3:44
 */
@Data
public class Trade {

    private String id;
    private String name;
    private double price;

    private AtomicInteger count = new AtomicInteger(0);

    public Trade(){

    }
}
