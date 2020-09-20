package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.factory.ClientScan;
import coding.dreamlash.drpcframework.rpc.core.proxy.ClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.FactoryClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.SingletonClientProxy;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class NettyClientApplication implements RpcClientApplication{
    private static Logger logger = LogManager.getLogger();
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
