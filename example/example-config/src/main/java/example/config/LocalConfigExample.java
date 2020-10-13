package example.config;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.manager.ConfigContextManager;
import coding.dreamlash.drpcframework.common.configfile.manager.LocalConfigContextManager;

public class LocalConfigExample {
    public static void main(String[] args) {
        // 将配置文件登录到ConfigContextManager中
        // 配置信息和NacosContextManager使用相同的静态ConfigContextManager，相同dataId会被覆盖
        LocalConfigContextManager.load("local", "local.yaml", ConfigContextType.YAML, true);
        // 使用id获取配置文件
        TestPojo local = ConfigContextManager.get("local", TestPojo.class);
        System.out.println(local.name);

        // 扫描指定的配置文件中配置文件信息进行登录，该方法不实现递归扫描
        LocalConfigContextManager.scan("local.yaml", ConfigContextType.YAML);
        TestPojo local1 = ConfigContextManager.get("local1", TestPojo.class);
        System.out.println(local1.name);
        TestPojo local2 = ConfigContextManager.get("local2", TestPojo.class);
        System.out.println(local2.name);
    }
}
