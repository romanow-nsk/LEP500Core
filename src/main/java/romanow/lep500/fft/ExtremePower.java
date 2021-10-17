package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremePower extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Мощность пика к спаду";
        }
    @Override
    public String getColName() {
        return "Мощн."; }
    @Override
    public double getValue() {
        return extreme.power;
        }
    @Override
    public Comparator<Extreme> comparator() {
        return new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                double vv1 = o1.power;
                double vv2 = o2.power;
                if (vv1==vv2) return 0;
                return vv1 > vv2 ? -1 : 1;
            }
        };
    }
}
