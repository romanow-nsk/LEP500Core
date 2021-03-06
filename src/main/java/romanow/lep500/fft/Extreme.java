package romanow.lep500.fft;

public class Extreme {
    public final double value;
    public final double diff;
    public final double trend;
    public final double power;
    public final double power2;
    public final int idx;
    public final int decSize;           // Интервал декремента в индексах частоты
    public Extreme(double value, int idx,double diff,double trend,double power, double power2,int decSize) {
        this.value = value;
        this.diff = diff;
        this.idx = idx;
        this.trend = trend;
        this.decSize = decSize;
        this.power = power;
        this.power2 = power2;
        }
}
