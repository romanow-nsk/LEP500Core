package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityLinkList;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.OwnDateTime;

public class MFSelection extends NamedEntity {
    @Getter EntityLinkList<MeasureFile> files = new EntityLinkList<>(MeasureFile.class);
    @Getter EntityLink<User> user = new EntityLink<>(User.class);
    @Getter OwnDateTime createDate = new OwnDateTime();
    public String getTitle(){
        return "["+getOid()+"] "+getName() + " "+user.getTitle()+" "+createDate.dateToString();
        }
}
