package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.entity.Entity;

public class NamedEntity extends Entity {
    private String name="";
    @Override
    public String getName() {
        return name; }
    public void setName(String name) {
        this.name = name; }
    public NamedEntity(String name) {
        this.name = name; }
    public NamedEntity(){}
    public String getTitle(){
        return super.getTitle()+" "+name;
    }
}
