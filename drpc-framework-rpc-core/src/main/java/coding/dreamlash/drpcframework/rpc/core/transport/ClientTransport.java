package coding.dreamlash.drpcframework.rpc.core.transport;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcRequest;
import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;

/**
 * 客户端发送请求接口
 * @author yhao
 * @creatDate 2020-09-05 14:50
 */
public interface ClientTransport {
    /**
     * 发送请求
     * @param rpcRequest 请求体
     * @return
     */
    RpcResponse<Object> sendRpcRequest(RpcRequest rpcRequest) throws DrpcException;

}
