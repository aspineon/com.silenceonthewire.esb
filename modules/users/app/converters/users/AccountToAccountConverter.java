package converters.users;

import models.users.Account;

import java.util.function.Function;

public class AccountToAccountConverter implements Function<Account, Account> {

    @Override
    public Account apply(Account account) {
        return account;
    }
}
