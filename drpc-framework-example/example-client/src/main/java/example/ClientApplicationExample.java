package example;

import coding.dreamlash.drpcframework.common.utils.PropertiesLoaderUtils;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;
import coding.dreamlash.drpcframework.netty.client.NettyClientApplication;
import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import example.api.HelloWorld;

import java.io.IOException;
import java.util.Properties;

public class ClientApplicationExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1 properties 读取配置文件
        Properties properties = PropertiesLoaderUtils.loadProperties("drpc.properties");
        // 2.1 实例化 服务中心
        ServiceCenter serviceCenter = new NacosServicesCenter();
        // 2.2 传入配置属性并启用服务中心
        serviceCenter.enable(properties);
        // 3.1 实例化客户端
        RpcClientApplication application = new NettyClientApplication();
        // 3.2 传入配置属性，服务中心以及工厂实体
        application.enable(properties, serviceCenter, new ClientFactory(), true);

        // 获取服务代理
        HelloWorld hello1 = application.getProxy("hello1");
        System.out.println(hello1.sayHello("Hello, world "));
        // 当服务失败时，返回客户端实现内容
        HelloWorld hello2 = application.getProxy("hello2");
        System.out.println(hello2.sayHello("Hello, world"));
        // 关闭客户端监听
        application.shutdown();
    }
}
