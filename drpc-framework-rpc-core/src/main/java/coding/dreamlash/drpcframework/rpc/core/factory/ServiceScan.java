package coding.dreamlash.drpcframework.rpc.core.factory;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.provider.FactoryServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.ServiceProvider;
import coding.dreamlash.drpcframework.rpc.core.provider.SingletonServiceProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceScan {
    private static final Logger log = LogManager.getLogger();

    public static void scan(Object factory, SingletonServiceProvider provider){
        for(Method method: factory.getClass().getMethods()){

            if(method.isAnnotationPresent(DrpcService.class)) {
                DrpcService annotation = method.getAnnotation(DrpcService.class);
                RpcServiceProperties serviceProperties = toRpcServiceProperties(annotation, method.getName());

                try {
                    if (method.getParameterCount() == 0) {
                        provider.publishService(serviceProperties, method.invoke(factory));
                        log.printf(Level.INFO, "publish service sucess: %s", serviceProperties.toString());
                    } else {
                        log.printf(Level.WARN, "publish service fail, DrpcService cannot handle methods containing parameters. service:%s", serviceProperties.toString());
                    }
                } catch (IllegalAccessException |InvocationTargetException e) {
                    log.printf(Level.WARN, "publish service fail, ServiceScan reflection failure. service:%s", serviceProperties.toString());
                }
            }
        }
    }

    public static void scan(Object factory, FactoryServiceProvider provider){
        for(Method method: factory.getClass().getMethods()){

            if(method.isAnnotationPresent(DrpcService.class)) {
                DrpcService annotation = method.getAnnotation(DrpcService.class);
                RpcServiceProperties serviceProperties = toRpcServiceProperties(annotation, method.getName());

                if(method.getParameterCount() == 0){
                    provider.publishService(serviceProperties, method);
                    log.printf(Level.INFO, "publish service sucess: %s", serviceProperties.toString());
                }else {
                    log.printf(Level.WARN, "publish service fail, DrpcService cannot handle methods containing parameters. service:%s", serviceProperties.toString());
                }
            }
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