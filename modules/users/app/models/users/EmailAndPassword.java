package models.users;

import play.data.validation.Constraints;

public class EmailAndPassword {

    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String password;
}
