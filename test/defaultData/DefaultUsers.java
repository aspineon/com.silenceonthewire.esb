package defaultData;

import io.ebean.Finder;
import models.users.Company;
import models.users.User;

import java.util.Date;
import java.util.List;

public class DefaultUsers {

    private Long firstCompanyId = 1L;
    private Long secondCompanyId = 2L;

    private Long    firstId             = 1L;
    private String  firstEmail          = "john@doe.com";
    private String  firstPhone          = "000000000";
    private String  firstPassword       = "R3v3l@t104LoA";

    private Long    secondId            = 2L;
    private String  secondEmail         = "john@smith.com";
    private String  secondPhone         = "1111111111";
    private String  secondPassword      = "R3v3l@t104LoA";

    private boolean isAdmin = true;

    private String firstName = "Jan";
    private String lastName = "Kowalski";

    public void createUsers(){

        createUser(this.firstCompanyId, this.firstId, this.firstEmail, this.firstPhone, this.firstPassword, this.isAdmin);
        createUser(this.secondCompanyId, this.secondId, this.secondEmail, this.secondPhone, this.secondPassword, this.isAdmin);
    }

    public void deleteUsers(){

        Finder<Long, User> finder = new Finder<Long, User>(User.class);
        List<User> users = finder.all();
        users.forEach(user -> user.delete());
    }

    public void createUser(Long companyId, Long id, String email, String phone, String password, boolean isAdmin){

        Finder<Long, Company> finder = new Finder<Long, Company>(Company.class);

        User userModel = new User();

        userModel.id = id;
        userModel.createdAt = new Date();
        userModel.updatedAt = new Date();
        userModel.company = finder.byId(companyId);
        userModel.firstName = this.firstName;
        userModel.lastName = this.lastName;
        userModel.phone = phone;
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.isAdmin = isAdmin;

        userModel.save();
    }

}