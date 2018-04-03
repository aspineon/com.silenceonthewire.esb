package converters.users;

import io.ebean.Finder;
import models.users.Account;
import models.users.NewAccount;
import models.users.User;

import java.util.Date;
import java.util.function.Function;

public class NewAccountToAccountConverter implements Function<NewAccount, Account> {
    @Override
    public Account apply(NewAccount newAccount) {

        Finder<Long, User> finder = new Finder<Long, User>(User.class);

        Account account = new Account();

        account.id = System.currentTimeMillis();
        account.createdAt = new Date();
        account.updatedAt = new Date();

        account.user = finder.byId(newAccount.userId);

        account.ssn = newAccount.ssn;
        account.taxNumber = newAccount.taxNumber;
        account.address = newAccount.address;
        account.city = newAccount.city;
        account.state = newAccount.state;
        account.country = newAccount.country;
        account.postalCode = newAccount.postalCode;

        return account;
    }
}
