package com.kenfo.disruptor;

import com.kenfo.entity.TranslatorData;
import com.kenfo.entity.TranslatorDataWapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor
 * @Description: 生产者
 * @date 2018/11/27 下午3:57
 */
public class MessageProducer {

    private String producerId;

    private RingBuffer<TranslatorDataWapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void onData(TranslatorData data, ChannelHandlerContext ctx) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWapper wapper = ringBuffer.get(sequence);
            wapper.setData(data);
            wapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
