package coding.dreamlash.drpcframework.rpc.core.proxy;

import coding.dreamlash.drpcframework.rpc.core.enitiy.*;
import coding.dreamlash.drpcframework.rpc.core.exceptionn.DrpcException;
import coding.dreamlash.drpcframework.rpc.core.transport.ClientTransport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Proxy;


public abstract class ClientProxy {
    protected static Logger log = LogManager.getLogger();
    private ClientTransport clientTransport;

    public ClientProxy(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }

    public abstract  <T> T getProxy(String clientName) throws DrpcException;

    protected Object createProxy(Class clazz, RpcServiceProperties properties, Object client){
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new ClientProxyInvokeHandler(clientTransport, properties, client));
    }

}
