package coding.dreamlash.drpcframework.rpc.core.enitiy;

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
