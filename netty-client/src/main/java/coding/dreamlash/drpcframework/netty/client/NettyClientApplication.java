package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.application.RpcClientApplication;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.factory.DrpcScan;
import coding.dreamlash.drpcframework.rpc.core.proxy.ClientProxy;
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
    private ChannelProvider channelProvider;
    private ResponseProvider responseProvider;
    private NettyClientBoot nettyClientBoot;
    private NettyClientHandler nettyClientHandler;
    private NettyClientTransport clientTransport;
    private ClientProxy proxy;

    private boolean isEnable = false;

    public NettyClientApplication(NettyClientProps props) {
        channelProvider = new ChannelProvider();
        responseProvider = new ResponseProvider();
        nettyClientHandler = new NettyClientHandler(responseProvider, channelProvider);
        nettyClientBoot = new NettyClientBoot(props, nettyClientHandler);

        channelProvider.setClient(nettyClientBoot);
    }

    @Override
    public boolean enable(ServiceCenter serviceCenter, Object factory) {
        if(!isEnable){
            clientTransport = new NettyClientTransport(channelProvider, responseProvider, serviceCenter);
            ClientProxy proxy = new ClientProxy(this.clientTransport);
            DrpcScan.clientScan(factory, proxy);
            this.proxy = proxy;
            this.isEnable = true;
        } else {
            logger.warn("Repeatedly enable NettyEnable");
        }
        return isEnable;
    }

    @Override
    public <T> T getProxy(String clientName) {
        try {
            return proxy.get(clientName);
        } catch (DrpcException e) {
            logger.warn(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public void shutdown() {
        nettyClientBoot.shutdown();
    }

}
