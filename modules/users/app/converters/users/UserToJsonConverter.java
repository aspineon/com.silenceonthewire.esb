package converters.users;

import models.users.User;

import java.util.function.Function;

public class UserToJsonConverter implements Function<User, User> {


    @Override
    public User apply(User user) {
        return user;
    }
}
