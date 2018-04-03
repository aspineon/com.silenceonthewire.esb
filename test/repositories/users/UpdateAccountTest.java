package repositories.users;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultAccount;
import defaultData.DefaultCompanies;
import defaultData.DefaultUsers;
import io.ebean.Finder;
import models.users.Account;
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

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UpdateAccountTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();
    private DefaultUsers defaultUsers = new DefaultUsers();
    private DefaultAccount defaultAccount = new DefaultAccount();

    private Long existsId = 1L;
    private Long notExistsId = 3L;

    private Long existsUserId = 1L;
    private Long notExistsUserId = 5L;

    private String notExistsSsn = "3";
    private String existsSsn = "2";

    private String existsTaxNumber = "2";
    private String notExistsTaxNumber = "3";

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
            defaultAccount.createAccounts();
        });
    }

    @After
    public void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultAccount.deleteAccounts();
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
    public void successUpdateAccount(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.existsId, this.existsUserId, this.notExistsSsn, this.notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.update(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentAccount -> {

                    return currentAccount.isPresent();
                });
            });

        });
    }

    @Test
    public void updateAccountWithNotExistingAccountId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.notExistsId, this.existsUserId, this.notExistsSsn, this.notExistsTaxNumber);

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
    public void updateAccountWithNotExistingUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.existsId, this.notExistsUserId, this.notExistsSsn, this.notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.update(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateAccountWithExistingSsnTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.existsId, this.existsUserId, this.existsSsn, this.notExistsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.update(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentAccount -> {

                    return !currentAccount.isPresent();
                });
            });
        });
    }

    @Test
    public void updateAccountWithExistingTaxNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Account account = createAccount(this.existsId, this.existsUserId, this.notExistsSsn, this.existsTaxNumber);

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.update(account);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
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
}
