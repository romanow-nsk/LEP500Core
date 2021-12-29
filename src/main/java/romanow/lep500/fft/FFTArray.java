/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.lep500.fft;

//     Нормальзованный массив данных

import romanow.lep500.LEP500Utils;

public class FFTArray {
    protected double data[]=new double[0];
    private double diff[]=new double[0];
    private double max=0;
    private int count=0;
    private double delta=0.95F;
    public int size(){
        return data.length;
        }
    public void clear(){
        count=0;
        max=0;
        for (int i=0;i<data.length;i++){
            data[i]=0;
            diff[i]=0;
            }
        }
    public double getSumDiff(int i){
        if (count==0)
            return 0;
        return (double)(Math.sqrt(diff[i])/count);
        }
    public int getCount(){
        return count;
        }
    public void nextStep(){
        max *= delta;
        count++;
        }
    public FFTArray(int size){
        data = new double[size];
        diff = new double[size];
        clear();
        }
    public FFTArray(double dd[]){
        data = dd;
        diff = new double[dd.length];
        for (int i=0;i<data.length;i++){
            diff[i]=0;
            }
        count=1;
        }
    public void clearMax(){
        max=0;
        }
    public double getMax(){
        return max;
        }
    public double get(int i){
        if (i<0 || i>=data.length)
            return 0;
        return data[i];
        }
    public void set(int i, double value){
        if (i<0 || i>=data.length)
            return;
        diff[i] += Math.abs(data[i]-value);
        count++;
        data[i] = value;
        if (value > max)
            max = value;
        }

    public String deciBellToString(){
        return deciBells(calcMaxAbs())+" db / "+deciBells(calcMaxesMidAbs())+" db / "+deciBells(calcMidAbs())+" db";
        }
    public double calcMaxAbs(){
        double max = 0;
        for(int i=0;i<data.length;i++){
            double vv = Math.abs(data[i]);
            if (vv > max)
                max = vv;
            }
        return max/count;
        }
    public double calcMidAbs(){
        double sum = 0;
        for(int i=0;i<data.length;i++){
            double vv = Math.abs(data[i]);
            sum += vv;
            }
        return sum/data.length/count;
        }
    public double calcMaxesMidAbs(){
        double sum = 0;
        int cnt=0;
        for(int i=1;i<data.length-1;i++){
            double vv = Math.abs(data[i]);
            double vv1 = Math.abs(data[i-1]);
            double vv2 = Math.abs(data[i+1]);
            if (vv > vv1 && vv > vv2){
                cnt++;
                sum += vv;
                }
            }
        return cnt==0 ? 0 : sum/count/cnt;
        }
    public static int deciBells(double vv){
        int maxInt = 0x7FFF;
        return (int)(20*Math.log10(vv/maxInt));
        }
    public void normalize(double k){
        for (int i=0;i<data.length;i++)
            data[i] *= k;
        }    
    public void normalize(){
        for (int i=0;i<data.length;i++)
            data[i]/= max;
        }    
    public double []getNormalized(double k){
        double out[] = (double[])data.clone();
        for (int i=0;i<data.length;i++)
            out[i] *= k;
        return out;
        }
    public double []getNormalized(){
        double out[] = (double[])data.clone();
        if (max==0)
            return out;
        for (int i=0;i<data.length;i++)
            out[i] /=max;
        return out;
        }
    public void compress(boolean compressMode, double compressGrade,double k){
        normalize(k);
        if (!compressMode)
            return;
        for(int i=0;i<data.length;i++){
            data[i] = 1- LEP500Utils.getExp(compressGrade*data[i]/max);
            }
        }
    public double []getCompressed(boolean compressMode, double compressGrade, double k){
        if (!compressMode)
            return getNormalized();
        double out[] = getNormalized(k);
        for(int i=0;i<out.length;i++){
            out[i] = 1-LEP500Utils.getExp(compressGrade*out[i]/max);
            }
        return out;
        }
    public double []getOriginal(){
        return data.clone();
        }
    public void removeTrend(int nPoints){
        if (nPoints==0)
            return;
        double middles[] = new double[data.length];
        for(int i=0;i<data.length;i++){
            middles[i]=0;
            for(int j=i-nPoints;j<=i+nPoints;j++){
                if (j<0)
                    middles[i]+=data[0];
                else
                if(j>=data.length)
                    middles[i]+=data[data.length-1];
                else
                    middles[i]+=data[j];
                }
            middles[i]/=2*nPoints+1;
            }
        for(int ii=0;ii<data.length;ii++)
            data[ii]-=middles[ii];
        }
}
