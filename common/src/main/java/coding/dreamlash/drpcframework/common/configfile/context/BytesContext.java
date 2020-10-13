package coding.dreamlash.drpcframework.common.configfile.context;

import java.io.IOException;

/**
 * 配置文件内容以Byte[]形式存储的实体
 * @author yhao
 */
public class BytesContext implements ConfigContext {
    private final byte[] context;
    public final ConfigContextType type;

    private BytesContext(byte[] context, ConfigContextType type) {
        this.context = context;
        this.type = type;
    }

    public static BytesContext creatByFilePath(String filePath, ConfigContextType type) throws IOException {
        byte[] context = ConfigContext.readFile(filePath);
        return new BytesContext(context, type);
    }

    public static BytesContext createByBytes(byte[] context, ConfigContextType type){
        return new BytesContext(context, type);
    }

    @Override
    public ConfigContextType getType() {
        return type;
    }

    @Override
    public byte[] getContext() {
        return context;
    }
}
