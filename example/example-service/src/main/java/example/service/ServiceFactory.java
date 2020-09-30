package example.service;

import coding.dreamlash.drpcframework.rpc.core.factory.DrpcService;
import example.api.HelloWorld;
// 服务实体的工厂类。 用于创建服务的实体类
public class ServiceFactory {
    String head;

    public ServiceFactory(String head) {
        this.head = head;
    }

    /**
     * 将该方法注解为返回服务实例的方法
     * 该方法返回实例作为服务注册到服务中心
     * 只支持无参方法
     * 只支持返回对象为代理的接口类，不能为实现类
     * serviceName : 服务名
     * version ： 版本号
     * group ： 组 默认 ""
     * isSingleton : 该对象是否使用单例模式
     * @return
     */
    @DrpcService(serviceName = "hello", version = "test")
    public HelloWorld hello(){
        return new HelloWorldImp(head);
    }
}
