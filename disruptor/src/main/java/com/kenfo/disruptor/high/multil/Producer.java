package com.kenfo.disruptor.high.multil;

import com.lmax.disruptor.RingBuffer; /**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high.multil
 * @Description: TODO
 * @date 2018/11/25 下午9:04
 */
public class Producer {

    private RingBuffer<Order> ringBuffer;

    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid){
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        }finally {
            ringBuffer.publish(sequence);

        }
    }
}
