package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置文件存储和获取的统一中心
 * @author yhao
 */
public class ConfigContextManager {

    static {
        STORE = new ConcurrentHashMap<>();

        JavaPropsMapper tj = new JavaPropsMapper();
        tj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PROPS_MAPPER = tj;

        YAMLMapper ty = new YAMLMapper();
        ty.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        YAML_MAPPER = ty;
    }


    protected static final Map<String, ConfigContext> STORE;
    public final static JavaPropsMapper PROPS_MAPPER;
    public final static YAMLMapper YAML_MAPPER;

    private final static Logger log = LoggerFactory.getLogger(ConfigContextManager.class);

    /**
     * 据dataId作为key 从存储的ConfigContext
     * @param dataId
     * @return
     */
    public static ConfigContext getContext(String dataId){
        return STORE.get(dataId);
    }

    /**
     * 根据dataId作为key 从存储的ConfigContext中获取其数据并使用Jackson进行反序列
     * @param dataId
     * @param vauleType
     * @param <T>
     * @return
     */
    public static  <T> T get(String dataId, Class<T> vauleType){
        ConfigContext configContext = STORE.get(dataId);
        if(configContext == null){
            log.warn("Config file [{}] not found", dataId);
            return null;
        }

        T result = null;
        try {
            switch (configContext.getType()){
                case YAML:
                    result = YAML_MAPPER.readValue(configContext.getContext(), vauleType);
                    break;
                case PROPS:
                    result = PROPS_MAPPER.readValue(configContext.getContext(), vauleType);
                    break;
            }
        } catch (IOException e){
            log.warn("Config file [{}] can't get, exception: {} ", dataId, e.getMessage());
        }

        return result;
    }

    /**
     * 根据dataId作为key 从存储的ConfigContext中获取其数据并使用Jackson进行反序列
     * @param dataId
     * @param vauleType
     * @param defualt
     * @param <T>
     * @return
     */
    public static <T> T getOrDefualt(String dataId, Class<T> vauleType, T defualt){
        T result = get(dataId, vauleType);
        return result == null ? defualt:result;
    }
}
