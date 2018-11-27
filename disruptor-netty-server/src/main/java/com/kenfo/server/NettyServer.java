package com.kenfo.server;


import com.kenfo.codec.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author kenfo
 * @version V1.0
 * @Package com.kenfo.server
 * @Description: netty服务端
 * @date 2018/11/26 下午10:28
 */
public class NettyServer {

    public NettyServer(){
        //1. 创建两个工作线程组：一个用于接收网络请求，另外一个用于实际处理业务的线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //2. 辅助类
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //表示缓存区动态调配（自适应）数据包相差不大的情况比较适合，如果相差大，最好设置为较大的
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓存区 池化操作
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });
            //绑定端口，同步等等请求连接
            ChannelFuture cf = bootstrap.bind(8765).sync();
            System.err.println("Server Startup...");
            cf.channel().closeFuture().sync();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //优雅停机
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.err.println("Sever ShutDown...");
        }


    }

}
