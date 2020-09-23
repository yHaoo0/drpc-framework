package coding.dreamlash.drpcframework.netty.client;


import io.netty.channel.Channel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Channel 存储和提供类
 * @author yhao
 * @createDate 2020-9-23
 */
public class ChannelProvider {
    private static Logger log = LogManager.getLogger("");
    private final Map<String, Channel> provider;
    private NettyClientBoot client;

    public ChannelProvider() {
        provider = new ConcurrentHashMap<>();
    }

    public void setClient(NettyClientBoot client) {
        this.client = client;
    }

    /**
     * 获取Channle实例。如果没有或失效，则链接新的Channel
     * @param host
     * @param port
     * @return
     */
    public Channel get(String host, int port){
        String key = host + ":" + port;
        Channel channel = provider.get(key);

        if(channel != null && channel.isActive()){
            return channel;
        }

        try {
            channel = client.doConnect(host, port);
        } catch (InterruptedException | ExecutionException e) {
            log.warn(e.getStackTrace().toString());
        }

        if(channel != null){
            provider.put(key, channel);
        }
        return channel;
    }
}