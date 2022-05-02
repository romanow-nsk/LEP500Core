package romanow.lep500;

import romanow.lep500.fft.I_Notify;

import java.io.*;
import java.util.ArrayList;

public class SqeezyWaveData {
    private final static int FindOver=0;
    private final static int FindMinAfterOver=1;
    private final static int CopyData=2;
    private final static int SkipData=2;
    private double data[];
    private double noiseLevel;
    private double sqData[];
    private int sqIdx=0;
    private int startCopyIdx=0;
    private int cMaxIdx=0;
    private double maxFull;
    private ArrayList<Integer> maxIdx;      // Положительные максимумы
    private ArrayList<Integer> maxStartIdx; // Максимумы стартов
    public SqeezyWaveData(){
        }
    private boolean isNoise() {
        double vv = data[maxIdx.get(cMaxIdx)];
        return vv > 0 && vv < maxFull * noiseLevel;
        }
    private boolean isMinMax(){
        double d1 = data[maxIdx.get(cMaxIdx-1)];
        double d2 = data[maxIdx.get(cMaxIdx)];
        double d3 = data[maxIdx.get(cMaxIdx+1)];
        return  d2>0 && d2 < d1 && d2 < d3;
        }
    public double []squeezy(double data0[], double startDiff, double startLevel, int skipPeaks){
        data = data0;
        sqData = new double[data.length];
        sqIdx=0;
        maxIdx = new ArrayList<>();          // Огибающая из максимумов
        for(int i=1; i<data.length-1;i++)
            if (data[i]>0 && data[i] > data[i-1] && data[i] > data[i+1])
                maxIdx.add(i);
        if (maxIdx.size()==0)
            return data;
        maxFull = data[maxIdx.get(0)];
        for(Integer idx : maxIdx)
            if (data[idx.intValue()]>maxFull)
                maxFull = data[idx.intValue()];
        maxStartIdx = new ArrayList<>();
        for(int i=1;i<maxIdx.size();i++){
            int idx = maxIdx.get(i);
            if (data[idx]/data[maxIdx.get(i-1)] >startDiff && data[idx]>maxFull*startLevel){
                maxStartIdx.add(i);
                System.out.println("overIdx="+idx+" "+(int)data[idx]);
                }
            }
        maxStartIdx.add(maxIdx.size()-1);                               // Добавить последний
        for (int i=0;i<maxStartIdx.size()-1;i++){
            int firstIdx = maxIdx.get(maxStartIdx.get(i)+skipPeaks);    // Первый пик интервала и пропустить
            int lastIdx = maxIdx.get(maxStartIdx.get(i+1)-1);           // Предпоследний пик перед следующим ударом
            for(int j=firstIdx; j<lastIdx;j++)             // Скопировать
                sqData[sqIdx++]=data[j];
            //------------------ TODO - copyData before Noise -------------------------
            }
        double out[] = new double[sqIdx];
        for(int i=0;i<out.length;i++)
            out[i]=sqData[i];
        return out;
        }
    public static void main(String ss[]) throws Exception {
        String test1="20220426T092725_45-1_СМ-300_Опора 125";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(test1+".txt"),"Windows-1251"));
        FFTAudioTextFile file = new FFTAudioTextFile();
        FileDescription description = new FileDescription("");
        file.readData(description,reader,true);
        double dd[] = file.getData();
        SqeezyWaveData sqeezyData = new SqeezyWaveData();
        double out[] = sqeezyData.squeezy(dd,10, 0.6,10);
        file.setData(out);
        file.writeWave("out.wav", 441, new I_Notify() {
            @Override
            public void onMessage(String mes) {
                System.out.println(mes);
                }
            @Override
            public void onError(Exception ee) {
                System.out.println(ee.toString());
                }
            });
        description.setSupport(description.getSupport()+"++");
        file.save("",description,new I_EventListener() {
            @Override
            public void onEvent(String ss) {
                System.out.println(ss);
                }
            });
        }
    }
