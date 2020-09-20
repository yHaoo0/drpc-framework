package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryClientProxy extends ClientProxy{
    private Map<String, ProxyProperties> proxyMap;
    private Object factory;

    public FactoryClientProxy(ClientTransport clientTransport, Object factory) {
        super(clientTransport);
        this.factory = factory;
        proxyMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T getProxy(String clientName) throws DrpcException{

        if(!proxyMap.containsKey(clientName)){
            String mesg = String.format("not found the proxy {ClientName: %s} return null", clientName);
            throw new DrpcException(mesg);
        }
        ProxyProperties proxyProperties = proxyMap.get(clientName);
        Method method = proxyProperties.method;
        RpcServiceProperties properties = proxyProperties.properties;
        Class inte = proxyProperties.clazz;

        try {
            Object client = method.invoke(factory);
            Object proxy = super.createProxy(inte,properties, client);
            return (T) proxy;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DrpcException("reflection failure service:"+clientName);
        }
    }

    public void putProxy(String clientName, RpcServiceProperties properties, Class clazz, Method o) {
        if (proxyMap.containsValue(clientName)) {
            log.warn("Duplicate client name, creating failed: " + clientName);
            return;
        }
        proxyMap.put(clientName, new ProxyProperties(properties, clazz, o));
    }

    private class ProxyProperties {
        RpcServiceProperties properties;
        Class clazz;
        Method method;

        public ProxyProperties(RpcServiceProperties properties, Class clazz, Method method) {
            this.properties = properties;
            this.clazz = clazz;
            this.method = method;
        }
    }

}
