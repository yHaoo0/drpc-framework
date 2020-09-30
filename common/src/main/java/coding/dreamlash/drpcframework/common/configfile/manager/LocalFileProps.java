package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;

public class LocalFileProps {
    public String dataId;
    public String filePath;
    public ConfigContextType type;
    public boolean inMemory = true;

    public LocalFileProps() {
    }

    public LocalFileProps(String dataId, String filePath, ConfigContextType type, boolean inMemory) {
        this.dataId = dataId;
        this.filePath = filePath;
        this.type = type;
        this.inMemory = inMemory;
    }
}
