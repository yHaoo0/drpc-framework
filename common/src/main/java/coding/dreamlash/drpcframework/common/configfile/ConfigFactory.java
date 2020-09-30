package coding.dreamlash.drpcframework.common.configfile;

import coding.dreamlash.drpcframework.common.configfile.manager.ConfigContextManager;
import coding.dreamlash.drpcframework.common.configfile.manager.LocalConfigContextManager;

public class ConfigFactory {

    static {
        manager = new LocalConfigContextManager();
    }

    private static ConfigContextManager manager;

    public static <T extends ConfigContextManager>  T getManager(){
        return (T) manager;
    }

}
