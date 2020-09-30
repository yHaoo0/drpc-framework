package coding.dreamlash.drpcframework.rpc.core.transport;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;

/**
 * 注册服务
 * @author yhao
 * @creatDate 2020-09-05 15:00
 */
public interface ServiceTransport {
    /**
     * 注册服务
     * @param rpcServiceProperties
     * @param service
     */
    void registerService(RpcServiceProperties rpcServiceProperties, Object service);
}
