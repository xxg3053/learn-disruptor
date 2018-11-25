package com.kenfo.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.quickstart
 * @Description: 订单工厂
 * @date 2018/11/25 下午12:21
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent(); //这个方式就是为了返回空的数据对象（Event)
    }
}
