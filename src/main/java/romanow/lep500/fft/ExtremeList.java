package romanow.lep500.fft;

import romanow.abc.core.constants.Values;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.utils.Pair;
import romanow.lep500.LEP500Params;

import java.util.ArrayList;
import java.util.Comparator;

public class ExtremeList extends DAO {
    private FFTStatistic source;
    private ArrayList<Extreme> data = new ArrayList<>();
    private transient ExtremeFacade facade;
    private int extremeMode=0;
    private int testResult=0;
    private String testComment="";
    public ExtremeFacade getFacade() {
        return facade;
        }
    public FFTStatistic getSource() {
        return source;
        }
    private void createFacade(){
        try {
            facade = (ExtremeFacade)Values.extremeFacade[extremeMode].newInstance();
            } catch (Exception e) {
                facade = new ExtremeNull();
                }
        }
    public ExtremeList(int mode,FFTStatistic src0){
        extremeMode = mode;
        source = src0;
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
            if (extreme1.value < p1){       // Фильтрация по основной амплитуде
                data.remove(i);
                continue;
                }
            if (val < (facade.isPowerFacade() ? p2 : p1)){                  // Фильтрация по параметру фасада ????
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
    //-------------------------------- Алгоритм базовой оценки --------------------------------------------
    private static String freqList(ArrayList<Extreme> freq, double freqStep){
        String testComment="";
        for(int i=0;i<freq.size();i++){
            if (i!=0)
                testComment+=",";
            testComment+=String.format("%5.2f",freq.get(i).idx*freqStep);
            testComment+=" гц";
            }
        return testComment;
        }
    public Pair<String,Integer> testAlarmBase(LEP500Params set, double freqStep){
        facade = new ExtremeAbs();
        testComment = "";
        boolean warning=false;
        int waveLevelDB = FFTArray.deciBells(source.getWave().calcMaxAbs(false));
        int normLevel = set.signalHighLevelDB;
        if (waveLevelDB < -normLevel){
            warning=true;
            String ss = "Уровни (волна): "+source.getWave().deciBellToString(false);
            testComment += "Низкий уровень сигнала < -"+normLevel+" db\n";
            }
        if (data.size()==0){
            testComment+="Нет пиков (шумы,слабый сигнал)";
            return  new Pair<>(testComment,Values.MSLowLevel);
            }
        Extreme extreme = data.get(0);
        double val0 = extreme.value;            // Значение параметра фасада
        double f0 = extreme.idx*freqStep;
        //------------------ Общий низкий уровень ------------------------------------------------
        if (extreme.value<set.K1){
            testComment+="Слабые пики (шумы,слабый сигнал)";
            return new Pair<>(testComment, Values.MSLowLevel);
            }
        //------------------- Фильтрация------------------------------------------------------------
        double p1 = extreme.value * set.amplLevelProc / 100;
        for(int i=1;i<data.size();) {
            Extreme extreme1 = data.get(i);
            double f = extreme1.idx * freqStep;
            double val = extreme1.value;
            if (extreme1.value < p1){       // Фильтрация по основной амплитуде
                data.remove(i);
                continue;
                }
                i++;
            }
        //------------------- Разделение по диапазонам ---------------------------------------------
        ArrayList<Extreme> highFreq = new ArrayList<>();
        ArrayList<Extreme> noizeFreq = new ArrayList<>();
        ArrayList<Extreme> workFreq = new ArrayList<>();
        for(Extreme extreme1 : data){
            double f = extreme1.idx * freqStep;
            if (f < set.lowFreqLimit){
                noizeFreq.add(extreme1);
                continue;
                }
            if (f > set.secondFreqLimit){
                highFreq.add(extreme1);
                continue;
                }
            if (f >= set.lowFreqLimit &&  f<=set.mainFreqHighLimit)
                workFreq.add(extreme1);
                }
        //------------------- ВЧ пики  ------------------------------------------------------------------
        if (highFreq.size()!=0){
            warning=true;
            testComment+="ВЧ колебания: "+freqList(highFreq,freqStep)+"\n";
            }
        //------------------- Выраженный пик в рабочем диапазоне-----------------------------------------------
        boolean all=true;
        for(Extreme extreme1 : workFreq) {
            double val = extreme1.value;
            if (val/val0>set.K2){
                all=false;
                break;
                }
            }
        if (all){
            testComment+="Нет выраженного пика в "+set.lowFreqLimit+"..."+set.mainFreqHighLimit+", шумы";
            return new Pair<>(testComment, Values.MSNoise);
            }
        //------------------- НЧ пики  ------------------------------------------------------------------
        if (noizeFreq.size()!=0){
            int fidx0 = noizeFreq.get(0).idx;
            sortByFreq(noizeFreq);
            sortByFreq(workFreq);
            int count=0;
            while(workFreq.size()>1){
                Extreme ex = workFreq.get(0);
                Extreme ex2 = workFreq.get(1);
                if (ex.idx-fidx0 < ex2.idx-ex.idx){
                    count++;
                    noizeFreq.add(workFreq.remove(0));
                    }
                else break;
                }
            sortByAmpl(workFreq);
            sortByAmpl(noizeFreq);
            if (count!=0)
                testComment+="Перенесено к НЧ пиков: "+ count+"\n";
            //----------------------------- перенос частот основного диапазона в шумы -------------------
            warning=true;
            testComment +="НЧ пики: "+freqList(noizeFreq,freqStep)+ "\nНедостаточное возбуждение опоры\n";
            if  (workFreq.size()!=0)
                testComment += "Уровень пика относительно шума "+(int)(workFreq.get(0).value*100/noizeFreq.get(0).value)+"%\n";
            }
        extreme = workFreq.get(0);
        double ff0 = extreme.idx*freqStep;
        testComment += String.format("Осн.част.=%5.2f гц Ампл.=%5.2f "+(extreme.decSize==-1 ? "" : "D=%5.2f")+"\n",
                ff0, extreme.value,Math.PI*extreme.decSize/extreme.idx);
        boolean lowArea=false;
        if (workFreq.size()==1 || workFreq.get(1).value/extreme.value*100 < set.neighborPeakAmplProc) {
            if (ff0>=set.mainFreqLowLimit){
                testComment+="Норма: пик в диапазоне "+set.mainFreqLowLimit+"..."+set.mainFreqHighLimit;
                testResult = warning ? Values.MSNormalMinus : Values.MSNormal;
                }
            else{
                lowArea=true;
                testComment+="Авария: пик в диапазоне "+set.lowFreqLimit+"..."+set.mainFreqLowLimit;
                testResult = warning ? Values.MSSumPeak1 : Values.MSFail;
                }
            return new Pair<>(testComment, testResult);
            }
        double f = workFreq.get(1).idx * freqStep;
        testComment+="Смежный пик "+String.format("%5.2f гц Ампл.=%5.2f ",f, workFreq.get(1).value);
        boolean bb = Math.abs((f0-f)/f0*100) > set.neighborPeakFreqProc;
        testComment+= bb || lowArea ? "(авария)" : "(предупр.)";
        return new Pair<>(testComment,  bb ? Values.MSSumPeak1 : Values.MSSumPeak2);
        }
    //-----------------------------------------------------------------------------------------------------
    public static void sortByFreq(ArrayList<Extreme> list){
        list.sort(new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                return o1.idx-o2.idx;
                }
            });
        }
    public static void sortByAmpl(ArrayList<Extreme> list){
        list.sort(new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                return (int)(o2.value-o1.value);
                }
            });
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
    public void sortByValue(){
        data.sort(new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                if (o1.value==o2.value) return 0;
                return o1.value > o2.value ? -1 : 1;
                }
            });
        }
    public void sortByFreq(final double baseIdx){
        for(int i=1;i<data.size();i++){
            for(int j=i;j>0;j--){
                if (Math.abs(data.get(j).idx-baseIdx) > Math.abs(data.get(j-1).idx-baseIdx))
                    break;
                Extreme extreme = data.get(j);
                Extreme extreme2 = data.get(j-1);
                data.set(j,extreme2);
                data.set(j-1,extreme);
                }
            }
        }
    }
