package modules.users;

import actors.users.UserActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class UserModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {

        bindActor(UserActor.class, "user-actor");
    }
}
