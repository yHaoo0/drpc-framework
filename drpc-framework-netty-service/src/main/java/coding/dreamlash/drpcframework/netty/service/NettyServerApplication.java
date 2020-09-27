package coding.dreamlash.drpcframework.netty.service;

import coding.dreamlash.drpcframework.rpc.core.application.RpcServiceApplication;
import coding.dreamlash.drpcframework.rpc.core.factory.DrpcScan;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Netty Server 对外展示的类， 对服务所需要的类进行组合
 * @author yhao
 * @creatDate 2020-09-06 08:40
 */
public class NettyServerApplication implements RpcServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(NettyServerApplication.class);
    ServiceCenter serviceCenter;
    ServiceProvider serviceProvider;
    NettyServerHandler serverHandler;
    NettyServerBoot serverBoot;
    InetSocketAddress address;
    boolean enable = false;

    public NettyServerApplication(Properties properties, ServiceCenter serviceCenter) {
        this.serviceCenter = serviceCenter;
        this.address = readAddress(properties);


    }

    @Override
    public boolean enable(Object factory) {
        if(!enable){
            ServiceProvider provider = new ServiceProvider(this.serviceCenter, address);
            DrpcScan.serviceScan(factory, provider);
            this.serviceProvider = provider;
            this.serverHandler = new NettyServerHandler(this.serviceProvider);
            this.serverBoot = new NettyServerBoot(this.address, this.serviceProvider);
            serverBoot.start();
        } else {
            log.error("Repeatedly enable NettyServer");
        }
        return enable;
    }


    private InetSocketAddress readAddress(Properties properties){
        String host = (String)properties.getOrDefault(PropertiesKey.HOST, PropertiesKey.DEFAULT_HOST);
        int port = Integer.parseInt((String) properties.getOrDefault(PropertiesKey.PORT, PropertiesKey.DEFAULR_PORT));
        return InetSocketAddress.createUnresolved(host, port);
    }
}
