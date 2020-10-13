package coding.dreamlash.drpcframework.nacos.config;

import coding.dreamlash.drpcframework.common.NacosProps;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实例化NacosCinfigContextManager的工厂类
 * @author yhao
 */
public class NacosConfigFactory {
    private final static Logger log = LoggerFactory.getLogger(NacosConfigFactory.class);

    public static NacosConfigContextManager create(NacosProps props){
        try {
            return new NacosConfigContextManager(props);
        } catch (NacosException e) {
            log.warn("NacosConfigContextManager create failer, message: {}", e.getErrMsg());
        }
        return null;
    }

}
