package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremeDiff extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Пик по спаду";
        }
    @Override
    public String getColName() {
        return "\u0394 Спад"; }
    @Override
    public double getValue() {
        return extreme.diff;
        }
    @Override
    public Comparator<Extreme> comparator() {
        return new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                if (o1.diff == o2.diff) return 0;
                return o1.diff > o2.diff ? -1 : 1;
            }
        };
    }
}
