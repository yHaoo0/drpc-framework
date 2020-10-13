package coding.dreamlash.drpcframework.netty.service;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * NettyServer 配置属性
 * @author yhao
 */
public class NettyServerProps {
    @JsonProperty("netty.service.host")
    public String host = "127.0.0.1"; // 打开服务的ip地址
    @JsonProperty("netty.service.port")
    public int port = 8080; // 打开服务的端口
    @JsonProperty("netty.service.so_backlog")
    public int backlog = 128;
    @JsonProperty("netty.service.idle.reader")
    public int idleReader = 30;
    @JsonProperty("netty.service.idle.writer")
    public int idleWrite = 0;
    @JsonProperty("netty.service.idle.all")
    public int idleAll = 0;

    public NettyServerProps() {
    }

    public NettyServerProps(String host, int port, int backlog, int idleReader, int idleWrite, int idleAll) {
        this.host = host;
        this.port = port;
        this.backlog = backlog;
        this.idleReader = idleReader;
        this.idleWrite = idleWrite;
        this.idleAll = idleAll;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new NettyServerProps(host, port, backlog, idleReader, idleWrite, idleAll);
    }
}
