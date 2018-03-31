package models.users;

import play.data.validation.Constraints;

public class PhoneAndPassword {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String password;
}
