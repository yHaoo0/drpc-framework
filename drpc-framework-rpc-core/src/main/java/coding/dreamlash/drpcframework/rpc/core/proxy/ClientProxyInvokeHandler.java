package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.*;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 动态代理动作
 * @author yhao
 * @createDate 2020-9-23
 */
public class ClientProxyInvokeHandler implements InvocationHandler {
    private static Logger log = LoggerFactory.getLogger(ClientProxyInvokeHandler.class);
    private ClientTransport clientTransport;
    private RpcServiceProperties properties;
    private Object object;


    public ClientProxyInvokeHandler(ClientTransport clientTransport, RpcServiceProperties properties, Object object) {
        this.clientTransport = clientTransport;
        this.properties = properties;
        this.object = object;
    }




    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        // 构建请求
        RpcRequest rpcRequest = new RpcRequest(
                properties.getVersion(),
                properties.getGroup(),
                UUID.randomUUID().toString(),
                properties.getServiceName(),
                method.getName(),
                method.getParameterTypes(),
                args,
                RpcMessageType.RPC_REQUEST
        );

        RpcResponse<Object> response = clientTransport.sendRpcRequest(rpcRequest);
        if (response == null || response.getCode() != RpcResponseCode.SUCCESS.code) {
            String message = response == null ? null : response.getMessage();
            log.warn("service {} is exception for respesponse: {}",
                    rpcRequest.toRpcServiceProperties().toString(), message);
            return method.invoke(object, args);
        }
        return response.getData();
    }
}
