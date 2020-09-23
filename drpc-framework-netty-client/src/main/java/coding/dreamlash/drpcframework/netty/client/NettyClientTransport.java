package coding.dreamlash.drpcframework.netty.client;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcRequest;
import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponseCode;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import io.netty.channel.Channel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * 通过Channel发送请求的封装
 * @author yhao
 * @createDate 2020-9-23
 */
public class NettyClientTransport implements ClientTransport {
    private static Logger log = LogManager.getLogger();
    private final ChannelProvider channelProvider;
    private final ResponseProvider responseProvider;
    private final ServiceCenter serviceCenter;

    public NettyClientTransport(ChannelProvider channelProvider, ResponseProvider responseProvider, ServiceCenter serviceCenter) {
        this.channelProvider = channelProvider;
        this.responseProvider = responseProvider;
        this.serviceCenter = serviceCenter;
    }

    @Override
    public RpcResponse<Object> sendRpcRequest(RpcRequest rpcRequest){
        log.printf(Level.DEBUG, "start send request, serviceName: %s, id: %s", rpcRequest.getServiceName(), rpcRequest.getRequestId());
        InetSocketAddress address = serviceCenter.discoveryService(rpcRequest.toRpcServiceProperties());
        if(address == null){
            log.warn("not find service: " + rpcRequest.toRpcServiceProperties().toString());
            return null;
        }

        Channel channel = channelProvider.get(address.getHostString(), address.getPort());
        if(channel == null){
            return RpcResponse.fail(RpcResponseCode.FAIL);
        }

        CompletableFuture<RpcResponse<Object>> future = new CompletableFuture<>();
        responseProvider.creat(rpcRequest.getRequestId(), future);
        try {
            channel.writeAndFlush(rpcRequest).sync();
            RpcResponse<Object> response = future.get();
            log.printf(Level.DEBUG, "end send request, serviceName: %s, id: %s", rpcRequest.getServiceName(), response.getRequestId());
            responseProvider.remove(rpcRequest.getRequestId());
            return response;
        } catch (Exception e) {
            responseProvider.remove(rpcRequest.getRequestId());
            e.printStackTrace();
            throw new DrpcException("the response miss");
        }
    }
}
