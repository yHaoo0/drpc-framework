package coding.dreamlash.drpcframework.rpc.core.application;

/**
 * rpc Service端的封装和启动
 * @author yhao
 * @createDate 20520-09-05 15:30
 */
public interface RpcServiceApplication {
    /**
     * 初始化服务端，如果以及初始化，返回true， 否则开始初始化，成功返回true
     * @return
     */
    public boolean enable(Object factory, boolean isSingleton);

}
