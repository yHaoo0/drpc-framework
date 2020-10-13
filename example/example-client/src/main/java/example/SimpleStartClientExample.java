package example;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;
import example.api.HelloWorld;

public class SimpleStartClientExample {
    public static void main(String[] args) {
        // 默认读取配置文件drpc.properties 如果不存在 读取drpc.yml 登录到ConfigContextManager中，dataid为drpc
        // 然后从ConfigContextManager中取id为drpc配置初始化服务
        // 可以使用DrpcSimpleApplication(String configId) 指定ConfigContextManager中登录的配置属性
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        application.enableClient(new ClientFactory());

        // 获取代理
        HelloWorld hello1 = application.proxy("hello1");
        System.out.println(hello1.sayHello(",Bye"));

        // 失败时调用客户端实例
        HelloWorld hello2 = application.proxy("hello2");
        System.out.println(hello2.sayHello(",Bye"));

        // netty监听主线程，如果要结束程序，需要关闭监听
        application.shutdownClient();
    }
}
