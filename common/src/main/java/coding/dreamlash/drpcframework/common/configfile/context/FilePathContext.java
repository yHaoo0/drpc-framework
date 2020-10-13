package coding.dreamlash.drpcframework.common.configfile.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 存储配置文件位置，每次读取时使用IO操作重新读取
 * @author yhao
 */
public class FilePathContext implements ConfigContext{
    private final String path;
    public final ConfigContextType type;

    public FilePathContext(String path, ConfigContextType type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public ConfigContextType getType() {
        return type;
    }

    @Override
    public byte[] getContext() throws IOException {
        return ConfigContext.readFile(path);
    }
}
