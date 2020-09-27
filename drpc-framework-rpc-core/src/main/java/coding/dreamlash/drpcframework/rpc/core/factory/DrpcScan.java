package coding.dreamlash.drpcframework.rpc.core.factory;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.provider.FactoryServiceEnitiy;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceEnitity;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.SingletonServiceEnitity;
import coding.dreamlash.drpcframework.rpc.core.proxy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 工厂扫描类，读取DrpcClint，DrpcSrvice注解获取反射配置和服务配置
 * @author yhao
 * @createDate 2020-9-26
 */
public class DrpcScan {
    private static final Logger log = LoggerFactory.getLogger(DrpcScan.class);

    /**
     * 扫描DrpcClient注解，登录代理类
     * @param factory
     * @param clientProxy
     */
    public static void clientScan(Object factory, ClientProxy clientProxy){
        for(Method method: factory.getClass().getMethods()){
            if(method.isAnnotationPresent(DrpcClient.class)){

                DrpcClient annotation = method.getAnnotation(DrpcClient.class);
                RpcServiceProperties properties = toRpcServiceProperties(annotation);
                String clientName = annotation.clientName().equals("") ? method.getName():annotation.clientName();
                Class inte = method.getReturnType();

                if(method.getParameterCount() != 0){
                    log.warn("put client fail, DrpcClient cannot handle methods containing parameters.{}", clientName);
                    continue;
                }

                try{
                    ClientEnitiy enitiy = null;
                    if(annotation.isSingleton()){
                        enitiy = SingletonClientEnitiy.create(inte, properties, method, factory, clientProxy);
                    } else {
                        enitiy = FactoryClientEnitiy.create(inte, properties, method, factory, clientProxy);
                    }
                    clientProxy.put(clientName, enitiy);
                    log.info("successfully loaded client:{}", clientName);
                } catch (DrpcException e){
                    log.warn("failed to load client:{}, message:{}", clientName, e.getMessage());
                }
            }
        }
    }

    /**
     * 扫描DrpcService注解，登录服务
     * @param factory
     * @param serviceProvider
     */
    public static void serviceScan(Object factory, ServiceProvider serviceProvider){
        for(Method method: factory.getClass().getMethods()){
            if(method.isAnnotationPresent(DrpcService.class)){

                DrpcService annotation = method.getAnnotation(DrpcService.class);
                RpcServiceProperties properties = toRpcServiceProperties(annotation, method.getName());

                if(method.getParameterCount() != 0){
                    log.warn("put client fail, DrpcClient cannot handle methods containing parameters.{}", properties.toString());
                    continue;
                }

                try {
                    ServiceEnitity enitity = null;
                    if(annotation.isSingleton()){
                        enitity = SingletonServiceEnitity.create(method, factory);
                    } else {
                        enitity = FactoryServiceEnitiy.create(method, factory);
                    }
                    serviceProvider.put(properties, enitity);
                    log.info("successfully loaded service:{}", properties.toString());
                } catch (DrpcException e){
                    log.warn("failed to load service:{}, message:{}", properties.toString(), e.getMessage());
                }
            }
        }
    }

    private static RpcServiceProperties toRpcServiceProperties(DrpcClient annotation){
        if(annotation.group().equals("")){
            return new RpcServiceProperties(annotation.serviceName(), annotation.version());
        } else {
            return new RpcServiceProperties(annotation.serviceName(), annotation.version(), annotation.group());
        }
    }

    private static RpcServiceProperties toRpcServiceProperties(DrpcService annotation, String methonName){
        String serviceName = annotation.serviceName().equals("") ? methonName:annotation.serviceName();
        if(annotation.group().equals("")){
            return new RpcServiceProperties(serviceName, annotation.version());
        } else {
            return new RpcServiceProperties(serviceName, annotation.version(), annotation.group());
        }
    }
}
