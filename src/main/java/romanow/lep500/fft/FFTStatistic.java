/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.lep500.fft;

// Сбор статистики по слою

import romanow.lep500.LEP500Params;
import romanow.lep500.LEP500Utils;

import java.util.ArrayList;
import java.util.Comparator;


public class FFTStatistic {
    public static Class extremeFacade[] = new Class[]{
        ExtremeAbs.class,
        ExtremeDiff.class,
        ExtremeTrend.class,
        ExtremePower.class,
        ExtremePower2.class
        };
    public final static int ExtremeAbsMode=0;
    public final static int ExtremeDiffMode=1;
    public final static int ExtremeTrendMode=2;
    public final static int ExtremePowerMode=3;
    public final static int ExtremePower2Mode=4;
    private boolean valid=true;
    private String message="";
    private double freqStep=0;
    private double freq=0;
    private String name="";
    private int count=0;
    private int size=0;
    private boolean noReset=true;
    private double prev[]=null;
    private FFTArray wave=null;             // Оригинальная волна
    private SmoothArray sumT=null;          // Сумма по времени
    private SmoothArray sum2T=null;         // Сумма квадратов по времени
    private SmoothArray sum2DiffF=null;     // Корреляция по частоте
    private SmoothArray sum2DiffT=null;     // Корреляция по времени
    private SmoothArray normalized = null;
    public void reset() {
        noReset=true;
        }
    public void lasyReset(double data[]){
        if (!noReset)
            return;
        count=0;
        noReset=false;
        prev = data;
        size = data.length;
        sumT=new SmoothArray(size);
        sum2T=new SmoothArray(size);
        sum2DiffF=new SmoothArray(size);
        sum2DiffT=new SmoothArray(size);
        }

    public FFTArray getSumT(){
        return sumT; }
    public FFTArray getWave() {
        return wave; }
    public void setWave(FFTArray wave) {
        this.wave = wave; }
    public void smooth(int steps){
        sumT.smooth(steps);
        sum2T.smooth(steps);
        sum2DiffF.smooth(steps);
        sum2DiffT.smooth(steps);
        }
    public FFTStatistic(String name){
        setObjectName(name);
        reset();
        }
    public FFTStatistic(String name, boolean valid0, String mes){
        setObjectName(name);
        reset();
        valid = valid0;
        message = mes;
        }
    public void addStatistic(double src[]) throws Exception{
        double data[] = src.clone();
        lasyReset(data);
        for(int i=0;i<size;i++){
            sumT.data[i]+=data[i];
            sum2T.data[i]+=data[i]*data[i];
            if (prev!=null)
                sum2DiffT.data[i]+=(data[i]-prev[i])*(data[i]-prev[i]);
            if (i!=0 && i!=size-1){
                sum2DiffF.data[i]+=(data[i]-data[i-1])*(data[i]-data[i-1]);
                sum2DiffF.data[i]+=(data[i]-data[i+1])*(data[i]-data[i+1]);
                }
            }
        prev = data;
        count++;
        }
    public int getCount(){
        return count;
        }
    //--------------- Среднее и дисперсия для массивов -------------------------
    private double getMid(double vv[]){
        double res=0;
        for(int i=0;i<size;i++)
            res+=vv[i];
        return res/size;
        }
    public double[] getDisps(double vv[]){
        double out[] = vv.clone();
        for(int i=0;i<size;i++){
            if (count==0)
                out[i]=0;
            else
                out[i] = (double)Math.sqrt(out[i]/count);
            }
        return out;
        }
    //--------------------------------------------------------------------------
    public double getDisp(){
        return getMid(getDisps(sum2T.data));
        }
    public double[] getMids(){
        double out[] = sumT.data.clone();
        for(int i=0;i<size;i++){
            if (count==0)
                out[i]=0;
            else
                out[i]/=count;
            }
        return out;
        }
    public double normalizeStart(int nPount){
        if (count==0) return 0;
        normalized = new SmoothArray(size);
        for(int i=0;i<normalized.data.length;i++)
            normalized.data[i]=sumT.data[i]/count;
        normalized.removeTrend(nPount);            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        double max=normalized.data[0];
        for(double vv : normalized.data)
            if (vv > max)
                max = vv;
        return max;
        }
    public void normalizeFinish(double max){
        for(int i=0;i<normalized.data.length;i++)
            normalized.data[i]/=max;
        }
    public double normalizeMax(){
        if (count==0) return 0;
        double max=sumT.data[0];
        for(double vv : sumT.data)
            if (vv > max)
                max = vv;
        return (double) max/count;
        }
    public double getMid(){
        return getMid(getMids());
        }
    public double[] getDisps(){
        return getDisps(sum2T.data);
        }
    public double[] getDiffsF(){
        return getDisps(sum2DiffF.data);
        }
    public double getDiffF(){
        return getMid(getDiffsF());
        }
    public double[] getDiffsT(){
        return getDisps(sum2DiffT.data);
        }
    public double getDiffT(){
        return getMid(getDiffsT());
        }
    public double[] getNormalized() { return normalized.data; }
    public double getFreq() { return freq; }
    public void setFreq(double freq) { this.freq = freq; }

    private void sort(ArrayList<Extreme> list, Comparator<Extreme> comparator){
        int sz = list.size();
        for(int i=1;i<sz;i++)
            for(int j=i;j>0 && comparator.compare(list.get(j-1),list.get(j))>0;j--){
                Extreme cc = list.get(j-1);
                list.set(j-1,list.get(j));
                list.set(j,cc);
                }
        }
    //----------------------------------------------------------------------------------------------
    // Возвращает массиы индексов перегибов (мин. или макс.)
    private static ArrayList<Integer> createPeakIdxs(double data[],int nFirst, int nLast){
        ArrayList<Integer> xx = new ArrayList<>();
        boolean up=true;
        int i=nFirst+1;
        while(i>1 && data[i-1] < data[i])
            i--;                                // Вернуться к первому перегибу
        xx.add(i);
        for(;i<data.length;i++){
            if (up && i>=data.length-nLast)
                break;                          // Дождаться возрастания интервала
            if (up && data[i-1] < data[i])
                continue;
            if (!up && data[i-1] > data[i])
                continue;
            xx.add(i-1);
            up=!up;
            }
        return xx;
        }
    //----------------------------------------------------------------------------------------------
    public ExtremeList createExtrems0(int mode, int nFirst, int nLast, int trendPointsNum){
        ExtremeList out = new ExtremeList(mode);
        double data[] = normalized.getOriginal();
        double trend[] = LEP500Utils.calcTrend(data,trendPointsNum);
        for(int i=nFirst+1;i<size-1-nLast;i++)
            if (data[i]>data[i-1] && data[i]>data[i+1]){
                int k1,k2,k3,k4,k5,k6;
                for(k1=i;k1>0 && data[k1]>data[k1-1];k1--);
                for(k2=i;k2<data.length-1 && data[k2]>data[k2+1];k2++);
                double v1 = data[k1];
                double v2 = data[k2];
                double dv = (v2-v1)/(k2-k1);
                double power=0;
                double power2=0;
                double vv = v1;
                double vmin = v1 < v2 ? v1 : v2;
                for(int j=k1;j<=k2;j++,vv+=dv){
                    //power +=  data[j]-vv;
                    power +=  data[j]-vmin;
                    power2 +=  data[j]-trend[j];
                    }
                /*
                for(k5=i;k5>0 && data[k5]>trend[k5];k5--);
                for(k6=i;k6<data.length-1 && data[k6]>trend[k6];k6++);
                for(int j=k5;j<=k6;j++){
                    power2 +=  data[j]-trend[j];
                    }
                */
                for(k3=i;k3>=0 && data[k3]>data[i]/2;k3--);
                for(k4=i;k4<data.length && data[k4]>data[i]/2;k4++);
                double d1 = data[i]-data[k1];
                double d2 = data[i]-data[k2];
                double diff = Math.sqrt((d1*d1+d2*d2)/2);
                int decrem=k4-k3;
                if (k3<k1 || k4>k2)
                    decrem = -1;
                double dd = 0;
                out.data().add(new Extreme(data[i],i,diff,(data[i]-trend[i]),power,power2,decrem));
                }
        try {
            ExtremeFacade facade=((ExtremeFacade) extremeFacade[mode].newInstance());
            sort(out.data(),facade.comparator());
            } catch (Exception ee){ }
        return out;
        }
    //----------------------------------------------------------------------------------------------
    public ExtremeList createExtrems(int mode, LEP500Params set) {
        return createExtrems(mode,noFirstPoints(set),noLastPoints(set),set.nTrendPoints);
        }
    public ExtremeList createExtrems(int mode, int nFirst, int nLast, int trendPointsNum){
        double data[] = normalized.getOriginal();
        ExtremeList out = new ExtremeList(mode);
        double trend[] = LEP500Utils.calcTrend(data,trendPointsNum);
        ArrayList<Integer> peaksIdx = createPeakIdxs(data,nFirst,nLast);
        for(int idx=1;idx<peaksIdx.size();idx+=2){
            int k1,k2,k3,k4,k5,k6;
            k1=peaksIdx.get(idx-1);
            k2=peaksIdx.get(idx+1);
            int i=peaksIdx.get(idx);
            double v1 = data[k1];
            double v2 = data[k2];
            double dv = (v2-v1)/(k2-k1);
            double power=0;
            double power2=0;
            double vv = v1;
            double vmin = v1 < v2 ? v1 : v2;
            for(int j=k1;j<=k2;j++,vv+=dv){
               power +=  data[j]-vmin;
               power2 +=  data[j]-trend[j];
               }
           for(k3=i;k3>=0 && data[k3]>data[i]/2;k3--);
           for(k4=i;k4<data.length && data[k4]>data[i]/2;k4++);
           double d1 = data[i]-data[k1];
           double d2 = data[i]-data[k2];
           double diff = Math.sqrt((d1*d1+d2*d2)/2);
           int decrem=k4-k3;
           if (k3<k1 || k4>k2)
               decrem = -1;
               double dd = 0;
               out.data().add(new Extreme(data[i],i,diff,(data[i]-trend[i]),power,power2,decrem));
               }
        try {
            ExtremeFacade facade=((ExtremeFacade) extremeFacade[mode].newInstance());
            sort(out.data(),facade.comparator());
            } catch (Exception ee){ }
        return out;
    }
    //--------------------------------------------------------------------------
    public String getTypeName() {
        return "Статистика";
        }
    public String getName() {
        return getObjectName();
        }    
    public String getObjectName() {
        return name;
        }
    public void setObjectName(String name) {
        this.name = name;
        }
    //------------------- Коррекция экспоненты--------------------------
    public double correctExp(int nPoints){
        double a0=sumT.data[0];
        double k=0;
        for(int i=0;i<nPoints;i++)
            k += -Math.log(sumT.data[i+1]/sumT.data[i]);
        k /=nPoints;
        for(int i=0;i<sumT.data.length;i++)
            sumT.data[i]-=a0*Math.exp(-k*i);
        return k;
        }
    //-----------------------------------------------------------------
    public double getFreqStep() {
        return freqStep;
        }
    public void setFreqStep(double freqStep) {
        this.freqStep = freqStep;
        }
    public String getMessage() {
        return message; }
    public void setMessage(String message) {
        this.message = message; }
    public boolean isValid() {
        return valid; }
    public void setValid(boolean valid) {
        this.valid = valid; }
    public int noFirstPoints(LEP500Params params){
        return  (int)(params.FirstFreq/freqStep);
        }
    public int noLastPoints(LEP500Params params){
        return (int)((freq/2-params.LastFreq)/freqStep);
        }
    }
