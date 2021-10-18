package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityRefList;

public class MeasureGroup extends NamedEntity{
    private int measureState = Values.MSUndefined;
    private EntityLink<PowerLine> PowerLine = new EntityLink<>();
    private EntityRefList<MeasureFile> files = new EntityRefList<>(MeasureFile.class);
    public int getMeasureState() {
        return measureState; }
    public void setMeasureState(int measureState) {
        this.measureState = measureState; }
    public EntityLink<PowerLine> getPowerLine() {
        return PowerLine; }
    public EntityRefList<MeasureFile> getFiles() {
        return files; }
}
