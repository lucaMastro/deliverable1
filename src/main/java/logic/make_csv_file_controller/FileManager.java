package logic.make_csv_file_controller;

import logic.config_manager.ConfigurationManager;
import logic.dataset_analysis.StatisticalAnalysis;
import logic.dataset_manager.MySet;
import logic.dataset_manager.Node;

import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    private File file;

    public FileManager(String path){
        this.file = new File(path);
    }

    public void writeToFile(MySet set, StatisticalAnalysis sa) throws IOException {
        StringBuilder sb = new StringBuilder("date,fixedCommits,totalCommits,mean,upperControlLimit,lowerControlLimit\n");

        Iterator<Node> it = set.iterator();
        Node obj = null;
        while (it.hasNext()){
            obj = it.next();
            if (obj.isValid())
                sb.append(obj.toString()).append(",")
                        .append(sa.getMean().toString()).append(",")
                        .append(sa.getUpperControlLimit().toString()).append(",")
                        .append(sa.getLowerControlLimit().toString()).append("\n");

        }

        try (FileWriter fw = new FileWriter(this.file)){
            fw.append(sb.toString());
            fw.flush();
        }catch (IOException e) {
            Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
            logger.log(Level.OFF, e.toString());
        }
    }
}

