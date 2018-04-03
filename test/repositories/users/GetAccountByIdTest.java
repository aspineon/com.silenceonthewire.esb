package repositories.users;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultAccount;
import defaultData.DefaultCompanies;
import defaultData.DefaultUsers;
import models.users.Account;
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

public class GetAccountByIdTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();
    private DefaultUsers defaultUsers = new DefaultUsers();
    private DefaultAccount defaultAccount = new DefaultAccount();

    private Long existsId = 1L;
    private Long notExistsId = 5L;

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
    public void getAccountWithExistingId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.getById(existsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(account -> {

                    return account.isPresent();
                });
            });
        });
    }

    @Test
    public void getAccountWithNotExistingId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final AccountRepository accountRepository = app.injector().instanceOf(AccountRepository.class);
            final CompletionStage<Optional<Account>> stage = accountRepository.getById(notExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(account -> {

                    return !account.isPresent();
                });
            });
        });
    }
}
