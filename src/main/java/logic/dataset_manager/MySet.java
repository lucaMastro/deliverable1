package logic.dataset_manager;

import logic.config_manager.ConfigurationManager;
import logic.dataset_analysis.StatisticalAnalysis;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySet extends TreeSet<Node> {
    /* This class has been created to better manage key-value dictiory where the key is
        a String-formatted date in %m-%Y, and the value is an Integer which counts the
        number of fixed bugs in a given project during the month specified in key.
        It will be also used to map <date -> number of total commit in that date>.
    * */

    private MySet(){
        /* Constructor that only initialize the comparator*/
        super((s, t1) -> {
            //strings are mm-dddd format
            //split:
            String[] date1 = s.date.split("-");
            String[] date2 = t1.date.split("-");
            Integer[] integers1 = {Integer.parseInt(date1[0]), Integer.parseInt(date1[1])};
            Integer[] integers2 = {Integer.parseInt(date2[0]), Integer.parseInt(date2[1])};

            //checking years:
            if (integers1[1] < integers2[1])
                return -1;
            else if (integers1[1] > integers2[1])
                return 1;
            else{
                //checking months:
                if (integers1[0] < integers2[0])
                    return -1;
                else if (integers1[0] > integers2[0])
                    return 1;
            }
            return 0;
        });
    }

    public MySet(JSONArray array){
        this();
        Integer i;
        String myDate;
        Node current;

        for (i = 0; i < array.length(); i++){
            //getting date from json array
            JSONObject fieldsArray = (JSONObject) array.getJSONObject(i).get("fields");
            String resolutionDate = fieldsArray.get("resolutiondate").toString();
            //formatting the date in %m-%Y
            myDate = this.formattingString(resolutionDate);
            //checking if this date is already in list
            if ( (current = this.get(myDate)) == null) {
                Node newNode = new Node(myDate, 1);
                this.add(newNode);
            }
            else
                current.fixedBugs += 1;
        }
        this.completeDataset();
    }

    public Node get(String date){
        /* This function check if there is a Data object which has
        *   the input String "date" as field "date" and return the
        *   total commits value */

        Iterator<Node> it = this.iterator();
        Node obj = null;
        Node searched = null;
        while (it.hasNext()){
            obj = it.next();
            if (obj.date.equals(date)) {
                searched = obj;
                break;
            }
        }
        return searched; //may return null
    }
    public void supplementsData(String gitCmd) throws IOException {
        /* This function retrieve all commit from git cmd. It's used to
        *  integrate data: it supplements fields. */
        String line;
        Node current;
        Runtime.getRuntime().exec(gitCmd);
        Process p = Runtime.getRuntime().exec(gitCmd);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) )){

            while ((line = in.readLine()) != null) {
                current = this.get(line);
                if (current == null) {
                    /* there aren't fixed commit in this date */
                    Node newNode = new Node(line, 0, 1);
                    this.add(newNode);
                }
                else
                    current.totalCommits += 1;
            }
        }catch (Exception e){
            Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
            logger.log(Level.OFF, e.toString());
        }

        this.validateNodes();
    }

    private void validateNodes(){
        /* invoked only after supplementing dataset. it needs to set valid when
        * the number of commit is more than THRESHOLD   */
        Iterator<Node> it = this.iterator();
        Node obj = null;
        while (it.hasNext()){
            obj = it.next();
            obj.setValid();
        }
    }



    public String formattingString(String date){
        /* get input date in format: %Y-%m-%dT%h:%min:%sec.000.000
            i only need the date in format %m-%Y. So: split string in T
            and slice
         */
        String completeDate = (date.split("T")[0]);
        /* it returns  [Year, month, day] */
        String[] parts = completeDate.split("-");
        /* format: %m-%Y */
        return parts[1] + "-" + parts[0];

    }

    @Override
    public String toString(){
        StringBuilder bld = new StringBuilder();
        String ret = "";
        Iterator<Node> it = this.iterator();
        Node obj = null;
        while (it.hasNext()){
            obj = it.next();
            ret = bld.append(obj.date).append(",")
                    .append(obj.fixedBugs.toString()).append(",")
                    .append(obj.totalCommits).toString();
        }


        return ret;
    }

    private void completeDataset(){
        /* This function inserts into dataset dates without commits. */

        Node start = this.first();
        Node last = this.last();

        Integer startingYear = Integer.parseInt(start.date.split("-")[1]);
        Integer startingMonth = Integer.parseInt(start.date.split("-")[0]);

        Integer endingYear = Integer.parseInt(last.date.split("-")[1]);
        Integer endingMonth = Integer.parseInt(last.date.split("-")[0]);

        Integer year;
        Integer month;
        Integer startingMonthIndex;
        Integer endingMonthIndex;
        DecimalFormat decimalFormat = new DecimalFormat("00");

        for (year = startingYear; year <= endingYear; year++){

            if (year.equals(startingYear))
                startingMonthIndex = startingMonth;
            else
                startingMonthIndex = 1;

            if (year.equals(endingYear))
                endingMonthIndex = endingMonth;
            else
                endingMonthIndex = 12;

            for (month = startingMonthIndex; month <= endingMonthIndex; month++){
                StringBuilder bld = new StringBuilder();
                String newDate = "";
                newDate = decimalFormat.format(month);
                newDate = bld.append(newDate).append("-").append(year.toString()).toString();

                if (this.get(newDate) == null){
                    Node newNode = new Node(newDate, 0);
                    this.add(newNode);
                }
            }
        }
    }

}

