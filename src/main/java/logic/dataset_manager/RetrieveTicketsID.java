package logic.dataset_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import logic.config_manager.ConfigurationManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsID {

    private FileManager fileManager;
    private String projectName;
    private JSONArray jsonArray;
    private Integer total;

    public RetrieveTicketsID(String projName, String pathToFile) throws IOException {

        this.fileManager = new FileManager(pathToFile);
        this.projectName = projName;
        this.jsonArray = new JSONArray();


        Integer j = 0;
        Integer i = 0;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + this.projectName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i.toString() + "&maxResults=" + j.toString();
            JSONObject json = readJsonFromUrl(url);

            this.jsonArray = concatenate(this.jsonArray, json.getJSONArray("issues"));

            this.total = json.getInt("total");
            //if total is >= jsonArrayLength * 1000, i need another iteration. just increment of 1000
            i += 1000;

        } while (i < this.total);

    }

    private JSONArray concatenate(JSONArray a1, JSONArray a2) {
        int i;
        JSONArray result = new JSONArray();
        for (i = 0; i < a1.length(); i++)
            result.put(a1.get(i));

        for (i = 0; i < a2.length(); i++)
            result.put(a2.get(i));

        return result;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

   public static void main(String[] args) throws IOException, JSONException {
        String projectName = ConfigurationManager.getConfigEntry("projectName");
        String pathFile = ConfigurationManager.getConfigEntry("outputFilePath");
        RetrieveTicketsID fixedBugs = new RetrieveTicketsID(projectName, pathFile);
        TreeMap<String, Integer> fixedBugsMap = new MyMap(fixedBugs.jsonArray);
        fixedBugs.fileManager.writeToFile(fixedBugsMap.toString());

    }


}
