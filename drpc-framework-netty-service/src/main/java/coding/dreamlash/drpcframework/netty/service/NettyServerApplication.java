package coding.dreamlash.drpcframework.netty.service;

import coding.dreamlash.drpcframework.rpc.core.application.RpcServiceApplication;
import coding.dreamlash.drpcframework.rpc.core.factory.ServiceScan;
import coding.dreamlash.drpcframework.rpc.core.provider.FactoryServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.SingletonServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.registry.ServiceCenter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Netty Server 对外展示的类， 对服务所需要的类进行组合
 * @author yhao
 * @creatDate 2020-09-06 08:40
 */
public class NettyServerApplication implements RpcServiceApplication {
    private static final Logger log = LogManager.getLogger();
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
    public boolean enable(Object factory, boolean isSingleton) {
        if(!enable){
            if(isSingleton){
                SingletonServiceProvider singletonProvider = new SingletonServiceProvider(this.serviceCenter, address);
                ServiceScan.scan(factory, singletonProvider);
                this.serviceProvider = singletonProvider;
            } else {
                FactoryServiceProvider factoryProvider = new FactoryServiceProvider(this.serviceCenter, address, factory);
                ServiceScan.scan(factory, factoryProvider);
                this.serviceProvider = factoryProvider;
            }
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
