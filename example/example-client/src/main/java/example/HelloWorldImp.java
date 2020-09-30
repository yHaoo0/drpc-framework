package example;

import example.api.HelloWorld;
// 接口的客户端实现，当无法获取正常响应时返回
public class HelloWorldImp implements HelloWorld {
    @Override
    public String sayHello(String message) {
        return "response is exception";
    }
}
