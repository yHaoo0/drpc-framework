package coding.dreamlash.drpcframework.common.configfile.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 配置文件的统一接口
 * @author yhao
 */
public interface ConfigContext {
    /**
     * 返回配置文件的解析类型
     * @return
     */
    public ConfigContextType getType();

    /**
     * 返回配置文件内容
     * @return
     * @throws IOException
     */
    public byte[] getContext() throws IOException;

    /**
     * 读取类资源文件夹下文件的内容
     * @param filePath
     * @return
     * @throws IOException
     */
    static byte[] readFile(String filePath) throws IOException{
        URL url = ClassLoader.getSystemResource(filePath);
        try (InputStream stream = new FileInputStream(url.getPath())){
            byte[] data = new byte[stream.available()];
            stream.read(data);
            return data;
        }
    }
}
