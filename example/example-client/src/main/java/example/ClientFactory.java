package example;

import coding.dreamlash.drpcframework.rpc.core.factory.DrpcClient;
import example.api.HelloWorld;
// 客户实体的工厂类。 用于创建客户端的实体类
public class ClientFactory {
    /**
     * 将该方法注解为返回客户实例的方法
     * 该方法返回实例将按配置查找服务，如果服务失败，则返回该实例的内容
     * 只支持无参方法
     * 只支持返回对象为代理的接口类，不能为实现类
     * clientName ： 该实例的名字。用于获取该实例
     * serviceName : 向服务中心申请的服务名
     * version ： 向服务中心申请的版本号
     * group ： 向服务中心申请的组 默认 ""
     * isSingleton : 该对象是否使用单例模式
     * @return 当返回失败时调用的实体
     */
    @DrpcClient(clientName = "hello1", serviceName = "hello", version = "test", isSingleton = true)
    public HelloWorld hello1(){
        return new HelloWorldImp();
    }

    @DrpcClient(clientName = "hello2", serviceName = "hello", version = "not_found")
    public HelloWorld hello2(){
        return new HelloWorldImp();
    }
}
