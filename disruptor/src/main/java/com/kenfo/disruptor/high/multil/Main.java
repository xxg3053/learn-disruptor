package com.kenfo.disruptor.high.multil;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high.multil
 * @Description: 多生产者 多消费者
 * @date 2018/11/25 下午5:22
 */
public class Main {

    public static void main(String[] args) throws Exception{
        //1. 创建RingBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(
                ProducerType.MULTI,
                ()-> new Order(),
                1024 * 1024,
                new YieldingWaitStrategy());
        //2. 通过ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //3. 构建多消费者
        Consumer[] consumers = new Consumer[10];
        for(int i=0; i<consumers.length; i++){
            consumers[i] = new Consumer("C" + i);
        }

        //4. 构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<Order>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers
        );

        //5. 设置多个消费者的sequence序号用于单独统计消费进度，并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        //6. 启动workerPool
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));


        CountDownLatch latch = new CountDownLatch(1);
        for(int i=0; i<100; i++){
            Producer producer = new Producer(ringBuffer);
            new Thread(()->{
                try{
                    latch.await();
                }catch (Exception e){
                    e.printStackTrace();
                }
                for (int j=0; j<100; j++){
                    producer.sendData(UUID.randomUUID().toString());
                }
            }).start();
        }

        Thread.sleep(2000);
        System.out.println("------线程创建完毕，开始生产数据------");
        latch.countDown();

        Thread.sleep(10000);
        System.out.println("任务总数：" + consumers[3].getCount());

    }





    static class EventExceptionHandler implements ExceptionHandler<Order>{

        @Override
        public void handleEventException(Throwable throwable, long l, Order order) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }


    }



}
