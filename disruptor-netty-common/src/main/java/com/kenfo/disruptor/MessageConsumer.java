package com.kenfo.disruptor;

import com.kenfo.entity.TranslatorDataWapper;
import com.lmax.disruptor.WorkHandler;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor
 * @Description: 消费者
 * @date 2018/11/27 下午3:56
 */
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId){
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
