package coding.dreamlash.drpcframework.rpc.core.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 客户端服务申请信息
 * @author yhao
 * @createDate 2020-9-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DrpcClient {
    String clientName();
    String serviceName();
    String version();
    String group() default "";
}
