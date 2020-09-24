package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 响应体临时存储
 * @author yhao
 * @createDate 2020-9-23
 */
public class ResponseProvider {
    private static Logger log = LoggerFactory.getLogger(ResponseProvider.class);
    private final Map<String, CompletableFuture<RpcResponse<Object>>> stroe;

    public ResponseProvider() {
        stroe = new ConcurrentHashMap<>();
    }

    /**
     * 标志响应体完成
     * @param requestId
     * @param futur
     */
    public void creat(String requestId, CompletableFuture<RpcResponse<Object>> futur) {
        stroe.put(requestId, futur);
        log.debug("creat Completable futur : [requestId: {}]", requestId);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse<Object>> future = stroe.get(response.getRequestId());
        future.complete(response);
        log.debug("response complete : [requestId: {}]", response.getRequestId());
    }

    public void remove(String requestId){
        stroe.remove(requestId);
    }
}
