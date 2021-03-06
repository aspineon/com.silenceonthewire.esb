package converters.users;

import io.ebean.Finder;
import models.users.Company;
import models.users.NewUser;
import models.users.User;

import java.util.Date;
import java.util.function.Function;

public class NewUserToUserConverter implements Function<NewUser, User> {


    @Override
    public User apply(NewUser newUser) {

        Finder<Long, Company> finder = new Finder<Long, Company>(Company.class);

        User user = new User();
        user.id = System.currentTimeMillis();
        user.createdAt = new Date();
        user.updatedAt = new Date();
        user.firstName = newUser.firstName;
        user.lastName = newUser.lastName;
        user.setEmail(newUser.email);
        user.setPassword(newUser.password);
        user.phone = newUser.phone;
        user.isAdmin = newUser.isAdmin;
        user.company = finder.byId(newUser.company);
        return user;
    }
}
