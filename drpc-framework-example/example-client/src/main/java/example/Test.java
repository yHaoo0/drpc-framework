package example;

import coding.dreamlash.drpcframework.netty.client.NettyClientHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NettyClientBoot boot = new NettyClientBoot();
        Channel channel1 = boot.doConnect("127.0.0.1", 8080);
        System.out.println("channel1 success" + channel1.isActive());
        Channel channel2 = boot.doConnect("127.0.0.1", 8090);
        System.out.println("channel2 success" + channel1.isActive() + channel2.isActive());
    }


}

class NettyClientBoot {
    private Bootstrap bootstrap = new Bootstrap();
    private NioEventLoopGroup group = new NioEventLoopGroup();

    public NettyClientBoot() {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                    }
                });

    }

    public Channel doConnect(String host, int port) throws InterruptedException, ExecutionException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                future.cause().printStackTrace();
                completableFuture.complete(null);
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public void shutdown(){
        group.shutdownGracefully();
    }
}


