package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;

import java.util.List;

/**
 * 扫描本地配置文件中的扫描属性POJO
 * @author yhao
 */
public class LocalConfigScan {
    public List<LocalConfigScabProps> localConfigs;

    public LocalConfigScan() {
    }
}

class LocalConfigScabProps {
    public String id;
    public String filePath;
    public ConfigContextType type;
    public boolean isMemory = true;

    public LocalConfigScabProps() {
    }

    public boolean checkNotNull(){
        return id != null && filePath != null && type != null;
    }

}
