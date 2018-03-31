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

public class GetUserByPhoneTest extends WithApplication {

    private DefaultUsers defaultUsers = new DefaultUsers();

    private String existsPhone = "000000000";
    private String notExistsPhone = "000000001";

    @Test
    public void getUserThroughExistingPhoneNumber(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.createUsers();

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.getByPhone(existsPhone);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return currentUser.isPresent();
                });
            });

            defaultUsers.deleteUsers();
        });
    }

    @Test
    public void getUserThroughNotExistingPhoneNumber(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.createUsers();

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<Optional<User>> stage = userRepository.getByPhone(notExistsPhone);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });

            defaultUsers.deleteUsers();
        });
    }
}
