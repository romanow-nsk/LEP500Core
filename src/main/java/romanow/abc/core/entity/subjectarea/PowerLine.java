package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.EntityRefList;

public class PowerLine extends NamedEntity {
    private int measureState = Values.MSUndefined;
    private EntityRefList<Support> group = new EntityRefList<>(Support.class);
    public int getMeasureState() {
        return measureState; }
    public void setMeasureState(int measureState) {
        this.measureState = measureState; }
    public EntityRefList<Support> getGroup() {
        return group; }
}
