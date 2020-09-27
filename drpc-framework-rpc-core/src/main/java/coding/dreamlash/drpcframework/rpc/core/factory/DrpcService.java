package coding.dreamlash.drpcframework.rpc.core.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务端服务信息
 * @author yhao
 * @createDate 2020-9-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DrpcService {
    String serviceName();
    String version();
    String group() default "";
    boolean isSingleton() default true;
}
