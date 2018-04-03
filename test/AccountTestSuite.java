import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import repositories.users.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        GetAccountByIdTest.class,
        GetAccountByUserTest.class,
        GetAccountBySsnTest.class,
        GetAccountByTaxNumberTest.class,
        CreateAccountTest.class,
        UpdateAccountTest.class
})
public class AccountTestSuite {

}
