package com.kenfo.disruptor.quickstart;


import com.lmax.disruptor.EventHandler;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.quickstart
 * @Description: 订单消费者
 * @date 2018/11/25 下午12:23
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long l, boolean b) throws Exception {
        System.out.println("消费者：" + orderEvent.getValue());
    }
}
