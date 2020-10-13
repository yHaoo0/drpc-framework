package example.config;

import coding.dreamlash.drpcframework.common.NacosProps;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.manager.ConfigContextManager;
import coding.dreamlash.drpcframework.common.configfile.manager.LocalConfigContextManager;
import coding.dreamlash.drpcframework.nacos.config.NacosConfigContextManager;
import coding.dreamlash.drpcframework.nacos.config.NacosConfigFactory;
import coding.dreamlash.drpcframework.simple.start.DrpcSimpleApplication;

public class NacosConfigExample {
    public static void main(String[] args) {
        // nacos配置属性
        NacosProps props = new NacosProps();
        props.serverAddr = "127.0.0.1:8848";
        // 启动nacos配置中心实例
        NacosConfigContextManager manager = NacosConfigFactory.create(props);
        // 登录配置到ConfigContextManager中
        // 配置信息和LocalContextManager使用相同的静态ConfigContextManager，相同dataId会被覆盖
        manager.load("example", "DEFAULT_GROUP", ConfigContextType.YAML, false);
        // 扫描指定的配置文件中配置文件信息进行登录，该方法不实现递归扫描
        // 如果是本地文件，可以使用manager.localScan
        manager.nacosScan("example", "DEFAULT_GROUP", ConfigContextType.YAML);

        TestPojo nacos = ConfigContextManager.get("example", TestPojo.class);
        System.out.println(nacos.name);

        TestPojo nacos1 = ConfigContextManager.get("example1", TestPojo.class);
        System.out.println(nacos1.name);

        /*
        你可以通过DrpcSimpleApplication 开启NacosConfigContextManager
        DrpcSimpleApplication application = new DrpcSimpleApplication();
        NacosConfigContextManager manager = application.getNacosConfigManager();
        */

    }
}
