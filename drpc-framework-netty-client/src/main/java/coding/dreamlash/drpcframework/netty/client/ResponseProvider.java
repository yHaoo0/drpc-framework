package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 响应体临时存储
 * @author yhao
 * @createDate 2020-9-23
 */
public class ResponseProvider {
    private static Logger log = LogManager.getLogger();
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
        log.printf(Level.DEBUG, "creat Completable futur : [requestId: %s]", requestId);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse<Object>> future = stroe.get(response.getRequestId());
        future.complete(response);
        log.printf(Level.DEBUG, "response complete : [requestId: %s]", response.getRequestId());
    }

    public void remove(String requestId){
        stroe.remove(requestId);
    }
}
