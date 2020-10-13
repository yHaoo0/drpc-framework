package example.service;

import coding.dreamlash.drpcframework.common.NacosProps;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;
import coding.dreamlash.drpcframework.netty.service.NettyServerApplication;
import coding.dreamlash.drpcframework.netty.service.NettyServerProps;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

public class NettyServiceExample {
    public static void main(String[] args) {
        // 启动Nacos 服务中心
        NacosProps nacosProps = new NacosProps();
        nacosProps.serverAddr = "127.0.0.1:8848";
        ServiceCenter serviceCenter = new NacosServicesCenter(nacosProps);
        serviceCenter.enable();
        // 启动Netty Service
        NettyServerProps serverProps = new NettyServerProps();
        serverProps.host = "127.0.0.1";
        serverProps.port = 8090;
        NettyServerApplication application = new NettyServerApplication(serverProps, serviceCenter);
        // 扫描提供服务实体的工厂类，登录服务到服务中心，并开始监听端口
        application.enable(new ServiceFactory("Hello World"));
    }
}
