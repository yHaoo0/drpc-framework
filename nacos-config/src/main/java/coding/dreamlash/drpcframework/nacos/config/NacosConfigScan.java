package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;

import java.util.List;

/**
 * 扫描本地配置文件中的扫描属性POJO
 * @author yhao
 */
public class NacosConfigScan {
    public List<NacosConfigScanProps> nacosConfigs;

    public NacosConfigScan() {
    }
}

class NacosConfigScanProps {
    public String id;
    public String group;
    public ConfigContextType type;
    public boolean isMemory = true;

    public NacosConfigScanProps() {
    }

    public boolean checkNotNull(){
        return id != null && group != null && type != null;
    }

}
