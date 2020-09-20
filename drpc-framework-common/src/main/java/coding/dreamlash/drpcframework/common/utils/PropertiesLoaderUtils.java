package coding.dreamlash.drpcframework.common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * 读取相对路径下 resources 下的 .properties 文件数据
 * @author yhao
 * @createTime 2020-9-25 10:00
 */
public class PropertiesLoaderUtils {
    /**
     * 相对目录下resources下查找文件，返回读取信息。
     * filename = "test.properties" -> 匹配 resources/test.properties
     * @param filename
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);
        if(url == null){
            throw new IOException("no found file: " + filename.toString());
        }

        Properties properties = new Properties();
        try(FileInputStream input = new FileInputStream(url.getFile())){
            properties.load(input);
        }

        return properties;
    }
}
