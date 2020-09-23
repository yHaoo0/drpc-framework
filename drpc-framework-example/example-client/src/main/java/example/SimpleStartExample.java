package example;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;
import example.api.HelloWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleStartExample {
    public static void main(String[] args) throws InterruptedException {
        // 实例化DrpcSimpleApplication，appliction会读取`drpc.properties`文件，并创建服务中心
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        // 传入工厂类登录服务，启用客户端，开始监听服务
        application.enableClient(new ClientFactory());
        // 获取服务代理
        HelloWorld hello1 = application.proxy("hello1");
        System.out.println(hello1.sayHello("Hello, world "));
        // 当服务失败时，返回客户端实现内容
        HelloWorld hello2 = application.proxy("hello2");
        System.out.println(hello2.sayHello("Hello, world"));
        // 关闭客户端监听
        application.shutdownClient();
    }
}
