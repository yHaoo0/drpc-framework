package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.NacosProps;
import coding.dreamlash.drpcframework.common.configfile.context.BytesContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.manager.ConfigContextManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Nacos 配置中心管理，负责配置文件信息登录到ConfigContextManager
 * @author yhao
 */
public class NacosConfigContextManager extends ConfigContextManager {
    public final ConfigService configService;
    private final static Logger log = LoggerFactory.getLogger(NacosConfigContextManager.class);
    private int timeout;

    public NacosConfigContextManager(NacosProps props) throws NacosException {
        this.configService = NacosFactory.createConfigService(props.toProperties());
        this.timeout = props.configTimeout;
        log.info("the nacos config enable, serverAddr:{}", props.serverAddr);
    }

    /**
     * 将指定的nacos配置中心的配置文件信息封装到NacosContext 并存储到ConfigContextManager
     * @param dataId
     * @param group
     * @param type 文件格式
     * @param isMemory 是否将一次性将内容写入内存中
     */
    public void load(String dataId, String group, ConfigContextType type, boolean isMemory){
        ConfigContext context;
        if(isMemory){
            String result = null;
            try {
                result = configService.getConfig(dataId, group, timeout);
            } catch (NacosException e) {
                log.warn("updata config data faile, id:{}, message:",dataId, e.getMessage());
                return;
            }
            context = BytesContext.createByBytes(result.getBytes(), type);
        } else {
            context = new NacosContext(dataId, group, type, this);
        }

        STORE.put(dataId, context);
        log.info("updata config data. id:{}", dataId);
    }

    /**
     * 直接nacos配置中心获取配置，不存储到ConfigContextManager
     * @param prpos
     * @return
     * @throws NacosException
     */
    public String get(NacosContext prpos) throws NacosException {
        return configService.getConfig(prpos.dataId, prpos.group, timeout);
    }

    /**
     * 从nacos配置中心中的配置获取nacosConfigs，并扫描nacosConfigs指定的nacos配置属性，登录到ConfigContextManager
     * @param dataId
     * @param group
     * @param type
     */
    public void nacosScan(String dataId, String group, ConfigContextType type){
        NacosConfigScan propsScan = null;

        try {
            String context = configService.getConfig(dataId, group, timeout);

            switch (type){
                case PROPS:
                    propsScan = PROPS_MAPPER.readValue(context.getBytes(), NacosConfigScan.class);
                    break;
                case YAML:
                    propsScan = YAML_MAPPER.readValue(context.getBytes(), NacosConfigScan.class);
                    break;
                default:
                    log.warn("excaption, The type does not exist");
                    return;
            }

        } catch (Exception e) {
            log.warn("scans nacos config data fail, dataId: {}, error message:{}", dataId, e.getMessage());
        }

        if(propsScan == null || propsScan.nacosConfigs == null){
            return;
        }

        scanForece(propsScan.nacosConfigs);
    }

    /**
     * 从相对类资源的配置文件获取nacosConfigs，并扫描nacosConfigs指定的nacos配置属性，登录到ConfigContextManager
     * @param filePath
     * @param type
     */
    public void localScan(String filePath, ConfigContextType type) {
        URL url = ClassLoader.getSystemResource(filePath);
        NacosConfigScan propsScan = null;

        try {
            switch (type){
                case PROPS:
                    propsScan = PROPS_MAPPER.readValue(url, NacosConfigScan.class);
                    break;
                case YAML:
                    propsScan = YAML_MAPPER.readValue(url, NacosConfigScan.class);
                    break;
                default:
                    log.warn("excaption, The type does not exist");
                    return;
            }
        } catch (Exception e) {
            log.warn("scans local config data fail, path: {}, message: {}", filePath, e.getMessage());
        }

        if(propsScan == null || propsScan.nacosConfigs == null){
            return;
        }

        scanForece(propsScan.nacosConfigs);
    }

    private void scanForece(List<NacosConfigScanProps> props){
        for(NacosConfigScanProps prop: props){
            if(!prop.checkNotNull()){
                log.warn("the config data load fail, id: {}, message: {}", prop.id, "id, group or type cann't null");
                continue;
            }
            load(prop.id, prop.group, prop.type, prop.isMemory);
        }
    }
}


