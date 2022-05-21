package romanow.lep500;

import romanow.abc.core.entity.subjectarea.MeasureFile;
import romanow.abc.core.mongo.DAO;
import romanow.lep500.fft.ExtremeList;
import romanow.lep500.fft.FFTStatistic;

import java.util.ArrayList;

public class AnalyseResult extends DAO implements I_TrendData{
    public final ArrayList<ExtremeList> data = new ArrayList<ExtremeList>();
    private String title="";
    public final double firstFreq,lastFreq,dFreq;
    public final int nFirst,nLast;
    public final double spectrum[];
    public final String message;
    public final boolean valid;
    public final MeasureFile measure;
    public AnalyseResult(FFTStatistic statistic,MeasureFile measureFile) {
        measure = measureFile;
        this.valid = statistic.isValid();
        this.dFreq = statistic.getFreqStep();
        if (valid)
            this.spectrum = statistic.getNormalized();
        else
            this.spectrum = new double[0];
                    this.message = statistic.getMessage();
        firstFreq = statistic.getFirstFreq();
        lastFreq = statistic.getLastFreq();
        nFirst = statistic.getnFirst();
        nLast = statistic.getnLast();
        }
    @Override
    public double[] getY() {
        double yy[] = new double[spectrum.length-nFirst];
        for(int i=0;i<yy.length;i++)
            yy[i]=spectrum[i+nFirst];
        return yy;
        }
    @Override
    public double[] getX() {
        double xx[] = new double[spectrum.length-nFirst];
        double x=nFirst*dFreq;
        for(int i=0;i<xx.length;i++,x+=dFreq)
            xx[i]=x;
        return xx;
        }
    @Override
    public double getX0() {
        return nFirst*dFreq;
        }
    @Override
    public double getDX() {
        return dFreq;
        }
    @Override
    public String getGraphTitle() {
        return measure.toString();
        }
    public String getTitle(){
        return "Измерение"+(valid?"+":"-")+":"+title;
        }
    public void setTitle(String title) {
        this.title = title; }
    public String toString(){
        String ss = getTitle()+"\n"+message+"\n";
        for(ExtremeList dd : data)
            ss += dd.toString()+"\n";
        return ss;
        }
    public String toStringFull() {
        String ss = getTitle() + "\n" + message + "\n";
        for (ExtremeList dd : data)
            ss += dd.showExtrems(firstFreq, lastFreq, dFreq) + "\n";
        return ss;
        }
}
