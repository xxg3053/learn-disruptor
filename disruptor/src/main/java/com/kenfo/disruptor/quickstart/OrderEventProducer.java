package com.kenfo.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.quickstart
 * @Description: 订单生产者
 * @date 2018/11/25 下午12:46
 */
public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer data){

        //1. 在生产者发送消息的时候，首先需要从我们的ringBuffer里面获取一个可用的序号
        long sequence = ringBuffer.next();
        try {
            //2. 根据这个序号，找到具体的 "OrderEvent" 元素, 注意：此时获取的OrderEvent对象是个没有被赋值的空对象
            OrderEvent event = ringBuffer.get(sequence);
            //3. 进行实际的赋值处理
            event.setValue(data.getLong(0));
        }finally {
            //4. 提交操作
            ringBuffer.publish(sequence);
        }


    }
}
