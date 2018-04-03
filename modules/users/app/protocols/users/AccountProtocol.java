package protocols.users;


import models.users.Account;

public class AccountProtocol {

    private AccountAction accountAction;
    private Long id;
    private String accountData;
    private Account account;

    public AccountProtocol(){}

    public AccountProtocol(AccountAction accountAction){

        this.accountAction = accountAction;
    }

    public AccountProtocol(AccountAction accountAction, Long id){

        this.accountAction = accountAction;
        this.id = id;
    }

    public AccountProtocol(AccountAction accountAction, String accountData){

        this.accountAction = accountAction;
        this.accountData = accountData;
    }

    public AccountProtocol(AccountAction accountAction, Account account){

        this.accountAction = accountAction;
        this.account = account;
    }

    public AccountAction getAccountAction() {
        return accountAction;
    }

    public Long getId() {
        return id;
    }

    public String getAccountData() {
        return accountData;
    }

    public Account getAccount() {
        return account;
    }
}
