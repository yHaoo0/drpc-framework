package coding.dreamlash.drpcframework.rpc.core.factory;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.provider.FactoryServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.SingletonServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 客户端服务实体，以及服务信息等的扫描
 * @author yhao
 * @createDate 2020-9-23
 */
public class ServiceScan {
    private static final Logger log = LoggerFactory.getLogger(ServiceScan.class);

    /**
     * 单例模式下的扫描处理
     * @param factory 实体工厂类
     * @param provider 单例模式的提供者
     */
    public static void scan(Object factory, SingletonServiceProvider provider){
        for(Method method: factory.getClass().getMethods()){

            if(method.isAnnotationPresent(DrpcService.class)) {
                DrpcService annotation = method.getAnnotation(DrpcService.class);
                RpcServiceProperties serviceProperties = toRpcServiceProperties(annotation, method.getName());

                try {
                    if (method.getParameterCount() == 0) {
                        provider.publishService(serviceProperties, method.invoke(factory));
                        log.info("publish service sucess: {}", serviceProperties.toString());
                    } else {
                        log.warn("publish service fail, DrpcService cannot handle methods containing parameters. service:{}", serviceProperties.toString());
                    }
                } catch (IllegalAccessException |InvocationTargetException e) {
                    log.warn("publish service fail, ServiceScan reflection failure. service:{}", serviceProperties.toString());
                }
            }
        }
    }

    /**
     * 单例模式下的扫描处理
     * @param factory 实体工厂类
     * @param provider 工厂模式的提供者
     */
    public static void scan(Object factory, FactoryServiceProvider provider){
        for(Method method: factory.getClass().getMethods()){

            if(method.isAnnotationPresent(DrpcService.class)) {
                DrpcService annotation = method.getAnnotation(DrpcService.class);
                RpcServiceProperties serviceProperties = toRpcServiceProperties(annotation, method.getName());

                if(method.getParameterCount() == 0){
                    provider.publishService(serviceProperties, method);
                    log.info("publish service sucess: {}", serviceProperties.toString());
                }else {
                    log.warn("publish service fail, ServiceScan reflection failure. service:{}", serviceProperties.toString());
                }
            }
        }
    }

    /**
     * 将注解信息封装为服务属性
     * @param annotation
     * @param methonName
     * @return
     */
    private static RpcServiceProperties toRpcServiceProperties(DrpcService annotation, String methonName){
        String serviceName = annotation.serviceName().equals("") ? methonName:annotation.serviceName();
        if(annotation.group().equals("")){
            return new RpcServiceProperties(serviceName, annotation.version());
        } else {
            return new RpcServiceProperties(serviceName, annotation.version(), annotation.group());
        }
    }
}
