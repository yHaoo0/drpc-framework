package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.*;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端代理实现
 * @author yhao
 * @createDate 2020-9-23
 */
public class ClientProxy {
    private static final Logger log = LoggerFactory.getLogger(ClientProxy.class);
    private ClientTransport clientTransport;
    private Map<String, ClientEnitiy> store;

    public ClientProxy(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
        store = new ConcurrentHashMap<>();
    }

    /**
     * 通过clientName获取实例
     * @param clientName
     * @param <T>
     * @return
     * @throws DrpcException
     */
    public  <T> T get(String clientName) throws DrpcException{
        if(!store.containsKey(clientName)){
            String mesg = String.format("not found the proxy {ClientName: %s} return null", clientName);
            throw new DrpcException(mesg);
        }
        ClientEnitiy result = store.get(clientName);
        return (T) result.get();
    }

    /**
     * 添加存储新的代理对象，如果clientName重复，则返回false
     * @param clientName
     * @param enitiy
     */
    public boolean put(String clientName, ClientEnitiy enitiy){
        if(store.containsValue(clientName)){
            log.warn("Duplicate client name, creating failed: {}", clientName);
            return false;
        }

        store.put(clientName, enitiy);
        return true;
    }

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
