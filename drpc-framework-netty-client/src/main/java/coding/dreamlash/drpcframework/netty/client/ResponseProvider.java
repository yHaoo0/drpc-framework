package coding.dreamlash.drpcframework.netty.client;


import coding.dreamlash.drpcframework.rpc.core.enitiy.RpcResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class ResponseProvider {
    private static Logger log = LogManager.getLogger();
    private final Map<String, CompletableFuture<RpcResponse<Object>>> stroe;

    public ResponseProvider() {
        stroe = new ConcurrentHashMap<>();
    }

    public void creat(String requestId, CompletableFuture<RpcResponse<Object>> futur) {
        stroe.put(requestId, futur);
        log.printf(Level.DEBUG, "creat Completable futur : [requestId: %s]", requestId);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse<Object>> future = stroe.get(response.getRequestId());
        future.complete(response);
        log.printf(Level.DEBUG, "response complete : [requestId: %s]", response.getRequestId());
    }

    public RpcResponse getAndRemove(String requestId) throws ExecutionException, InterruptedException {
        CompletableFuture<RpcResponse<Object>> future = stroe.remove(requestId);
        RpcResponse<Object> response = future.get();
        log.printf(Level.DEBUG, "response release : [requestId: %s]", response.getRequestId());
        return response;
    }

    public void remove(String requestId){
        stroe.remove(requestId);
    }
}
