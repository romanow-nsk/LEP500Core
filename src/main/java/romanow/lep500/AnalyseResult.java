package romanow.lep500;

import romanow.lep500.fft.ExtremeList;
import romanow.lep500.fft.FFTStatistic;

import java.util.ArrayList;

public class AnalyseResult extends ArrayList<ExtremeList> {
    public final String title;
    public final double dFreq;
    public final double spectrum[];
    public final String message;
    public final boolean valid;
    public AnalyseResult(FFTStatistic statistic) {
        this.title = statistic.getName();
        this.dFreq = statistic.getFreqStep();
        this.spectrum = statistic.getNormalized();
        this.message = statistic.getMessage();
        this.valid = statistic.isValid();
    }
}
