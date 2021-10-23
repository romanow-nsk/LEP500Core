package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.core.utils.Pair;
import romanow.lep500.FileDescription;
import romanow.lep500.FFTAudioTextFile;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MeasureFile extends Entity {
    private EntityLink<Support> Support = new EntityLink<>();
    private EntityLink<Artifact> artifact = new EntityLink<>(Artifact.class);   // Файл волны
    private OwnDateTime importDate = new OwnDateTime();                         // Время создания
    private OwnDateTime measureDate = new OwnDateTime();                        // Время создания
    private String sensor="";                                                   // Имя датчика
    private String powerLineName="";                                            // Линия
    private String supportName="";                                              // Опора
    private String comment="";                                                  // Комментарий
    private GPSPoint gps = new GPSPoint();
    private int expertResult= Values.MSUndefined;
    private double fileFreq = 0;                                                // Частота измерений из файла
    private int measureCounter=0;                                           // Последовательный номер измерения из файла
    //------------------------------------------------------------------------------------
    //--------------- Получение праметров записи из заголовка файла
    public String getOriginalFileName(){
        return artifact.getRef()==null ? "" : artifact.getRef().getOriginalName();
        }
    public String getTitle(){
        return toString()+" ("+getOriginalFileName()+")";
    }
    public Pair<String,FileDescription> loadMetaData(Artifact art, String path){
        FileDescription fd = new FileDescription(art.getOriginalName());
        String error = fd.getFormatError();
        if (error.length()!=0){
            return new Pair<>(error,null);
            }
        String ss = path+"/"+art.createArtifactServerPath();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(ss),"Windows-1251"));
            FFTAudioTextFile file = new FFTAudioTextFile();
            file.readData(fd,reader);
            error = fd.getFormatError();
            if (error.length()!=0){
                return new Pair<>(error,null);
                }
            reader.close();
            measureDate = fd.getCreateDate();
            sensor = fd.getSensor();
            measureCounter = fd.getMeasureCounter();
            fileFreq = fd.getFileFreq();
            gps = fd.getGps();
            comment = fd.getComment();
            artifact.setOidRef(art);
            return new Pair<>(null,fd);
            } catch (IOException ex){
                if (reader!=null){
                    try {
                        reader.close();
                        } catch (Exception ee){}
                    }
                return new Pair<>("Ошибка чтения файла: "+ex.toString(),null);
                }
            }
    private static String def(String ss){
        return ss.length()==0 ? "..." : ss;
    }
    public String toString(){
        return def(powerLineName)+" / "+def(supportName)+" / "+def(sensor)+" ("+def(""+measureCounter)+")"+"\n"+measureDate.dateTimeToString();
        }
    public EntityLink<Support> getSupport() {
        return Support; }
    public OwnDateTime getImportDate() {
        return importDate; }
    public EntityLink<Artifact> getArtifact() {
        return artifact; }
    public String getSensor() {
        return sensor; }
    public String getComment() {
        return comment; }
    public GPSPoint getGps() {
        return gps; }
    public double getFileFreq() {
        return fileFreq; }
    public OwnDateTime getMeasureDate() {
        return measureDate; }
    public int getMeasureCounter() {
        return measureCounter; }
    public String getPowerLineName() {
        return powerLineName; }
    public void setPowerLineName(String powerLineName) {
        this.powerLineName = powerLineName; }
    public String getSupportName() {
        return supportName; }
    public int getExpertResult() {
        return expertResult; }
    public void setExpertResult(int expertResult) {
        this.expertResult = expertResult; }
    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }
}
