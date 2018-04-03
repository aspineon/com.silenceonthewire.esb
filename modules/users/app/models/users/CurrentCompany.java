package models.users;

import play.data.validation.Constraints;

import java.util.Date;

public class CurrentCompany {

    @Constraints.Required
    public Long id;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String taxNumber;

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

    @Constraints.Required
    public Date createdAt;

    @Constraints.Required
    public Date updatedAt;
}
