package coding.dreamlash.drpcframework.rpc.core.application;

/**
 * rpc Service端的封装和启动
 * @author yhao
 * @createDate 20520-09-05 15:30
 */
public interface RpcServiceApplication {
    /**
     * 注册服务，启用服务器监听
     * @return
     */
    public boolean enable(Object factory, boolean isSingleton);

}
