package coding.dreamlash.drpcframework.rpc.core.enitiy;

import java.io.Serializable;

/**
 * 请求体
 * @author yhao
 * @createDate 2020-9-23
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -1628053665403326240L;
    private String version; // 版本
    private String group; // 组
    private String requestId; // 请求ID
    private String serviceName; // 服务名
    private String methodName; // 请求方法
    private Class<?>[] parameType; // 参数类型列表
    private Object[] parame; // 参数列表
    private RpcMessageType type;

    public RpcRequest() {
    }

    public RpcRequest(String version, String group, String requestId, String serviceName, String methodName,
                      Class<?>[] parameType, Object[] parame, RpcMessageType type) {
        this.version = version;
        this.group = group;
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameType = parameType;
        this.parame = parame;
        this.type = type;
    }

    /**
     * 从该请求信息中获取服务属性
     * @return
     */
    public RpcServiceProperties toRpcServiceProperties(){
        return new RpcServiceProperties(serviceName, version, group);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameType() {
        return parameType;
    }

    public Object[] getParame() {
        return parame;
    }

    public RpcMessageType getType() {
        return type;
    }
}
