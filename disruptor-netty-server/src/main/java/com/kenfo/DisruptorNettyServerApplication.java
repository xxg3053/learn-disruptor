package com.kenfo;

import com.kenfo.disruptor.MessageConsumer;
import com.kenfo.disruptor.RingBufferWorkerPoolFactory;
import com.kenfo.server.MessageConsumerImpl;
import com.kenfo.server.NettyServer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyServerApplication.class, args);

		//netty启动前disruptor
		MessageConsumer[] conusmers = new MessageConsumer[4];
		for(int i =0; i < conusmers.length; i++) {
			MessageConsumer messageConsumer = new MessageConsumerImpl("code:serverId:" + i);
			conusmers[i] = messageConsumer;
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
				1024*1024,
				//new YieldingWaitStrategy(),
				new BlockingWaitStrategy(),
				conusmers);

		new NettyServer();
	}
}
