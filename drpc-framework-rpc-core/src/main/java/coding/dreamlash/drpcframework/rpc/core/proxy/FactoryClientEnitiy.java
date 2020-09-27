package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 工厂模式的客户实体存储
 * @author yhao
 * @createDate 2020-9-26
 */
public class FactoryClientEnitiy implements ClientEnitiy {
    private static final Logger log = LoggerFactory.getLogger(SingletonClientEnitiy.class);
    private Class clazz;
    private RpcServiceProperties properties;
    private Method method;
    private Object factory;
    private ClientProxy clientProxy;

    public FactoryClientEnitiy(Class clazz, RpcServiceProperties properties, Method method, Object factory, ClientProxy clientProxy) {
        this.clazz = clazz;
        this.properties = properties;
        this.method = method;
        this.factory = factory;
        this.clientProxy = clientProxy;
    }

    public static ClientEnitiy create(Class clazz, RpcServiceProperties properties, Method method, Object factory, ClientProxy clientProxy) {
        return new FactoryClientEnitiy(clazz, properties, method, factory, clientProxy);
    }

    @Override
    public Object get() throws DrpcException {
        try {
            Object client = method.invoke(factory);
            Object proxy = clientProxy.createProxy(clazz, properties, client);
            return proxy;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DrpcException("method invoke faile: " + e.getMessage());
        }
    }
}
