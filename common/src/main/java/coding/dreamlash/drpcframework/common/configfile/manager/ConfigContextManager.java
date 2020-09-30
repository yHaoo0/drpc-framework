package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ConfigContextManager {
    public static final Map<String, ConfigContext> STORE = new ConcurrentHashMap<>();

    final static JavaPropsMapper PROPS_MAPPER = new JavaPropsMapper();
    final static YAMLMapper YAML_MAPPER = new YAMLMapper();
    final static Logger log = LoggerFactory.getLogger(ConfigContextManager.class);

    public static ConfigContext getContext(String dataId){
        return STORE.get(dataId);
    }

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
            }
        } catch (IOException e){
            log.warn("Config file [{}] can't get, exception: {} ", dataId, e.getMessage());
        }

        return result;
    }
}
