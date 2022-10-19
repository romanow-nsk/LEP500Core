package romanow.lep500.fft;

import java.util.Comparator;

public abstract class ExtremeFacade {
    Extreme extreme;
    public abstract String getTitle();
    public abstract String getColName();
    public abstract double getValue();
    public abstract boolean isPowerFacade();
    public void setExtreme(Extreme extreme){
        this.extreme = extreme;
        }
    public Extreme extreme(){ return extreme; }
    public abstract Comparator<Extreme> comparator();
}
