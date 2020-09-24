package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.*;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;

import java.lang.reflect.Proxy;

/**
 * 客户端代理实现
 * @author yhao
 * @createDate 2020-9-23
 */
public abstract class ClientProxy {
    private ClientTransport clientTransport;

    public ClientProxy(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }

    /**
     * 通过clientName获取实例
     * @param clientName
     * @param <T>
     * @return
     * @throws DrpcException
     */
    public abstract  <T> T getProxy(String clientName) throws DrpcException;

    /**
     * 通过Java创建代理对象
     * @param clazz
     * @param properties
     * @param client
     * @return
     */
    protected Object createProxy(Class clazz, RpcServiceProperties properties, Object client){
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new ClientProxyInvokeHandler(clientTransport, properties, client));
    }

}
