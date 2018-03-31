package repositories.users;

import defaultData.DefaultUsers;
import helpers.BeforeAndAfter;
import models.users.User;
import org.junit.Test;
import play.test.WithApplication;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class FindAllUsersRepositoryTest extends WithApplication {

    private int expectedSize = 2;

    private DefaultUsers defaultUsers = new DefaultUsers();

    @Test
    public void testAllUsers(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultUsers.createUsers();

            final UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
            final CompletionStage<List<User>> stage = userRepository.getAll();
            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
            defaultUsers.deleteUsers();
        });
    }
}
