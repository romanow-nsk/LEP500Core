package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremeAbs extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Амплитуда";
        }
    @Override
    public String getColName() {
        return "Ампл."; }
    @Override
    public double getValue() {
        return extreme.value;
        }
    @Override
    public boolean isPowerFacade() {
        return false;
        }
    @Override
    public Comparator<Extreme> comparator() {
        return new Comparator<Extreme>() {
            @Override
            public int compare(Extreme o1, Extreme o2) {
                if (o1.value==o2.value) return 0;
                return o1.value > o2.value ? -1 : 1;
            }
        };
    }
}
