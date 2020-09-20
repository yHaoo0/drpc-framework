package coding.dreamlash.drpcframework.rpc.core.provider;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.InetSocketAddress;
import java.util.Map;

public abstract class ServiceProvider {
    protected static Logger log = LogManager.getLogger();
    private ServiceCenter serviceCenter;
    private InetSocketAddress socketAddress;

    public ServiceProvider(ServiceCenter serviceCenter, InetSocketAddress socketAddress) {
        this.serviceCenter = serviceCenter;
        this.socketAddress = socketAddress;
    }

    public abstract Object getService(RpcServiceProperties rpcServiceProperties) throws DrpcException;

    protected boolean publishService(RpcServiceProperties properties){
        return serviceCenter.registerService(properties, socketAddress);
    }


}
