package logic.dataset_manager;

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
        /* it returns  [Year, month, day] */
        String[] parts = completeDate.split("-");
        /* format: %m-%Y */
        return parts[1] + "-" + parts[0];

    }

    @Override
    public String toString(){
        StringBuilder bld = new StringBuilder();
        Integer value;
        String ret = "";
        String key;
        for (Map.Entry<String,Integer> entry : this.entrySet()){
            key = entry.getKey();
            value = this.get(key);
            ret = bld.append(key).append(",").append(value.toString()).append("\n").toString();
        }
        return ret;
    }

    private void completeDataset(){
        String start = this.firstKey();
        String last = this.lastKey();

        Integer startingYear = Integer.parseInt(start.split("-")[1]);
        Integer startingMonth = Integer.parseInt(start.split("-")[0]);

        Integer endingYear = Integer.parseInt(last.split("-")[1]);
        Integer endingMonth = Integer.parseInt(last.split("-")[0]);

        Integer year;
        Integer month;
        Integer startingMonthIndex;
        Integer endingMonthIndex;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String newDate = "";
        StringBuilder bld = new StringBuilder();
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

                newDate = decimalFormat.format(month);
                newDate = bld.append(newDate).append("-").append(year.toString()).toString();

                this.putIfAbsent(newDate, 0);
            }
        }
    }

}

