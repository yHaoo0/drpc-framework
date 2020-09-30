package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.configfile.manager.LocalConfigContextManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;

public class NacosConfigManager extends LocalConfigContextManager {
    private final ConfigService service;

    public NacosConfigManager(String host, int port) {
        String serverAddr = host + ":" + port;
        this(serverAddr);
    }

    public NacosConfigManager(String serverAddr) {
        service = NacosFactory.createConfigService(serverAddr);
    }
}
