package example;

import coding.dreamlash.drpcframework.rpc.core.factory.DrpcClient;
import example.api.HelloWorld;

public class ClientFactory {
    @DrpcClient(clientName = "hello1", serviceName = "hello", version = "test")
    public HelloWorld hello1(){
        return new HelloWorldImp();
    }

    @DrpcClient(clientName = "hello2", serviceName = "hello", version = "not_found")
    public HelloWorld hello2(){
        return new HelloWorldImp();
    }
}
