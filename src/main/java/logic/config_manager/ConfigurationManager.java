package logic.config_manager;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationManager {

    private ConfigurationManager(){}

    public static String getConfigEntry(String key){
        String c = "";
        try (InputStream inputStream = new FileInputStream("src/main/resources/config.properties")){
            Properties p = new Properties();
            p.load(inputStream);
            c = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }
}
