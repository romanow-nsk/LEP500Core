package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.core.utils.Pair;
import romanow.lep500.FileDescription;
import romanow.lep500.fft.FFTAudioTextFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MeasureFile extends Entity {
    private EntityLink<MeasureGroup> MeasureGroup = new EntityLink<>();
    private OwnDateTime importDate = new OwnDateTime();                         // Время создания
    private EntityLink<Artifact> artifact = new EntityLink<>(Artifact.class);   // Файл волны
    private String srcNumber="";                                                // Имя датчика
    private String comment="";                                                  // Комментарий
    private GPSPoint gps = new GPSPoint();
    private double fileFreq = 0;                                                // Частота измерений из файла
    private OwnDateTime measureDate = new OwnDateTime();                        // Время создания
    private String fileSensorName="";                                           // Имя сенсора из файла
    private int fileMeasureCounter=0;                                           // Последовательный номер измерения из файла
    //------------------------------------------------------------------------------------
    //--------------- Получение праметров записи из заголовка файла
    public Pair<String,FileDescription> loadMetaData(Artifact art, String path){
        FileDescription fd = new FileDescription(art.getOriginalName());
        String zz = fd.parseFromName();
        if (zz!=null){
            return new Pair<>(zz,null);
            }
        String ss = path+"/"+art.createArtifactServerPath();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(ss),"Windows-1251"));
            FFTAudioTextFile file = new FFTAudioTextFile();
            file.readData(fd,reader);
            measureDate = new OwnDateTime(fd.createDate.getMillis());
            srcNumber = fd.srcNumber;
            fileMeasureCounter = fd.fileMeasureCounter;
            fileSensorName = fd.fileSensorName;
            fileFreq = fd.fileFreq;
            gps = fd.gps;
            comment = fd.comment;
            artifact.setOidRef(art);
            return new Pair<>(null,fd);
            } catch (IOException ex){
                if (reader!=null){
                    try {
                        reader.close();
                        } catch (Exception ee){}
                    }
                return new Pair<>("Ошибка формата файла: "+ex.toString(),null);
                }
            }
    public String toString(){
        return getOid()+" "+fileSensorName+"("+fileMeasureCounter+") "+srcNumber+"/"+fileSensorName+" "+measureDate.dateTimeToString()+"/"+importDate.dateTimeToString();
        }
    public EntityLink<romanow.abc.core.entity.subjectarea.MeasureGroup> getMeasureGroup() {
        return MeasureGroup; }
    public OwnDateTime getImportDate() {
        return importDate; }
    public EntityLink<Artifact> getArtifact() {
        return artifact; }
    public String getSrcNumber() {
        return srcNumber; }
    public String getComment() {
        return comment; }
    public GPSPoint getGps() {
        return gps; }
    public double getFileFreq() {
        return fileFreq; }
    public OwnDateTime getMeasureDate() {
        return measureDate; }
    public String getFileSensorName() {
        return fileSensorName; }
    public int getFileMeasureCounter() {
        return fileMeasureCounter; }
}
