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
    public final double spectrum[];
    public final String message;
    public final boolean valid;
    public final MeasureFile measure;
    public AnalyseResult(FFTStatistic statistic,MeasureFile measureFile,double firstFreq0, double lastFreq0) {
        this.dFreq = statistic.getFreqStep();
        this.spectrum = statistic.getNormalized();
        this.message = statistic.getMessage();
        this.valid = statistic.isValid();
        measure = measureFile;
        firstFreq = firstFreq0;
        lastFreq = lastFreq0;
        }
    @Override
    public double[] getY() {
        return spectrum.clone();
        }
    @Override
    public double[] getX() {
        double xx[] = new double[spectrum.length];
        double x=0;
        for(int i=0;i<spectrum.length;i++,x+=dFreq)
            xx[i]=x;
        return xx;
        }
    @Override
    public double getX0() {
        return 0;
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
