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

/**
 * 对注册中心，服务端，客户端的启动进行封装
 * @author yhao
 * @createDate 2020-9-23
 */
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

    /**
     * 启动客户端监听
     * @param facory 客户端服务实体工厂
     * @param isSingleton 是否启用服务实体单例模式
     */
    public void enableClient(Object facory, boolean isSingleton){
        client = new NettyClientApplication();
        client.enable(properties, serviceCenter, facory, isSingleton);
    }

    public void enableClient(Object facory){
        this.enableClient(facory, true);
    }

    /**
     * 获取服务实体
     * @param clientName
     * @param <T>
     * @return
     */
    public <T> T proxy(String clientName){
        return client.getProxy(clientName);
    }

    /**
     * 关闭客户端监听
     */
    public void shutdownClient(){
        client.shutdown();
    }

    public RpcClientApplication getClient() {
        return client;
    }

    /**
     * 启动服务端监听
     * @param factory 服务实体提供的工厂实例
     * @param isSingleton 是否启用单例模式
     */
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
