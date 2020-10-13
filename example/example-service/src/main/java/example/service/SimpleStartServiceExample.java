package example.service;

import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;

public class SimpleStartServiceExample {
    public static void main(String[] args) {
        // 默认读取配置文件drpc.properties 如果不存在 读取drpc.yml 登录到ConfigContextManager中，dataid为drpc
        // 然后从ConfigContextManager中取id为drpc配置初始化服务
        // 可以使用DrpcSimpleApplication(String configId) 指定ConfigContextManager中登录的配置属性
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        application.enableService(new ServiceFactory("Hello Simple World"));
    }
}
