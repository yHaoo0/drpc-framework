package coding.dreamlash.drpcframework.netty.service;

import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import coding.dreamlash.drpcframework.rpc.netty.kryo.KryoDecoder;
import coding.dreamlash.drpcframework.rpc.netty.kryo.KryoEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Netty Server的 ServerBootstrap启动类。
 * 启动监听和数据传输端口的类
 * @author yhao
 * @createDate 2020-09-06 08:30
 */
public class NettyServerBoot {
    private static Logger log = LoggerFactory.getLogger(NettyServerBoot.class);
    private InetSocketAddress socketAddress;
    private ServiceProvider serviceProvider;

    public NettyServerBoot(InetSocketAddress socketAddress, ServiceProvider serviceProvider) {
        this.socketAddress = socketAddress;
        this.serviceProvider = serviceProvider;
    }

    /**
     * Netty 服务端启动动作
     */
    public void start(){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder", new KryoDecoder());
                            ch.pipeline().addLast("encoder", new KryoEncoder());
                            ch.pipeline().addLast(new NettyServerHandler(serviceProvider));
                        }
                    });
            log.info("The Netty server starts and starts listening. server ip: {}", socketAddress.getHostString() + ":" + socketAddress.getPort());
            ChannelFuture f = bootstrap.bind(socketAddress.getHostString(), socketAddress.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.warn("occur coding.dreamlash.drpc_framework.rpc.core.exception when start server:");
            log.warn(e.getStackTrace().toString());
        } finally {
            log.info("shutdown bossGroup and workerGroup");
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
