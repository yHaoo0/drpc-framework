package coding.dreamlash.drpcframework.rpc.core.factory;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.proxy.FactoryClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.SingletonClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 客户端熔断实体，以及服务请求的扫描
 * @author yhao
 * @createDate 2020-9-23
 */
public class ClientScan {
    private static final Logger log = LoggerFactory.getLogger(ClientScan.class);

    /**
     * 单例模式下的扫描处理
     * @param factory 实体工厂类
     * @param proxy 单例模式代理
     */
    public static void scan(Object factory, SingletonClientProxy proxy){

        for(Method method: factory.getClass().getMethods()){
            if(method.isAnnotationPresent(DrpcClient.class)){
                DrpcClient annoattion = method.getAnnotation(DrpcClient.class);
                RpcServiceProperties properties = toRpcServiceProperties(annoattion, method.getName());
                String clientName = method.getName();
                Class inte = method.getReturnType();

                try {
                    if (method.getParameterCount() == 0) {
                        Object o = method.invoke(factory);
                        proxy.putProxy(clientName, properties, inte, o);
                        log.info("put client success: {}", annoattion.clientName());
                    } else {
                        log.warn("put client fail, DrpcClient cannot handle methods containing parameters.{}", clientName);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.warn("put client fail, ServiceScan reflection failure.{}", clientName);
                }
            }
        }
    }

    public static void scan(Object factory, FactoryClientProxy proxy){
        for(Method method: factory.getClass().getMethods()){
            if(method.isAnnotationPresent(DrpcClient.class)){
                DrpcClient annoattion = method.getAnnotation(DrpcClient.class);
                RpcServiceProperties properties = toRpcServiceProperties(annoattion, method.getName());
                String clientName = method.getName();
                Class inte = method.getReturnType();

                if (method.getParameterCount() == 0) {
                    proxy.putProxy(clientName, properties, inte, method);
                    log.info("put client success: {}", annoattion.clientName());
                } else {
                    log.warn("put client fail, DrpcClient cannot handle methods containing parameters. {}", clientName);
                }
            }
        }
    }

    private static RpcServiceProperties toRpcServiceProperties(DrpcClient annotation, String methonName){
        String serviceName = annotation.serviceName().equals("") ? methonName:annotation.serviceName();
        if(annotation.group().equals("")){
            return new RpcServiceProperties(serviceName, annotation.version());
        } else {
            return new RpcServiceProperties(serviceName, annotation.version(), annotation.group());
        }
    }
}
