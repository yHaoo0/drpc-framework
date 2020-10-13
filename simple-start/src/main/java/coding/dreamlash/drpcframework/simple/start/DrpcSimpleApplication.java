package coding.dreamlash.drpcframework.simple.start;

import coding.dreamlash.drpcframework.common.NacosProps;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.manager.ConfigContextManager;
import coding.dreamlash.drpcframework.common.configfile.manager.LocalConfigContextManager;
import coding.dreamlash.drpcframework.nacos.config.NacosConfigContextManager;
import coding.dreamlash.drpcframework.nacos.config.NacosConfigFactory;
import coding.dreamlash.drpcframework.netty.client.NettyClientApplication;
import coding.dreamlash.drpcframework.netty.client.NettyClientProps;
import coding.dreamlash.drpcframework.netty.service.NettyServerApplication;
import coding.dreamlash.drpcframework.netty.service.NettyServerProps;
import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.application.RpcServiceApplication;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import coding.dreamlash.drpcframework.nacos.registry.NacosServicesCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * 对注册中心，服务端，客户端的启动进行封装
 * @author yhao
 * @createDate 2020-9-23
 */
public class DrpcSimpleApplication {
    private static Logger log = LoggerFactory.getLogger(DrpcSimpleApplication.class);
    private ServiceCenter serviceCenter;
    private RpcClientApplication client;
    private RpcServiceApplication server;

    private boolean nacosConfigEnable = false;
    private NacosConfigContextManager nacosConfigManager;

    private String configId = "drpc";

    public DrpcSimpleApplication(){
        URL url;
        url = ClassLoader.getSystemResource("drpc.properties");
        if(url != null){
            LocalConfigContextManager.load(configId, "drpc.properties", ConfigContextType.PROPS, false);
            LocalConfigContextManager.scan("drpc.properties", ConfigContextType.PROPS);

            NacosProps nacosProps = ConfigContextManager.getOrDefualt(configId, NacosProps.class, new NacosProps());
            this.nacosConfigEnable = nacosProps.configEnable;
            if(enableNacosConfig(nacosProps)){
                this.nacosConfigManager.localScan("drpc.properties", ConfigContextType.PROPS);
            }
        } else {
            url = ClassLoader.getSystemResource("drpc.yml");
            if(url != null){
                LocalConfigContextManager.load(configId, "drpc.yml", ConfigContextType.YAML, false);
                LocalConfigContextManager.scan("drpc.yml", ConfigContextType.YAML);

                NacosProps nacosProps = ConfigContextManager.getOrDefualt(configId, NacosProps.class, new NacosProps());
                this.nacosConfigEnable = nacosProps.configEnable;
                if(enableNacosConfig(nacosProps)){
                    this.nacosConfigManager.localScan("drpc.yml", ConfigContextType.YAML);
                }
            } else {
                log.error("No configuration file found (drpc.properties or drpc.yml). running fail");
            }
        }

        NacosProps nacosProps = ConfigContextManager.get(this.configId, NacosProps.class);
        this.serviceCenter = new NacosServicesCenter(nacosProps);
        this.serviceCenter.enable();
    }

    public DrpcSimpleApplication(String configId){
        this.configId = configId;
        NacosProps nacosProps = ConfigContextManager.get(this.configId, NacosProps.class);
        enableNacosConfig(nacosProps);
        this.serviceCenter = new NacosServicesCenter(nacosProps);
        this.serviceCenter.enable();
    }

    /**
     * 启动客户端监听
     * @param facory 客户端服务实体工厂
     */
    public void enableClient(Object facory){
        NettyClientProps clientProps = ConfigContextManager.get(this.configId, NettyClientProps.class);
        client = new NettyClientApplication(clientProps);
        client.enable(serviceCenter, facory);
    }

    /**
     * 获取服务实体
     * @param clientName
     * @param <T>
     * @return
     */
    public <T> T proxy(String clientName){
        return client.getProxy(clientName);
    }

    /**
     * 关闭客户端监听
     */
    public void shutdownClient(){
        client.shutdown();
    }

    public RpcClientApplication getClient() {
        return client;
    }

    /**
     * 启动服务端监听
     * @param factory 服务实体提供的工厂实例
     */
    public void enableService(Object factory){
        NettyServerProps serverProps = ConfigContextManager.get(this.configId, NettyServerProps.class);
        this.server = new NettyServerApplication(serverProps, serviceCenter);
        this.server.enable(factory);
    }

    public ServiceCenter getServiceCenter() {
        return serviceCenter;
    }

    /**
     * 启动Naocos配置中心服务
     * @param nacosProps
     * @return
     */
    public boolean enableNacosConfig(NacosProps nacosProps){
        if(nacosProps.configEnable && !this.nacosConfigEnable){
            this.nacosConfigEnable = true;
            this.nacosConfigManager = NacosConfigFactory.create(nacosProps);
            return true;
        }
        log.info("Nacos Config enable fiale, NacosProps.configEnable = false or DrpcSimpleApplication running Nacos Config");
        return false;
    }

    /**
     * 获取正在运行的Naocos配置中心实例
     * @return
     */
    public NacosConfigContextManager getNacosConfigManager(){
        if(isNacosConfigEnable()){
            return this.nacosConfigManager;
        }else {
            return null;
        }
    }

    /**
     * 检查是否有nacos配置中心实例
     * @return
     */
    public boolean isNacosConfigEnable(){
        return this.nacosConfigEnable && this.nacosConfigManager != null;
    }
}
