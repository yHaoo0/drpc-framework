package coding.dreamlash.drpcframework.rpc.core.enitiy;

import java.io.Serializable;

/**
 * Rpc 通信的响应体。
 * @param <T> 传输数据的类型
 * @author yhao
 * @creatDate 2020-09-05 14:30
 */
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = -2294259775040578528L;
    private String requestId; // 匹配请求ID
    private Integer code; // 响应码
    private String message; // 响应信息
    private T data; // 响应数据

    public RpcResponse() {
    }

    public RpcResponse(String requestId, Integer code, String message, T data) {
        this.requestId = requestId;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRequestId() {
        return requestId;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> RpcResponse<T> success(T data, String requestId){
        return new RpcResponse<T>(requestId, RpcResponseCode.SUCCESS.code, RpcResponseCode.SUCCESS.message, data);
    }

    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
        return new RpcResponse<T>(null, rpcResponseCode.code, rpcResponseCode.message, null);
    }

    public static <T> RpcResponse<T> fail(int code, String message){
        return new RpcResponse<T>(null, code, message, null);
    }
}
