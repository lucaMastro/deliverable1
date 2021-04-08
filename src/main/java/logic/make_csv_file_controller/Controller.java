package logic.make_csv_file_controller;

import logic.config_manager.ConfigurationManager;
import logic.dataset_analysis.StatisticalAnalysis;
import logic.dataset_manager.MySet;
import logic.dataset_manager.RetrieveInformations;
import org.json.JSONException;
import java.io.IOException;

public class Controller {

    public static void main(String[] args) throws IOException, JSONException {
        String projectName = ConfigurationManager.getConfigEntry("projectName");
        String pathFile = ConfigurationManager.getConfigEntry("outputFilePath");
        RetrieveInformations fixedBugs = new RetrieveInformations(projectName);
        MySet mySet = new MySet(fixedBugs.getJsonArray());
        String cmd = ConfigurationManager.getConfigEntry("gitCommand");
        mySet.supplementsData(cmd);
        StatisticalAnalysis sa = new StatisticalAnalysis(mySet);
        FileManager fm = new FileManager(pathFile);
        fm.writeToFile(mySet, sa);
    }

}
