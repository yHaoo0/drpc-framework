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
        // 1 properties 读取配置文件
        Properties properties = PropertiesLoaderUtils.loadProperties("test.properties");
        // 2.1 实例化 服务中心
        ServiceCenter serviceCenter = new NacosServicesCenter();
        // 2.2 传入配置属性并启用服务中心
        serviceCenter.enable(properties);
        // 3.1 实例化服务中心，传入配置属性以及服务中心
        RpcServiceApplication application = new NettyServerApplication(properties, serviceCenter);
        // 3.2 传入工厂的实例，启动服务监听
        application.enable(new ServiceFactory("Service Application: "));
    }
}
