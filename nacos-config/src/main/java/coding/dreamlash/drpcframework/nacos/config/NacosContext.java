package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.IOException;

/**
 * nacos 配置文件属性
 * @author yhao
 */
public class NacosContext implements ConfigContext {
    public final String dataId;
    public final String group;
    private final ConfigContextType type;
    private NacosConfigContextManager manager;

    public NacosContext(String dataId, String group, ConfigContextType type, NacosConfigContextManager manager) {
        this.dataId = dataId;
        this.group = group;
        this.type = type;
        this.manager = manager;
    }

    @Override
    public ConfigContextType getType() {
        return type;
    }

    @Override
    public byte[] getContext() throws IOException {
        try {
            return manager.get(this).getBytes();
        } catch (NacosException e) {
            throw new IOException("this Exception for NacosException, " + e.getMessage());
        }
    }
}
