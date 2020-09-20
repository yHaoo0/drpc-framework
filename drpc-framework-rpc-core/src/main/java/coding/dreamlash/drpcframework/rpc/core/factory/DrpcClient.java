package coding.dreamlash.drpcframework.rpc.core.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DrpcClient {
    String clientName();
    String serviceName();
    String version();
    String group() default "";
}
