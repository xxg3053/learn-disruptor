package com.kenfo.disruptor.high.chain.handler;

import com.kenfo.disruptor.high.chain.Trade;
import com.lmax.disruptor.EventHandler;

import java.util.UUID;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high
 * @Description: TODO
 * @date 2018/11/25 下午4:09
 */
public class Handler2 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler 2 : SET ID");
        trade.setId(UUID.randomUUID().toString());
        Thread.sleep(2000);
    }
}
