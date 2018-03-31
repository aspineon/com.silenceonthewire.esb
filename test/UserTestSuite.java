import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import repositories.users.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        FindAllUsersRepositoryTest.class,
        AddUserTest.class,
        UpdateUserTest.class,
        DeleteUserTest.class,
        GetUserByIdTest.class,
        GetUserByEmailTest.class,
        GetUserByPhoneTest.class,
        GetUserByEmailAndPasswordTest.class,
        GetUserByPhoneAndPasswordTest.class
})
public class UserTestSuite {

}
