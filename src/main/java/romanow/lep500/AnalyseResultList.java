package romanow.lep500;

import romanow.abc.core.Pair;
import romanow.lep500.fft.ExtremeList;

import java.util.ArrayList;

public class AnalyseResultList {
    private ArrayList<AnalyseResult> data;
    public AnalyseResultList(ArrayList<AnalyseResult> data0){
        data = data0;
        }
    public Statistic []calcFirstFreq(){
        if (data.size()==0)
            return new Statistic[0];
        int nRes = data.get(0).data.size();          // Количество характеристик
        Statistic out[] = new Statistic[nRes];
        for(int i=0;i<out.length;i++)
            out[i]=new Statistic();
        int count=0;
        double sum=0;
        double sum2=0;
        for(AnalyseResult res : data){
            int i=0;
            for(ExtremeList list : res.data){
                double freq = res.dFreq*list.data().get(0).idx;
                if (i<out.length)
                    out[i].addNotNull(freq);
                }
            }
        return out;
    }
}
