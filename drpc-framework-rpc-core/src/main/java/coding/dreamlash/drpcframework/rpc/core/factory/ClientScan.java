package coding.dreamlash.drpcframework.rpc.core.factory;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.proxy.FactoryClientProxy;
import coding.dreamlash.drpcframework.rpc.core.proxy.SingletonClientProxy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientScan {
    private static final Logger log = LogManager.getLogger();

    public static void scan(Object factory, SingletonClientProxy proxy){
        log.printf(Level.INFO, "start scan ractory: %s", factory.getClass().getSimpleName());
        for(Method method: factory.getClass().getMethods()){
            if(method.isAnnotationPresent(DrpcClient.class)){
                DrpcClient annoattion = method.getAnnotation(DrpcClient.class);
                RpcServiceProperties properties = toRpcServiceProperties(annoattion, method.getName());
                String clientName = method.getName();
                Class inte = method.getReturnType();

                try {
                    if (method.getParameterCount() == 0) {
                        Object o = method.invoke(factory);
                        proxy.putProxy(clientName, properties, inte, clientName);
                        log.printf(Level.INFO,  "put client success: %s", annoattion.clientName());
                    } else {
                        log.printf(Level.WARN, "put client fail, DrpcClient cannot handle methods containing parameters. client:%s", clientName);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.printf(Level.WARN, "put client fail, ServiceScan reflection failure. client:%s", clientName);
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
                    log.printf(Level.INFO,  "put client success: %s", annoattion.clientName());
                } else {
                    log.printf(Level.WARN, "put client fail, DrpcClient cannot handle methods containing parameters. client:%s", clientName);
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
