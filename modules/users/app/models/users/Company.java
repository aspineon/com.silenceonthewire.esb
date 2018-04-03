package models.users;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company extends BaseModel{

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String taxNumber;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String address;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String city;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String state;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String country;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String postalCode;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public List<User> users;

}
