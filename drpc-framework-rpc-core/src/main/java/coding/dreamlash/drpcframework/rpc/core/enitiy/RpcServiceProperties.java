package coding.dreamlash.drpcframework.rpc.core.enitiy;

import java.util.Objects;

/**
 * 服务属性(服务名,组,版本)
 * @author yhao
 * @email 673067630@qq.com
 * @createTime 2020-09-05 14:30
 */
public class RpcServiceProperties {
    private String serviceName; // 服务名
    private String version; // 服务版本
    private String group; // 组

    public RpcServiceProperties(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
    }

    public RpcServiceProperties(String serviceName, String version, String group) {
        this.serviceName = serviceName;
        this.version = version;
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcServiceProperties that = (RpcServiceProperties) o;
        return Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(version, that.version) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version, group);
    }

    @Override
    public String toString() {
        return "{" +
                "serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
