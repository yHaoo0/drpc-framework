package coding.dreamlash.drpcframework.simple.start;

import coding.dreamlash.drpcframework.common.utils.PropertiesLoaderUtils;
import coding.dreamlash.drpcframework.netty.client.NettyClientApplication;
import coding.dreamlash.drpcframework.netty.service.NettyServerApplication;
import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.application.RpcServiceApplication;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class DrpcSimpleApplication {
    private static Logger log = LogManager.getLogger();
    private Properties properties;
    private ServiceCenter serviceCenter;
    private RpcClientApplication client;
    private RpcServiceApplication server;

    public DrpcSimpleApplication(){
        this("drpc.properties");
    }

    public DrpcSimpleApplication(String propertiesName){
        try {
            properties = PropertiesLoaderUtils.loadProperties(propertiesName);
        } catch (IOException e) {
            log.warn("not found drpc.properties");
            log.error(e.getStackTrace());
        }
        serviceCenter = new NacosServicesCenter();
        serviceCenter.enable(properties);
    }

    public void enableClient(Object facory, boolean isSingleton){
        client = new NettyClientApplication();
        client.enable(properties, serviceCenter, facory, isSingleton);
    }

    public void enableClient(Object facory){
        this.enableClient(facory, true);
    }

    public <T> T proxy(String clientName){
        return client.getProxy(clientName);
    }

    public void shutdownClient(){
        client.shutdown();
    }

    public RpcClientApplication getClient() {
        return client;
    }

    public void enableService(Object factory, boolean isSingleton){
        server = new NettyServerApplication(properties, serviceCenter);
        server.enable(factory, isSingleton);
    }

    public void enableService(Object factory){
        enableService(factory, true);
    }

    public ServiceCenter getServiceCenter() {
        return serviceCenter;
    }
}
