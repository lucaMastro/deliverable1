package logic.dataset_analysis;

import logic.dataset_manager.MyMap;

import java.util.Map;

public class StatisticalAnalysis {
    /* This class is used to compute mean and std-dev of dataset. Inother, it validate entries:
     *  if in a month there aren't at least THRESOLD commit.
     *  Note: THRESHOLD is a conf variable. */

    private String stringToWrite;

    public String getStringToWrite() {
        return stringToWrite;
    }

    public void setStringToWrite(String stringToWrite) {
        this.stringToWrite = stringToWrite;
    }

    public StatisticalAnalysis() {
    }

    public void makeCsvTest(MyMap fixedBugMap, MyMap allCommitMap) {
        String key;
        StringBuilder text = new StringBuilder("date,fixed bugs,all commit,use it in data-analysis(1=y, 0=n)\n");
        Integer value;
        Integer fixedBugs;
        for (Map.Entry<String, Integer> entry : fixedBugMap.entrySet()) {
            key = entry.getKey();
            text.append(key).append(",");
            fixedBugs = entry.getValue();
            value = allCommitMap.get(key);
            text.append(fixedBugs.toString()).append(",").append(value.toString()).append(",");
            if (value < 5)
                text.append("0");
            else
                text.append("1");
            text.append("\n");
        }
        this.setStringToWrite(text.toString());

    }

}
