package com.kenfo;

import com.kenfo.client.MessageConsumerImpl;
import com.kenfo.client.NettyClient;
import com.kenfo.disruptor.MessageConsumer;
import com.kenfo.disruptor.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyClientApplication.class, args);
		MessageConsumer[] conusmers = new MessageConsumer[4];
		for(int i =0; i < conusmers.length; i++) {
			MessageConsumer messageConsumer = new MessageConsumerImpl("code:clientId:" + i);
			conusmers[i] = messageConsumer;
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
				1024*1024,
				//new YieldingWaitStrategy(),
				new BlockingWaitStrategy(),
				conusmers);
		//建立连接，发送消息
		new NettyClient().sendData();
	}
}
