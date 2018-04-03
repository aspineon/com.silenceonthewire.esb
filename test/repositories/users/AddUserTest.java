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

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class AddUserTest extends WithApplication {

    private Long newUserId = 3L;
    private String newUserEmail = "susan@moore.com";
    private String newUserPhone = "999999999";

    private Long existsUserId = 1L;
    private String existsUserEmail = "john@doe.com";
    private String existsUserPhone = "000000000";

    private String firstName = "Susan";
    private String lastName = "Moore";

    private boolean isAdmin = true;

    private DefaultUsers defaultUsers = new DefaultUsers();
    private DefaultCompanies defaultCompanies = new DefaultCompanies();

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
    public void creatingUserWithNewData(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(newUserId, newUserEmail, newUserPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.addUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void creatingUserWithExistingIdNumber(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(existsUserId, newUserEmail, newUserPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.addUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void creatingUserWithExistingEmail(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(newUserId, existsUserEmail, newUserPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.addUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void creatingUserWithExistingPassword(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(newUserId, newUserEmail, existsUserPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.addUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    private User createUserToTest(Long id, String email, String phone) {

        User user = new User();
        user.id = id;
        user.createdAt = new Date();
        user.updatedAt = new Date();
        user.firstName = this.firstName;
        user.lastName = this.lastName;
        user.setEmail(email);
        user.phone = phone;
        user.isAdmin = this.isAdmin;
        return user;
    }
}
