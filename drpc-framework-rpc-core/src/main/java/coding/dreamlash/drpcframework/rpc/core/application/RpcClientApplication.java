package coding.dreamlash.drpcframework.rpc.core.application;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;

import java.util.Properties;

/**
 * rpc client端的封装
 * @author yhao
 * @creatDate 2020-09-05 15:30
 */
public interface RpcClientApplication {
    /**
     * 初始化客户端，如果以及初始化，返回true， 否则开始初始化，成功返回true
     * @param properties
     * @param serviceCenter
     * @return
     */
    public boolean enable(Properties properties, ServiceCenter serviceCenter, Object factory, boolean isSingleton);

    /**
     * 获取服务代理类
     * @param clientName
     * @param <T>
     * @return
     */
    public <T> T getProxy(String clientName);


    public void shutdown();
}
