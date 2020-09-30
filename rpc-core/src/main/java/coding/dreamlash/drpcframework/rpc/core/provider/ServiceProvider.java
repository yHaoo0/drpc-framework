package coding.dreamlash.drpcframework.rpc.core.provider;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供者，存储服务实体以及其返回
 * @author yhao
 * @createDate 2020-9-23
 */
public class ServiceProvider {
    private ServiceCenter serviceCenter;
    private InetSocketAddress socketAddress;
    private Map<String, ServiceEnitity> store;


    public ServiceProvider(ServiceCenter serviceCenter, InetSocketAddress socketAddress) {
        this.serviceCenter = serviceCenter;
        this.socketAddress = socketAddress;
        this.store = new ConcurrentHashMap<>();
    }

    /**
     * 返回存储中的服务实体
     * @param rpcServiceProperties 服务属性
     * @return 服务实体
     * @throws DrpcException
     */
    public Object get(RpcServiceProperties rpcServiceProperties) throws DrpcException {
        String key = rpcServiceProperties.toString();
        if(!store.containsKey(key)){
            throw new DrpcException("the service was not found:"+rpcServiceProperties);
        }

        return store.get(key).get();
    }

    /**
     * 注册并存储实体代理，如果以及存储相同的服务信息的代理，则返回false
     * 如果无法注册到注册中心，返回异常。
     * @param properties
     * @param enitity
     * @return
     */
    public boolean put(RpcServiceProperties properties, ServiceEnitity enitity) throws DrpcException {
        String key = properties.toString();
        if(store.containsValue(key)){
            return false;
        }

        if(publishService(properties)){
            store.put(key, enitity);
        } else {
            throw new DrpcException("publish service fiale");
        }
        return true;
    }



    /**
     * 向服务中心登录服务信息
     * @param properties 服务属性
     * @return
     */
    private boolean publishService(RpcServiceProperties properties){
        return serviceCenter.registerService(properties, socketAddress);
    }


}
