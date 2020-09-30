package coding.dreamlash.drpcframework.common.configfile.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


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
        try (InputStream stream = new FileInputStream(path)){
            byte[] data = new byte[stream.available()];
            stream.read(data);
            return data;
        }
    }
}
