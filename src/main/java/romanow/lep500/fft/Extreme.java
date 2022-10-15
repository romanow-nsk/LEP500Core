package romanow.lep500.fft;

public class Extreme {
    public final double value;      // Амплитуда
    public final double diff;       // Амплитуда к спаду (усредненному)
    public final double trend;      // Амплитуда к тренду
    public final double power;      // Мощность к минимальному спаду
    public final double power2;     // Мощность к тренду
    public final int idx;
    public final int decSize;       // Интервал декремента в индексах частоты
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
