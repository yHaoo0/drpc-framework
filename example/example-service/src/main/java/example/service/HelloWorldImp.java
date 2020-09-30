package example.service;

import example.api.HelloWorld;
// 接口的服务实现
public class HelloWorldImp implements HelloWorld {
    String head;

    public HelloWorldImp(String head) {
        this.head = head;
    }

    @Override
    public String sayHello(String message) {
        return head + message;
    }
}
