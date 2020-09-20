package example;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;
import example.api.HelloWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleStartExample {
    static Logger log = LogManager.getLogger();
    public static void main(String[] args) {
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        application.enableClient(new ClientFactory(), false);

        for (int i = 0; i < 10; i++) {
            HelloWorld hello1 = application.proxy("hello1");
            System.out.println(hello1.sayHello("Hello, world " + i));
        }

        HelloWorld hello2 = application.proxy("hello2");
        System.out.println(hello2.sayHello("Hello, world"));

        application.shutdownClient();
    }
}
