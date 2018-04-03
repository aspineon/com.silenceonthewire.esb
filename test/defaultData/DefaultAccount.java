package defaultData;

import io.ebean.Finder;
import models.users.Account;
import models.users.User;

import java.util.Date;

public class DefaultAccount {

    private Long firstId = 1L;
    private Long secondId = 2L;

    private Long firstUserId = 1L;
    private Long secondUserId = 2L;

    private String firstTaxNumber = "1";
    private String secondTaxNumber = "2";

    private String firstSsn = "1";
    private String secondSsn = "2";

    private String address = "Street 1";
    private String city = "City";
    private String state = "State";
    private String country = "Country";
    private String postalCode = "1";

    public void createAccounts(){

        this.createAccount(this.firstId, this.firstUserId, this.firstTaxNumber, this.firstSsn);
        this.createAccount(this.secondId, this.secondUserId, this.secondTaxNumber, this.secondSsn);
    }

    private void createAccount(Long id, Long userId, String taxNumber, String ssn){

        Finder<Long, User> finder = new Finder<Long, User>(User.class);

        Account account = new Account();

        account.id = id;
        account.user = finder.byId(userId);
        account.taxNumber = taxNumber;
        account.ssn = ssn;
        account.address  = this.address;
        account.city = this.city;
        account.state = this.state;
        account.country = this.country;
        account.postalCode = this.postalCode;
        account.createdAt = new Date();
        account.updatedAt = new Date();

        account.save();

    }

    public void deleteAccounts(){

        Finder<Long, Account> finder = new Finder<Long, Account>(Account.class);
        finder.all().forEach(account -> account.delete());
    }
}
