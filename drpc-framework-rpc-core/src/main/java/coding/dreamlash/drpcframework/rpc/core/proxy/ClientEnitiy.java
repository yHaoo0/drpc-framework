package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;

/**
 * 客户实体存储
 * @author yhao
 * @createDate 2020-9-26
 */
public  interface ClientEnitiy {

    /**
     * 返回存储的客户实体
     * @return
     * @throws DrpcException
     */
    public Object get() throws DrpcException;
}
