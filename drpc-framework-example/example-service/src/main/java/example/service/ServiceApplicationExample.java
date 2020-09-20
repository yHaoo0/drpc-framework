package example.service;

import coding.dreamlash.drpcframework.common.utils.PropertiesLoaderUtils;
import coding.dreamlash.drpcframework.netty.service.NettyServerApplication;
import coding.dreamlash.drpcframework.rpc.core.application.RpcServiceApplication;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;

import java.io.IOException;
import java.util.Properties;

public class ServiceApplicationExample {
    public static void main(String[] args) throws IOException {
        // properties 配置
        Properties properties = PropertiesLoaderUtils.loadProperties("test.properties");
        // 实例化 服务中心
        ServiceCenter serviceCenter = new NacosServicesCenter();
        serviceCenter.enable(properties);
        // 实例化 服务端
        RpcServiceApplication application = new NettyServerApplication(properties, serviceCenter);
        application.enable(new ServiceFactory("Service Application: "), true);
    }
}
