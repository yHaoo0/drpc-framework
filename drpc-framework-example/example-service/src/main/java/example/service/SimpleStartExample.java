package example.service;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;

public class SimpleStartExample {
    public static void main(String[] args) {
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        application.enableService(new ServiceFactory("Simple Start: "));
    }
}
