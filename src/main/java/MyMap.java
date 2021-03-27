import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.*;

public class MyMap extends TreeMap<String, Integer> {
    /* This class has been created to better manage key-value dictiory where the key is
        a String-formatted date in %m-%Y, and the value is an Integer which counts the
        number of fixed bugs in a given project during the month specified in key.
    * */


    public MyMap(JSONArray array){
        super(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                //strings are mm-dddd format
                //split:
                String[] date1 = s.split("-");
                String[] date2 = t1.split("-");
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
            }
        });
        Integer i;
        String myDate;
        Integer value;

        for (i = 0; i < array.length(); i++){
            //getting date from json array
            JSONObject fieldsArray = (JSONObject) array.getJSONObject(i).get("fields");
            String resolutionDate = fieldsArray.get("resolutiondate").toString();
            //formatting the date in %m-%Y
            myDate = this.formattingString(resolutionDate);
            //checking if this date is already in list
            if ( (value = this.get(myDate)) == null)
                this.put(myDate, 1);
            else
                this.put(myDate, value + 1);
        }
        this.completeDataset();

    }


    public String formattingString(String date){
        /* get input date in format: %Y-%m-%dT%h:%min:%sec.000.000
            i only need the date in format %m-%Y. So: split string in T
            and slice
         */
        String completeDate = (date.split("T")[0]);
        String[] parts = completeDate.split("-"); //{ Year, month, day}
        String my_date = parts[1] + "-" + parts[0]; //format: %m-%Y
        return my_date;
    }

    @Override
    public String toString(){
        int i;
        String ret = "";
        Integer value;

        for (String key : this.keySet()){
            value = this.get(key);
            ret = ret + key + " : " + value.toString() + "\n";
        }
        return ret;
    }

    private void completeDataset(){
        String start = this.firstKey();
        String last = this.lastKey();

        Integer starting_year = Integer.parseInt(start.split("-")[1]);
        Integer starting_month = Integer.parseInt(start.split("-")[0]);

        Integer ending_year = Integer.parseInt(last.split("-")[1]);
        Integer ending_month = Integer.parseInt(last.split("-")[0]);

        Integer year;
        Integer month;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String newDate = "";
        for (year = starting_year; year <= ending_year; year++){
            for (month = 1; month <= 12; month++){
                newDate = decimalFormat.format(month);
                newDate += "-" + year.toString();

                if (this.get(newDate) == null)
                    this.put(newDate, 0);
            }
        }
    }

}

