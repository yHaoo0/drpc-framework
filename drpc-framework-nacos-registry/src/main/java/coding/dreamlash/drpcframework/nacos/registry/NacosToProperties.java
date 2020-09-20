package coding.dreamlash.drpcframework.nacos.registry;

import java.util.Properties;

public class NacosToProperties {
    public static final String[] nacosKey = new String[]{
            "endpoint", "endpointPort", "namespace", "serverAddr",
            "namingLoadCacheAtStart", "namingClientBeatThreadCount",
            "namingPollingThreadCount"
        };

    public static Properties nacosProperties(Properties properties){
        Properties result = new Properties();
        for(String key: nacosKey){
            String drpcKey = "nacos." + key;
            if(properties.containsKey(drpcKey)){
                result.put(key, properties.getProperty(drpcKey));
            }
        }
        return result;
    }
}
