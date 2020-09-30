package coding.dreamlash.drpcframework.common.configfile.context;

import java.io.IOException;

public interface ConfigContext {
    public ConfigContextType getType();
    public byte[] getContext() throws IOException;
}
