package com.kenfo.disruptor.high.chain;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high
 * @Description: 生产者
 * @date 2018/11/25 下午3:56
 */
public class TradePushlisher implements Runnable {

    private Disruptor disruptor;
    private CountDownLatch latch;

    private static int PUBLISH_COUNT = 10;

    public TradePushlisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }

    @Override
    public void run() {
        TradeEventTranslator eventTranslator = new TradeEventTranslator();
        //for(int i=0; i < PUBLISH_COUNT; i++){
            //新提交任务的方式
            disruptor.publishEvent(eventTranslator);
       // }
        latch.countDown();

    }
}


class TradeEventTranslator implements EventTranslator<Trade>{

    private Random random = new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private void generateTrade(Trade event) {
        event.setPrice(random.nextDouble() * 999);
    }

}