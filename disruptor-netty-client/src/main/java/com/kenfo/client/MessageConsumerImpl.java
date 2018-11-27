package com.kenfo.client;

import com.kenfo.disruptor.MessageConsumer;
import com.kenfo.entity.TranslatorData;
import com.kenfo.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.client
 * @Description: 客户端消息消费者
 * @date 2018/11/27 下午4:06
 */
public class MessageConsumerImpl extends MessageConsumer {

    public MessageConsumerImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData response = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        //业务逻辑处理:
        try {
            System.err.println("Client端: id= " + response.getId()
                    + ", name= " + response.getName()
                    + ", message= " + response.getMessage());
        } finally {
            ReferenceCountUtil.release(response);
        }
    }
}
