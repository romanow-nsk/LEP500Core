package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremePower2 extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Мощность пика к тренду";
        }
    @Override
    public double getValue() {
        return extreme.power2;
        }
    @Override
    public boolean isPowerFacade() {
        return true;
        }
    @Override
    public String getColName() {
        return "Мощн."; }
    @Override
    public Comparator<Extreme> comparator() {
        return new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                double vv1 = o1.power2;
                double vv2 = o2.power2;
                if (vv1==vv2) return 0;
                return vv1 > vv2 ? -1 : 1;
            }
        };
    }
}
