package coding.dreamlash.drpcframework.netty.client;

import coding.dreamlash.drpcframework.rpc.netty.kryo.KryoDecoder;
import coding.dreamlash.drpcframework.rpc.netty.kryo.KryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Netty客户端启动
 * @author yhao
 * @createDate 2020-9-23
 */
public class NettyClientBoot {
    private static Logger log = LogManager.getLogger();
    private Bootstrap bootstrap = new Bootstrap();
    private NioEventLoopGroup group = new NioEventLoopGroup();

    public NettyClientBoot(NettyClientHandler handler) {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        pipeline.addLast("encoder", new KryoEncoder());
                        pipeline.addLast("decoder", new KryoDecoder());
                        pipeline.addLast(handler.clone());
                    }
                });
        log.info("Netty Client BootStrap initialized finally");
    }

    /**
     * Channel 链接
     * @param host
     * @param port
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Channel doConnect(String host, int port) throws InterruptedException, ExecutionException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{ " + host + port + "}] successful!");
                completableFuture.complete(future.channel());
            } else {
                future.cause().printStackTrace();
                completableFuture.complete(null);
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    /**
     * 关闭客户端监听
     */
    public void shutdown(){
        group.shutdownGracefully();
    }
}
