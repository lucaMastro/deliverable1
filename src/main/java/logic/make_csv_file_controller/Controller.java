package logic.make_csv_file_controller;

import logic.config_manager.ConfigurationManager;
import logic.dataset_analysis.StatisticalAnalysis;
import logic.dataset_manager.MySet;
import logic.dataset_manager.Node;
import logic.dataset_manager.RetrieveInformations;
import org.json.JSONException;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class Controller {



    public static void main(String[] args) throws IOException, JSONException {
        String projectName = ConfigurationManager.getConfigEntry("projectName");
        String pathFile = ConfigurationManager.getConfigEntry("outputFilePath");
        RetrieveInformations fixedBugs = new RetrieveInformations(projectName, pathFile);
        MySet mySet = new MySet(fixedBugs.getJsonArray());
        String cmd = ConfigurationManager.getConfigEntry("gitCommand");
        mySet.supplementsData(cmd);


//        TreeMap<String, Integer> fixedBugsMap = new MySet(fixedBugs.jsonArray);


  //      TreeMap<String, Integer> allCommitsMap = new MySet(cmd);

        StatisticalAnalysis sa = new StatisticalAnalysis(mySet);
        FileManager fm = new FileManager(ConfigurationManager.getConfigEntry("outputFilePath"));
        fm.writeToFile(mySet, sa);
        //fixedBugs.fileManager.writeToFile(sa.getStringToWrite());
    }

}
