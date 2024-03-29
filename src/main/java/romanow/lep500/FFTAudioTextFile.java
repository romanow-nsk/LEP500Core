/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.lep500;


import java.io.*;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import romanow.abc.core.constants.Values;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;
import romanow.lep500.fft.FFTAudioSource;
import romanow.lep500.fft.FFTFileSource;
import romanow.lep500.fft.I_Notify;


/**
 *
 * @author romanow
 */
public class FFTAudioTextFile implements FFTFileSource {
    protected String fspec=null;
    protected int sz=0;
    protected double data[]=null;
    protected int nPoints=0;                    // Количество точек для удаления тренда
    private int expertNote = Values.ESNotSet;   // Экспертная оценка
    //--------------------------------------------------------------------------------------
    private transient int cnum;
    private transient BufferedReader AudioFile=null;
    //---------------------------------------------------------------------------------------
    public FFTAudioTextFile(){}
    public void setnPoints(int nPoints) {
        this.nPoints = nPoints; }
    public void write_little_endian(int word, int num_bytes, OutputStream wav_file) throws IOException{
        int buf;
        while(num_bytes>0){   
            buf = word & 0xff;
            wav_file.write(buf);
    		num_bytes--;
            word >>= 8;
            }
        }       
    public void write_string(String ss, OutputStream wav_file) throws IOException{
        char cc[] = ss.toCharArray();
        for(int i=0;i<cc.length;i++){   
            wav_file.write((byte)cc[i]);
            }
        }
    public double[] getData(){
        return data.clone();
        }
    public void setData(double dd[]){
        data = dd.clone();
        }
    public void setData(short dd[]){
        data = new double[dd.length];
        for(int i=0;i<dd.length;i++)
            data[i]=dd[i];
        }
    public int getExpertNote() {
        return expertNote;}
    public void setExpertNote(int expertNote) {
        this.expertNote = expertNote;}
    public static void readHeader(FileDescription fd, BufferedReader AudioFile) throws IOException {
        String in;
        String dateTime = AudioFile.readLine();             // 0
        String groupTitle = AudioFile.readLine();           // 1
        String line = fd.getPowerLine();
        if (line.length()==0){
            int idx=groupTitle.lastIndexOf("_");
            if (idx==-1)
                idx=groupTitle.lastIndexOf(" ");
            if (idx==-1)
                fd.setPowerLine(groupTitle);
            else{
                fd.setPowerLine(groupTitle.substring(0,idx));
                fd.setSupport(groupTitle.substring(idx+1));
                }
            }
        else{
            if (fd.getSupport().length()==0){
                if (groupTitle.startsWith(line)){
                    fd.setSupport(groupTitle.substring(line.length()+1));
                    }
                }
            }
        if (!fd.getCreateDate().dateTimeValid()){
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy' 'HH:mm:ss");
            DateTime tt = formatter.parseDateTime(dateTime);
            fd.setCreateDate(new OwnDateTime(tt.getMillis()));
            }
        String geoY = AudioFile.readLine();                 // 2
        String geoX = AudioFile.readLine();                 // 3
        String gState = AudioFile.readLine();               // 4
        try {
            int gpsState = Integer.parseInt(gState);
            if (gpsState == ValuesBase.GeoNone){
                if (geoX.length()!=0)
                    fd.setGps(new GPSPoint(geoY,geoX,true));
                    }
                else
                    fd.setGps(new GPSPoint(geoY,geoX,gpsState==ValuesBase.GeoGPS));
            } catch (Exception ee){}
        in = AudioFile.readLine();      // 5
        in = AudioFile.readLine();      // 6
        try {
            int vv = Integer.parseInt(in);
            fd.setExpertNote(vv);
            } catch (Exception ee){ fd.setMeasureCounter(Values.ESNotSupported); }
        in = AudioFile.readLine();      // 7
        try {
            fd.setMeasureCounter(Integer.parseInt(in));
            } catch (Exception ee){ fd.setMeasureCounter(0); }
        in = AudioFile.readLine();      // 8
        try {
            fd.setFileFreq(Integer.parseInt(in)/100.);
            } catch (Exception ee){ fd.setFileFreq(100); }
        in = AudioFile.readLine();      // 9
        if (fd.getSensor().length()==0){
        if (in.toLowerCase().startsWith(SensorPrefix))
            fd.setSensor(in.substring(SensorPrefix.length()));
        else
            fd.setSensor(in);
            }
        }
    public void readData(FileDescription fd, BufferedReader AudioFile) {
        readData(fd,AudioFile,true);
        }
    public void readData(FileDescription fd, BufferedReader AudioFile, boolean normalize) {
        try {
            readHeader(fd, AudioFile);
            sz = Integer.parseInt(AudioFile.readLine());
            data = new double[sz];
            double mid = 0, min = data[0], max = data[0];
            for (int i = 0; i < sz; ) {
                String ss = AudioFile.readLine();
                if (ss.length() == 0)
                    continue;
                data[i] = Double.parseDouble(ss);
                mid += data[i];
                i++;
                }
            int midd = (int) (mid / sz);
            for (int i = 0; i < sz; i++) {         // Убрать постоянную составляющую
                data[i] -= midd;
                }
            if (!normalize)
                return;
            for (int i = 0; i < sz; i++) {
                if (data[i] > max) max = data[i];
                if (data[i] < min) min = data[i];
                }
            if (Math.abs(max) > Math.abs(min))
                min = max;
            min = Math.abs(min);
            min = Short.MAX_VALUE * 0.9f / min;
            for (int i = 0; i < sz; i++) {
                data[i] *= min;
                }
            }catch (IOException ee){
                fd.setFormatError("Ошибка: "+ee.toString());
                try { AudioFile.close(); } catch (Exception ex){}
                }
        }
    public boolean convertToWave(FileDescription fd, double freq, String outFile, String pathToFile, I_Notify back){
        fspec=null;
        try {
            AudioFile = new BufferedReader(new FileReader(pathToFile));
            } catch (FileNotFoundException ex) {
                return false;
                }
        String in;
        try {
            readData(fd, AudioFile);
            String error = fd.getFormatError();
            if (error.length()!=0){
                back.onMessage(error);
                return false;
                }
            String outname = outFile != null ? outFile : pathToFile;
            int k = outname.lastIndexOf(".");
            outname = outname.substring(0, k) + ".wav";
            writeWave(outname,freq,back);
            fspec = pathToFile;
            } catch(Exception ee){
                back.onError(ee);
                close();
                return false;
                }
            return true;
            }
    public boolean convertToWave(FFTAudioSource source, String outFile, I_Notify back){
        try {
            int sz = (int)source.getFrameLength();
            data = new double[sz];
            source.read(data,0,sz);
            writeWave(outFile,source.getSampleRate(),back);
            } catch(Exception ee){
                back.onError(ee);
                close();
                return false;
                }
            return true;
        }
    public void writeWave(String outname,double freq, I_Notify back) throws Exception{
            removeTrend(nPoints);
            FileOutputStream wav_file = new FileOutputStream(outname);
        	int sample_rate;
            int num_channels;
            int bytes_per_sample;
            int byte_rate;
            int num_samples = data.length;
            int i;  
            num_channels = 1;  
            bytes_per_sample = 2;
            sample_rate = (int)(freq*100); //100; // 44100;
        	byte_rate = sample_rate*num_channels*bytes_per_sample;
        	write_string("RIFF", wav_file);
            write_little_endian(36 + bytes_per_sample* num_samples*num_channels, 4, wav_file);
            write_string("WAVE", wav_file);
            write_string("fmt ", wav_file);
            write_little_endian(16, 4, wav_file);   
            write_little_endian(1, 2, wav_file);    
            write_little_endian(num_channels, 2, wav_file);
            write_little_endian(sample_rate, 4, wav_file);
            write_little_endian(byte_rate, 4, wav_file);
            write_little_endian(num_channels*bytes_per_sample, 2, wav_file);  
            write_little_endian(8*bytes_per_sample, 2, wav_file);  
            write_string("data", wav_file);
            write_little_endian(bytes_per_sample* num_samples*num_channels, 4, wav_file);
            for (i=0; i< num_samples; i++){
                write_little_endian((short)data[i],bytes_per_sample, wav_file);
                }   
            wav_file.flush();
            wav_file.close();
            back.onMessage("Записано "+num_samples+" сэмплов, "+ ((double)num_samples)/sample_rate+ " сек");
            close();
            }
    public final static int Test=0;
    public final static int Open=1;
    public final static int OpenAndPlay=2;
    public boolean testAndOpenFile(FileDescription fd, int mode, String PatnToFile, int sizeHZ, I_Notify back){
        try {
            AudioFile = new BufferedReader(new FileReader(PatnToFile));
            } catch (FileNotFoundException ex) {
                return false;
                }
        if (mode==Test){
            close();
            return true;
            }
        String in;
        try {
            readData(fd,AudioFile);
            removeTrend(nPoints);
            close();
            cnum=0;
            return true;
            } catch(Exception ee){
                close();
                return false;
                }
        }

    @Override
    public String getFileSpec() {
        return fspec;
        }

    @Override
    public String testSource(int sizeHZ) {
        // Формат не проверяется
        return null;
        }

    @Override
    public long getFrameLength() {
        return sz;
        }
    @Override
    public int read(double[] buf, int offset, int lnt) throws IOException {
        if (sz==0)
            return 0;
        int cnt=0;
        for(int i=offset; i<offset+lnt && cnum<sz; i++){
            buf[i] = data[cnum] /Short.MAX_VALUE;
            //System.out.println("text "+i+" "+buf[i]);
            cnum++;
            cnt++;
            }
        return cnt;
        }
    @Override
    public void close() {
        try {
            if (AudioFile!=null){
                AudioFile.close();
                }
            } catch (IOException ex) {}
        }

    public String getTypeName() {
        return "Текстовый файл";
        }
    public String getName() {
        return getTypeName();
        }

    @Override
    public double getSampleRate() {
        return 44100;
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
    //------------------------------------------------------------------------------------------ LEP500File
    public final static String SensorPrefix="канал-";
    private DateTime createDate = new DateTime();
    private LEP500Params settings;
    private String sensorName;
    private GPSPoint gps;
    public FFTAudioTextFile(LEP500Params settings0, String sensorName0, GPSPoint gps0){
        settings = settings0;
        gps = gps0;
        sensorName = sensorName0;
        }
    public LEP500Params getSettings() {
        return settings; }
    public String createOriginalFileName(){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("HHmmss");
        return dtf.print(createDate)+"T"+dtf2.print(createDate)+"_"+ settings.measureCounter+"-"+sensorName+"_"+settings.measureGroup+"_"+settings.measureTitle+".txt";
        }
    public void createTestMeasure(){
        int v0=1000;
        int ampl = 200;
        sz=10000;
        data = new double[sz];
        // 2pi = 100гц = 10 мс, 2 гц = 500 мс, период = 250 отсчетов
        for(int i=0;i<data.length;i++){
            data[i]=(short) (v0/5+1000*Math.sin(i*2*Math.PI/25.)+500*Math.sin(i*2*Math.PI/10.));
            v0++;
            }
        }
    public void save(String path,I_EventListener back){
        save(false,path,back);
        }
    public void save(boolean fullPath,String path,I_EventListener back){
        FileOutputStream out=null;
        String fspec = path + (fullPath ? "" : ("/"+createOriginalFileName()));
        try {
            out = new FileOutputStream(fspec);
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(out,"Windows-1251"));
            //0 16 октября 2020г. 16:53:01
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
            os.write(dtf.print(createDate));
            os.newLine();
            //1 CM-316 ужур сора 1 цепь опора 352
            os.write(settings.measureGroup+"_"+settings.measureTitle);
            os.newLine();
            //2 -------
            os.write(gps.toStrY());
            os.newLine();
            //3 -------
            os.write(gps.toStrX());
            os.newLine();
            //4 0
            os.write(""+gps.state());
            os.newLine();
            //5 16 бит  тдм 003
            os.write("16 бит");
            os.newLine();
            //6 1
            os.write(""+expertNote);
            os.newLine();
            //7
            os.write(""+settings.measureCounter);
            os.newLine();
            //8 10000
            os.write(""+(int)(settings.measureFreq*100));
            os.newLine();
            //9 канал-1 баланс=128 температура=8C
            os.write(SensorPrefix+sensorName);
            os.newLine();
            os.write(""+data.length);
            os.newLine();
            for(int i=0;i<data.length;i++) {
                os.write(""+data[i]);
                os.newLine();
            }
            os.flush();
            os.close();
            out.close();
        } catch (Exception e) {
            back.onEvent("Ошибка записи в файл "+fspec+": "+e.toString());
            if (out!=null) {
                try {
                    out.close();
                } catch (IOException ex) {}
            }
        }
    }
    public void save(String path,FileDescription fd, I_EventListener back){
        save(false,path,fd,back);
        }
    public void save(boolean full, String path,FileDescription fd, I_EventListener back){
        FileOutputStream out=null;
        String fspec = fd.createOriginelFileName();
        if (path!=null && path.length()!=0)
            fspec = path+(full ? "" : "/"+fspec);
        try {
            out = new FileOutputStream(fspec);
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(out,"Windows-1251"));
            //0 16 октября 2020г. 16:53:01
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
            os.write(dtf.print(fd.getCreateDate().timeInMS()));
            os.newLine();
            //1 CM-316 ужур сора 1 цепь опора 352
            os.write(fd.getPowerLine()+"_"+fd.getSupport());
            os.newLine();
            //2 -------
            os.write(fd.getGps().toStrY());
            os.newLine();
            //3 -------
            os.write(fd.getGps().toStrX());
            os.newLine();
            //4 0
            os.write(""+fd.getGps().state());
            os.newLine();
            //5 16 бит  тдм 003
            os.write("16 бит");
            os.newLine();
            //6 1
            os.write(""+fd.getExpertNote());
            os.newLine();
            //7
            os.write(""+fd.getMeasureCounter());
            os.newLine();
            //8 10000
            os.write(""+(int)(fd.getFileFreq()*100));
            os.newLine();
            //9 канал-1 баланс=128 температура=8C
            os.write(SensorPrefix+sensorName);
            os.newLine();
            os.write(""+data.length);
            os.newLine();
            for(int i=0;i<data.length;i++) {
                os.write(""+data[i]);
                os.newLine();
                }
            os.flush();
            os.close();
            out.close();
        } catch (Exception e) {
            back.onEvent("Ошибка записи в файл "+fspec+": "+e.toString());
            if (out!=null) {
                try {
                    out.close();
                } catch (IOException ex) {}
            }
        }
    }
    //------------------------------ Удаление БОМ-БОМ ------------------------------------------------------------------
    public void squeezy(double startDiff, double startLevel, int skipPeaks){
        double sqData[] = new double[data.length];
        int sqIdx=0;
        ArrayList<Integer> maxIdx = new ArrayList<>();          // Огибающая из максимумов
        for(int i=1; i<data.length-1;i++)
            if (data[i]>0 && data[i] > data[i-1] && data[i] > data[i+1])
                maxIdx.add(i);
        if (maxIdx.size()==0)
            return;
        double maxFull = data[maxIdx.get(0)];
        for(Integer idx : maxIdx)
            if (data[idx.intValue()]>maxFull)
                maxFull = data[idx.intValue()];
        ArrayList<Integer> maxStartIdx = new ArrayList<>();
        for(int i=1;i<maxIdx.size();i++){
            int idx = maxIdx.get(i);
            if (data[idx]/data[maxIdx.get(i-1)] >startDiff && data[idx]>maxFull*startLevel){
                maxStartIdx.add(i);
                System.out.println("overIdx="+idx+" "+(int)data[idx]);
            }
        }
        maxStartIdx.add(maxIdx.size()-1);                               // Добавить последний
        for (int i=0;i<maxStartIdx.size()-1;i++){
            int firstIdx = maxIdx.get(maxStartIdx.get(i)+skipPeaks);    // Первый пик интервала и пропустить
            int lastIdx = maxIdx.get(maxStartIdx.get(i+1)-1);           // Предпоследний пик перед следующим ударом
            for(int j=firstIdx; j<lastIdx;j++)             // Скопировать
                sqData[sqIdx++]=data[j];
            //------------------ TODO - copyData before Noise -------------------------
            }
        double out[] = new double[sqIdx];
        for(int i=0;i<out.length;i++)
            out[i]=sqData[i];
        data = out;
        sz = data.length;
        }
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<FFTAudioTextFile> splitMeasure(boolean size32768, int startOver, int startLevelProc, int skipTimeMS, double fileFreq){
        int skipCount = (int)fileFreq*skipTimeMS/1000;
        ArrayList<FFTAudioTextFile> out = new ArrayList<>();
        ArrayList<Integer> maxIdx = new ArrayList<>();          // Огибающая из максимумов
        for(int i=1; i<data.length-1;i++)
            if (data[i]>0 && data[i] > data[i-1] && data[i] > data[i+1])
                maxIdx.add(i);
        if (maxIdx.size()==0)
            return out;
        double maxFull = data[maxIdx.get(0)];
        for(Integer idx : maxIdx)
            if (data[idx.intValue()]>maxFull)
                maxFull = data[idx.intValue()];
        ArrayList<Integer> maxStartIdx = new ArrayList<>();
        for(int i=1;i<maxIdx.size();i++){
            int idx = maxIdx.get(i);
            if (data[idx]/data[maxIdx.get(i-1)] >=startOver && data[idx]>maxFull*startLevelProc/100){
                maxStartIdx.add(i);
                System.out.println("overIdx="+idx+" "+(int)data[idx]);
                }
            }
        if (maxStartIdx.size()==0)
            return out;
        maxStartIdx.add(maxIdx.size()-1);                               // Добавить последний
        for (int i=0;i<maxStartIdx.size()-1;i++){
            int firstIdx = maxIdx.get(maxStartIdx.get(i))+skipCount;    // Первый пик интервала и пропустить
            int lastIdx = maxIdx.get(maxStartIdx.get(i+1)-skipCount);   // Предпоследний пик перед следующим ударом
            if (firstIdx>=lastIdx)
                continue;
            int srcSize=lastIdx-firstIdx+1;
            int finSize = size32768 ? 32768 : 1024;
            if (!size32768)
                while(finSize<srcSize)
                    finSize*=2;
            double dd[] = new double[finSize];
            for(int j=0;j<finSize;j++)
                dd[j]=0;
            for(int j=firstIdx; j<=lastIdx;j++)
                dd[j-firstIdx]=data[j];
            FFTAudioTextFile ff = new FFTAudioTextFile();
            ff.setData(dd);
            ff.sz = dd.length;
            out.add(ff);
            }
        return out;
        }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String ss[]) throws Exception {
        String test1="20220426T092725_45-1_СМ-300_Опора 125";
        //String test1="20220504T113411_112-2_сокур рука_Опора 85";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(test1+".txt"),"Windows-1251"));
        FFTAudioTextFile file = new FFTAudioTextFile();
        FileDescription description = new FileDescription("");
        file.readData(description,reader,true);
        ArrayList<FFTAudioTextFile> list = file.splitMeasure(false,3,60,50, description.getFileFreq());
        if (list.size()==0){
            System.out.println("Отсутствуют (короткие) интервалы возбуждения");
            return;
            }
        for(int i=0;i<list.size();i++){
            FFTAudioTextFile file2 = list.get(i);
            file2.writeWave("out-"+(i+1)+".wav", 441, new I_Notify() {
                @Override
                public void onMessage(String mes) {
                    System.out.println(mes);
                    }
                @Override
                public void onError(Exception ee) {
                    System.out.println(ee.toString());
                    }
                });
            description.setMeasureCounter(i+1);
            file2.save("",description,new I_EventListener() {
                @Override
                public void onEvent(String ss) {
                    System.out.println(ss);
                    }
                });
            }
        /*
        file.squeezy(3, 0.7,5);
        file.writeWave("out.wav", 441, new I_Notify() {
            @Override
            public void onMessage(String mes) {
                System.out.println(mes);
            }
            @Override
            public void onError(Exception ee) {
                System.out.println(ee.toString());
            }
            });
        description.setSupport(description.getSupport()+"++");
        file.save("",description,new I_EventListener() {
            @Override
            public void onEvent(String ss) {
                System.out.println(ss);
            }
            });
         */
    }
}
