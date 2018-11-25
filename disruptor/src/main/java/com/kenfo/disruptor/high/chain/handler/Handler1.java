package com.kenfo.disruptor.high.chain.handler;

import com.kenfo.disruptor.high.chain.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high
 * @Description: 消费者1
 * @date 2018/11/25 下午4:09
 */
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade>{

    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler 1 : SET NAME");
        trade.setName("H1");
        Thread.sleep(1000);
    }
}
