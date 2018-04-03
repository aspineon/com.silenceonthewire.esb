package models.users;

import play.data.validation.Constraints;

public class NewAccount {

    @Constraints.Required
    public Long userId;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String taxNumber;

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
