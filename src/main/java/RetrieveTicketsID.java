import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsID {

    private FileManager fileManager;
    private String projectName;
    private ArrayList <JSONArray> jsonArray; /*if more than 1000 elements, it will be split in max 1000-sized items
                                        1001 items will be saved in:
                                        { JSONArray_of_size_1000, JSONArray_of_size_1 }
                                    */

    private Integer total;
    private Integer jsonArrayLength = 0;

    public RetrieveTicketsID(String projName, String pathToFile) throws IOException {

        this.fileManager = new FileManager(pathToFile);
        this.projectName = projName;

        this.jsonArray = new ArrayList<>();

        Integer j = 0, i = 0;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i.toString() + "&maxResults=" + j.toString();

            JSONObject json = readJsonFromUrl(url);

            this.jsonArray.add(json.getJSONArray("issues"));
            this.jsonArrayLength++;
            this.total = json.getInt("total");
            //if total is >= jsonArrayLength * 1000, i need another iteration. just increment of 1000
            i += 1000;

        } while (i < this.total);

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String formattingString(String date){
        /* get input date in format: %Y-%m-%dT%h:%min:%sec.000.000
            i only need the date in format %m-%Y. So: split string in T
            and slice
         */
        String completeDate = (date.split("T")[0]);
        String[] parts = completeDate.split("-"); //{ Year, month, day}
        String my_date = parts[1] + "-" + parts[0]; //format: %m-%Y
        return my_date;
    }

    public static void main(String[] args) throws IOException, JSONException {
        String projectName = "MAHOUT";
        String path_file = "/home/luca/Scrivania/ISW2/deliverables/deliverable1/bug_tickets";
        RetrieveTicketsID retrieveTicketsID = new RetrieveTicketsID(projectName, path_file);


    }


    /*
    public static void main(String[] args) throws IOException, JSONException {

        String path_file = "/home/luca/Scrivania/ISW2/deliverables/deliverable1/bug_tickets";
        FileManager fileManager = new FileManager(path_file);


        String projName ="MAHOUT";
        String allTickets = "";
        Integer j = 0, i = 0, total = 1;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i.toString() + "&maxResults=" + j.toString();

            JSONObject json = readJsonFromUrl(url);
            JSONArray issues = json.getJSONArray("issues");
            total = json.getInt("total");
            for (; i < total && i < j; i++) {
                //Iterate through each bug
                String key = issues.getJSONObject(i%1000).get("key").toString();
                JSONObject fieldsArray = (JSONObject) issues.getJSONObject(i%1000).get("fields"); //.get("resolutiondate").toString();
                String resolutionDate = fieldsArray.get("resolutiondate").toString();
                resolutionDate = formattingString(resolutionDate);
                //System.out.println(key);
                allTickets += key + "," + resolutionDate + "\n";
            }
        } while (i < total);

        fileManager.writeToFile(allTickets);
        RetrieveTicketsID rt = new RetrieveTicketsID("MAHOUT", "~/Scrivania/test");


        return;
    }*/


}
