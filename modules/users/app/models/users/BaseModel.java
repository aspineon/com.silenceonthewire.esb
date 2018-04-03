package models.users;

import io.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BaseModel extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy/mm/dd")
    public Date createdAt;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy/mm/dd")
    public Date updatedAt;
}
