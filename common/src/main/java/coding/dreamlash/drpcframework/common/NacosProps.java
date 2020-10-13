package coding.dreamlash.drpcframework.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Nacos配置属性
 */
public class NacosProps {
    @JsonProperty("nacos.endpoint")
    public String endpoint;
    @JsonProperty("nacos.endpointPort")
    public String endpointPort;
    @JsonProperty("nacos.namespace")
    public String namespace;
    @JsonProperty("nacos.serverAddr")
    public String serverAddr = "127.0.0.1:8848";

    @JsonProperty("nacos.namingLoadCacheAtStart")
    public boolean namingLoadCacheAtStart = false;
    @JsonProperty("nacos.namingClientBeatThreadCount")
    public int namingClientBeatThreadCount = -1;
    @JsonProperty("nacos.namingPollingThreadCount")
    public int namingPollingThreadCount = -1;

    @JsonProperty("nacos.configEnable")
    public boolean configEnable = true; //是否启动nacos配置中心客户端服务
    @JsonProperty("nacos.configLongPollTimeout")
    public int configLongPollTimeout = 30000;
    @JsonProperty("nacos.configRetryTime")
    public int configRetryTime = 2000;
    @JsonProperty("nacos.maxRetry")
    public int maxRetry = 3;
    @JsonProperty("nacos.enableRemoteSyncConfig")
    public boolean enableRemoteSyncConfig = false;
    @JsonProperty("nacos.configTimeout")
    public int configTimeout = 5000;

    public NacosProps() {
    }

    public Properties toProperties(){
        Properties properties = new Properties();

        Class<NacosProps> clazz = NacosProps.class;
        Field[] fields = clazz.getFields();

        for(Field field:fields){
            try {
                String key = field.getName();
                Object value = field.get(this);
                if(value == null){
                    continue;
                }
                properties.put(key, value);
            } catch (IllegalAccessException e) {
                continue;
            }
        }

        return properties;
    }
}
