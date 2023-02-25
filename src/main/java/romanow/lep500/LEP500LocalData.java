package romanow.lep500;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.subjectarea.MFSelection;
import romanow.abc.core.entity.subjectarea.MeasureFile;
import romanow.abc.core.entity.subjectarea.PowerLine;
import romanow.abc.core.entity.subjectarea.Support;

import java.util.ArrayList;

public class LEP500LocalData {
    public LEP500LocalData(){}
    @Getter @Setter boolean valid=false;
    @Getter private ArrayList<LEP500Params> lep500ParamList = new ArrayList<>();
    @Getter private ArrayList<MFSelection> selections = new ArrayList<>();
    @Getter private ArrayList<FileDescription> files = new ArrayList<>();
    @Getter transient private EntityRefList<PowerLine> powerLines = new EntityRefList<>();
    @Getter @Setter private String measureFilesDir = "";
    public void createPowerLines(){
        powerLines.clear();
        for(FileDescription fd : files){
            PowerLine line = powerLines.getByName(fd.getPowerLine());
            if (line==null){
                line = new PowerLine();
                line.setName(fd.getPowerLine());
                powerLines.add(line);
                powerLines.createMap();
                }
            Support support = line.getGroup().getByName(fd.getSupport());
            if (support==null){
                support = new Support();
                support.setName(fd.getSupport());
                line.getGroup().add(support);
                line.getGroup().createMap();
                }
            support.getFiles().add(new MeasureFile(fd));
            }
        }
    }
