package coding.dreamlash.drpcframework.rpc.core.exceptionn;

/**
 * Drpc处理异常
 * @author yhao
 * @createDate 2020-9-23
 */
public class DrpcException extends RuntimeException{
    public DrpcException(String message) {
        super(message);
    }
}
