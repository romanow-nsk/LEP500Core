package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremeTrend extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Пик по тренду";
        }
    @Override
    public String getColName() {
        return "\u0394 Тренд"; }
    @Override
    public double getValue() {
        return extreme.trend;
        }
    @Override
    public boolean isPowerFacade() {
        return false;
        }
    @Override
    public Comparator<Extreme> comparator() {
        return         new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                double vv1 = o1.trend;
                double vv2 = o2.trend;
                if (vv1==vv2) return 0;
                return vv1 > vv2 ? -1 : 1;
            }
        };
    }
}
