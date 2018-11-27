package com.kenfo.client;

import com.kenfo.disruptor.MessageProducer;
import com.kenfo.disruptor.RingBufferWorkerPoolFactory;
import com.kenfo.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.client
 * @Description: 客户端处理
 * @date 2018/11/27 下午3:43
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
        try {
            TranslatorData response = (TranslatorData)msg;
            System.err.println("Client端: id= " + response.getId()
                    + ", name= " + response.getName()
                    + ", message= " + response.getMessage());
        } finally {
            //一定要注意 用完了缓存 要进行释放
            ReferenceCountUtil.release(msg);
        }**/
        TranslatorData response = (TranslatorData)msg;
        String producerId = "code:seesionId:002";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(response, ctx);
    }
}
