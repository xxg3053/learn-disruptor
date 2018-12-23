package com.kenfo.review;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并发编程
 */
public class Review {

    public static void main(String[] args) {


        ConcurrentHashMap<String , String> map = new ConcurrentHashMap<>();

        /**
         * 全量拷贝，适合读多写少，数量不大的时候
         */
        CopyOnWriteArrayList<String> cowal = new CopyOnWriteArrayList<>();
        cowal.add("a");


        AtomicLong count = new AtomicLong(1);
        boolean flag = count.compareAndSet(0, 2);
        System.out.println(flag);
        System.out.println(count.get());

        /**
         * 1. 不建议使用工厂类里面创建线程池的方法
         * 2. 怎么定义线程池数量
         *  计算机密集型与IO密集型
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                5,
                Runtime.getRuntime().availableProcessors() * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),//有界队列
                r -> {
                    Thread t = new Thread(r);
                    //设置线程相关属性
                    t.setName("order-thread");
                    return t;
                },
                (r, executor) -> {
                    System.out.println("拒绝策略");
                    //日志、补偿等等
                }
        );



    }
}
