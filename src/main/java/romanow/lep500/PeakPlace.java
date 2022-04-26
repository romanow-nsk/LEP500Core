package romanow.lep500;

import lombok.Getter;
import lombok.Setter;

public class PeakPlace {
    @Getter @Setter private double freq;
    @Getter @Setter private double placeValue=0;
    @Getter @Setter private int placeCount=0;
    @Getter private double placeSum=0;
    @Getter private double sum=0;
    @Getter private int count=0;
    public void addValue(double value, int placeIdx){
        placeCount+=placeIdx;
        placeSum+=value*placeIdx;
        sum += value;
        count++;
        }
    public PeakPlace(double freq) {
        this.freq = freq;
        }
    public String toString(){
        return String.format("%6.3f   %3в  %3d   %6.3f    %6.3f",freq,count,placeCount,sum, placeSum);
    }
}
