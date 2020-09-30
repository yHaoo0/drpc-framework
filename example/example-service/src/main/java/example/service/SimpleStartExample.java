package example.service;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;

public class SimpleStartExample {
    public static void main(String[] args) {
        // 实例化DrpcSimpleAppliction，appliction会读取`drpc.properties`文件，并创建服务中心
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        // 启用服务，并注入工厂类，appliction将会组装 `NacosServicesCenter` 以及 `NettyServerApplication`
        // 并且开始监听
        application.enableService(new ServiceFactory("Simple Start: "));
    }
}
