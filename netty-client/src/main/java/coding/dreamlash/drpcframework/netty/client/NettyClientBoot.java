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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Netty客户端启动
 * @author yhao
 * @createDate 2020-9-23
 */
public class NettyClientBoot {
    private static Logger log = LoggerFactory.getLogger(NettyClientBoot.class);
    private Bootstrap bootstrap = new Bootstrap();
    private NioEventLoopGroup group = new NioEventLoopGroup();

    public NettyClientBoot(NettyClientProps props, NettyClientHandler handler) {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(props.idleReader, props.idleWrite, props.idleAll, TimeUnit.SECONDS));
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
                log.debug("The client has connected [{}:{}] successful!", host, port);
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
