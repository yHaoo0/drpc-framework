package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.IOException;

public class NacosConfigContext implements ConfigContext {
    private final String dataId;
    private final String group;
    private final ConfigContextType type;
    private final ConfigService service;

    public NacosConfigContext(String dataId, String group, ConfigContextType type, ConfigService service) {
        this.dataId = dataId;
        this.group = group;
        this.type = type;
        this.service = service;
    }

    @Override
    public ConfigContextType getType() {
        return type;
    }

    @Override
    public byte[] getContext() throws IOException {
        try {
            return service.getConfig(dataId, group, 3000).getBytes();
        } catch (NacosException e) {
            throw new IOException(e.getMessage());
        }
    }
}
