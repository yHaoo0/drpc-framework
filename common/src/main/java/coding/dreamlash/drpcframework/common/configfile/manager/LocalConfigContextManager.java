package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.BytesContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.context.FilePathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * 读取和扫描本地配置文件的工具类
 * 读取的配置文件登录到ConfigContextManager
 * @author yhao
 */
public class LocalConfigContextManager extends ConfigContextManager{
    private final static Logger log = LoggerFactory.getLogger(LocalConfigContextManager.class);

    public LocalConfigContextManager() {
    }

    /**
     * 将配置文件封装为ConfigContext 并存储到ConfigContextManger
     * @param dataId 登录到ConfigContextManger的唯一key 如果重复则覆盖
     * @param filePath 相对类资源路径下的文件路径
     * @param type 文件格式 POPRS or YAML
     * @param isMemory 是否将一次性将内容写入内存中
     */
    public static void load(String dataId, String filePath, ConfigContextType type, boolean isMemory){

        if(ClassLoader.getSystemResource(filePath) == null){
            log.warn("not found the filePath: {} on stsren resource. the config dataId: {} load faile", filePath, dataId);
            return;
        }

        ConfigContext config;
        if(isMemory){
            try {
                config = BytesContext.creatByFilePath(filePath, type);
            } catch (IOException e) {
                log.warn("updata config data faile, id:{}, message:",dataId, e.getMessage());
                return;
            }
        } else {
            config = new FilePathContext(filePath, type);
        }

        STORE.put(dataId, config);
        log.info("updata config data. id:{}", dataId);
    }

    /**
     * 序列化指定文件中的localConfigs配置，并扫描localConfigs下配置的配置文件和登录
     * @param filePath
     * @param type
     */
    public static void scan(String filePath, ConfigContextType type){
        URL url = ClassLoader.getSystemResource(filePath);
        LocalConfigScan propsScan = null;

        try {
            switch (type){
                case PROPS:
                    propsScan = PROPS_MAPPER.readValue(url, LocalConfigScan.class);
                    break;
                case YAML:
                    propsScan = YAML_MAPPER.readValue(url, LocalConfigScan.class);
                    break;
            }
        } catch (Exception e) {
            log.warn("scans local config data fail, path: {}, message: {}", filePath, e.getMessage());
        }

        if(propsScan == null || propsScan.localConfigs == null){
            return;
        }

        for(LocalConfigScabProps prop: propsScan.localConfigs){
            if(prop.checkNotNull()){
                load(prop.id, prop.filePath, prop.type, prop.isMemory);
            } else {
                log.warn("the config data load fail, id: {}, message: {}", prop.id, "id, filePath or type cann't null");
            }
        }
    }
}


