package example.service;

import coding.dreamlash.drpcframework.rpc.core.factory.DrpcService;
import example.api.HelloWorld;
// 服务实体工厂
public class ServiceFactory {
    String head;

    public ServiceFactory(String head) {
        this.head = head;
    }

    @DrpcService(serviceName = "hello", version = "test")
    public HelloWorld hello(){
        return new HelloWorldImp(head);
    }
}
