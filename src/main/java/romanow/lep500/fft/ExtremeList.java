package romanow.lep500.fft;

import romanow.abc.core.constants.Values;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.utils.Pair;
import romanow.lep500.LEP500Params;

import java.util.ArrayList;

public class ExtremeList extends DAO {
    private ArrayList<Extreme> data = new ArrayList<>();
    private transient ExtremeFacade facade;
    private int extremeMode=0;
    private int testResult=0;
    private String testComment="";
    public ExtremeFacade getFacade() {
        return facade;
        }
    private void createFacade(){
        try {
            facade = (ExtremeFacade)Values.extremeFacade[extremeMode].newInstance();
            } catch (Exception e) {
                facade = new ExtremeNull();
                }
        }
    public ExtremeList(int mode){
        extremeMode = mode;
        createFacade();
        }
    public Pair<String,Integer> testAlarm2(LEP500Params set, double freqStep){
        testComment = "Критерий: "+getFacade().getTitle()+"\n";
        if (data.size()==0){
            testComment+="Экстремумы отсутствуют\n";
            return  new Pair<>(testComment,Values.MSUndefined);
            }
        Extreme extreme = data.get(0);
        double f0 = extreme.idx*freqStep;
        testComment += String.format("Осн.част.=%5.2f гц "+(extreme.decSize==-1 ? "" : "D=%5.2f")+"\n",
                extreme.idx*freqStep, Math.PI*extreme.decSize/extreme.idx);
        testComment+=String.format("Ампл.=%5.2f\n",extreme.value);
        facade.setExtreme(extreme);
        double val0 = facade.getValue();            // Значение параметра фасада
        //------------------- Фильтрация------------------------------------------------------------
        double p1 = extreme.value * set.amplLevelProc / 100;
        double p2 = val0 * set.powerLevelProc / 100;
        for(int i=1;i<data.size();) {
            Extreme extreme1 = data.get(i);
            double f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double val = facade.getValue();
            if (val < p2){
                data.remove(i);
                continue;
                }
            if (extreme1.value < p1){
                data.remove(i);
                continue;
                }
            i++;
            }
        testComment+="после фильтрации: "+data.size()+"\n";
        //------------------------------------------------------------------------------------------
        if (extreme.value<set.K1){
            testComment+="п.1. Слабый сигнал";
            testResult = Values.MSLowLevel;
            return new Pair<>(testComment, testResult);
            }
        if (data.size()==1) {
            testComment+="п.2. Единственный пик";
            testResult = Values.MSNormal;
            return new Pair<>(testComment, testResult);
            }
        boolean all=true;
        for(int i=1;i<data.size();i++) {
            Extreme extreme1 = data.get(i);
            double f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double val = facade.getValue();         // Значение параметра фасада
            if (val/val0>set.K2){
                all=false;
                break;
                }
            }
        if (all){
            testComment+="п.3. Нет выраженного пика";
            testResult = Values.MSNoPeak;
            return new Pair<>(testComment, testResult);
            }
        Extreme extreme1 = data.get(1);
        double f = extreme1.idx * freqStep;
        facade.setExtreme(extreme1);
        double val = facade.getValue();         // Значение параметра фасада
        double dd = 1-set.K5*Math.abs(f-f0)/f0;
        if (dd<0) dd=0;
        dd *= val/val0;
        if (dd>set.K3){
            testResult = dd>set.K4 ? Values.MSSecond1 : Values.MSSecond2;
            testComment+="п.4. Смежный пик d="+String.format("%4.2f",dd);
            return new Pair<>(testComment, testResult);
            }
        double sum=0;
        for(int i=1;i<data.size();i++) {
            extreme1 = data.get(i);
            f = extreme1.idx * freqStep;
            facade.setExtreme(extreme1);
            double d = 1-set.K5*Math.abs(f-f0)/f0;
            if (d<0) d=0;
            val = facade.getValue();         // Значение параметра фасада
            sum+=val*d;
            }
        sum/=(data.size()-1);
        if (sum==0) sum=0.01;
        double x = val0/sum;
        testComment+=String.format("п.5. Взвешенная сумма %5.2f",x);
        testResult = x>set.K6 ? Values.MSNormalMinus : (x<set.K7 ? Values.MSSumPeak1 : Values.MSSumPeak2);
        return new Pair<>(testComment,testResult);
        }
    public int getExtremeMode() {
        return extremeMode; }
    public int getTestResult() {
        return testResult; }
    public String getTestComment() {
        return testComment; }
    public String toString(){
        return testResult+" "+ testComment;
        }
    public ArrayList<Extreme> data(){ return data; }

    public String showExtrems(double firstFreq,double lastFreq, double  freqStep){
        createFacade();
        String out="";
        if (data().size()==0){
            return "";
            }
        out += String.format("Диапазон экстремумов: %6.3f-%6.3f\n",firstFreq,lastFreq);
        int count = data().size();
        ExtremeFacade facade = getFacade();
        facade.setExtreme(data().get(0));
        double val0 = facade.getValue();
        out += facade.getTitle()+"\n";
        Extreme extreme = facade.extreme();
        out += "Ампл     "+facade.getColName()+"    f(гц)     Декремент\n";
        out += String.format("%6.3f   %6.3f    %6.3f"+(extreme.decSize==-1 ?"":"      %6.3f")+"\n",
                extreme.value,facade.getValue(),
                extreme.idx*freqStep, Math.PI*extreme.decSize/extreme.idx);
        double sum=0;
        for(int i=1; i<count;i++){
            facade.setExtreme(data().get(i));
            double proc = facade.getValue()*100/val0;
            sum+=proc;
            extreme = facade.extreme();
            out += String.format("%6.3f   %6.3f    %6.3f"+(extreme.decSize==-1 ?"":"      %6.3f")+"\n",
                    extreme.value,facade.getValue(),
                    extreme.idx*freqStep, Math.PI*extreme.decSize/extreme.idx);
            }
        out += String.format("Средний - %d%% к первому\n",(int)(sum/(count-1)));
        return out;
        }
}
