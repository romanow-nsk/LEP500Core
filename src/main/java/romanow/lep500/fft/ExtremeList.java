package romanow.lep500.fft;

import romanow.abc.core.constants.Values;
import romanow.abc.core.utils.Pair;
import romanow.lep500.LEP500Params;

import java.util.ArrayList;

public class ExtremeList extends ArrayList<Extreme> {
    public final static int StateGrayIdx=0;
    public final static int StateRedIdx=1;
    public final static int StateYellowIdx=2;
    public final static int StateGreenIdx=3;
    private transient ExtremeFacade facade;
    private int extremeMode=0;
    private int testResult=0;
    private String testComment="";
    public ExtremeFacade getFacade() {
        return facade;
        }
    public ExtremeList(int mode){
        extremeMode = mode;
        try {
            facade = (ExtremeFacade)FFTStatistic.extremeFacade[mode].newInstance();
            } catch (Exception e) {
                facade = new ExtremeNull();
                }
            }
    public Pair<String,Integer> testAlarm2(LEP500Params set, double freqStep){
        Extreme extreme = get(0);
        double f0 = extreme.idx*freqStep;
        String ss = getFacade().getTitle()+"\n";
        ss+=String.format("Осн.част.=%5.2f гц "+(extreme.decSize==-1 ? "" : "D=%5.2f")+"\n",
                extreme.idx*freqStep, Math.PI*extreme.decSize/extreme.idx);
        ss+=String.format("Ампл.=%5.2f\n",extreme.value);
        facade.setExtreme(extreme);
        double val0 = facade.getValue();            // Значение параметра фасада
        //------------------- Фильтрация------------------------------------------------------------
        double p1 = extreme.value * set.amplLevelProc / 100;
        double p2 = val0 * set.powerLevelProc / 100;
        for(int i=1;i<size();) {
            Extreme extreme1 = get(i);
            double f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double val = facade.getValue();
            if (val < p2){
                remove(i);
                continue;
                }
            if (extreme1.value < p1){
                remove(i);
                continue;
                }
            i++;
            }
        ss+="после фильтрации: "+size()+"\n";
        //------------------------------------------------------------------------------------------
        if (extreme.value<set.K1){
            ss+="п.1. Слабый сигнал\n";
            return new Pair<>(ss, Values.MSLowLevel);
            }
        if (size()==1) {
            ss+="п.2. Единственный пик";
            return new Pair<>(ss, Values.MSNormal);
            }
        boolean all=true;
        for(int i=1;i<size();i++) {
            Extreme extreme1 = get(i);
            double f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double val = facade.getValue();         // Значение параметра фасада
            if (val/val0>set.K2){
                all=false;
                break;
                }
            }
        if (all){
            ss+="п.3. Нет выраженного пика";
            return new Pair<>(ss, Values.MSNoPeak);
            }
        Extreme extreme1 = get(1);
        double f = extreme1.idx * freqStep;
        facade.setExtreme(extreme1);
        double val = facade.getValue();         // Значение параметра фасада
        double dd = 1-set.K5*Math.abs(f-f0)/f0;
        if (dd<0) dd=0;
        dd *= val/val0;
        if (dd>set.K3){
            ss+="п.4. Смежный пик d="+String.format("%4.2f",dd);
            return new Pair<>(ss, dd>set.K4 ? Values.MSSecond1 : Values.MSSecond2);
            }
        double sum=0;
        for(int i=1;i<size();i++) {
            extreme1 = get(i);
            f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double d = 1-set.K5*Math.abs(f-f0)/f0;
            if (d<0) d=0;
            val = facade.getValue();         // Значение параметра фасада
            sum+=val*d;
            }
        sum/=(size()-1);
        if (sum==0) sum=0.01;
        double x = val0/sum;
        ss+=String.format("п.5. Взвешенная сумма %5.2f",x);
        return new Pair<>(ss,x>set.K6 ? Values.MSNormalMinus : (x<set.K7 ? Values.MSSumPeak1 : Values.MSSumPeak2));
        }
    public int getExtremeMode() {
        return extremeMode; }
    public int getTestResult() {
        return testResult; }
    public String getTestComment() {
        return testComment; }
}
