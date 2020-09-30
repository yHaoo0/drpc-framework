package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 工厂模式的服务实体存储
 * @author yhao
 * @createDate 2020-9-26
 */
public class FactoryServiceEnitiy implements ServiceEnitity{
    private Method method;
    private Object factory;

    private FactoryServiceEnitiy(Method method, Object factory) {
        this.method = method;
        this.factory = factory;
    }

    public static ServiceEnitity create(Method method, Object factory){
        return new FactoryServiceEnitiy(method, factory);
    }

    @Override
    public Object get() throws DrpcException {
        try {
            return method.invoke(factory);
        }catch (IllegalAccessException | InvocationTargetException e) {
            throw new DrpcException("method invoke faile: " + e.getMessage());
        }
    }
}
