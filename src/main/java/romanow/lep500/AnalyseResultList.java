package romanow.lep500;

import romanow.abc.core.Pair;
import romanow.abc.core.constants.Values;
import romanow.lep500.fft.Extreme;
import romanow.lep500.fft.ExtremeFacade;
import romanow.lep500.fft.ExtremeList;
import romanow.lep500.fft.ExtremeNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AnalyseResultList {
    private ArrayList<AnalyseResult> data;

    public AnalyseResultList(ArrayList<AnalyseResult> data0) {
        data = data0;
    }

    public Statistic[] calcFirstFreq() {
        if (data.size() == 0)
            return new Statistic[0];
        int nRes = data.get(0).data.size();          // Количество характеристик
        Statistic out[] = new Statistic[nRes];
        for (int i = 0; i < out.length; i++)
            out[i] = new Statistic();
        int count = 0;
        double sum = 0;
        double sum2 = 0;
        for (AnalyseResult res : data) {
            int i = 0;
            for (ExtremeList list : res.data) {
                double freq = res.dFreq * list.data().get(0).idx;
                if (i < out.length)
                    out[i].addNotNull(freq);
            }
        }
        return out;
    }

    public ArrayList<PeakPlace> calcPeakPlaces() {
        int size = Values.extremeFacade.length;
        ExtremeFacade facades[] = new ExtremeFacade[size];
        for (int i = 0; i < size; i++) {
            try {
                facades[i] = (ExtremeFacade) Values.extremeFacade[i].newInstance();
            } catch (Exception e) {
                facades[i] = new ExtremeNull();
            }
        }
        HashMap<Integer, PeakPlace> placeMap = new HashMap<>();
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            StringBuffer ss = new StringBuffer();
            AnalyseResult result = data.get(i);
            for (int k = 0; k < result.data.size(); k++) {
                ExtremeFacade facade = facades[k];
                ExtremeList list = result.data.get(k);
                for (int j = 0; j < list.data().size(); j++) {
                    Extreme extreme = list.data().get(j);
                    facade.setExtreme(extreme);
                    PeakPlace peak = placeMap.get(extreme.idx);
                    if (peak == null) {
                        peak = new PeakPlace(extreme.idx * result.dFreq);
                        placeMap.put(extreme.idx,peak);
                        }
                    peak.addValue(facade.getValue(), list.data().size() - j,extreme.decSize*result.dFreq);
                    }
                }
            }
        ArrayList<PeakPlace> vv = new ArrayList<>();
        Object oo[] = placeMap.values().toArray();
        for(Object bb : oo)
            vv.add((PeakPlace)bb);
        vv.sort(new Comparator<PeakPlace>() {
            @Override
            public int compare(PeakPlace o1, PeakPlace o2) {
                return (int)(1000*(o1.getFreq()-o2.getFreq()));
                //return o2.getPlaceCount()-o1.getPlaceCount();
                }
            });
        return vv;
        }
    }

