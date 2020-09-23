package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例模式服务提供者
 * @author yhao
 * @createDate 2020-9-23
 */
public class SingletonServiceProvider extends ServiceProvider{
    private Map<String, Object> serviceMap;

    public SingletonServiceProvider(ServiceCenter serviceCenter, InetSocketAddress socketAddress) {
        super(serviceCenter, socketAddress);
        serviceMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) throws DrpcException {
        if(!serviceMap.containsKey(rpcServiceProperties.toString())){
            throw new DrpcException("the service was not found:"+rpcServiceProperties);
        }
        Object result = serviceMap.get(rpcServiceProperties.toString());
        return result;
    }

    /**
     * 存储以及发布服务
     * @param rpcServiceProperties 服务属性
     * @param service 服务实体
     */
    public void publishService(RpcServiceProperties rpcServiceProperties, Object service) {
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
