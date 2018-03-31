package models.users;

import play.data.validation.Constraints;

public class NewUser {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String firstName;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String lastName;

    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String password;

    @Constraints.Required
    public boolean isAdmin;
}
