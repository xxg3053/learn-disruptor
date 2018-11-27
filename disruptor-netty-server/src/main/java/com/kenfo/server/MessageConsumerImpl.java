package com.kenfo.server;

import com.kenfo.disruptor.MessageConsumer;
import com.kenfo.entity.TranslatorData;
import com.kenfo.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.server
 * @Description: 服务端消费者实现
 * @date 2018/11/27 下午4:02
 */
public class MessageConsumerImpl extends MessageConsumer {

    public MessageConsumerImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData request = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        //1.业务处理逻辑:
        System.err.println("Sever端: id= " + request.getId()
                + ", name= " + request.getName()
                + ", message= " + request.getMessage());

        //2.回送响应信息:
        TranslatorData response = new TranslatorData();
        response.setId("resp: " + request.getId());
        response.setName("resp: " + request.getName());
        response.setMessage("resp: " + request.getMessage());
        //写出response响应信息:
        ctx.writeAndFlush(response);
    }
}
