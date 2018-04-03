package repositories.users;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultAccount;
import defaultData.DefaultCompanies;
import defaultData.DefaultUsers;
import io.ebean.Finder;
import models.users.Account;
import models.users.Company;
import models.users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CreateAccountTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();
    private DefaultUsers defaultUsers = new DefaultUsers();
    private DefaultAccount defaultAccount = new DefaultAccount();

    private Long accountId = 3L;
    private Long notExistsAccountId = 4L;

    private Long userId = 3L;
    private Long notExistsUserId = 4L;

    private String firstName = "Susan";
    private String lastName = "Moore";

    private String email = "susan@moore.com";

    private String phone = "4";

    private String password = "P@ssw0rd";

    private Long companyId = 1L;

    private boolean isAdmin = true;

    private String existsTaxNumber = "1";
    private String notExistsTaxNumber = "4";

    private String existsSsn = "1";
    private String notSsn = "4";

    private String address = "Street 1";
    private String city = "City";
    private String state = "State";
    private String country = "Country";
    private String postalCode = "1";

    Database database;

    @Before
    public void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            database = Databases.inMemory(
                    "test",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(database);

            defaultCompanies.createCompanies();
            defaultUsers.createUsers();
            this.createNewUser();
            defaultAccount.createAccounts();
        });
    }

    @After
    public void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.deleteUsers();
            defaultCompanies.deleteCompanies();

            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void successCreateNewAccountTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.accountId, this.userId, this.notSsn, this.notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.create(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void createAccountWithExistingIdTest(){

        running(fakeApplication(inMemoryDatabase("test")), () ->{

            Account account = createAccount(notExistsAccountId, userId, notSsn, notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.create(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentAccount -> {

                    return !currentAccount.isPresent();
                });
            });
        });
    }

    @Test
    public void createAccountWithNotExistingUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(notExistsAccountId, userId, notSsn, notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.create(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void createAccountWithExistingSsnTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(notExistsUserId, notExistsAccountId, existsSsn, notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.create(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentAccount -> {

                    return !currentAccount.isPresent();
                });
            });
        });
    }

    @Test
    public void createAccountWithExistsTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(notExistsAccountId, notExistsUserId, notSsn, notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.create(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentAccount -> {

                    return !currentAccount.isPresent();
                });
            });
        });
    }

    private Account createAccount(long id, Long userId, String ssn, String taxNumber){

        Finder<Long, User> finder = new Finder<Long, User>(User.class);

        Account account = new Account();

        account.id = id;
        account.user = finder.byId(userId);
        account.ssn = ssn;
        account.taxNumber = taxNumber;
        account.address = this.address;
        account.city = city;
        account.state = state;
        account.country = country;
        account.postalCode = postalCode;

        return account;
    }

    private void createNewUser(){

        Finder<Long, Company> finder = new Finder<Long, Company>(Company.class);

        User user = new User();
        user.id = this.userId;
        user.createdAt = new Date();
        user.updatedAt = new Date();
        user.firstName = this.firstName;
        user.lastName = this.lastName;
        user.company = finder.byId(this.companyId);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.phone = this.phone;
        user.isAdmin = this.isAdmin;
    }
}
