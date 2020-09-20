package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonClientProxy extends ClientProxy{
    private Map<String, Object> proxyMap;

    public SingletonClientProxy(ClientTransport clientTransport) {
        super(clientTransport);
        proxyMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T getProxy(String clientName){
        if(!proxyMap.containsKey(clientName)){
            String mesg = String.format("not found the proxy {ClientName: %s} return null", clientName);
            throw new DrpcException(mesg);
        }
        return (T) proxyMap.get(clientName);
    }

    public void putProxy(String clientName, RpcServiceProperties properties, Class clazz, Object o){
        if(proxyMap.containsValue(clientName)){
            log.warn("Duplicate client name, creating failed: " + clientName);
            return;
        }

        Object proxyO = super.createProxy(clazz, properties, o);
        proxyMap.put(clientName, proxyO);
    }
}
