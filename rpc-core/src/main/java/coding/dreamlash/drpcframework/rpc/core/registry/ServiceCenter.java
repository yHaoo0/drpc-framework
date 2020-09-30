package coding.dreamlash.drpcframework.rpc.core.registry;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 *  服务注册，发现，注销的接口
 * @author yhao
 * @email
 * @createTime 2020-09-05 14:30
 */
public interface ServiceCenter {

    /**
     * 注册服务
     * @param rpcServiceProperties
     * @param address
     * @return 注册成功,返回true.否则 返回falsh
     */
    boolean registerService(RpcServiceProperties rpcServiceProperties, InetSocketAddress address);

    /**
     * 通过服务信息寻找服务
     * @param rpcServiceProperties
     * @return
     */
    InetSocketAddress discoveryService(RpcServiceProperties rpcServiceProperties);

    /**
     * 注销指定地址的服务
     * @param rpcServiceProperties
     * @param address
     */
    void deregistryService(RpcServiceProperties rpcServiceProperties, InetSocketAddress address);

    /**
     * 启动服务的统一接口
     * @param properties 配置参数
     * @return
     */
    boolean enable(Properties properties);
}
