package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityRefList;

public class MeasureGroup extends Entity{
    private String groupName="";
    private EntityRefList<PowerLine> PowerLine = new EntityRefList<>(PowerLine.class);
    public String getGroupName() {
        return groupName; }
    public EntityRefList<PowerLine> getPowerLine() {
        return PowerLine; }
    public void setGroupName(String groupName) {
        this.groupName = groupName; }
}
