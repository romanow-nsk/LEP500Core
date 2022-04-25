package romanow.lep500;


import lombok.Getter;

public class Statistic {
    private double sum=0;
    private double sum2=0;
    private int count=0;
    public void add(double vv){
        sum+=vv;
        sum2+=vv*vv;
        count++;
        }
    public void addNotNull(double vv){
        if (vv==0)
            return;
        sum+=vv;
        sum2+=vv*vv;
        count++;
        }
    public double middle(){
        return count==0 ? 0 : sum/count;
        }
    public double stdOtkl(){
        return count==0 ? 0 : Math.sqrt(sum2/count - middle()*middle());
        }

}
