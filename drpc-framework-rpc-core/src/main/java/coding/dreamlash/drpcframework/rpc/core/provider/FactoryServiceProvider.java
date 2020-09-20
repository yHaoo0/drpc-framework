package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryServiceProvider extends ServiceProvider{
    private Map<String, Method> serviceMap;
    private Object factory;

    public FactoryServiceProvider(ServiceCenter serviceCenter, InetSocketAddress socketAddress, Object factory) {
        super(serviceCenter, socketAddress);
        this.factory = factory;
        serviceMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        if(serviceMap.containsKey(rpcServiceProperties.toString())){
            try {
                Method method = serviceMap.get(rpcServiceProperties.toString());
                return method.invoke(factory);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new DrpcException("reflection failure service:"+rpcServiceProperties);
            }
        }else {
            throw new DrpcException("the service was not found:"+rpcServiceProperties);
        }
    }

    public void publishService(RpcServiceProperties rpcServiceProperties, Method service) {
        boolean sucess;
        String serviceName = rpcServiceProperties.toString();

        if(serviceMap.containsKey(serviceName)){
            log.warn("Duplicate service name, publishing failed: " + service.getClass().getName());
            return;
        }

        sucess = super.publishService(rpcServiceProperties);

        if(sucess){
            serviceMap.put(serviceName, service);
            log.info("Publishing service: "+ serviceName);
        } else {
            log.warn("Failed to register service with the service center: " + serviceName);
        }
    }
}
