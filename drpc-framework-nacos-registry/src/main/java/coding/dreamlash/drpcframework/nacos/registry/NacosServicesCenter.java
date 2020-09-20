package coding.dreamlash.drpcframework.nacos.registry;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * @author yhao
 * @cretaDate 2020-9-5 20:00
 */
public class NacosServicesCenter implements ServiceCenter {
    private static final Logger log = LogManager.getLogger("coding.dreamlash");
    private boolean enable = false;
    private NamingService naming ;

    public NacosServicesCenter() {
    }

    @Override
    public boolean enable(Properties properties) {
        Properties nacosProperties = NacosToProperties.nacosProperties(properties);
        if(!enable){
            try {
                naming = NamingFactory.createNamingService(nacosProperties);
                log.info("Nacos Naming started successfully, service center ip:" + (String) nacosProperties.get("serverAddr"));
                enable = true;
            } catch (NacosException e) {
                log.error("Nacos Naming failed to start");
                log.error(e.getStackTrace().toString());
            }
        } else {
            log.error("The NacloServicesCenter starts repeatedly");
        }
        return enable;
    }

    @Override
    public boolean registerService(RpcServiceProperties properties, InetSocketAddress address) {
        String service = toServiceName(properties);
        try {
            if(properties.getGroup() == null){
                naming.registerInstance(service, address.getHostString(), address.getPort());
            } else {
                naming.registerInstance(service, address.getHostString(), address.getPort(), properties.getGroup());
            }
            return true;
        }catch (NacosException e) {
            log.warn("nacos exception: " + e.getMessage());
            log.warn(e.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public InetSocketAddress discoveryService(RpcServiceProperties properties) {
        InetSocketAddress result = null;
        try {
            String serviceName = toServiceName(properties);
            Instance instance;

            if(properties.getGroup() == null){
                instance =  naming.selectOneHealthyInstance(serviceName);
            }else {
                instance = naming.selectOneHealthyInstance(serviceName, properties.getGroup());
            }
            result = InetSocketAddress.createUnresolved(instance.getIp(), instance.getPort());
        } catch (Exception e) {
            log.warn("nacos exception: " + e.getMessage());
            log.warn(e.getStackTrace().toString());
        }
        return result;
    }

    @Override
    public void deregistryService(RpcServiceProperties properties, InetSocketAddress address) {
        try {
            naming.deregisterInstance(toServiceName(properties), address.getHostString(), address.getPort());
        } catch (NacosException e) {
            log.error("nacos exception: " + e.getErrMsg());
            log.error(e.getStackTrace().toString());
        }
    }

    private String toServiceName(RpcServiceProperties properties){
        return properties.getServiceName() + ":" + properties.getVersion();
    }
}
