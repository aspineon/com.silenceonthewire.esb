package repositories.users;

import defaultData.DefaultUsers;
import models.users.User;
import org.junit.Test;
import play.test.WithApplication;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class DeleteUserTest extends WithApplication {

    private DefaultUsers defaultUsers = new DefaultUsers();

    private Long existsId = 1L;
    private Long notExistsId = 3L;

    @Test
    public void deleteUserWithExistsIdTest() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.createUsers();

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.deleteUser(existsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });

            defaultUsers.deleteUsers();
        });
    }

    @Test
    public void deleteUserWithNotExistsIdTest(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.createUsers();

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.deleteUser(notExistsId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });

            defaultUsers.deleteUsers();
        });
    }
}
