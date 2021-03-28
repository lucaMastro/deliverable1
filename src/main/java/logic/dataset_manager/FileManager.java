package logic.dataset_manager;

import logic.config_manager.ConfigurationManager;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    private File file;

    public FileManager(String path){
        this.file = new File(path);
    }

    public void writeToFile(String s) throws IOException {

        try (FileWriter fw = new FileWriter(this.file)){
            fw.append(s);
            fw.flush();
        }catch (IOException e) {
            Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
            logger.log(Level.OFF, e.toString());
        }
    }

    public String readIthLineFromFile(int lineNumber) throws IOException {

        String line = null;
        int i = 0;
        try(FileReader fr = new FileReader(this.file);
            BufferedReader br = new BufferedReader(fr)){
            while ((line = br.readLine()) != null) {
                i++;
                if (i == lineNumber)
                    break;
            }
        } catch (IOException e){
            Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
            logger.log(Level.OFF, e.toString());
        }
        if (i == lineNumber)
            return line;
        else
            return null;
    }

}

