package com.kenfo.disruptor.high.multil;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.disruptor.high.multil
 * @Description: 多消费者 只能实现WorkHandler
 * @date 2018/11/25 下午5:26
 */
public class Consumer implements WorkHandler<Order> {

    private String consumerId;
    private static AtomicInteger count = new AtomicInteger(0);
    private Random random = new Random();

    public Consumer(String consumerId){
        this.consumerId = consumerId;
    }

    public int getCount(){
        return count.get();
    }

    @Override
    public void onEvent(Order order) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        System.out.println("当前消费者： " + this.consumerId + " , 消费信息：" + order.getId());
        count.incrementAndGet();
    }
}
