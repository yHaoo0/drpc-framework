package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 单例模式的服务实体存储
 * @author yhao
 * @createDate 2020-9-26
 */
public class SingletonServiceEnitity implements ServiceEnitity {
    private Object object;

    private SingletonServiceEnitity(Object object) {
        this.object = object;
    }

    public static ServiceEnitity create(Method method, Object factory) throws DrpcException {
        try {
            Object object = method.invoke(factory);
            return new SingletonServiceEnitity(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DrpcException("method invoke faile: " + e.getMessage());
        }
    }

    @Override
    public Object get() {
        return object;
    }
}
