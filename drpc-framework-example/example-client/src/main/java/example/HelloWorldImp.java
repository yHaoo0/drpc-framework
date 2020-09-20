package example;

import example.api.HelloWorld;

public class HelloWorldImp implements HelloWorld {
    @Override
    public String sayHello(String message) {
        return "response is exception";
    }
}
