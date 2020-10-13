package example;

import coding.dreamlash.drpcframework.common.NacosProps;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;
import coding.dreamlash.drpcframework.netty.client.NettyClientApplication;
import coding.dreamlash.drpcframework.netty.client.NettyClientProps;
import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import example.api.HelloWorld;

public class NettyClientExample {
    public static void main(String[] args) {
        // 启动Nacos 服务中心
        NacosProps nacosProps = new NacosProps();
        nacosProps.serverAddr = "127.0.0.1:8848";
        ServiceCenter serviceCenter = new NacosServicesCenter(nacosProps);
        serviceCenter.enable();
        // 启动Netty Client
        NettyClientProps clientProps = new NettyClientProps();
        RpcClientApplication application = new NettyClientApplication(clientProps);
        application.enable(serviceCenter, new ClientFactory());

        // 获取代理
        HelloWorld hello1 = application.getProxy("hello1");
        System.out.println(hello1.sayHello(",Bye"));

        // 失败时调用客户端实例
        HelloWorld hello2 = application.getProxy("hello2");
        System.out.println(hello2.sayHello(",Bye"));

        // netty监听主线程，如果要结束程序，需要关闭监听
        application.shutdown();
    }
}
