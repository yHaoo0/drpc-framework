package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcServiceProperties;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 单例模式的客户实体存储
 * @author yhao
 * @createDate 2020-9-26
 */
public class SingletonClientEnitiy implements ClientEnitiy {
    private static final Logger log = LoggerFactory.getLogger(SingletonClientEnitiy.class);
    private Object o;

    private SingletonClientEnitiy(Object o) {
        this.o = o;
    }

    public static ClientEnitiy create(Class clazz, RpcServiceProperties properties, Method method, Object factory, ClientProxy clientProxy) throws DrpcException {
        try {
            Object client = method.invoke(factory);
            Object proxy = clientProxy.createProxy(clazz, properties, client);
            return new SingletonClientEnitiy(proxy);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DrpcException("method invoke faile: " + e.getMessage());
        }
    }

    @Override
    public Object get() {
        return o;
    }
}
