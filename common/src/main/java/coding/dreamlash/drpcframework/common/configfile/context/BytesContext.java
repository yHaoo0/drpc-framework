package coding.dreamlash.drpcframework.common.configfile.context;

public class BytesContext implements ConfigContext {
    private final byte[] context;
    public final ConfigContextType type;

    public BytesContext(byte[] context, ConfigContextType type) {
        this.context = context;
        this.type = type;
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
