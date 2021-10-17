package romanow.abc.core.entity.subjectarea;

import org.joda.time.DateTime;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.utils.GPSPoint;
import romanow.lep500.FileDescription;
import romanow.lep500.fft.FFTAudioTextFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MeasureFile extends Entity {
    private EntityRefList<MeasureGroup> MeasureGroup = new EntityRefList<>(MeasureGroup.class);
    private DateTime createDate = new DateTime();                               // Время создания
    private EntityLink<Artifact> artifact = new EntityLink<>(Artifact.class);   // Файл волны
    private String srcNumber="";                                                // Номер датчика
    private String comment="";                                                  // Комментарий
    private GPSPoint gps = new GPSPoint();
    private double fileFreq = 0;                                                // Частота измерений из файла
    private DateTime measureDate = new DateTime();                              // Время создания
    private String fileSensorName="";                                           // Имя сенсора из файла
    private int fileMeasureCounter=0;                                           // Последовательный номер измерения из файла
    //------------------------------------------------------------------------------------
    //--------------- Получение праметров записи из заголовка файла
    public FileDescription loadMetaData(Artifact art, String path) throws IOException{
        FileDescription fd = new FileDescription(art.getOriginalName());
        String ss = path+"/"+art.createArtifactServerPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ss),"UTF-8"));
        FFTAudioTextFile.readHeader(fd,reader);
        createDate = fd.createDate;
        srcNumber = fd.srcNumber;
        fileMeasureCounter = fd.fileMeasureCounter;
        fileSensorName = fd.fileSensorName;
        fileFreq = fd.fileFreq;
        gps = fd.gps;
        comment = fd.comment;
        artifact.setOidRef(art);
        return fd;
        }
}
