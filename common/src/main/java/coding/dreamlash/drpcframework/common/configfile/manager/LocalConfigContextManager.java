package coding.dreamlash.drpcframework.common.configfile.manager;

import coding.dreamlash.drpcframework.common.configfile.context.BytesContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContext;
import coding.dreamlash.drpcframework.common.configfile.context.ConfigContextType;
import coding.dreamlash.drpcframework.common.configfile.context.FilePathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class LocalConfigContextManager implements ConfigContextManager{
    private final static Logger log = LoggerFactory.getLogger(LocalConfigContextManager.class);

    public static boolean putByLocalFile(String filePath){
        return putByLocalFile(filePath, filePath);
    }

    public static boolean putByLocalFile(String dataId, String filePath){
        return putByLocalFile(dataId, filePath, true);
    }

    public static boolean putByLocalFile(String dataId, String filePath, boolean inMemory){
        ConfigContextType type = null;
        if(filePath.endsWith(".properties")){
            type = ConfigContextType.PROPS;
        } else if (filePath.endsWith(".yaml")){
            type = ConfigContextType.YAML;
        } else {
            log.warn("Config filePath [{}] load error, Only supports properties and yml format", filePath);
            return false;
        }

        return putByLocalFile(new LocalFileProps(dataId, filePath, type, inMemory));
    }

    public static boolean putByLocalFile(String dataId, String filePath, ConfigContextType type){
        return putByLocalFile(new LocalFileProps(dataId, filePath, type, true));
    }

    public static boolean putByLocalFile(String dataId, String filePath, ConfigContextType type, boolean inMemory){
        return putByLocalFile(new LocalFileProps(dataId, filePath, type, inMemory));
    }

    public static boolean putByLocalFile(LocalFileProps props){
        URL url = ClassLoader.getSystemResource(props.filePath);
        if(url == null){
            log.warn("not found file: {}", props.filePath);
            return false;
        }

        if(props.dataId == null){
            props.dataId = props.filePath;
        }
        if(props.type == null){
            if(props.filePath.endsWith(".properties")){
                props.type = ConfigContextType.PROPS;
            } else if (props.filePath.endsWith(".yaml")){
                props.type = ConfigContextType.YAML;
            } else {
                log.warn("Config filePath [{}] load error, Only supports properties and yml format", props.filePath);
                return false;
            }
        }

        try{
            ConfigContext context;
            if(props.inMemory){
                context = createBytesContext(url.getPath(), props.type);
            } else {
                context = createFilePathContext(url.getPath(), props.type);
            }

            if(STORE.containsKey(props.dataId)){
                log.info("updata dataId [{}]", props.dataId);
            }else {
                log.info("Config dataId [{}] sucess, file name: [{}]", props.dataId, props.filePath);
            }

            STORE.put(props.dataId, context);
            return true;
        } catch (IOException e){
            log.warn("Config dataId [{}] load error, exception: {} ", props.dataId, e.getMessage());
            return false;
        }
    }

    public static void scanLocalFile(LocalFileScan scan){
        for(LocalFileProps props: scan.localConfig){
            putByLocalFile(props);
        }
    }

    private static BytesContext createBytesContext(String path, ConfigContextType type) throws IOException {
        try (InputStream stream = new FileInputStream(path)){
            byte[] data = new byte[stream.available()];
            return new BytesContext(data, type);
        }
    }

    private static FilePathContext createFilePathContext(String path, ConfigContextType type){
        return new FilePathContext(path, type);
    }


    public class LocalFileScan{
        public List<LocalFileProps> localConfig;

        public LocalFileScan() {
        }
    }
}
