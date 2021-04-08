package logic.dataset_analysis;

import logic.dataset_manager.MySet;
import logic.dataset_manager.Node;
import org.decimal4j.util.DoubleRounder;

import java.util.Iterator;

public class StatisticalAnalysis {
    /* This class is used to compute mean and std-dev of dataset. Inother, it validate entries:
     *  if in a month there aren't at least THRESOLD commit.
     *  Note: THRESHOLD is a conf variable. */

    protected Double mean;
    protected Double variance;
    protected Double upperControlLimit;
    protected Double lowerControlLimit;

    public StatisticalAnalysis(MySet mySet){
        this.computeMean(mySet);
        this.computeVariance(mySet);
        this.computeUpperControlLimit();
        this.computeLowerControlLimit();
    }


    private void computeMean(MySet set) {
        Integer n = 0;
        Double m = 0.0;
        Iterator<Node> it = set.iterator();
        Node obj = null;
        while (it.hasNext()){
            obj = it.next();
            if (Boolean.TRUE.equals(obj.isValid())){
                m += obj.getFixedBugs();
                n++;
            }
        }
        m /= n;
        this.mean = DoubleRounder.round(m, 3);

    }

    private void computeVariance(MySet set){
        Integer n = 0;

        Double v = 0.0;
        Iterator<Node> it = set.iterator();
        Node obj = null;
        while (it.hasNext()){
            obj = it.next();
            if (Boolean.TRUE.equals(obj.isValid())){
                v += Math.pow(this.mean - obj.getFixedBugs(), 2);
                n++;
            }
        }
        v /= n;
        this.variance = DoubleRounder.round(v, 3);
    }

    private void computeUpperControlLimit(){
        Double ucl = this.mean + 3 * this.variance;
        this.upperControlLimit = DoubleRounder.round(ucl, 3);
    }

    private void computeLowerControlLimit(){
        /* it has not sense to put negative values in the "fixed bugs" */
        Double lcl = this.mean - 3 * this.variance;
        this.lowerControlLimit = lcl <= 0 ? 0 : DoubleRounder.round(lcl, 3);
    }

    public Double getMean() {
        return mean;
    }

    public Double getVariance() {
        return variance;
    }

    public Double getUpperControlLimit() {
        return upperControlLimit;
    }

    public Double getLowerControlLimit() {
        return lowerControlLimit;
    }
}
