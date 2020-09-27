package coding.dreamlash.drpcframework.rpc.core.provider;

import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;

/**
 * 服务实体存储和获取
 * @author yhao
 * @createDate 2020-9-26
 */
public interface ServiceEnitity {
    /**
     * 返回存储的服务实体
     * @return
     * @throws DrpcException
     */
    public Object get() throws DrpcException;
}
