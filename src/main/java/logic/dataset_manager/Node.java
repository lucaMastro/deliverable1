package logic.dataset_manager;

import logic.config_manager.ConfigurationManager;

public class Node {
    protected String date;
    protected Integer fixedBugs;
    protected Integer totalCommits;
    protected Boolean isValid;

    public Node(String date, Integer fixedBugs, Integer totalCommits){
        this.date = date;
        this.fixedBugs = fixedBugs;
        this.totalCommits = totalCommits;

        Integer threshold = Integer.parseInt(ConfigurationManager.getConfigEntry("THRESHOLD"));
        this.isValid = totalCommits > threshold ? Boolean.TRUE : Boolean.FALSE;
    }

    public Node(String date, Integer fixedBugs){
        this(date, fixedBugs, 0);
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid() {
        Integer threshold = Integer.parseInt(ConfigurationManager.getConfigEntry("THRESHOLD"));
        this.isValid = totalCommits >= threshold ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getFixedBugs() {
        return fixedBugs;
    }

    public void setFixedBugs(Integer fixedBugs) {
        this.fixedBugs = fixedBugs;
    }

    public Integer getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(Integer totalCommits) {
        this.totalCommits = totalCommits;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        return sb.append(this.date).append(",")
                .append(this.fixedBugs).append(",")
                .append(this.totalCommits).toString();
    }

}
