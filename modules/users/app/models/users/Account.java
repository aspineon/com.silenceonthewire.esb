package models.users;

import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Account extends BaseModel {

    @Column(unique = true, nullable = false)
    @OneToOne
    @Constraints.Required
    public User user;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String taxNumber;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String ssn;

    @Constraints.Required
    @Constraints.MinLength(255)
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
}
