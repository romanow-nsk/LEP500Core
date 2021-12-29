package romanow.lep500.fft;

public class SmoothArray extends FFTArray{
    public SmoothArray(int size){
        super(size);
    }
    void smoothOne(){
        int size = data.length;
        double out[] = new double[size];
        out[0]=(double)( 0.5*(data[1]+data[0]));
        for(int i=1;i<size-1;i++)
            out[i] = (double)( 0.5*(0.5*(data[i-1]+data[i+1])+data[i]));
        out[size-1]=(double)( 0.5*(data[size-2]+data[size-1]));
        data = out;
    }
    void smooth(int count){
        while (count-->0)
            smoothOne();
    }
}
