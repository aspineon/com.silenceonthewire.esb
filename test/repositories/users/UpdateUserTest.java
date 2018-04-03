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

public class UpdateUserTest extends WithApplication {

    private DefaultUsers defaultUsers = new DefaultUsers();
    private DefaultCompanies defaultCompanies = new DefaultCompanies();

    private Long idToTest = 1L;
    private Long notExistsId = 3L;

    private String newEmail ="susan@moore.com";
    private String newPhone = "999999999";

    private String existsEmail = "john@smith.com";
    private String existsPhone = "1111111111";

    private String firstName = "Susan";
    private String lastName = "Moore";

    private String password = "R3v3l@t104LoA";

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
    public void correctUserUpdateTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(idToTest, newEmail, newPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.updateUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOfNonExistentUserTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(notExistsId, newEmail, newPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.updateUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateUserUsingExistingEmailAddressTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(idToTest, existsEmail, newPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.updateUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateUserUsingExistingPhoneNumberTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            User user = createUserToTest(idToTest, newEmail, existsPhone);

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.updateUser(user);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    private User createUserToTest(Long id, String email, String phone){

        User user = new User();
        user.id = id;
        user.firstName = this.firstName;
        user.lastName = this.lastName;
        user.setEmail(email);
        user.phone = phone;
        user.setPassword(password);
        user.isAdmin = true;
        user.createdAt = new Date();
        user.updatedAt = new Date();

        return user;
    }
}
