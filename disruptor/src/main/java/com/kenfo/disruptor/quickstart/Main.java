package com.kenfo.disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.quickstart
 * @Description: 启动类
 * @date 2018/11/25 下午12:26
 */
public class Main {

    public static void main(String[] args) {

        //参数准备
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //1. 实例化disruptor对象
        /**
         * 1. 消息（Event）工厂对象
         * 2. ringBufferSize" 容器的长度
         * 3. executor：线程池（建议使用自定义线程池，不使用fixed固定线程池，防止内存溢出） RejectedExecutionHandler
         * 4. ProducerType: 单生产者还是多生产者
         * 5. WaitStrategy: 等待策略
         */
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(
                orderEventFactory,
                ringBufferSize,
                executor,
                ProducerType.SINGLE,       //生产者类型
                new BlockingWaitStrategy() //等待策略
        );

        //2. 添加消费者的监听
        disruptor.handleEventsWith(new OrderEventHandler());

        //3. 启动disruptor
        disruptor.start();

        //4. 获取实际存储数据的容器：RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for(long i=0; i<100; i++){
            bb.putLong(0, i);
            producer.sendData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();

    }
}
