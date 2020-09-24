package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.factory.ClientScan;
import coding.dreamlash.drpcframework.rpc.core.proxy.ClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.FactoryClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.SingletonClientProxy;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Drpc 客户端启动的封装
 * @author yhao
 * @createDate 2020-9-23
 */
public class NettyClientApplication implements RpcClientApplication{
    private static Logger logger = LoggerFactory.getLogger(NettyClientApplication.class);
    private boolean isEnable = false;
    private ChannelProvider channelProvider;
    private ResponseProvider responseProvider;
    private NettyClientBoot nettyClientBoot;
    private NettyClientHandler nettyClientHandler;
    private NettyClientTransport clientTransport;
    private ClientProxy proxy;

    public NettyClientApplication() {
        channelProvider = new ChannelProvider();
        responseProvider = new ResponseProvider();
        nettyClientHandler = new NettyClientHandler(responseProvider, channelProvider);
        nettyClientBoot = new NettyClientBoot(nettyClientHandler);

        channelProvider.setClient(nettyClientBoot);
    }

    @Override
    public boolean enable(Properties properties, ServiceCenter serviceCenter, Object factory, boolean isSingleton) {
        if(!isEnable){
            clientTransport = new NettyClientTransport(channelProvider, responseProvider, serviceCenter);
            if (isSingleton){
                SingletonClientProxy singletonClientProxy = new SingletonClientProxy(clientTransport);
                ClientScan.scan(factory, singletonClientProxy);
                proxy = singletonClientProxy;
            } else {
                FactoryClientProxy factoryClientProxy = new FactoryClientProxy(clientTransport, factory);
                ClientScan.scan(factory, factoryClientProxy);
                proxy = factoryClientProxy;
            }
            isEnable = true;
        } else {
            logger.warn("Repeatedly enable NettyEnable");
        }
        return isEnable;
    }

    @Override
    public <T> T getProxy(String clientName) {
        return proxy.getProxy(clientName);
    }

    @Override
    public void shutdown() {
        nettyClientBoot.shutdown();
    }

}
