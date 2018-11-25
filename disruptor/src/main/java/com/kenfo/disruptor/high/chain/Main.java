package com.kenfo.disruptor.high.chain;

import com.kenfo.disruptor.high.chain.handler.*;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high
 * @Description: 高级操作
 * @date 2018/11/25 下午3:44
 */
public class Main {

    public static void main(String[] args) throws Exception{

        ExecutorService es1 = Executors.newFixedThreadPool(8);
        //构建一个线程池用于提交任务
        ExecutorService es2 = Executors.newFixedThreadPool(4);//fix用无界队列，有安全隐患

        //1. 构建disruptor
        Disruptor<Trade> disruptor = new Disruptor<>(
                () -> new Trade(),
                1024 * 1024,
                es1,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        //2. 把消费者设置到disruptor中
        //2.1 串行操作
        /**
        disruptor
            .handleEventsWith(new Handler1())
            .handleEventsWith(new Handler2())
            .handleEventsWith(new Handler3());
        **/


        //2.2 并行操作
        /**
        disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());
        **/
        //disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3());


        //2.3 菱形操作（一）
        /**
        disruptor.handleEventsWith(new Handler1(), new Handler2())
                .handleEventsWith(new Handler3());
         **/
        //2.3 菱形操作（二）
        /**
        EventHandlerGroup<Trade> group = disruptor.handleEventsWith(new Handler1(), new Handler2());
        group.then(new Handler3());
         **/


        //2.4 六边形操作
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        disruptor.handleEventsWith(h1, h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);


        //3. 启动
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);

        long begin = System.currentTimeMillis();

        es2.submit(new TradePushlisher(latch, disruptor));

        latch.await();//countDown之后才往下走

        disruptor.shutdown();
        es2.shutdown();
        es1.shutdown();
        System.out.println("总耗时：" + (System.currentTimeMillis() - begin));

    }
}
