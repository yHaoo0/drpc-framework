package coding.dreamlash.drpcframework.netty.service;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcRequest;
import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponseCode;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Netty Server 处理请求事件
 * @author yhao
 * @createDate 2020-09-06 08:50
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    private final ServiceProvider provider;

    public NettyServerHandler(ServiceProvider provider){
        this.provider = provider;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RpcRequest request = (RpcRequest) msg;

            switch (request.getType()){
                case RPC_REQUEST:
                    rpcRequestHandler(ctx, request);
                    break;
                case HEART_BEAT:
                    rpcHeartBeatHandler(ctx, request);
                    break;
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE) {
                log.debug("idle check happen, so close the connection");
                ctx.close();
            }
        } else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * RPC请求信息动作
     * @param ctx
     * @param request
     */
    private void rpcRequestHandler(ChannelHandlerContext ctx, RpcRequest request){
        Object result = null;
        RpcResponse<Object> response;
        log.debug("Get request:{}", request.toRpcServiceProperties());
        try {
            result = invokeTargetMethod(request);
            if(ctx.channel().isActive() && ctx.channel().isWritable()){
                response = RpcResponse.success(result, request.getRequestId());
            } else {
                response = RpcResponse.fail(RpcResponseCode.FAIL);
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            log.warn("invoke mthod error:{}", e.getMessage());
            log.warn(e.getStackTrace().toString());
            response = RpcResponse.fail(RpcResponseCode.INVOKE_FAIL);
        }

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        log.debug("Return response:{}", response.getMessage());
    }

    /**
     * RPC 心跳动作
     * @param ctx
     * @param request
     */
    private void rpcHeartBeatHandler(ChannelHandlerContext ctx, RpcRequest request){
        // log.debug("Heart Beat");
        return;
    }

    /**
     * 基于反射获取响应体数据
     * @param request
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object invokeTargetMethod(RpcRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object result;
        Object service = provider.getService(request.toRpcServiceProperties());
        Method method = service.getClass().getMethod(request.getMethodName(), request.getParameType());
        result = method.invoke(service, request.getParame());
        return result;
    }
}
