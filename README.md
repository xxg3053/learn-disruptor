

#### Disruptor Quick Start
[代码](https://github.com/xxg3053/learn-disruptor/blob/master/disruptor/src/main/java/com/kenfo/disruptor/quickstart/Main.java)
1. 建立一个工厂Event类， 用于创建Event类实例对象
2. 需要有一个监听事件类，用于处理数据（Event类）
3. 实例化Disruptor实例，配置一些列参数，编写Distruptor核心组件
4. 编写生产者组件，向Distruptor容器中投递数据

```sql
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.3.2</version>
</dependency>
```

#### Disruptor核心原理
- 初看Disruptor， 给人的印象就是RingBuffer是其核心，生产者向Ringbuffer中写入元素，消费者从RingBuffer中消费元素
- RingBuffer是一个环，首尾相接的环
- RingBuffer它用户在不同上下文（线程）间传递数据的buffer   
- RingBuffer拥有一个序号，这个序号指向数组中下一个可用元素   
- 生产者慢，消费者快，则消费者会等待，反之生产者等待
- 随着不停的填充这个buffer，这个序号会一直增长，一直绕过这个环
- 要找到数组中当前序号指向的元素，可以通过mod操作：sequence mod array length = array index(取模操作)， 加入Ringbuffer长10，第12个元素的位置：12%10=2
- 如果长度是2的N次方更有利于基于二进制的计算机计算

###### RingBuffer
- RingBuffer: 基于数组的缓存实现，也是创建sequencer与定义waitStratergy的入口
- Disruptor: 持有RingBuffer, 消费者线程池Executor, 消费者集合ConSumerRepository等引用

###### Sequence
- 通过顺序递增的序号来编号，管理进行交换的数据（事件）
- 对数据（事件）的处理过程总是沿着序号逐个递增处理
- 一个Sequence用于跟踪表识某个特定的事件处理者（RingBuffer/Producer/Consumer）的处理进度
- Sequence可以看成是一个AtomicLong用于标识进度
- 还有另外一个目的就是防止不同Sequence之间Cup缓存伪共享（Flase Sharing）的问题

###### Sequencer
- Sequencer是Disruptor的真正核心
- 此接口有两个实现类
  - SingleProducerSequencer
  - MultiProducerSequencer
- 主要实现生产者和消费者之间快速、正确的传递数据的并发算法

###### Sequence Barrier
- 用于保持对RingBuffer的Main Published Sequence(Producer)和Consumer之间的平衡关系；Sequence Barrier还定义了决定Consumer是否还有可处理的事件的逻辑

###### WaitStrategy
- 决定一个消费者将如何等待生产者将Event置入Disruptor
- 主要策略有：
    - BlockingWaitStrategy
    - SleepingWaitStrategy
    - YieldingWaitStrategy
    - ...
- BlockingWaitStrategy是最低效能的策略，但其对cpu的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
- SleepingWaitStrategy的性能表现跟BlockingWaitStrategy差不多，对cpu的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
- YieldingWaitStrategy的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小雨CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性

###### Event
- 从生产者到消费者过程中所处理的数据单元
- Disruptor中没有代码表示Event,因为它完全是由用户定义的

###### EventProcessor
- 主要事件循环，处理Disruptor中的Event，拥有消息者的Sequence
- 它有一个实现类是BatchEventProcessor, 包含了Event loop有效的实现，并且将回调到一个EventHandler接口的实现对象


###### EventHandler
- 有用户实现并且代表了Disruptor中的一个消费者的接口，也就是我们的消费者逻辑都需要写在里面

###### WorkProcessor
- 确保每个sequence只被一个消费者，在同一个workPool中处理多个WorkProcessor不会消费同样的sequence



#### 并行计算
##### 串、并行操作
```
EventHandlerGroup<T> handleEventsWith(final EventHandler<? super T> ...handlers)
```
- 串行操作： 使用链式调用的方式
    - [代码](https://github.com/xxg3053/learn-disruptor/blob/master/disruptor/src/main/java/com/kenfo/disruptor/high/chain/Main.java)
- 并行操作：使用单独调用的方式
    - [代码](https://github.com/xxg3053/learn-disruptor/blob/master/disruptor/src/main/java/com/kenfo/disruptor/high/chain/Main.java)

##### 多边形高端操作
```
Handler1 h1 = new Handler1();
Handler2 h2 = new Handler2();
Handler3 h3 = new Handler3();
Handler4 h4 = new Handler4();
Handler5 h5 = new Handler5();
disruptor.handleEventsWith(h1, h4);
disruptor.after(h1).handleEventsWith(h2);
disruptor.after(h4).handleEventsWith(h5);
disruptor.after(h2, h5).handleEventsWith(h3);

```

#### 多生产者，多消费者模型
[代码](https://github.com/xxg3053/learn-disruptor/blob/master/disruptor/src/main/java/com/kenfo/disruptor/high/multil/Main.java)


#### Netty 与 disruptor


###### jboss marshalling


#### 分布式统一ID生成策略
1. zookeeper
2. redis

业界主流的分布式ID生成器的策略是：   
1. 提前加载，也就是预加载的机制（加载到内存）
    - 并发的获取，采用Disruptor框架去提升性能
    - 监控，按配置规则，用量在去生成
2. 单点生成方式
    - 固定的一个机器节点来生成一个唯一的ID，好处是能做到全局唯一
    - 需要相应的业务规则：机器码 + 时间戳 + 自增序列 （NTP时间同步问题）
    - NTP是网络时间协议（Network Time Protocol），它是用来同步网络中各个计算机的时间协议
    
