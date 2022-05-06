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
    @Getter private int decCount=0;
    @Getter private double decSum=0;
    public void addValue(double value, int placeIdx, double decrem){
        placeCount+=placeIdx;
        placeSum+=value*placeIdx;
        sum += value;
        count++;
        if (decrem!=0){
            decSum+=decrem;
            decCount++;
            }
        }
    public double decMiddle(){
        return decCount==0 ? 0 : decSum/decCount;
        }
    public PeakPlace(double freq) {
        this.freq = freq;
        }
    public String toString(){
        String ss  = String.format("%6.3f   %-3d   %3d   %6.3f    %6.3f",freq,count,placeCount,sum, placeSum);
        return decCount==0 ? ss : ss + String.format("    %6.3f [%d]",decMiddle(),decCount);
    }
}
