package romanow.lep500.fft;

import java.util.Comparator;

public class ExtremeNull extends ExtremeFacade {
    @Override
    public String getTitle() {
        return "Ошибка";
        }
    @Override
    public String getColName() {
        return ""; }
    @Override
    public double getValue() {
        return 0;
        }
    @Override
    public Comparator<Extreme> comparator() {
        return null;
        }
}
