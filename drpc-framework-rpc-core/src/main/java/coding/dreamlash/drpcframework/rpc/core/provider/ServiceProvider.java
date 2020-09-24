package coding.dreamlash.drpcframework.rpc.core.provider;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

import java.net.InetSocketAddress;

/**
 * 服务提供者，存储服务实体以及其返回
 * @author yhao
 * @createDate 2020-9-23
 */
public abstract class ServiceProvider {
    private ServiceCenter serviceCenter;
    private InetSocketAddress socketAddress;


    public ServiceProvider(ServiceCenter serviceCenter, InetSocketAddress socketAddress) {
        this.serviceCenter = serviceCenter;
        this.socketAddress = socketAddress;
    }

    /**
     * 返回存储中的服务实体
     * @param rpcServiceProperties 服务属性
     * @return 服务实体
     * @throws DrpcException
     */
    public abstract Object getService(RpcServiceProperties rpcServiceProperties) throws DrpcException;

    /**
     * 向服务中心登录服务信息
     * @param properties 服务属性
     * @return
     */
    protected boolean publishService(RpcServiceProperties properties){
        return serviceCenter.registerService(properties, socketAddress);
    }


}
