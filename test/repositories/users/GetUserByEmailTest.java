package repositories.users;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultCompanies;
import defaultData.DefaultUsers;
import models.users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.test.WithApplication;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class GetUserByEmailTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();
    private DefaultUsers defaultUsers = new DefaultUsers();

    private String existsEmail = "john@doe.com";
    private String notExistsEmail = "susan@moore.com";

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

    @Test
    public void getUserThroughExistingEmailTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.getByEmail(existsEmail);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void getUserThroughNotExistingEmailTest(){


        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.getByEmail(notExistsEmail);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }
}
