package converters.users;

import io.ebean.Finder;
import models.users.Account;
import models.users.CurrentAccount;
import models.users.User;

import java.util.Date;
import java.util.function.Function;

public class CurrentAccountToAccountConverter implements Function<CurrentAccount, Account>{

    @Override
    public Account apply(CurrentAccount currentAccount) {

        Finder<Long, User> finder = new Finder<Long, User>(User.class);

        Account account = new Account();
        account.id = currentAccount.id;
        account.createdAt = currentAccount.createdAt;
        account.user = finder.byId(currentAccount.userId);
        account.updatedAt = new Date();
        account.ssn = currentAccount.ssn;
        account.taxNumber = currentAccount.taxNumber;
        account.address = currentAccount.address;
        account.city = currentAccount.city;
        account.state = currentAccount.state;
        account.country = currentAccount.country;
        account.postalCode = currentAccount.postalCode;

        return account;

    }
}
