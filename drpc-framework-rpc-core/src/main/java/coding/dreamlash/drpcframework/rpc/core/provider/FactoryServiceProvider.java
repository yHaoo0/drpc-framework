package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂模式服务提供者
 * @author yhao
 * @createDate 2020-9-23
 */
public class FactoryServiceProvider extends ServiceProvider{
    private final static Logger log = LoggerFactory.getLogger(FactoryServiceProvider.class);
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

    /**
     * 存储以及发布服务
     * @param rpcServiceProperties 服务属性
     * @param service 提供服务实体方法
     */
    public void publishService(RpcServiceProperties rpcServiceProperties, Method service) {
        boolean sucess;
        String serviceName = rpcServiceProperties.toString();

        if(serviceMap.containsKey(serviceName)){
            log.warn("Duplicate service name, publishing failed: {}", service.getClass().getName());
            return;
        }

        sucess = super.publishService(rpcServiceProperties);

        if(sucess){
            serviceMap.put(serviceName, service);
            log.info("Publishing service:{} ", serviceName);
        } else {
            log.warn("Failed to register service with the service center:{}", serviceName);
        }
    }
}
