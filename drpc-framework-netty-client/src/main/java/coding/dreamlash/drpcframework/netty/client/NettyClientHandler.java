package coding.dreamlash.drpcframework.netty.client;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * netty 通信动作
 * @author yhao
 * @createDate 2020-9-23
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LogManager.getLogger();
    private final ResponseProvider responseProvider;
    private final ChannelProvider channelProvider;

    public NettyClientHandler(ResponseProvider responseProvider, ChannelProvider channelProvider) {
        this.responseProvider = responseProvider;
        this.channelProvider = channelProvider;
    }

    public NettyClientHandler clone(){
        return new NettyClientHandler(responseProvider, channelProvider);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcResponse){
            RpcResponse<Object> response = (RpcResponse<Object>) msg;
            responseProvider.complete(response);
        } else {
            log.warn("response not instanceof RpcResponse for channel: %s", ctx.channel().remoteAddress());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                log.printf(Level.DEBUG,"write idle happen %s:%d", address.getHostString(), address.getPort());
                Channel channel = channelProvider.get(address.getHostString(), address.getPort());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("client catch exception：" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
