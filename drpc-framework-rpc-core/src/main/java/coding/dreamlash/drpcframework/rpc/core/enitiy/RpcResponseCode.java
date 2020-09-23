package coding.dreamlash.drpcframework.rpc.core.enitiy;

/**
 * 常用的响应体状态代码
 * @author yhao
 * @createDate 2020-9-23
 */
public enum RpcResponseCode {
    SUCCESS(200, "the remote call is successful"),
    FAIL(500, "the remote call is fail"),
    INVOKE_FAIL(500, "the method fail invok");

    public int code;
    public String message;

    RpcResponseCode() {
    }

    RpcResponseCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
