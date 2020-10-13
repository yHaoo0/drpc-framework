package coding.dreamlash.drpcframework.netty.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NettyClient 配置属性
 * @author yhao
 */
public class NettyClientProps {
    @JsonProperty("netty.client.timeout")
    public int timeout = 5000; // 超时时间
    @JsonProperty("netty.client.idle.reader")
    public int idleReader = 30; // 心跳时间配置
    @JsonProperty("netty.client.idle.writer")
    public int idleWrite = 0; // 心跳时间配置
    @JsonProperty("netty.client.idle.all")
    public int idleAll = 0; // 心跳时间配置

    public NettyClientProps() {
    }

    public NettyClientProps(int timeout, int idleReader, int idleWrite, int idleAll) {
        this.timeout = timeout;
        this.idleReader = idleReader;
        this.idleWrite = idleWrite;
        this.idleAll = idleAll;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new NettyClientProps(timeout, idleReader, idleWrite, idleAll);
    }
}
