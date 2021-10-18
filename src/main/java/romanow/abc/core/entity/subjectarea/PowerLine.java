package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.EntityRefList;

public class PowerLine extends NamedEntity {
    private int measureState = Values.MSUndefined;
    private EntityRefList<MeasureGroup> group = new EntityRefList<>(MeasureGroup.class);
    public int getMeasureState() {
        return measureState; }
    public void setMeasureState(int measureState) {
        this.measureState = measureState; }
    public EntityRefList<MeasureGroup> getGroup() {
        return group; }
}
